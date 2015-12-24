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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.joml.Vector3f;

import com.sun.xml.internal.ws.util.StringUtils;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.kobj.GKO_Scene;


public abstract class KObj_Node implements Map<String, KObj_Node> {

	protected static final boolean pointer = true;
	
	protected final List<KObj_Node> children;
	protected final List<String> childNames;
	protected KObj_Node parent;
	public String name;
	public ScriptObjectMirror s;
	
	public abstract void update();
	
	public KObj_Node() {
		getClass().getName();
		children = new ArrayList<KObj_Node>();
		childNames = new ArrayList<String>();
		name = getClass().getSimpleName();
	}
	
	/*public Scene getScene() {
		KObj_Node eggs = this.parent;
		while (!(eggs instanceof Scene)) {
			eggs = eggs.parent;
			//if (eggs == this) {
			//	System.err.println("PARENT LOOP DETECTED");
			//	return null;
			//}
		}
		return (Scene) eggs;
	}*/
	
	public String nameTree() {
		String s = "****Tree for " + name + "****";
		for (int i = 0; i < size(); i++) {
			s += "\n" + "--> (" + children.get(i).getClass().getSimpleName()
					+ ") " + childNames.get(i);
			if (children.get(i).size() > 0) {
				s = children.get(i).nameTree(s, 3);
			}
		}
		return s;
	}
	
	public String nameTree(String current, int depth) {
		for (int i = 0; i < size(); i++) {
			current += "\n";
			current += new String(new char[depth - 1]).replace("\0", "--") + ">";
			current += " (" + children.get(i).getClass().getSimpleName()
					+ ") " + childNames.get(i);
			if (children.get(i).size() > 0)
				current = children.get(i).nameTree(current, depth + 1);
		}
		return current;
	}
	
	public String listChildren() {
		String s = "****Children of " + name + "****";
		for (int i = 0; i < childNames.size(); i++) {
			s += "\n--> (" + children.get(i).getClass().getSimpleName()
					+ ") " + childNames.get(i);
		}
		return s;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
	
	@Override
	public int size() {
		return children.size();
	}

	@Override
	public boolean isEmpty() {
		return children.isEmpty() || childNames.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return childNames.indexOf(key) != -1;
	}

	@Override
	public boolean containsValue(Object value) {
		return children.indexOf(value) != -1;
	}

	@Override
	public KObj_Node get(Object key) {
		return children.get(childNames.indexOf(key));
	}

	@Override
	public KObj_Node put(String key, KObj_Node value) {
		if (key != null && value != null) {
			if (!containsKey(key)) {
				children.add(value);
				childNames.add(key);
			} else {
				children.set(childNames.indexOf(key), value);
			}
			if (!pointer) {
				value.parent = this;
				value.name = key;
			}
		}
		return this;
	}

	/**
	 * Remove an object from this node
	 * @param key Name of the object or the object itself.
	 */
	@Override
	public KObj_Node remove(Object key) {
		
		if (key instanceof String) {
			int index = childNames.indexOf(key);
			children.remove(index);
			childNames.remove(index);
		} else if (key instanceof KObj_Node) {
			int index = children.indexOf(key);
			children.remove(index);
			childNames.remove(index);
		} else {
			System.err.println("throw here");
		}
		return this;
	}

	@Override
	public void putAll(Map<? extends String, ? extends KObj_Node> m) {
		System.err.println("Not yet implemented");
	}

	@Override
	public void clear() {
		children.clear();
		childNames.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> keySet() {
		return (Set<String>) Collections.unmodifiableList(childNames);
	}

	@Override
	public Collection<KObj_Node> values() {
		return Collections.unmodifiableList(children);
	}

	@Override
	public Set<java.util.Map.Entry<String, KObj_Node>> entrySet() {
		return null;
	}
}
