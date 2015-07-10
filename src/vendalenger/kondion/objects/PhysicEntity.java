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

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.lwjgl.util.vector.Vector3f;

import vendalenger.kondion.KInput;
import vendalenger.kondion.collision.EntityCollider;
import vendalenger.kondion.collision.FixedCylinderCollider;
import vendalenger.kondion.Kondion;

public class PhysicEntity extends Entity {

	protected float gravity = 0.2f;
	protected float drag = 0.97f;
	protected List<EntityCollider> colliders;
	protected Vector3f velocity;

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
			Math.sqrt(
					(velocity.x * velocity.x)
					+ (velocity.z * velocity.z));
			velocity.x -= (float) Math.cos(radian) * amt;
			velocity.z -= (float) Math.sin(radian) * amt;
		}
	}

	public Vector3f getVelocity() {
		return velocity;
	}
	
	public void collideTerrain() {
		for (int i = 0; i < colliders.size(); i++) {
			if (colliders.get(i) instanceof FixedCylinderCollider) {
				if (!Kondion.getCurrentScene().entityCheckFixedCylinder(this, (FixedCylinderCollider) colliders.get(i))) {
					//position.y += 0.3f;
					velocity.y = 0.3f;
				}
			}
		}
	}

	public void move() {
		position.x += velocity.x * Kondion.getDelta();
		position.y += velocity.y * Kondion.getDelta();
		position.z += velocity.z * Kondion.getDelta();
		collideTerrain();
		//velocity.y -= gravity;
		//velocity.x *= drag;
		//velocity.y *= drag;
		//velocity.z *= drag;
	}
}
