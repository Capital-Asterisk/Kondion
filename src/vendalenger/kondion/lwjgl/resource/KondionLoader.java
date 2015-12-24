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

import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class KondionLoader {

	public static List<Object[]> queue;
	public static HashMap<String, KondionTexture> textures;

	private static KondionTexture missingTexture;
	
	/**
	 * Initialize textures and queue
	 */
	public static void init() {
		textures = new HashMap<String, KondionTexture>();
		queue = new ArrayList<Object[]>();
		missingTexture = null;
	}

	/**
	 * Load the queue. All the textures and shaders queued in queueTexture /
	 * shader are loaded.
	 */
	public static void load() {
		
		if (missingTexture == null) {
			missingTexture = registerTexture(KondionLoader.class.getResourceAsStream("missingno.png"),
					"Missing_Texture", GL_NEAREST, GL_NEAREST,
					GL_REPEAT, GL_REPEAT, false);
		}
		for (int i = 0; i < queue.size(); i++) {
			if ((boolean) queue.get(i)[0]) {
				// its a shader
			} else {
				// its a texture
				registerTexture((File) queue.get(i)[1],
						(String) queue.get(i)[2], (int) queue.get(i)[3],
						(int) queue.get(i)[4], (int) queue.get(i)[5],
						(int) queue.get(i)[6], true);
			}
		}
	}

	/**
	 * Load a nash. (sh on nash already means shader)
	 * 
	 * @param nash
	 *            The .nash shader file being loaded.
	 */
	public static KondionShader loadNashShader(File nash) {
		boolean mode_vert = false;
		boolean mode_frag = false;
		BufferedReader reader;
		KondionShader nash_shader = null;
		String line;
		StringBuilder vertSB = new StringBuilder();
		StringBuilder fragSB = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(nash));
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("#mode")) {
					if (mode_vert)
						vertSB.append(line + "\n");
					if (mode_frag)
						fragSB.append(line + "\n");
				} else {
					mode_vert = false;
					mode_frag = false;
					String[] mode = line.split(" ");
					for (int i = 0; i < mode.length; i++) {
						if (i != 0) {
							// System.out.println(mode[i]);
							if (mode[i].startsWith("//"))
								break;
							if (mode[i].startsWith("vert")) {
								mode_vert = true;
							}
							if (mode[i].startsWith("frag")) {
								mode_frag = true;
							}
						}
					}
				}
			}
			reader.close();
			int program = glCreateProgram();
			int vertShader = newShader(GL_VERTEX_SHADER, vertSB.toString(),
					nash.getPath() + " (VERTEX)");
			int fragShader = newShader(GL_FRAGMENT_SHADER, fragSB.toString(),
					nash.getPath() + " (FRAGMENT)");
			glAttachShader(program, vertShader);
			glAttachShader(program, fragShader);
			int loc = glGetUniformLocation(program, "texture1");
			glUniform1i(loc, 0);
			glLinkProgram(program);

			nash_shader = new KondionShader(vertShader, fragShader, program);
			/*
			 * int vert_shader = ARBShaderObjects
			 * .glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
			 * ARBShaderObjects.glShaderSourceARB(vert_shader,
			 * vertSB.toString());
			 * ARBShaderObjects.glCompileShaderARB(vert_shader); if
			 * (ARBShaderObjects.glGetObjectParameteriARB(vert_shader,
			 * ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE) {
			 * System.out.println("Failed to load Vertex shader: " +
			 * nash.getPath()); }
			 * 
			 * int frag_shader = ARBShaderObjects
			 * .glCreateShaderObjectARB(ARBFragmentShader
			 * .GL_FRAGMENT_SHADER_ARB);
			 * ARBShaderObjects.glShaderSourceARB(frag_shader,
			 * fragSB.toString());
			 * ARBShaderObjects.glCompileShaderARB(frag_shader); if
			 * (ARBShaderObjects.glGetObjectParameteriARB(frag_shader,
			 * ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE) {
			 * System.out.println("Failed to load Fragment shader: " +
			 * nash.getPath()); System.out
			 * .println(ARBShaderObjects.glGetInfoLogARB( vert_shader,
			 * ARBShaderObjects .glGetObjectParameteriARB( vert_shader,
			 * ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB))); }
			 * 
			 * int program = ARBShaderObjects.glCreateProgramObjectARB();
			 * ARBShaderObjects.glAttachObjectARB(program, vert_shader);
			 * ARBShaderObjects.glAttachObjectARB(program, frag_shader);
			 * ARBShaderObjects.glLinkProgramARB(program); if
			 * (ARBShaderObjects.glGetObjectParameteriARB(program,
			 * ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL_FALSE) {
			 * System.out.println("Failed to create Shader Program: " +
			 * nash.getPath()); System.out
			 * .println(ARBShaderObjects.glGetInfoLogARB( frag_shader,
			 * ARBShaderObjects .glGetObjectParameteriARB( frag_shader,
			 * ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB))); }
			 * ARBShaderObjects.glValidateProgramARB(program);
			 * 
			 * nash_shader = new KondionShader(vert_shader, frag_shader,
			 * program);
			 */

		} catch (FileNotFoundException e1) {
			System.err.println(e1.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nash_shader;
	}

	public static int newShader(int t, String code, String errName) {
		int s = glCreateShader(t);
		glShaderSource(s, code);
		glCompileShader(s);
		int c = glGetShaderi(s, GL_COMPILE_STATUS);
		String shaderLog = glGetShaderInfoLog(s);
		if (shaderLog.trim().length() > 0) {
			System.err.println(shaderLog);
		}
		if (c == 0) {
			throw new AssertionError("Error in compiling shader: " + errName);
		}
		return s;
	}

	/**
	 * Puts a texture into queue. The texture will be loaded once load() is
	 * called. see registerTexture
	 */
	public static void queueTexture(File image, String id, int minFilter,
			int magFilter, int wrapS, int wrapT) {
		// Use object arrays to queue textures to load later.
		queue.add(new Object[] {false, image, id, minFilter, magFilter, wrapS,
				wrapT});
	}
	

	/**
	 * 
	 * @param image
	 *            The path to the image file
	 * @param id
	 *            The string Id to create / replace
	 * @param minFilter
	 *            Min Filter use GL11.NEAREST of linear or anything
	 * @param magFilter
	 *            Mag Filter use GL11.NEAREST of linear or anything
	 * @param wrapS
	 * @param wrapT
	 * @return
	 */
	public static KondionTexture registerTexture(InputStream image, String id,
			int minFilter, int magFilter, int wrapS, int wrapT, boolean add) {
		try {
			// loading the image
			BufferedImage i = ImageIO.read(image);

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
			int tex = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, tex);

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
			KondionTexture kt = new KondionTexture(tex, i.getWidth(),
					i.getHeight(), minFilter, magFilter, wrapS, wrapT, mipmap);

			if (add) {
				if (textures.containsKey(id)) {
					System.out.println("Replacing texture: " + id);
					System.out.println("    with <UNKNOWN PATH>");
					textures.replace(id, kt);
				} else {
					System.out.println("Adding texture: " + id);
					System.out.println("    from <UNKNOWN PATH>");
					textures.put(id, kt);
				}
			} else {
				System.out.println("Loaded texture: " + id);
				System.out.println("    path <UNKNOWN PATH>");
			}

			// return stuff
			return kt;
		} catch (FileNotFoundException e) {
			// else return nulls
			System.err.println("Failed to load texture: " + id);
			System.err.println("    in <UNKNOWN PATH>");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("Failed to load texture: " + id);
			System.err.println("    in <UNKNOWN PATH>");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param image
	 *            The path to the image file
	 * @param id
	 *            The string Id to create / replace
	 * @param minFilter
	 *            Min Filter use GL11.NEAREST of linear or anything
	 * @param magFilter
	 *            Mag Filter use GL11.NEAREST of linear or anything
	 * @param wrapS
	 * @param wrapT
	 * @return
	 */
	public static KondionTexture registerTexture(File image, String id,
			int minFilter, int magFilter, int wrapS, int wrapT, boolean add) {
		try {
			// loading the image
			FileInputStream in = new FileInputStream(image);
			BufferedImage i = ImageIO.read(in);

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
			int tex = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, tex);

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
			KondionTexture kt = new KondionTexture(tex, i.getWidth(),
					i.getHeight(), minFilter, magFilter, wrapS, wrapT, mipmap);

			if (add) {
				if (textures.containsKey(id)) {
					System.out.println("Replacing texture: " + id);
					System.out.println("    with " + image.getCanonicalPath());
					textures.replace(id, kt);
				} else {
					System.out.println("Adding texture: " + id);
					System.out.println("    from " + image.getCanonicalPath());
					textures.put(id, kt);
				}
			} else {
				System.out.println("Loaded texture: " + id);
				System.out.println("    path " + image.getCanonicalPath());
			}

			// return stuff
			return kt;
		} catch (FileNotFoundException e) {
			// else return nulls
			System.err.println("Failed to load texture: " + id);
			System.err.println("    in " + image.getPath());
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("Failed to load texture: " + id);
			System.err.println("    in " + image.getPath());
			e.printStackTrace();
			return null;
		}
	}
	
	public static KondionTexture getMissingTexture() {
		return missingTexture;
	}
}
