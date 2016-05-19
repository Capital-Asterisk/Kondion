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

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Renderable;
import vendalenger.kondion.objectbase.KObj_Solid;

public class GKO_Scene extends KObj_Node {
	
	private final Vector3f temp0;
	private final FloatBuffer facebook;
	private List<KObj_Node> allObjects;
	private KObj_Solid currentA;
	private KObj_Solid currentB;
	
	public boolean collisions = true;
	public boolean disable = false;
	
	public GKO_Scene() {
		facebook = BufferUtils.createFloatBuffer(6);
		temp0 = new Vector3f();
		pointer = false;
		allObjects = new ArrayList<KObj_Node>();
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
	
	protected void immaListYourChildren(KObj_Node child) {
		allObjects.add(child);
		for (KObj_Node or : child.values()) {
			immaListYourChildren(or);
		}
	}
	
	public void rescan() {
		allObjects.clear();
		for (KObj_Node kids : children) {
			immaListYourChildren(kids);
		}
		
	}
	
	@Override
	public void clear() {
		children.clear();
		childNames.clear();
		allObjects.clear();
	}

	@Override
	public void update() {

		Kondion.getNashorn().put("delta", Kondion.getDelta());
		defaultUpdate();
		
		if (!disable) {
			// It seems there is a better way of
			// doing this
			
			// UpdateA first, with movement and js
			//for (KObj_Node kobj : children) {
			//	immaWulaYourChildren(kobj);
			//	// else not an oriented
			//}
			for (int i = 0; i < allObjects.size(); i++) {
				allObjects.get(i).update();
				if (allObjects.get(i).killMe || (allObjects.get(i).getParent() != null && allObjects.get(i).getParent().killMe)) {
					//System.out.println(allObjects.get(i).getParent());
					allObjects.get(i).getParent().remove(allObjects.get(i));
					allObjects.get(i).killMe = true;
					allObjects.remove(i);
					i --;
				}
			}
			
			// Then apply apply transformations
			//for (KObj_Node kobj : children) {
			//	// Is it solid?
			//	if (kobj instanceof KObj_Oriented) {
			//		immaEatYourChildren(kobj);
			//		//System.out.println("neat " + kobj);
			//	}
				// else not an oriented
			//}
			for (KObj_Node kobj : allObjects) {
				if (kobj instanceof KObj_Oriented)
					((KObj_Oriented) kobj).applyTransform();
			}
			
			// Then Collisions
			if (collisions) {
				doCollisions();
			}
			
			// UpdateB all the objects, with detectors
			//for (KObj_Node kobj : children) {
			//	// Is it solid?
			//	if (kobj instanceof KObj_Oriented) {
			//		immaGulaYourChildren(kobj);
			//	}
			//	// else not an oriented
			//}
			
			for (KObj_Node kobj : allObjects) {
				if (kobj instanceof KObj_Oriented)
					((KObj_Oriented) kobj).updateB();
			}
			
			if (Kondion.getWorld().mic == null)
				setListener(Kondion.getWorld().camera);
			else
				setListener(Kondion.getWorld().mic);
			
			// Render at the same time
			/*for (GKO_RenderPass rp : Kondion.getWorld().passes) {
				rp.render();
			}*/
			
			Kondion.getWorld().composite();
		}
		
	}
	
	private void setListener(KObj_Oriented ear) {
		if (ear != null) {
			AL10.alListener3f(AL10.AL_POSITION, ear.actTransform.m30, ear.actTransform.m31, ear.actTransform.m32);
			temp0.set(0.0f, 0.0f, -1.0f);
			ear.actTransform.transformDirection(temp0);
			facebook.position(0);
			facebook.put(temp0.x);
			facebook.put(temp0.y);
			facebook.put(temp0.z);
			temp0.set(0.0f, 1.0f, 0.0f);
			ear.actTransform.transformDirection(temp0);
			facebook.put(temp0.x);
			facebook.put(temp0.y);
			facebook.put(temp0.z);
			facebook.position(0);
			//AL10.alListener3f(AL10.AL_ORIENTATION, ear.actTransform.m30, ear.actTransform.m31, ear.actTransform.m32);
			AL10.alListenerfv(AL10.AL_ORIENTATION, facebook);
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
						if (currentA != currentB
							&& (
									// Are layers valid?
									((currentB.collideType & currentA.collideCall) == currentB.collideType)
									|| ((currentB.collideType & currentA.collideMove) == currentB.collideType))) {
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

	public Collection<KObj_Node> everything() {
		return Collections.unmodifiableList(allObjects);
	}

	
}
