/*
 * Copyright 2015 Neal Nicdao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_RENDERBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenRenderbuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glRenderbufferStorageEXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.js.JSDrawable;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KShader;
import vendalenger.kondion.lwjgl.resource.KTexture;
import vendalenger.kondion.materials.KMat_Monotexture;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Renderable;
import vendalenger.kondion.objectbase.KObj_Solid;

public class GKO_DeferredPass extends GKO_RenderPass {
	
	//protected int texId = 0;
	// texId is result
	// drbId is unused
	private int briId = 0; // Brightness
	private int depId = 0; // Depth texture
	private int dffId = 0; // Diffuse texture
	private int nrmId = 0; // Normal texture
	private int skyUni = 0;
	private int fogUni = 0;
	protected IntBuffer ducks;
	protected List<KObj_Renderable> lights;
	private KShader program;
	
	public GKO_DeferredPass() {
		this(0, true);
	}
	
	public GKO_DeferredPass(int id) {
		this(id, true);
	}
	
	public GKO_DeferredPass(int id, boolean a) {
		items = new ArrayList<KObj_Renderable>();
		lights = new ArrayList<KObj_Renderable>();
		type = 30;
		this.id = id;
		this.auto = a;
		
		ducks = BufferUtils.createIntBuffer(4);
		ducks.put(GL_COLOR_ATTACHMENT0_EXT);
		ducks.put(GL_COLOR_ATTACHMENT1_EXT);
		ducks.put(GL_COLOR_ATTACHMENT2_EXT);
		ducks.put(GL_COLOR_ATTACHMENT3_EXT);
		
		program = KLoader.shaders.get("K_DeferredRender");
		skyUni = program.uniformLocation("skyColor");
		fogUni = program.uniformLocation("fog");
		
		if (a)
			scan();
	}
	
	public int getTextureId() {
		return texId;
	}
	
	public int getDepthId() {
		return 0;
	}
	
	public int getType() {
		return type;
	}
	
	@Override
	public void consider(KObj_Renderable f) {
		System.out.print("a: " + f);
		if (f.isLight()) {
			lights.add(f);
			System.out.print(f);
		} else if ((f instanceof KObj_Renderable)
				&& (id & ((KObj_Renderable) f).getId()) == id)
			items.add((KObj_Renderable) f);
	}
	
	public void forceAddLight(RKO_Light f) {
		lights.add(f);
	}
	
	public void setType(int t) {
		System.err.println("Type is unused in DeferredPass");
	}
	
	public void scan() {
		super.scan();
	}

	public void render() {
		if (!sizeOverride) {
			// No multiple window support yet!
			width = Window.getWidth();
			height = Window.getHeight();
		}
		if (!ready) {
			if (!framebuffered) {
				generate();
				framebuffered = true;
			}
			//glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
			//		GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, drbId);
			ready = EXTFramebufferObject.glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT) == EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT;
			
		} else {
			glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			if (Kondion.showPrespective) {
				//System.out.println("b4: " + ducks.remaining());
				
				
				//System.out.println("after: " + ducks.remaining());
				
				glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
				ducks.position(0);
				glDrawBuffers(ducks);
				
				//System.out.println(glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT));
				//System.out.println(GL_FRAMEBUFFER_COMPLETE_EXT);
				glClearColor(Kondion.getWorld().skyColor.x, Kondion.getWorld().skyColor.y,
						Kondion.getWorld().skyColor.z, Kondion.getWorld().skyColor.w);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
				if (cameraOverride && camera != null) 
					TTT.three(camera, width, height);
				else
					TTT.three(width, height);
				
				//glPushMatrix();
				//new KMat_Monotexture().bind(30);
				//KLoader.textures.get("neat").bind();
				//glTranslatef(-getCamera().actTransform.m30, -getCamera().actTransform.m31 + 10, -getCamera().actTransform.m32);
				//KLoader.obj.get("kaytrav").draw();
				//glPopMatrix();
				
				for (int i = 0; i < items.size(); i++) {
					if (!items.get(i).killMe)
						if (!items.get(i).hidden)
							items.get(i).render(30, this);
					else {
						items.remove(i);
						i --;
					}
				}
				
				glViewport(0, 0, width, height);
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				
				glOrtho(0, width, height,
						0, 6.0f, -6.0f);
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();
				
				glDepthMask(false);
				glDisable(GL_DEPTH_TEST);
				
				glActiveTexture(GL_TEXTURE0); // Diffuse
				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, dffId);
				glActiveTexture(GL_TEXTURE1); // Depth
				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, depId);
				glActiveTexture(GL_TEXTURE2); // Normals
				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, nrmId);
				glActiveTexture(GL_TEXTURE3); // Brightness
				glEnable(GL_TEXTURE_2D);
				glBindTexture(GL_TEXTURE_2D, briId);
				GLDrawing.setCoords(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
				glTranslatef(width / 2, height / 2, 0);
				for (KObj_Renderable light : lights) {
					((RKO_Light) light).apply(width, height);
				}
				program.useProgram();
				glUniform4f(skyUni, Kondion.getWorld().skyColor.x, Kondion.getWorld().skyColor.y,
						Kondion.getWorld().skyColor.z, Kondion.getWorld().skyColor.w);
				glUniform1f(fogUni, 1f);
				GLDrawing.renderQuad(width, height);
				glTranslatef(-width / 2, -height / 2, 0);
				KShader.unbind();
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE1);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE2);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE3);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE0);
				
				glDepthMask(true);
				
				glViewport(0, 0, Window.getWidth(), Window.getHeight());
			}
		}
	}

	private int neat(int tex, int internal, int format, int thisisnotcppp) {
		glBindTexture(GL_TEXTURE_2D, tex);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, internal, width,
				height, 0, format, thisisnotcppp, (java.nio.ByteBuffer)
				null);
		
		return tex;
	}
	
	private void generate() {
		
		fboId = glGenFramebuffersEXT();
		//drbId = glGenRenderbuffersEXT();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
		
		briId = neat(glGenTextures(), GL_RGB, GL_RGB, GL_UNSIGNED_BYTE); // Brightness
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,
				GL_COLOR_ATTACHMENT3_EXT, GL_TEXTURE_2D,
				briId, 0);
		depId = neat(glGenTextures(), GL14.GL_DEPTH_COMPONENT32, GL_DEPTH_COMPONENT, GL_FLOAT); // Depth texture
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,
				GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D,
				depId, 0);
		dffId = neat(glGenTextures(), GL_RGB, GL_RGB, GL_UNSIGNED_BYTE); // Diffuse texture
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,
				GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D,
				dffId, 0);
		nrmId = neat(glGenTextures(), GL_RGB, GL_RGB, GL_FLOAT); // Normal texture
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,
				GL_COLOR_ATTACHMENT1_EXT, GL_TEXTURE_2D,
				nrmId, 0);
		texId = neat(glGenTextures(), GL_RGB, GL_RGB, GL_UNSIGNED_BYTE); // Result
		if (!pixelate) {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		}
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,
				GL_COLOR_ATTACHMENT2_EXT, GL_TEXTURE_2D,
				texId, 0);
		
		System.out.println("Retextured");
	}
	
	@Override
	public void update() {
		defaultUpdate();
	}
	
	@Override
	public void bind() {
		// TODO Auto-generated method stub
		glBindTexture(GL_TEXTURE_2D, texId);
	}
}
