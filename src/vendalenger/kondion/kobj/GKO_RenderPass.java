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

import vendalenger.kondion.Kondion;
import vendalenger.kondion.js.JSDrawable;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionTexture;
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
		CUSTOM		= 9,
		DEFERRED	= 10,
		HRD_RESULT	= 11,
		GUI			= 20;
	
	private List<KObj_Renderable> items;
	private KondionTexture texture;
	private boolean framebuffered = false;
	private boolean ready = false;
	private int drbId = 0;
	private int fboId = 0;
	private int texId = 0;
	public OKO_Camera_ camera;
	public boolean disable = false;
	public boolean auto = true;
	public boolean cameraOverride = false;
	public boolean sizeOverride = false;
	public int width, height;
	public int type;
	public int id;
	
	public GKO_RenderPass() {
		items = new ArrayList<KObj_Renderable>();
		camera = null;
		type = 0;
	}
	
	public GKO_RenderPass(int t) {
		items = new ArrayList<KObj_Renderable>();
		type = t;
	}
	
	public int getTextureId() {
		return texId;
	}
	
	public void addItem(KObj_Renderable f) {
		items.add(f);
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
		if (!ready) {
			if (!framebuffered) {
				drbId = glGenRenderbuffersEXT();
				fboId = glGenFramebuffersEXT();
				texId = glGenTextures();//KondionLoader.getMissingTexture().getTextureId();
				
				//System.out.println(height);
				
				glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
				
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, texId);
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width,
				height, 0, GL_RGB, GL_UNSIGNED_BYTE, (java.nio.ByteBuffer)
				null); // Create the texture data
				
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	
				glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,
						GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D,
						texId, 0);
				
				glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, drbId);
				glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT,
						GL14.GL_DEPTH_COMPONENT24, width,
						height);
				glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, drbId);
				glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
				
				framebuffered = true;
			}
			//glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
			//		GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, drbId);
			ready = EXTFramebufferObject.glCheckFramebufferStatusEXT(GL_FRAMEBUFFER_EXT) == EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT;
			System.out.println("Create Buffers for Render Pass");
			
		} else {
			
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fboId);
			glClearColor(Kondion.getWorld().skyColor.x, Kondion.getWorld().skyColor.y,
					Kondion.getWorld().skyColor.z, Kondion.getWorld().skyColor.w);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
			if (cameraOverride && camera != null) 
				TTT.three(camera);
			else
				TTT.three();
			for (KObj_Renderable kobj : items) {
				if (kobj instanceof KObj_Oriented) {
					((KObj_Oriented) kobj).updateB();
					if (Kondion.showPrespective && kobj instanceof KObj_Renderable)
						((KObj_Renderable) kobj).render(type, this);
				}
			}
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
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

	@Override
	public void update() {
		defaultUpdate();
	}

	@Override
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texId);
	}
}
