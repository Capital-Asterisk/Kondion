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

var GKO_RenderPass = Java.type("vendalenger.kondion.kobj.GKO_RenderPass");
var GKO_DeferredPass = Java.type("vendalenger.kondion.kobj.GKO_DeferredPass");
var GKO_Scene = Java.type("vendalenger.kondion.kobj.GKO_Scene");

var OKO_Camera_ = Java.type("vendalenger.kondion.kobj.OKO_Camera_");

var RKO_Board = Java.type("vendalenger.kondion.kobj.RKO_Board");
 
var SKO_Cube = Java.type("vendalenger.kondion.kobj.SKO_Cube");
var SKO_InfinitePlane = Java.type("vendalenger.kondion.kobj.SKO_InfinitePlane");

//^kdion.rungamedir (electricfence/kondion.json)
//var KJS = Java.type("vendalenger.kondion.KJS")


var patchObject = function(obj, patch) {
	for (var aname in patch) {
		obj[aname] = patch[aname];
	}
	return obj;
};

var kondionInit = function() {
	delete kondionInit;
}