package vendalenger.kondion.lwjgl.resource;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class KondionModel {
	int vert_handle;
	int norm_handle;
	int cord_handle;
	FloatBuffer vert_data;
	FloatBuffer norm_data;
	FloatBuffer cord_data;
	float radius = 24;
	boolean vbo = false;
	boolean selected = false;
	boolean clicked = false;
	String name = "Unnammed";
	ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
	ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
	ArrayList<Vector2f> texture = new ArrayList<Vector2f>();
	ArrayList<Face> faces = new ArrayList<Face>(); //Come and take a look at my beautiful list of faces :D
	
	public void createVbo(){
		vbo = true;
		
		vert_data = BufferUtils.createFloatBuffer(faces.size() * 3 * 3);
		norm_data = BufferUtils.createFloatBuffer(faces.size() * 3 * 3);
		cord_data = BufferUtils.createFloatBuffer(texture.size() * 2 * 3);
		
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
}

class Corn{
	int vertex = 0;
	int texture = 0;
	int normal = 0;
	
}

class Face{
	ArrayList<Corn> corns = new ArrayList<Corn>();
	boolean normal = true;
	boolean textured = true;
	int sides = 3;
	
}