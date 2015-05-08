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

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.lwjgl.util.vector.Vector3f;

import vendalenger.kondion.KInput;
import vendalenger.kondion.Kondion;

// Ambient Obscurance
public class PhysicEntity extends Entity {

	private boolean hasGravity = true;
	private float drag = 0.97f;
	private Vector3f velocity;

	public PhysicEntity(ProtoEntity p, ScriptObjectMirror m) {
		super(p, m);
		velocity = new Vector3f();
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void move() {
		if (hasGravity) {
			velocity.y -= 0.007;
		}
		// position.x += 0.01;
		if ((boolean) mirror.get("mouseRotate")) {
			rotation.x += KInput.getMouseDX() / 300;
			rotation.y -= KInput.getMouseDY() / 300;
			if (rotation.y > Math.PI / 2)
				rotation.y = (float) (Math.PI / 2);
			if (rotation.y < -Math.PI / 2)
				rotation.y = (float) (-Math.PI / 2);
		}
		position.x += velocity.x * Kondion.getDelta();
		position.y += velocity.y * Kondion.getDelta();
		position.z += velocity.z * Kondion.getDelta();
		//velocity.x *= drag;
		//velocity.y *= drag;
		//velocity.z *= drag;
	}
}
