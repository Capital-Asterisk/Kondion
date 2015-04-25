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

package vendalenger.kondion.scene;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector3f;

import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.resource.KondionLoader;

public class Scene {

	public static final byte AA_A = 0, WEDGE_A = 1, WEDGE_B = 2;
	public static final byte B_SOLID = 0, B_CUTOUT = 1, B_EXCEPT = 2;

	private FloatBuffer vertData;
	private FloatBuffer normData;
	private FloatBuffer cordData;
	private int vertHandle;
	private int normHandle;
	private int cordHandle;

	private List<Collider> colliders;
	private boolean solidWorld = false;

	public Scene() {
		colliders = new ArrayList<Collider>();
	}

	public void doGlBuffers() {

		List<Float> verts = new ArrayList<Float>();
		List<Float> norms = new ArrayList<Float>();
		List<Float> cords = new ArrayList<Float>();

		Vector3f a = new Vector3f();
		Vector3f b = new Vector3f();
		Vector3f c = new Vector3f();
		Vector3f d = new Vector3f();

		for (int i = 0; i < colliders.size(); i++) {
			if (colliders.get(i).type == AA_A) {
				// loop through each face
				System.out.println("EGGSUS");
				// top face
				a.set(-colliders.get(i).we, colliders.get(i).up,
						-colliders.get(i).no);
				b.set(colliders.get(i).ea, colliders.get(i).up,
						-colliders.get(i).no);
				c.set(-colliders.get(i).we, colliders.get(i).up,
						colliders.get(i).so);
				d.set(colliders.get(i).ea, colliders.get(i).up,
						colliders.get(i).so);

				TTT.quad(verts, a, b, c, d);

				cords.add(0.0f);
				cords.add(1.0f);

				cords.add(1.0f);
				cords.add(1.0f);

				cords.add(0.0f);
				cords.add(0.0f);

				cords.add(0.0f);
				cords.add(0.0f);

				cords.add(1.0f);
				cords.add(1.0f);

				cords.add(1.0f);
				cords.add(0.0f);

				a.set(-colliders.get(i).we, -colliders.get(i).dn,
						-colliders.get(i).no);
				b.set(colliders.get(i).ea, -colliders.get(i).dn,
						-colliders.get(i).no);
				c.set(-colliders.get(i).we, -colliders.get(i).dn,
						colliders.get(i).so);
				d.set(colliders.get(i).ea, -colliders.get(i).dn,
						colliders.get(i).so);

				TTT.quad(verts, a, b, c, d);

				cords.add(0.0f);
				cords.add(1.0f);

				cords.add(1.0f);
				cords.add(1.0f);

				cords.add(0.0f);
				cords.add(0.0f);

				cords.add(0.0f);
				cords.add(0.0f);

				cords.add(1.0f);
				cords.add(1.0f);

				cords.add(1.0f);
				cords.add(0.0f);
			}
		}

		vertData = BufferUtils.createFloatBuffer(verts.size());
		normData = BufferUtils.createFloatBuffer(norms.size());
		cordData = BufferUtils.createFloatBuffer(cords.size());

		// cordData.flip();

		TTT.listPutFloatBuffer(verts, vertData);
		TTT.listPutFloatBuffer(norms, normData);
		TTT.listPutFloatBuffer(cords, cordData);

		vertData.flip();
		normData.flip();
		cordData.flip();

		vertHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertData, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// normHandle = GL15.glGenBuffers();
		// GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normHandle);
		// GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normData,
		// GL15.GL_STATIC_DRAW);
		// GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		cordHandle = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cordHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, cordData, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public void render() {

		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertHandle);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0l);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normHandle);
		// GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0l);
		// GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cordHandle);
		GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0l);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		KondionLoader.textures.get("noah").bind();
		// GL11.glDisable(GL11.GL_TEXTURE_2D);
		// GL11.glColor3f(0.0f, 1.0f, 1.0f);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertData.capacity() / 3);

		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		// GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	public void addAABlock(Vector3f vector3f, int up, int dn, int no, int ea,
			int so, int we) {
		Collider c = new Collider();
		c.type = AA_A;
		c.up = up;
		c.dn = dn;
		c.no = no;
		c.ea = ea;
		c.so = so;
		c.we = we;
		colliders.add(c);
	}
}

class Collider {
	public byte type;
	public float up, dn, no, ea, so, we;
	public int x, y, z;
}