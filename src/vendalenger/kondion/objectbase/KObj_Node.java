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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.joml.Vector3f;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.scene.Scene;


public abstract class KObj_Node implements Map<String, KObj_Node> {

	public final List<KObj_Node> children;
	protected KObj_Node parent;
	public String name;
	public ScriptObjectMirror s;
	
	public KObj_Node() {
		children = new ArrayList<KObj_Node>();
	}

	/**
	 * Add an object to this object.
	 * <pre>
	 * {@code
	 * scene.add(floor);
	 * }
	 * </pre>
	 * @param e A new object to add
	 * @return this
	 */
	public KObj_Node add(KObj_Node e) {
		e.parent = this;
		return this;
	}
	
	/**
	 * Add multiple objects to this object.
	 * <pre>
	 * {@code
	 * scene.addMulti(objA, objB, objC, objD);
	 * }
	 * </pre>
	 * @param e A new object to add
	 * @return this
	 */
	public KObj_Node addMulti(KObj_Node... list) {
		for (KObj_Node e : list) {
			e.parent = this;
		}
		return this;
	}
	
	public Scene getScene() {
		KObj_Node eggs = this.parent;
		while (!(eggs instanceof Scene)) {
			eggs = eggs.parent;
			/*if (eggs == this) {
				System.err.println("PARENT LOOP DETECTED");
				return null;
			}*/
		}
		return (Scene) eggs;
	}
	
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public KObj_Node get(Object key) {
		System.out.println("SCOOP");
		return null;
	}

	@Override
	public KObj_Node put(String key, KObj_Node value) {
		System.out.println("POOT");
		return null;
	}

	@Override
	public KObj_Node remove(Object key) {
		System.out.println("EXTERMINATE");
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends KObj_Node> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<KObj_Node> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<String, KObj_Node>> entrySet() {
		System.out.println("SET!");
		return null;
	}
}
