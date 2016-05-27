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

package vendalenger.kondion.objectbase;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class KObj_Solid extends KObj_Renderable {
	
	public boolean axisAlign = true;
	public boolean collide = false;
	public boolean anchor = false;
	public float mass = 1.0f;
	public float divFriction = 1.1f;
	public float staticFriction = 0.0f;
	public int collideType = 1; // this type
	public int collideCall = 1; // types to call collide function
	public int collideMove = 1; // types that move this
	
	public final Matrix4f prevTransform;
	public final Quaternionf rotVelocity;
	public final Vector3f velocity;
	
	public KObj_Solid() {
		this(1);
	}
	
	public KObj_Solid(int id) {
		super(id);
		prevTransform = new Matrix4f();
		rotVelocity = new Quaternionf();
		velocity = new Vector3f();
	}
	
	public void accelerateTo(float x, float y, float z, float r8, boolean ix, boolean iy, boolean iz) {
		
		float xx = (x - velocity.x);
		float yy = (y - velocity.y);
		float zz = (z - velocity.z);
		double mag = Math.sqrt(xx * xx + yy * yy + zz * zz);
	
		xx /= mag;
		yy /= mag;
		zz /= mag;
		
		// now we have normalized vector
		
		velocity.x += ix ? xx * r8 : 0;
		velocity.y += iy ? yy * r8 : 0;;
		velocity.z += iz ? zz * r8 : 0;;
		// get signs
		//byte bx = (velocity.x == 0) ? 0 : ((velocity.x < 0) ? (byte) 1 : (byte) -1);
		//byte by = (velocity.y == 0) ? 0 : ((velocity.y < 0) ? (byte) 1 : (byte) -1);
		//byte bz = (velocity.z == 0) ? 0 : ((velocity.z < 0) ? (byte) 1 : (byte) -1);
		
		
		
		//velocity.x += Math.signum(x - velocity.x) * r8;
		//if (Math.abs(velocity.x - x) < r8)
		//	velocity.x = x;
		//velocity.y += Math.signum(y - velocity.y) * r8;
		//if (Math.abs(velocity.y - y) < r8)
		//	velocity.y = y;
		//velocity.z += Math.signum(z - velocity.z) * r8;
		//if (Math.abs(velocity.z - z) < r8)
		//	velocity.z = z;
		
	}

	public abstract CollisionData collisionCheck(KObj_Solid kobj);
	public abstract boolean checkPoint(float x, float y, float z);
	
}
