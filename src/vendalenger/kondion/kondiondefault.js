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

var Quaternionf = Java.type("org.joml.Quaternionf");
var Matrix3f = Java.type("org.joml.Matrix3f");
var Matrix2f = Java.type("org.joml.Matrix4f");
var Vector2f = Java.type("org.joml.Vector2f");
var Vector3f = Java.type("org.joml.Vector3f");
var Vector4f = Java.type("org.joml.Vector4f");

var Quaterniond = Java.type("org.joml.Quaterniond");
var Matrix3d = Java.type("org.joml.Matrix3d");
var Matrix2d = Java.type("org.joml.Matrix4d");
var Vector2d = Java.type("org.joml.Vector2d");
var Vector3d = Java.type("org.joml.Vector3d");
var Vector4d = Java.type("org.joml.Vector4d");

//^kdion.rungamedir (electricfence/kondion.json)
var KJS = {
	java: Java.type("vendalenger.kondion.KJS"),
	kondion: Java.type("vendalenger.kondion.Kondion"),
	kinput: Java.type("vendalenger.kondion.KInput"),
	b: {
		// Buttons go here
	},
	c: {
		freeCam: function(mode) {KJS.kondion.getCurrentCamera().setFreeMode(mode);},
		bindCam: function(ent) {KJS.kondion.getCurrentCamera().bindToEntity(ent)}
	},
	g: {
		setMouseGrab: function(g) {KJS.kinput.setMouseLock(g);}
	},
	i: {
		buttonDown: function(b) {return KJS.kinput.buttonIsDown(b);},
		keyboardDown: function(b) {return KJS.kinput.keyboardDown(b);},
		mouseDown: function(b) {return KJS.kinput.mouseDown(b);}
	},
	o: {
		
	},
	r: {
		Board: Java.type("vendalenger.kondion.kobj.KObj_Board")
	},
	eggs: function() {
		KJS.kondion.eggs();
	}
};

var scene;

var patchObject = function(obj, patch) {
	for (var aname in patch) {
		obj[aname] = patch[aname];
	}
	return obj;
};