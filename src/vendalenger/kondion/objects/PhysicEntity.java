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

package vendalenger.kondion.objects;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.collision.EntityCollider;
import vendalenger.kondion.collision.FixedCylinderCollider;
import vendalenger.kondion.scene.MapCollider;
import vendalenger.kondion.Kondion;

public class PhysicEntity extends Entity {

	protected float gravity = 0.2f;
	protected float drag = 0.97f;
	protected List<EntityCollider> colliders;
	protected Vector3f prevPos;
	protected Vector3f prevRot;
	protected Vector3f velocity;
	private EntityCollider temp0;

	public PhysicEntity(ProtoEntity p, ScriptObjectMirror m) {
		super(p, m);
		velocity = new Vector3f();
		colliders = new ArrayList<EntityCollider>();
	}

	public void thrust(float x, float y, float z) {
		velocity.x += x;
		velocity.y += y;
		velocity.z += z;
	}

	public void thrustYAngle(float radian, float amt, float max) {

		// If velocity magnitude (without y) is lower than max, or max is 0
		if (max == 0) {
			velocity.x -= (float) Math.cos(radian) * amt;
			velocity.z -= (float) Math.sin(radian) * amt;
		} else {
			Math.sqrt((velocity.x * velocity.x) + (velocity.z * velocity.z));
			velocity.x -= (float) Math.cos(radian) * amt;
			velocity.z -= (float) Math.sin(radian) * amt;
		}
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void collideTerrain() {
		for (int i = 0; i < colliders.size(); i++) {
			temp0 = colliders.get(i);
			if (colliders.get(i) instanceof FixedCylinderCollider) {
				Kondion.getCurrentScene().entityCheckFixedCylinder(this,
						(FixedCylinderCollider) temp0);
				if (((FixedCylinderCollider) temp0).collisionAmt > 0) {
					for (int j = 0; j < ((FixedCylinderCollider) temp0).collisionAmt; j++) {
						
						position.x += ((FixedCylinderCollider) temp0).collisions[j][0]
								* ((FixedCylinderCollider) temp0).collisions[j][3];
						position.y += ((FixedCylinderCollider) temp0).collisions[j][1]
								* ((FixedCylinderCollider) temp0).collisions[j][3];
						position.z += ((FixedCylinderCollider) colliders.get(i)).collisions[j][2]
								* ((FixedCylinderCollider) temp0).collisions[j][3];
						
						//System.out.println("b4(" + velocity.x + ", " + velocity.y + ", " + velocity.z + ")");
						velocity.x *= 1 - Math.abs(((FixedCylinderCollider) temp0).collisions[j][0]);
						velocity.y *= 1 - Math.abs(((FixedCylinderCollider) temp0).collisions[j][1]);
						velocity.z *= 1 - Math.abs(((FixedCylinderCollider) temp0).collisions[j][2]);
						
						//System.out.println(((FixedCylinderCollider) temp0).collisions[j][1]);
						//System.out.println("af(" + velocity.x + ", " + velocity.y + ", " + velocity.z + ")");
					}
				}
			}
		}
		//System.out.println(velocity.x);
	}

	public void move() {
		collideTerrain();
		position.x += velocity.x * Kondion.getDelta();
		position.y += velocity.y * Kondion.getDelta();
		position.z += velocity.z * Kondion.getDelta();
		
		// velocity.y -= gravity;
		// velocity.x *= drag;
		// velocity.y *= drag;
		// velocity.z *= drag;
	}
}
