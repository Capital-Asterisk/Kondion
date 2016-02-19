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

public abstract class KObj_Oriented extends KObj_Node {
	
	//public static final int 
	public final Matrix4f transform = new Matrix4f();
	public final Matrix4f actTransform = new Matrix4f();
	
	public abstract void updateB();
	
	public void applyTransform() {
		//transform.
		actTransform.identity();
		if (parent instanceof KObj_Oriented) {
			((KObj_Oriented) parent).multiplyByAct(actTransform);
		}
		//System.out.println("PARENT: " + parent);
		actTransform.mul(transform);
	}
	
	public void multiplyByAct(Matrix4f h) {
		h.mul(actTransform);
	}
	
	public float getOffsetX() {
		return transform.m30;
	}
	
	public float getOffsetY() {
		return transform.m31;
	}
	
	public float getOffsetZ() {
		return transform.m32;
	}
}
