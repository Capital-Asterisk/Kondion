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
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.js.JSDrawable;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionTexture;
import vendalenger.kondion.materials.KMat_Monotexture;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Renderable;
import vendalenger.kondion.objectbase.KObj_Solid;

public class GKO_DeferredPass extends GKO_RenderPass {
	
	//protected int texId = 0;
	// texId is result
	// drbId is unused
	protected int briId = 0; // Brightness
	protected int depId = 0; // Depth texture
	protected int dffId = 0; // Diffuse texture
	protected int nrmId = 0; // Normal texture
	protected IntBuffer ducks;
	protected List<RKO_Light> lights;
	
	public GKO_DeferredPass() {
		this(0, true);
	}
	
	public GKO_DeferredPass(int id) {
		this(id, true);
	}
	
	public GKO_DeferredPass(int id, boolean a) {
		items = new ArrayList<KObj_Renderable>();
		type = 30;
		this.id = id;
		this.auto = a;
		ducks = BufferUtils.createIntBuffer(2);
		//ducks.put
		ducks.put(GL_COLOR_ATTACHMENT0_EXT);
		ducks.put(GL_COLOR_ATTACHMENT1_EXT);
		
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
	
	public void consider(KObj_Renderable f) {
		if ((id | f.getId()) == id)
			if (f instanceof RKO_Light)
				lights.add((RKO_Light) f);
			else
				items.add(f);
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
				reFB();
				framebuffered = true;
			}
			//glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
			//		GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, drbId);
			ready = EXTFramebufferObject.glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT) == EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT;
			
		} else {
			if (Kondion.showPrespective) {
				//System.out.println("b4: " + ducks.remaining());
				
				
				//System.out.println("after: " + ducks.remaining());
				
				glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
				ducks.position(0);
				GL20.glDrawBuffers(ducks);
				
				//System.out.println(glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT));
				//System.out.println(GL_FRAMEBUFFER_COMPLETE_EXT);
				glClearColor(Kondion.getWorld().skyColor.x, Kondion.getWorld().skyColor.y,
						Kondion.getWorld().skyColor.z, Kondion.getWorld().skyColor.w);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
				if (cameraOverride && camera != null) 
					TTT.three(camera);
				else
					TTT.three();
				
				glPushMatrix();
				new KMat_Monotexture().bind(30);
				KondionLoader.textures.get("neat").bind();
				glTranslatef(-getCamera().actTransform.m30, -getCamera().actTransform.m31 + 10, -getCamera().actTransform.m32);
				Kondion.km.draw();
				glPopMatrix();
				
				for (KObj_Renderable kobj : items) {
					kobj.render(30, this);
				}
			}
		}
	}

	private int neat(int tex, int internal, int format, int thisisnotcppp) {
		glBindTexture(GL_TEXTURE_2D, tex);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexImage2D(GL_TEXTURE_2D, 0, internal, width,
				height, 0, format, thisisnotcppp, (java.nio.ByteBuffer)
				null);
		
		return tex;
	}
	
	private void reFB() {
		
		fboId = glGenFramebuffersEXT();
		//drbId = glGenRenderbuffersEXT();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
		
		briId = neat(glGenTextures(), GL_RED, GL_RED, GL_UNSIGNED_BYTE); // Brightness
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
		
		System.out.println("Retextured");
	}
	
	@Override
	public void update() {
		defaultUpdate();
	}

	@Override
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, nrmId);
	}
}
