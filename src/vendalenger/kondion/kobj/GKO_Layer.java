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
import vendalenger.kondion.objectbase.KObj_Renderable;
import vendalenger.kondion.objectbase.KObj_Solid;

public class GKO_Layer extends KObj_Node {
	
	public static final int UNUSED = 0, RENDER = 1, UPDATE = 2, UPDATE_RENDER = 3;
	
	private KObj_Solid currentA;
	private KObj_Solid currentB;
	
	protected static final boolean pointer = false;
	
	public int type = 0;
	public boolean disable = false;
	public boolean collisions = true;
	
	public GKO_Layer() {
		parent = this;
	}
	
	public GKO_Layer(int t) {
		if (t <= 3 && t >= 0)
			type = t;
		else
			System.err.println(t + " is not a valid layer type. Using defaut: 0 (UNUSED)");
		parent = this;
	}

	@Override
	public void update() {
		if (!disable) {
			// It seems there is a better way of
			// doing this
			
			switch (type) {
			case 1:
				// Render all the objects (if they can)
				if (Kondion.showPrespective) {
					for (KObj_Node kobj : children) {
						if (kobj instanceof KObj_Renderable) {
							((KObj_Renderable) kobj).render();
						}
					}
				}
				break;
			case 2:
				// Collisions first
				
				// Update all the objects
				for (KObj_Node kobj : children) {
					kobj.update();
				}
				break;
			case 3:
				// Update while rendering all the objects
				for (KObj_Node kobj : children) {
					kobj.update();
					if (Kondion.showPrespective && kobj instanceof KObj_Renderable) {
						((KObj_Renderable) kobj).render();
					}
				}
				break;
			}
		}
	}
	
	private void doCollisions() {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i) instanceof KObj_Solid) {
				currentA = (KObj_Solid) children.get(i);
				for (int j = 0; j < children.size(); j++) {
					if (children.get(j) instanceof KObj_Solid) {
						
					}
				}
				
				
			}
		}
	}
}
