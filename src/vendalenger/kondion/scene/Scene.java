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

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.resource.KondionLoader;

public class Scene {

	public static final byte AA_A = 0, WEDGE_A = 1, WEDGE_B = 2;

	private FloatBuffer vertData;
	private FloatBuffer normData;
	private FloatBuffer cordData;
	private int vertHandle;
	private int normHandle;
	private int cordHandle;

	private List<Collider> colliders;
	private boolean solidWorld = true;

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
		
		Area[] walls = new Area[6];
		
		List<Vector2f> polys;

		Collider to;
		
		for (int i = 0; i < colliders.size(); i++) {
			to = colliders.get(i);
			if (to.shape == AA_A) {
				// loop through each face
				
				// Floor
				walls[0] = new Area(new Rectangle2D.Double(
						-to.we, -to.no,
						to.we + to.ea, 
						to.no + to.so));
				// Ceiling
				walls[1] = new Area(new Rectangle2D.Double(
						-to.we, -to.no,
						to.we + to.ea, 
						to.no + to.so));
				walls[1].subtract(new Area(new Rectangle2D.Double(-3, -3, 2, 2)));
				
				// North wall
				walls[2] = new Area(new Rectangle2D.Double(
						-to.we, -to.up,
						to.we + to.ea,
						to.up + to.dn));
				
				// East wall
				walls[3] = new Area(new Rectangle2D.Double(
						-to.so, -to.up,
						to.so + to.no,
						to.up + to.dn));
				walls[3].subtract(new Area(new Rectangle2D.Double(-1, -1, 2, 2)));
				
				// South wall
				walls[4] = new Area(new Rectangle2D.Double(
						-to.we, -to.up,
						to.we + to.ea,
						to.up + to.dn));
				walls[4].subtract(new Area(new Rectangle2D.Double(-3, -3, 2, 2)));
				
				// West Wall
				walls[5] = new Area(new Rectangle2D.Double(
						-to.so, -to.up,
						to.so + to.no,
						to.up + to.dn));
				
				// loop through other colliders and check for collisions
				
				boolean[] exposedWalls = new boolean[] {false, false, false, false, false, false};
				boolean xInt, yInt, zInt;
				Collider co = null;
				
				for (int j = 0; j < colliders.size(); j ++) {
					co = colliders.get(j);
					xInt = false;
					yInt = false;
					zInt = false;
					if (co != to) {
						// skip itself to prevent errors
						if (!to.solid) {
							// if this is cutout
							// make walls if it cuts into something or if world is solid
							
							// Get intersections, all intersections true means touching
							xInt = (to.x + to.ea + co.we >= co.x
									&& to.x - to.we - co.ea <= co.x);
							yInt = (to.y + to.up + co.dn >= co.y
									&& to.y - to.dn - co.up <= co.y);
							zInt = (to.z + to.so + co.no >= co.z
									&& to.z - to.no - co.so <= co.z);
							
							if (yInt && xInt && zInt) {
								// Get touching walls then subtract their areas

								// North
								walls[2].subtract(new Area(new Rectangle2D.Double(
									-co.we + (co.x - to.x), -co.up + (co.y - to.y),
									co.ea + co.we,
									co.up + co.dn)));
							}
							
							//System.out.println(xInt + " " + yInt + " " + zInt);
						}
					}
				}
				
				for (int j = 0; j < walls.length; j ++) {
					polys = TTT.areaToTriangles(walls[j]);
					for (int k = 0; k < polys.size(); k++) {
						switch (j) {
							case 0:
								a.set(polys.get(k).x, -to.dn,
										polys.get(k).y);
								break;
							case 1:
								a.set(polys.get(k).x, to.up,
										polys.get(k).y);
								break;
							case 2:
								a.set(polys.get(k).x, polys.get(k).y,
										-to.no);
								break;
							case 3:
								a.set(to.ea, polys.get(k).y,
										polys.get(k).x);
								break;
							case 4:
								a.set(polys.get(k).x, polys.get(k).y,
										to.so);
								break;
							case 5:
								a.set(-to.we, polys.get(k).y,
										polys.get(k).x);
								break;
						}
						a.translate(to.x, to.y, to.z);
						TTT.addVect(verts, a);
						cords.add(9 / (to.we + to.ea) * polys.get(k).x);
						cords.add(9 / (to.no + to.so) * polys.get(k).y);
					}
				}
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

		vertHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertHandle);
		glBufferData(GL_ARRAY_BUFFER, vertData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// normHandle = glGenBuffers();
		// glBindBuffer(GL_ARRAY_BUFFER, normHandle);
		// glBufferData(GL_ARRAY_BUFFER, normData,
		// GL_STATIC_DRAW);
		// glBindBuffer(GL_ARRAY_BUFFER, 0);

		cordHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, cordHandle);
		glBufferData(GL_ARRAY_BUFFER, cordData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void render() {

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glBindBuffer(GL_ARRAY_BUFFER, vertHandle);
		glVertexPointer(3, GL_FLOAT, 0, 0l);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// glBindBuffer(GL_ARRAY_BUFFER, normHandle);
		// glNormalPointer(GL_FLOAT, 0, 0l);
		// glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, cordHandle);
		glTexCoordPointer(2, GL_FLOAT, 0, 0l);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		KondionLoader.textures.get("noah").bind();
		//glDisable(GL_TEXTURE_2D);
		//glColor3f(0.0f, 1.0f, 1.0f);
		glDrawArrays(GL_TRIANGLES, 0, vertData.capacity() / 3);
		glColor3f(1.0f, 1.0f, 1.0f);
		
		glDisableClientState(GL_VERTEX_ARRAY);
		// glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	}

	public void addAABlock(Vector3f pos, int up, int dn, int no, int ea,
			int so, int we) {
		Collider c = new Collider();
		c.shape = AA_A;
		c.solid = false;
		c.up = up;
		c.dn = dn;
		c.no = no;
		c.ea = ea;
		c.so = so;
		c.we = we;
		c.x = pos.x;
		c.y = pos.y;
		c.z = pos.z;
		c.priority = 2;
		colliders.add(c);
	}
}

class Collider {
	public boolean solid;
	public byte shape;
	public float up, dn, no, ea, so, we;
	public float x, y, z;
	public int priority;
}