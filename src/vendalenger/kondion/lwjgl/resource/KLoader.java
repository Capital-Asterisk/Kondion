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

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
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
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL14.GL_GENERATE_MIPMAP;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.*;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;

import lwjgl.IOUtil;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.port.FileShortcuts;

public class KLoader {

	public static List<Object[]> queue;
	public static List<KResource> resources;
	public static HashMap<String, KTexture> textures;
	public static HashMap<String, KShader> shaders;
	public static HashMap<String, KModel> obj;

	private static KTexture missingTexture;
	
	/**
	 * Initialize textures and queue
	 */
	public static void init() {
		textures = new HashMap<String, KTexture>();
		shaders = new HashMap<String, KShader>();
		obj = new HashMap<String, KModel>();
		resources = new ArrayList<KResource>();
		queue = new ArrayList<Object[]>();
		missingTexture = null;
		
		resources.add(new DefaultResource());
	}
	
	public static KResource addFolderResource(File folder) {
		if (folder.exists() && folder.isDirectory()) {
			FolderResource fr = new FolderResource(folder);
			fr.init();
			resources.add(fr);
			return fr;
		} else {
			//System.err.println();
			TTT.error("Directory \"" + folder.getPath() + "\" is not a valid directory");
		}
		return null;
	}
	
	public static KResource getKResource(String name) {
		for (KResource kres : resources) {
			//System.out.println(name + " == " + kres.getName() + " horse");
			if (kres.getName().equals(name)) {
				return kres;
			}
		}
		return null;
	}
	
	public static InputStream get(String path) {
		String[] split = path.split(":");
		System.out.println(path + " eggs " + split[0] + " eggus " + split[1]);
		return getKResource(split[0]).get(split[1]);
	}

	/**
	 * Load the queue. All the textures and shaders queued in queueTexture /
	 * shader are loaded.
	 * 
	 */
	public static void load() {
		
		if (missingTexture == null) {
			missingTexture = internalTexture(KLoader.class.getResourceAsStream("missingno.png"),
					"K_Missing", GL_LINEAR, GL_NEAREST,
					GL_REPEAT, GL_REPEAT, true);
			internalTexture(KLoader.class.getResourceAsStream("uvcube.png"),
					"K_Cube", GL_LINEAR, GL_NEAREST,
					GL_REPEAT, GL_REPEAT, true);
		}
		
		shaders.put("K_Strange", loadShader(
				KLoader.class.getResourceAsStream("/vendalenger/kondion/materials/glsl/solid_col.vert"),
				KLoader.class.getResourceAsStream("/vendalenger/kondion/materials/glsl/strange.frag"),
				"Thee strange shader with egg uniforms", 0));
		
		shaders.put("K_FlatCol", loadShader(
				KLoader.class.getResourceAsStream("/vendalenger/kondion/materials/glsl/solid_col.vert"),
				KLoader.class.getResourceAsStream("/vendalenger/kondion/materials/glsl/solid_col.frag"),
				"K_FlatCol", 0));
		
		shaders.put("K_Monotexture", loadShader(
				KLoader.class.getResourceAsStream("/vendalenger/kondion/materials/glsl/solid_col.vert"),
				KLoader.class.getResourceAsStream("/vendalenger/kondion/materials/glsl/solid_tex.frag"),
				"K_Monotexture", 1));
		
		shaders.put("K_DeferredRender", loadShader(
				KLoader.class.getResourceAsStream("/vendalenger/kondion/materials/glsl/solid_col.vert"),
				KLoader.class.getResourceAsStream("/vendalenger/kondion/materials/glsl/deferred.frag"),
				"K_DeferredRender", 4));
		
		shaders.put("K_AmbientLight", loadShader(
				KLoader.class.getResourceAsStream("/vendalenger/kondion/materials/glsl/solid_col.vert"),
				KLoader.class.getResourceAsStream("/vendalenger/kondion/materials/glsl/light_ambient.frag"),
				"K_AmbientLight", 4));
		
		// Default font informal loading (Ubuntu Mono)
		//STBTruetype
		System.out.println("EGGSU");
		try {
			// TODO put into function some time
			//InputStream is = KondionLoader.class.getResourceAsStream("/vendalenger/kondion/lwjgl/resource/UbuntuMono-R.ttf");
			//byte[] friendlyArray = new byte[is.available()];
			//is.read(friendlyArray);
			//ByteBuffer bloat = ByteBuffer.wrap(friendlyArray);
			ByteBuffer bloat = IOUtil.ioResourceToByteBuffer("vendalenger/kondion/lwjgl/resource/UbuntuMono-R.ttf", 205748);
			ByteBuffer porn = BufferUtils.createByteBuffer(512 * 512);
			System.out.println(bloat.remaining());
			STBTTBakedChar.Buffer stbttseemsneat = STBTTBakedChar.mallocBuffer(96);
			STBTruetype.stbtt_BakeFontBitmap(bloat, 32, porn, 512, 512, 32, stbttseemsneat);
			
			int tex = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, tex);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, 512,
					512, 0, GL_ALPHA, GL_UNSIGNED_BYTE, porn);
			glBindTexture(GL_TEXTURE_2D, 0);
			
