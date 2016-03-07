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

import vendalenger.kondion.Kondion;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Renderable;
import vendalenger.kondion.objectbase.KObj_Solid;

public class GKO_Scene extends KObj_Node {
	
	private KObj_Solid currentA;
	private KObj_Solid currentB;
	
	public boolean collisions = true;
	public boolean disable = false;
	
	public GKO_Scene() {
		
	}
	
	protected static void immaEatYourChildren(KObj_Node child) {
		((KObj_Oriented) child).applyTransform();
		for (KObj_Node or : child.values()) {
			if (or instanceof KObj_Oriented)
				immaEatYourChildren(or);
		}
	}
	
	protected static void immaWulaYourChildren(KObj_Node child) {
		child.update();
		for (KObj_Node or : child.values()) {
				immaWulaYourChildren(or);
		}
	}
	
	protected static void immaGulaYourChildren(KObj_Node child) {
		((KObj_Oriented) child).updateB();
		for (KObj_Node or : child.values()) {
			if (or instanceof KObj_Oriented)
				immaGulaYourChildren(or);
		}
	}

	@Override
	public void update() {

		Kondion.getNashorn().put("delta", Kondion.getDelta());
		defaultUpdate();
		
		if (!disable) {
			// It seems there is a better way of
			// doing this
			
			// UpdateA first, with movement and js
			for (KObj_Node kobj : children) {
				immaWulaYourChildren(kobj);
				// else not an oriented
			}
			
			// Then apply apply transformations
			for (KObj_Node kobj : children) {
				// Is it solid?
				if (kobj instanceof KObj_Oriented) {
					immaEatYourChildren(kobj);
					//System.out.println("neat " + kobj);
				}
				// else not an oriented
			}
			
			// Then Collisions
			if (collisions) {
				doCollisions();
			}
			
			// UpdateB all the objects, with detectors
			for (KObj_Node kobj : children) {
				// Is it solid?
				if (kobj instanceof KObj_Oriented) {
					immaGulaYourChildren(kobj);
				}
				// else not an oriented
			}
			
			// Render at the same time
			/*for (GKO_RenderPass rp : Kondion.getWorld().passes) {
				rp.render();
			}*/
			
			Kondion.getWorld().composite();
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
