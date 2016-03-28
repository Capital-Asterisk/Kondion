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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

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
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KTexture;
import vendalenger.kondion.materials.KMat_Monotexture;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Renderable;
import vendalenger.kondion.objectbase.KObj_Solid;

public class GKO_RenderPass extends KObj_Node implements JSDrawable {
	
	public static final int
		DEFAULT		= 0,
		DIFFUSE		= 1,
		DEPTH		= 2,
		NORMALS		= 3,
		BRIGHTNESS	= 4,
		HDR			= 5,
		DEFERRED	= 10,
		GUI			= 20;
	
	protected List<KObj_Renderable> items;
	protected boolean framebuffered = false;
	protected boolean ready = false;
	protected boolean typechanged = false;
	protected int type;
	protected int id;
	protected int drbId = 0; // Renderbuffer
	protected int fboId = 0; // Framebuffer
	protected int texId = 0; // Texture (type)
	public OKO_Camera_ camera;
	public boolean disable = false;
	public boolean auto = true;
	public boolean cameraOverride = false;
	public boolean sizeOverride = false;
	public int width, height;
	
	public GKO_RenderPass() {
		this(0, 0, true, null);
	}
	
	public GKO_RenderPass(int id) {
		this(id, 0, true, null);
	}
	
	public GKO_RenderPass(int id, int t) {
		this(id, t, true, null);
	}
	
	public GKO_RenderPass(int id, int t, boolean a) {
		this(id, t, a, null);
	}
	
	public GKO_RenderPass(int id, int t, boolean a, GKO_RenderPass depth) {
		items = new ArrayList<KObj_Renderable>();
		type = t;
		this.id = id;
		this.auto = a;
		if (a)
			scan();
	}
	
	public int itemCount() {
		return items.size();
	}
	
	public int getTextureId() {
		return texId;
	}
	
	public int getType() {
		return type;
	}
	
	public void consider(KObj_Renderable f) {
		if ((id & f.getId()) == id)
			items.add(f);
	}
	
	public void forceAdd(KObj_Renderable f) {
		items.add(f);
	}
	
	public void setType(int t) {
		type = t;
		typechanged = true;
	}
	
	public void scan() {
		
	}
	
	public OKO_Camera_ getCamera() {
		return cameraOverride ? camera : Kondion.getCurrentCamera();
	}

	public void render() {
		if (!sizeOverride) {
			// No multiple window support yet!
			width = Window.getWidth();
			height = Window.getHeight();
		}
		if (typechanged) {
			glDeleteTextures(texId);
			GL30.glDeleteRenderbuffers(drbId);
			reFB();
			typechanged = false;
		}
		if (!ready) {
			if (!framebuffered) {
				fboId = glGenFramebuffersEXT();
				reFB();
				framebuffered = true;
			}
			//glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
			//		GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, drbId);
			ready = EXTFramebufferObject.glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT) == EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT;
			System.out.println("Create Buffers for Render Pass");
			
		} else {
			if (Kondion.showPrespective) {
				glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
				glClearColor(Kondion.getWorld().skyColor.x, Kondion.getWorld().skyColor.y,
						Kondion.getWorld().skyColor.z, Kondion.getWorld().skyColor.w);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
				if (cameraOverride && camera != null) 
					TTT.three(camera);
				else
					TTT.three();
				
				glPushMatrix();
				new KMat_Monotexture().bind(type);
				KLoader.textures.get("neat").bind();
				glTranslatef(-getCamera().actTransform.m30, -getCamera().actTransform.m31 + 10, -getCamera().actTransform.m32);
				Kondion.km.draw();
				glPopMatrix();
				
				for (KObj_Renderable kobj : items) {
					kobj.render(type, this);
				}
			}
			//glDisable(GL_DEPTH_TEST);
			/*for (KObj_Renderable kobj : items) {
				if (kobj instanceof KObj_Oriented) {
					((KObj_Oriented) kobj).updateB();
					if (Kondion.showPrespective && kobj instanceof KObj_Renderable)
						((KObj_Renderable) kobj).render(type);
				}
			}*/
		}
	}

	private void reFB() {
		
		drbId = glGenRenderbuffersEXT();
		texId = glGenTextures();//KondionLoader.getMissingTexture().getTextureId();
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
		
		//GL13.glActiveTexture(GL13.GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texId);
		
		
		if (type != DEPTH) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width,
					height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer)
					null); // Create the texture data
			
			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,
					GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D,
					texId, 0);
			
			glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, drbId);
			glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT,
					GL14.GL_DEPTH_COMPONENT24, width,
				height);
			glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, drbId);
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width,
					height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (java.nio.ByteBuffer)
					null); // Create the texture data
			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,
					GL_DEPTH_ATTACHMENT_EXT, GL_TEXTURE_2D,
					texId, 0);
		}
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		System.out.println("Retextured");
	}
	
	@Override
	public void update() {
		defaultUpdate();
	}

	@Override
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texId);
	}
}
