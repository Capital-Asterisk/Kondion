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

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class ProtoEntity {

	private String id;
	private String name;
	private ScriptObjectMirror object;
	private Short[] traits;

	public ProtoEntity(ScriptObjectMirror obj) {
		object = obj;
		id = (String) obj.get("id");
		name = (String) obj.get("name");
		List<Short> l = new ArrayList<Short>();
		for (Object s : ((ScriptObjectMirror) obj.get("traits")).values()) {
			System.err.println(s);
			l.add(Traits.getTraitIndex((String) s));
			if (l.get(l.size() - 1) == -1) {
				l.remove(l.size() - 1);
			}
		}
		traits = l.toArray(new Short[l.size()]);
	}

	public void changeObject(ScriptObjectMirror obj) {
		object = obj;
		id = (String) obj.get("id");
		name = (String) obj.get("name");
		List<Short> l = new ArrayList<Short>();
		for (String s : ((ScriptObjectMirror) obj.get("traits")).keySet()) {
			l.add(Traits.getTraitIndex(s));
			if (l.get(l.size() - 1) == -1) {
				l.remove(l.size() - 1);
			}
		}
		traits = l.toArray(new Short[l.size()]);
	}

	@SuppressWarnings("unchecked")
	public ScriptObjectMirror create(ScriptObjectMirror obj,
			ScriptObjectMirror xtra) {
		Entity e;
		if (Traits.hasTrait(traits, Traits.getTraitIndex("et_static"))) {
			// is physics entity
			e = new Entity(this, obj);
		} else if (Traits.hasTrait(traits, Traits.getTraitIndex("et_physic"))) {
			e = new PhysicEntity(this, obj);
		} else if (Traits.hasTrait(traits, Traits.getTraitIndex("et_alive"))) {
			e = new LivingEntity(this, obj);
		} else {
			System.err.println("Unknown entity type (et) trait");
			return null;
		}
		obj.put("mouseRotate", false);
		if (xtra.containsKey("traits")) {
			// Add extra traits
			// e.setExtraTraits(xtra);
			List<Short> l = new ArrayList<Short>();
			for (Object s : ((ScriptObjectMirror) xtra.get("traits")).values()) {
				System.out.println(Traits.getTraitIndex((String) s));
				l.add(Traits.getTraitIndex((String) s));
			}
		}
		e.setTickInterval((int) object.get("tickInterval"));
		obj.put("obj", e);
		return obj;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ScriptObjectMirror getObject() {
		return object;
	}
}
