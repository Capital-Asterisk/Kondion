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

import org.lwjgl.util.vector.Vector3f;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class PhysicEntity extends Entity {

	private float drag = 0.97f;
	private boolean hasGravity = false;
	private Vector3f velocity;

	public PhysicEntity(ProtoEntity p, ScriptObjectMirror m) {
		super(p, m);
		velocity = new Vector3f();
		velocity.y = 0.7f;
	}

	public void move() {
		if (hasGravity) {
			Vector3f.add(velocity, position, position);
			velocity.x *= drag;
			velocity.y *= drag;
			velocity.z *= drag;
		}
	}

	public Vector3f getVelocity() {
		return velocity;
	}
}
