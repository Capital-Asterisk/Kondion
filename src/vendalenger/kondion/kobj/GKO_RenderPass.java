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

package vendalenger.kondion.kobj;

import java.util.LinkedHashSet;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Renderable;
import vendalenger.kondion.objectbase.KObj_Solid;

public class GKO_RenderPass extends KObj_Node {
	
	private KObj_Solid currentA;
	private KObj_Solid currentB;
	
	protected static final boolean pointer = false;
	
	public boolean disable = false;
	public boolean collisions = true;
	
	public GKO_RenderPass() {
		LinkedHashSet<KObj_Renderable> f = new LinkedHashSet<KObj_Renderable>();

		parent = this;
	}

	@Override
	public void update() {
		if (!disable) {
			// It seems there is a better way of
			// doing this
			
			// UpdateA first, with movement
			for (KObj_Node kobj : children) {
				kobj.update();
			}
			
			// Then apply apply transformations
			for (KObj_Node kobj : children) {
				// Is it solid?
				if (kobj instanceof KObj_Oriented)
					((KObj_Oriented) kobj).applyTransform();
				// else not an oriented
			}
			
			// Then Collisions
			if (collisions) {
				doCollisions();
			}
			
			// UpdateB all the objects, with detectors
			// Render at the same time
			for (KObj_Node kobj : children) {
				if (kobj instanceof KObj_Oriented) {
					((KObj_Oriented) kobj).updateB();
					if (Kondion.showPrespective && kobj instanceof KObj_Renderable)
						((KObj_Renderable) kobj).render();
				}
			}
		}
	}
	
	private void doCollisions() {
		// Loop through children
		
		for (int i = 0; i < children.size(); i++) {
			// Is it solid?
			if (children.get(i) instanceof KObj_Solid) {
				// Does this make it faster?
				currentA = (KObj_Solid) children.get(i);
				// Loop through all children again
				for (int j = 0; j < children.size(); j++) {
					if (children.get(j) instanceof KObj_Solid) {
						currentB = (KObj_Solid) children.get(j);
						// DO I COLLIDE WITH MYSELF?
						if (currentA != currentB) {
							currentA.collisionCheck(currentB);
							//currentA.pos.z += 0.001f;
						}
					}
				}
			} else {
				// TODO warning?
			}
		}
	}
}
