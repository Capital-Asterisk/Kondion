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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import vendalenger.kondion.lwjgl.resource.KondionTexture;

public class FlatDrawing {

	// private static ArrayList<int[]> canvasTextures = new ArrayList<int[]>();
	private static FloatBuffer texCoords;
	public static int vbo_texCoords;
	public static int vbo_unitSquare;

	public static void renderBillboard(float width, float height,
			KondionTexture t) {
		glPushMatrix();
		glEnable(GL_TEXTURE_2D);
		glScalef(width, height, 0);
		t.bind();

		setCoords(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
		glBindBuffer(GL_ARRAY_BUFFER, vbo_unitSquare);
		glVertexPointer(3, GL_FLOAT, 0, 0l);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, vbo_texCoords);
		glTexCoordPointer(2, GL_FLOAT, 0, 0l);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glDrawArrays(GL_QUADS, 0, 3 * 4);

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);

		glPopMatrix();
	}

	public static void renderBillboardRec(float width, float height) {
		glPushMatrix();
		glDisable(GL_TEXTURE_2D);
		glScalef(width, height, 0);

		setCoords(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
		glBindBuffer(GL_ARRAY_BUFFER, vbo_unitSquare);
		glVertexPointer(3, GL_FLOAT, 0, 0l);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, vbo_texCoords);
		glTexCoordPointer(2, GL_FLOAT, 0, 0l);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glDrawArrays(GL_QUADS, 0, 3 * 4);

		glDisableClientState(GL_VERTEX_ARRAY);
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
		texCoords = BufferUtils.createFloatBuffer(8);
		texCoords.put(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
		texCoords.flip();
		vbo_texCoords = glGenBuffers();

		glBindBuffer(GL_ARRAY_BUFFER, vbo_texCoords);
		glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		FloatBuffer square = BufferUtils.createFloatBuffer(12);
		square.put(new float[] {0.5f, 0.5f, 0.0f});
		square.put(new float[] {-0.5f, 0.5f, 0.0f});
		square.put(new float[] {-0.5f, -0.5f, 0.0f});
		square.put(new float[] {0.5f, -0.5f, 0.0f});
		square.flip();
		vbo_unitSquare = glGenBuffers();

		glBindBuffer(GL_ARRAY_BUFFER, vbo_unitSquare);
		glBufferData(GL_ARRAY_BUFFER, square, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

	}

	public static void translate2D(float x, float y) {
		glTranslatef(x, y, 0);
	}
}
