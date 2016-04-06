package vendalenger.kondion.lwjgl.resource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import vendalenger.kondion.lwjgl.TTT;

public class KModel {
	
	private String source;
	private String dtext;
	private int vert_handle;
	private int norm_handle;
	private int cord_handle;
	private FloatBuffer vert_data;
	private FloatBuffer norm_data;
	private FloatBuffer cord_data;
	private boolean loaded = false;
	private boolean selected = false;
	private boolean clicked = false;
	private ArrayList<Vector3f> vertices;
	private ArrayList<Vector3f> normals;
	private ArrayList<Vector2f> texture;
	private ArrayList<Face> faces; // Come and take a look at my beautiful list of faces :D
	
	public KModel(String path, String text) {
		source = path;
		dtext = text;
		vertices = new ArrayList<Vector3f>();
		normals = new ArrayList<Vector3f>();
		texture = new ArrayList<Vector2f>();
		faces = new ArrayList<Face>();
	}

	private void createVbo() {
		
		vert_data = BufferUtils.createFloatBuffer(faces.size() * 3 * 3);
		norm_data = BufferUtils.createFloatBuffer(faces.size() * 3 * 3);
		cord_data = BufferUtils.createFloatBuffer(faces.size() * 2 * 3);
		
		System.out.println("face: " + faces.size());
		//System.out.println("norm: " + faces.size());
		System.out.println("text: " + texture.size());
		
		for (int i = 0; i < faces.size(); i++) {
			vert_data.put(new float[] {vertices.get(faces.get(i).corns.get(2).vertex - 1).x, vertices.get(faces.get(i).corns.get(2).vertex - 1).y, vertices.get(faces.get(i).corns.get(2).vertex - 1).z});
			vert_data.put(new float[] {vertices.get(faces.get(i).corns.get(0).vertex - 1).x, vertices.get(faces.get(i).corns.get(0).vertex - 1).y, vertices.get(faces.get(i).corns.get(0).vertex - 1).z});
			vert_data.put(new float[] {vertices.get(faces.get(i).corns.get(1).vertex - 1).x, vertices.get(faces.get(i).corns.get(1).vertex - 1).y, vertices.get(faces.get(i).corns.get(1).vertex - 1).z});
			
			if (faces.get(i).normal){
				norm_data.put(new float[] {normals.get(faces.get(i).corns.get(2).normal - 1).x, normals.get(faces.get(i).corns.get(2).normal - 1).y, normals.get(faces.get(i).corns.get(2).normal - 1).z});
				norm_data.put(new float[] {normals.get(faces.get(i).corns.get(0).normal - 1).x, normals.get(faces.get(i).corns.get(0).normal - 1).y, normals.get(faces.get(i).corns.get(0).normal - 1).z});
				norm_data.put(new float[] {normals.get(faces.get(i).corns.get(1).normal - 1).x, normals.get(faces.get(i).corns.get(1).normal - 1).y, normals.get(faces.get(i).corns.get(1).normal - 1).z});
			}
			if (faces.get(i).textured) {
				cord_data.put(new float[] {texture.get(faces.get(i).corns.get(2).texture - 1).x, texture.get(faces.get(i).corns.get(2).texture - 1).y});
				cord_data.put(new float[] {texture.get(faces.get(i).corns.get(0).texture - 1).x, texture.get(faces.get(i).corns.get(0).texture - 1).y});
				cord_data.put(new float[] {texture.get(faces.get(i).corns.get(1).texture - 1).x, texture.get(faces.get(i).corns.get(1).texture - 1).y});
			}
		}
		
		vert_data.flip();
		norm_data.flip();
		cord_data.flip();
		
        vert_handle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vert_handle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vert_data, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        
        norm_handle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, norm_handle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, norm_data, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        
        cord_handle = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cord_handle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cord_data, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        
        System.out.println("verthandle: " + vert_handle);
        
        loaded = true;
        
        //vertices.clear();
    	//normals.clear();
    	//texture.clear();
    	//faces.clear();
 
	}
	
