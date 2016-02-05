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
	
	public boolean collide = false;
	public boolean dynamic = false;
	public boolean rotate = false;
	
	public final Matrix4f prevTransform;
	public final Quaternionf rotVelocity;
	public final Vector3f velocity;
	
	public KObj_Solid() {
		prevTransform = new Matrix4f();
		rotVelocity = new Quaternionf();
		velocity = new Vector3f();
	}

	public abstract CollisionData collisionCheck(KObj_Solid kobj);
	
}
