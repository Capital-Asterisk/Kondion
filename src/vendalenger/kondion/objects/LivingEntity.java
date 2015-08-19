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

import vendalenger.kondion.KInput;
import vendalenger.kondion.Kondion;
import vendalenger.kondion.collision.FixedCylinderCollider;
import vendalenger.kondion.objects.control.LEControl;

public class LivingEntity extends PhysicEntity {

	private LEControl intel;

	public LivingEntity(ProtoEntity p, ScriptObjectMirror m) {
		super(p, m);
		colliders.add(new FixedCylinderCollider(1, 1, 1));
		intel = null;
	}

	@Override
	public void move() {
		// if (intel != null)
		// intel.tick();
		if ((boolean) mirror.get("mouseRotate")) {
			rotation.x += KInput.getMouseDX() / 300;
			rotation.y -= KInput.getMouseDY() / 300;
			if (rotation.y > Math.PI / 2)
				rotation.y = (float) (Math.PI / 2) - 0.001f;
			if (rotation.y < -Math.PI / 2)
				rotation.y = (float) (-Math.PI / 2) + 0.001f;
		}
		position.x += velocity.x * Kondion.getDelta();
		position.y += velocity.y * Kondion.getDelta();
		position.z += velocity.z * Kondion.getDelta();

		collideTerrain();

		velocity.y -= gravity;
		// velocity.x *= drag;
		// velocity.y *= drag;
		// velocity.z *= drag;
	}
}
