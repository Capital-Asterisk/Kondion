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

package vendalenger.kondion.lwjgl;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import vendalenger.kondion.lwjgl.resource.KondionTexture;
import vendalenger.port.FileShortcuts;

public class GLDrawing {

	private static int vbo_texCoords;
	private static int vbo_unitSquare;

	private static int vbo_cube;

	// private static ArrayList<int[]> canvasTextures = new ArrayList<int[]>();
	private static FloatBuffer texCoords;

	public static void renderCube(float size, KondionTexture t) {
		glPushMatrix();
		glEnable(GL_TEXTURE_2D);
		glScalef(size, size, size);
		t.bind();

		// setCoords(new float[] {1, 1, 0, 1, 0, 0, 1, 0});

		// floats are equal to 4 bytes
		// "Interleaved Data"
		glBindBuffer(GL_ARRAY_BUFFER, vbo_cube);
		glVertexPointer(3, GL_FLOAT, 32, 0l); // First object
		glNormalPointer(GL_FLOAT, 32, 12l); // Second
		glTexCoordPointer(2, GL_FLOAT, 32, 24l); // Third
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		// System.out.print("EGGUS");
		glDrawArrays(GL_TRIANGLES, 0, 36);

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glPopMatrix();
	}

	public static void renderQuad(float width, float height) {
		glPushMatrix();
		glScalef(width, height, 1);

		glBindBuffer(GL_ARRAY_BUFFER, vbo_unitSquare);
		glVertexPointer(3, GL_FLOAT, 24, 0l); // First object
		glNormalPointer(GL_FLOAT, 24, 12l); // Second
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, vbo_texCoords);
		glTexCoordPointer(2, GL_FLOAT, 0, 0l);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glDrawArrays(GL_QUADS, 0, 8);

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);

		glPopMatrix();
	}

	public static void renderQuad(float width, float height, KondionTexture t) {
		glPushMatrix();
		glEnable(GL_TEXTURE_2D);
		glScalef(width, height, 1);
		t.bind();

		glBindBuffer(GL_ARRAY_BUFFER, vbo_unitSquare);
		glVertexPointer(3, GL_FLOAT, 24, 0l); // First object
		glNormalPointer(GL_FLOAT, 24, 12l); // Second
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, vbo_texCoords);
		glTexCoordPointer(2, GL_FLOAT, 0, 0l);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glDrawArrays(GL_QUADS, 0, 8);

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);

		glPopMatrix();
	}

	public static void renderBillboardRec(float width, float height) {
		glPushMatrix();
		glDisable(GL_TEXTURE_2D);
		glScalef(width, height, 1);

		glBindBuffer(GL_ARRAY_BUFFER, vbo_unitSquare);
		glVertexPointer(3, GL_FLOAT, 24, 0l); // First object
		glNormalPointer(GL_FLOAT, 24, 12l); // Second
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, vbo_texCoords);
		glTexCoordPointer(2, GL_FLOAT, 0, 0l);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glDrawArrays(GL_QUADS, 0, 8);

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);

		glPopMatrix();
	}

	public static void rotate2D(float ang) {
		glRotatef(ang, 0, 0, 1);
	}

	public static void scale2D(float x, float y) {
		glScalef(x, y, 1);
	}

	public static void setCoords(float[] i) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo_texCoords);
		texCoords.rewind();
		texCoords.put(i);
		texCoords.flip();
		glBufferSubData(GL_ARRAY_BUFFER, 0, texCoords); // wrong
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public static void setup() {

		// Plane

		texCoords = BufferUtils.createFloatBuffer(8);
		texCoords.put(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
		texCoords.flip();
		vbo_texCoords = glGenBuffers();

		glBindBuffer(GL_ARRAY_BUFFER, vbo_texCoords);
		glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Interleaved Vertex, Normal
		FloatBuffer square = BufferUtils.createFloatBuffer(24);
		square.put(new float[] {0.5f, 0.5f, 0.0f});
		square.put(new float[] {0.0f, 0.0f, 1.0f});
		square.put(new float[] {-0.5f, 0.5f, 0.0f});
		square.put(new float[] {0.0f, 0.0f, 1.0f});
		square.put(new float[] {-0.5f, -0.5f, 0.0f});
		square.put(new float[] {0.0f, 0.0f, 1.0f});
		square.put(new float[] {0.5f, -0.5f, 0.0f});
		square.put(new float[] {0.0f, 0.0f, 1.0f});
		square.flip();
		vbo_unitSquare = glGenBuffers();

		glBindBuffer(GL_ARRAY_BUFFER, vbo_unitSquare);
		glBufferData(GL_ARRAY_BUFFER, square, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Cube

		FloatBuffer interleaved = BufferUtils.createFloatBuffer(288);

		// VERTEX/COORD/NORM in obj
		// Vertex, Normal, TexCoord here
		try {
			// Already calculated data for cube
			interleaved.put(fromResource(GLDrawing.class.getResourceAsStream("/vendalenger/models/cube")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		interleaved.flip();

		vbo_cube = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo_cube);
		glBufferData(GL_ARRAY_BUFFER, interleaved, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * 
	 * @param is Input data
	 * @return
	 * @throws IOException
	 */
	public static float[] fromResource(InputStream is) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		String longstring = "";
		while ((line = br.readLine()) != null) {
			longstring += line;
		}
		br.close();
		
		String[] split = longstring.split(",");
		float[] eggs = new float[split.length];
		
		for (int i = 0; i < split.length; i++) {
			eggs[i] = Float.valueOf(split[i]);
		}
		
		return eggs;
	}

	public static void translate2D(float x, float y) {
		glTranslatef(x, y, 0);
	}
}