	public void draw() {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vert_handle);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0l);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, norm_handle);
		GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0l);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cord_handle);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0l);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, faces.size() * 3);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}
	
	public void load() {
		boolean output = false;
		try {
			//double plus = words / 100;
			BufferedReader reader = new BufferedReader(new InputStreamReader(KLoader.get(source)));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("# ")) {
					// Its a comment
					System.out.println("Message: " + line);
			
				}
			
				if (line.startsWith("v ")) {
					Vector3f vert = new Vector3f(Float.parseFloat(line.split(" ")[1]), Float.parseFloat(line.split(" ")[2]), Float.parseFloat(line.split(" ")[3]));
					vertices.add(vert);
					if (output) System.out.println("Vertex: x: " + vert.x + " y: " + vert.y + " z: " + vert.z);
				
				}
			
				if (line.startsWith("vt ")) {
					Vector2f text = new Vector2f(Float.parseFloat(line.split(" ")[1]), Float.parseFloat(line.split(" ")[2]));
					texture.add(text);
					if (output) System.out.println("Texture Coordinate: x:" + text.x + " y: " + text.y + " ");
				
				}
			
				if (line.startsWith("vn ")) {
					Vector3f norm = new Vector3f(Float.parseFloat(line.split(" ")[1]), Float.parseFloat(line.split(" ")[2]), Float.parseFloat(line.split(" ")[3]));
					this.normals.add(norm);
					if (output) System.out.println("Normal: x: " + norm.x + " y: " + norm.y + " z: " + norm.z);
				
				}
			
				if (line.startsWith("f ")) {
					Face f = new Face();
					f.sides = line.split(" ").length - 1;
					if (output) System.out.println("Face: # of faces:" + f.sides);
					if (f.sides != 3) {}
					for (int i = 0; i < line.split(" ").length - 1; i++) {
						Corn c = new Corn();
						if (line.split(" ")[i + 1].contains("//")){
							if (output) System.out.println("V/N ");
							f.textured = false;
							c.vertex = Integer.parseInt(line.split(" ")[i + 1].split("//")[0]);
							c.normal = Integer.parseInt(line.split(" ")[i + 1].split("//")[1]);
						} else if (line.split(" ")[i + 1].contains("/")){
							if (line.split(" ")[i + 1].split("/").length == 3){
								if (output) System.out.println("V/T/N ");
								c.vertex = Integer.parseInt(line.split(" ")[i + 1].split("/")[0]);
								c.texture = Integer.parseInt(line.split(" ")[i + 1].split("/")[1]);
								c.normal = Integer.parseInt(line.split(" ")[i + 1].split("/")[2]);
								if (output) System.out.println("VERT: " + this.vertices.get(c.vertex - 1));
							} else {
								if (output) System.out.println("V/T ");
								f.normal = false;
								c.vertex = Integer.parseInt(line.split(" ")[i + 1].split("/")[0]);
								c.texture = Integer.parseInt(line.split(" ")[i + 1].split("/")[1]);
							
							}
						
						} else {
							if (output) System.out.println("V ");
							f.normal = false;
							f.textured = false;
							c.vertex = Integer.parseInt(line.split(" ")[i + 1]);
						}
						f.corns.add(c);
					
					}
					faces.add(f);
					
				}
			}
			reader.close();
			
			Vector3f av = new Vector3f();
			for (int i = 0; i < vertices.size(); i++) {
				av.x += vertices.get(i).x;
				av.y += vertices.get(i).y;
				av.z += vertices.get(i).z;
			}
			
			av.x /= vertices.size();
			av.y /= vertices.size();
			av.z /= vertices.size();
			
			System.out.println(toString());
			
		} catch (FileNotFoundException e) {
			TTT.error("FileNotFoundException: \n" + e.getMessage());
		} catch (NumberFormatException e) {
			TTT.error("NumberFormatException: \n" + e.getMessage());
		} catch (IOException e) {
			TTT.error("IOException: \n" + e.getMessage());
		}
		
		createVbo();
	}
	
	public void unload() {
		
	}
	
	public String getDefaultTexture() {
		return dtext;
	}
	
}

class Corn { // just use a struct
	public int vertex = 0;
	public int texture = 0;
	public int normal = 0;
}

class Face {
	public ArrayList<Corn> corns = new ArrayList<Corn>();
	public boolean normal = true;
	public boolean textured = true;
	public int sides = 3;
}