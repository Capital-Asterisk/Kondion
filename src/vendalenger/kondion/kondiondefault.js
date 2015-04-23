/*
 * Copyright 2014 Neal Nicdao
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

var EntityPhysClass = Java.type("vendalenger.kondion.objects.Entity");
var ProtoEntityClass = Java.type("vendalenger.kondion.objects.ProtoEntity");

var blockTypes = {
	SOLID: 0, CUTOUT: 1, EXCEPT: 2,
};

//^kdion.rungamedir (electricfence/kondion.json)
var KJS = {
	java: Java.type("vendalenger.kondion.KJS"),
	kondion: Java.type("vendalenger.kondion.Kondion"),
	kinput: Java.type("vendalenger.kondion.KInput"),
	e: {
		rEnt: function(props) {
			var e = new ProtoEntityClass(props);
			KJS.kondion.getProtoEntityList().add(e);
			return e;
		},
		spawnEnt: function(id) {
			var p = KJS.java.getProtoEntity(id);
			var e;
			if (p != null) {
				e = p.create(patchObject({}, p.getObject()));
				KJS.kondion.getEntityList().add(e.class);
				KJS.kondion.getMirrorList().add(e);
			}
			return e;
		}
	},
	g: {
		freeCam: function() {KJS.java.freeCam();},
		setMouseGrab: function(g) {KJS.kinput.setMouseLock(g);}
	},
	s: {
		setRootCollision: function() {
			
		},
		newAABlock: function(x, y, z, up, down, left, right, type) {
		
		}
	},
	eggs: function() {
		KJS.kondion.eggs();
	}
};

var patchObject = function(obj, patch) {
	for (var aname in patch) {
		obj[aname] = patch[aname];
	}
	return obj;
};

var init = function() {
	KJS.java.issueCommand("^eggs");
};

var start = function() {
	KJS.g.setMouseGrab(true);
	KJS.e.spawnEnt("ef_klein");
	KJS.g.freeCam();
};

KJS.e.rEnt({
	id: "ef_klein",
	name: "Klein",
	traits: ["ph_alive", "kg_playable"],
	tick: function() {
		//print(this.name);
	},
	notify: function(msg) {
	}
});