			GLDrawing.setDefaultFont(stbttseemsneat, 32, tex);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("EGGSUS");
		for (int i = 0; i < queue.size(); i++) {
			if ((boolean) queue.get(i)[0]) {
				// its a shader
			} else {
				// its a texture
				//registerTexture((File) queue.get(i)[1],
				//		(String) queue.get(i)[2], (int) queue.get(i)[3],
				//		(int) queue.get(i)[4], (int) queue.get(i)[5],
				//		(int) queue.get(i)[6], true);
			}
		}
	}
	
	public static KShader loadShader(InputStream vert, InputStream frag, String name, int textureCount) {
		// create a null object
		KShader shader = null;
		
		try {
			// Create readers to read vert and frag files (or whatever)
			BufferedReader readV = new BufferedReader(new InputStreamReader(vert));
			BufferedReader readF = new BufferedReader(new InputStreamReader(frag));
			
			// Not using string builders is error. Encoding stuff...
			StringBuilder vertSB = new StringBuilder();
			StringBuilder fragSB = new StringBuilder();
			
			String line;
			
			// Read both files, append to string builders
			while ((line = readV.readLine()) != null) {
				vertSB.append(line + "\n");
			}
			
			// is this very inefficient?
			while ((line = readF.readLine()) != null) {
				fragSB.append(line + "\n");
			}
			
			// Close them, because I have to
			readV.close();
			readF.close();
			
			int program = glCreateProgram();
			int vertShader = newShader(GL_VERTEX_SHADER, vertSB.toString(),
					name + " (VERTEX)");
			int fragShader = newShader(GL_FRAGMENT_SHADER, fragSB.toString(),
					name + " (FRAGMENT)");
			glAttachShader(program, vertShader);
			glAttachShader(program, fragShader);
			//glUniform1i(glGetUniformLocation(program, "texture0"), 0);
			//glUniform1i(glGetUniformLocation(program, "texture1"), 1);
			
			
			
			//glUnifor
			
			glLinkProgram(program);
			glUseProgram(program);
			
			for (int i = 0; i < textureCount; i++) {
				//glUniform1i(glGetUniformLocation(program, "texture" + i), i);
				int fu = glGetUniformLocation(program, "texture" + i);
				//System.out.println("" +i + " " + fu);
				glUniform1i(fu, i);
				//System.out.println("res: " + glGetUniformi(program, fu));
			}
			
			shader = new KShader(vertShader, fragShader, program);
			
		} catch (IOException e) {
			System.err.println("Error loading shader: " + name);
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("Error loading shader: " + name);
			e.printStackTrace();
		}
		
		return shader;
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
	 *            The path to the image file in game:dir/file format
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
	public static KTexture registerTexture(String path, String id,
			int minFilter, int magFilter, int wrapS, int wrapT, boolean add) {
		// Pack it up
		KTexture kt = new KTexture(path, minFilter, magFilter, wrapS, wrapT);

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
	public static KTexture internalTexture(InputStream image, String id,
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
			KTexture kt = new KTexture(tex, i.getWidth(),
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
	/*public static KTexture registerTexture(File image, String id,
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
			KTexture kt = new KTexture(tex, i.getWidth(),
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
	}*/
	
	public static KTexture getMissingTexture() {
		return missingTexture;
	}
	
	/**
	 * Code from PureArm
	 */
	public static KModel loadObj(InputStream file) {
		KModel model = new KModel();
		boolean output = false;
		try {
			boolean tri = true;
			//int words = Conmeth.count(ob.getAbsolutePath());
			int currentline = 0;
			//double plus = words / 100;
			BufferedReader reader = new BufferedReader(new InputStreamReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				//Main.progress = (int)((double)currentline / plus);
				//if (output) System.out.println("Progress: " + (int)((double)currentline / plus));
				currentline++;
				if(line.startsWith("# ")){
					// Its a comment
					System.out.println("Message: " + line);
			
				}
			
				if(line.startsWith("v ")){
					Vector3f vert = new Vector3f(Float.parseFloat(line.split(" ")[1]), Float.parseFloat(line.split(" ")[2]), Float.parseFloat(line.split(" ")[3]));
					model.vertices.add(vert);
					if (output) System.out.println("Vertex: x: " + vert.x + " y: " + vert.y + " z: " + vert.z);
				
				}
			
				if(line.startsWith("vt ")){
					Vector2f text = new Vector2f(Float.parseFloat(line.split(" ")[1]), Float.parseFloat(line.split(" ")[2]));
					model.texture.add(text);
					if (output) System.out.println("Texture Coordinate: x:" + text.x + " y: " + text.y + " ");
				
				}
			
				if(line.startsWith("vn ")){
					Vector3f norm = new Vector3f(Float.parseFloat(line.split(" ")[1]), Float.parseFloat(line.split(" ")[2]), Float.parseFloat(line.split(" ")[3]));
					model.normals.add(norm);
					if (output) System.out.println("Normal: x: " + norm.x + " y: " + norm.y + " z: " + norm.z);
				
				}
			
				if(line.startsWith("f ")){
					Face f = new Face();
					f.sides = line.split(" ").length - 1;
					if (output) System.out.println("Face: # of faces:" + f.sides);
					if (f.sides != 3) tri = false;
					for (int i = 0; i < line.split(" ").length - 1; i++) {
						Corn c = new Corn();
						if (line.split(" ")[i + 1].contains("//")){
							if (output) System.out.println("V/N ");
							f.textured = false;
							c.vertex = Integer.parseInt(line.split(" ")[i + 1].split("//")[0]);
							c.normal = Integer.parseInt(line.split(" ")[i + 1].split("//")[1]);
						}else if (line.split(" ")[i + 1].contains("/")){
							if (line.split(" ")[i + 1].split("/").length == 3){
								if (output) System.out.println("V/T/N ");
								c.vertex = Integer.parseInt(line.split(" ")[i + 1].split("/")[0]);
								c.texture = Integer.parseInt(line.split(" ")[i + 1].split("/")[1]);
								c.normal = Integer.parseInt(line.split(" ")[i + 1].split("/")[2]);
								if (output) System.out.println("VERT: " + model.vertices.get(c.vertex - 1));
							}else{
								if (output) System.out.println("V/T ");
								f.normal = false;
								c.vertex = Integer.parseInt(line.split(" ")[i + 1].split("/")[0]);
								c.texture = Integer.parseInt(line.split(" ")[i + 1].split("/")[1]);
							
							}
						
						}else{
							if (output) System.out.println("V ");
							f.normal = false;
							f.textured = false;
							c.vertex = Integer.parseInt(line.split(" ")[i + 1]);
						}
						f.corns.add(c);
					
					}
					model.faces.add(f);
					
				}
			}
			reader.close();
			//Main.progress = 0;
			Vector3f av = new Vector3f();
			for (int i = 0; i < model.vertices.size(); i++) {
				av.x += model.vertices.get(i).x;
				av.y += model.vertices.get(i).y;
				av.z += model.vertices.get(i).z;
				
			}
			
			av.x /= model.vertices.size();
			av.y /= model.vertices.size();
			av.z /= model.vertices.size();
			//Main.componentc_1_load.setText("<Waiting user input>");
			//String name = (String)JOptionPane.showInputDialog(Main.jf, "Enter name for " + ob.getName(), ob.getName(), JOptionPane.QUESTION_MESSAGE);
			//model.name = name;
			//Main.componentc_1_load.setText("Load OBJ");
			//Main.componentc_1_load.setEnabled(true);
			System.out.println(model.toString());
			//if (tri){
			//	Space.workcomponents.add(model);
			//	Main.listUpdate();
			//}else TTT.Error("PureArm only uses Triangles. Please see Help > OBJ format");
		} catch (FileNotFoundException e) {
			TTT.error("FileNotFoundException: \n" + e.getMessage());
		} catch (NumberFormatException e) {
			TTT.error("NumberFormatException: \n" + e.getMessage());
		} catch (IOException e) {
			TTT.error("IOException: \n" + e.getMessage());
		}
		
		return model;
	}
}
