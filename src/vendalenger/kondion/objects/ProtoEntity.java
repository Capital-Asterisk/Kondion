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

import java.util.List;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.lwjgl.util.vector.Vector3f;

import com.sun.javafx.geom.Vec3f;

public class ProtoEntity {

	private short[] traits;
	private ScriptObjectMirror object;
	private String id;
	private String name;

	public ProtoEntity(ScriptObjectMirror obj) {
		object = obj;
		id = (String) obj.get("id");
		name = (String) obj.get("name");
	}

	public ScriptObjectMirror getObject() {
		return object;
	}

	public void changeObject(ScriptObjectMirror obj) {
		object = obj;
		id = (String) obj.get("id");
		name = (String) obj.get("name");
	}

	public ScriptObjectMirror create(ScriptObjectMirror obj) {
		Entity e;
		if (true) {
			// is physics entity
			e = new PhysicEntity(this, obj);
		}
		obj.put("class", e);
		return obj;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
