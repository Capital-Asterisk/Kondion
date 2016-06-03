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

package vendalenger.kondion.lwjgl.resource;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;

import vendalenger.kondion.js.JSDrawable;

public class KTexture implements JSDrawable {

	public int width, height;
	private String source;
	private boolean internal;
	private boolean isLoaded;
	private boolean mipmapped;
	private int imageWidth, imageHeight;
	private int minFilter, magFilter;
	private int textureId;
	private int wrapS, wrapT;

	public KTexture(String path, int miFilter,
			int maFilter, int awrapS, int awrapT) {
		//textureId = id;
		source = path;
		imageWidth = width;
		imageHeight = height;
		minFilter = miFilter;
		magFilter = maFilter;
		wrapS = awrapS;
		wrapT = awrapT;
		internal = false;
		//mipmapped = mipped;
		//this.width = width;
		//this.height = height;
	}
	
	public KTexture(int id, int miFilter, int width, int height,
			int maFilter, int awrapS, int awrapT, boolean mipped) {
		source = "INTERNAL";
		textureId = id;
		imageWidth = width;
		imageHeight = height;
		minFilter = miFilter;
		magFilter = maFilter;
		wrapS = awrapS;
		wrapT = awrapT;
		mipmapped = mipped;
		internal = true;
		this.width = width;
		this.height = height;
	}
	
	public void load() {
		if (!internal && !isLoaded) {
			// loading the image
			
			try {
				BufferedImage i = ImageIO.read(KLoader.get(source));
				
				// flip the image
				AffineTransform transform = AffineTransform.getScaleInstance(1f,
						-1f);
				transform.translate(0, -i.getHeight());
				AffineTransformOp operation = new AffineTransformOp(transform,
						AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				i = operation.filter(i, null);

				// Putting pixel data in a buffer
				int[] pixels = new int[i.getWidth() * i.getHeight()];
				i.getRGB(0, 0, i.getWidth(), i.getHeight(), pixels, 0, i.getWidth());
				ByteBuffer buffer = BufferUtils.createByteBuffer(i.getWidth()
						* i.getHeight() * 4);
				for (int y = 0; y < i.getHeight(); y++) {
					for (int x = 0; x < i.getWidth(); x++) {
						int pixel = pixels[y * i.getWidth() + x];
						buffer.put((byte) ((pixel >> 16) & 0xFF));
						buffer.put((byte) ((pixel >> 8) & 0xFF));
						buffer.put((byte) (pixel & 0xFF));
						buffer.put((byte) ((pixel >> 24) & 0xFF));
					}
				}
				buffer.flip();

				// Creating the opengl texture
				textureId = glGenTextures();
				glBindTexture(GL_TEXTURE_2D, textureId);

				// set filters and wraps
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);

				// generate mipmaps if required
				boolean mipmap = (minFilter == GL_NEAREST_MIPMAP_NEAREST
						|| minFilter == GL_NEAREST_MIPMAP_LINEAR
						|| minFilter == GL_LINEAR_MIPMAP_NEAREST || minFilter == GL_LINEAR_MIPMAP_LINEAR);
				if (mipmap) {
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 2);
					glTexParameteri(GL_TEXTURE_2D, GL_GENERATE_MIPMAP, GL_TRUE);
				}
				
				
				// put pixel data into texture
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, i.getWidth(),
						i.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

				// Pack it up
				width = i.getWidth();
				height = i.getHeight();
				//KTexture kt = new KTexture(tex, i.getWidth(),
				//		i.getHeight(), minFilter, magFilter, wrapS, wrapT, mipmap);
				
				System.out.println("Loaded Texture: " + source);
				isLoaded = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("TEXTURE ALREADUY LOADED");
		}
	}
	
	public void unLoad() {
		if (isLoaded && !internal) {
			System.out.println("Unloaded Texture: " + source);
			glDeleteTextures(textureId);
		} else {
			System.out.println("Cannot unload " + source);
		}
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureId);
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public int getImageWidth() {
		return imageWidth;
	}
	
	public String getSource() {
		return source;
	}

	public int getTextureId() {
		return textureId;
	}

	public int getWrapS() {
		return wrapS;
	}

	public int getWrapT() {
		return wrapT;
	}

	public boolean isMipmap() {
		return mipmapped;
	}

	public static void unBind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
