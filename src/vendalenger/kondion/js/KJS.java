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

package vendalenger.kondion.js;

import java.io.InputStreamReader;

import javax.script.ScriptException;

import org.joml.Vector3f;

import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KAudio;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KModel;
import vendalenger.kondion.lwjgl.resource.KTexture;
import vendalenger.kondion.objectbase.KObj_Solid;
import vendalenger.port.Command;

public class KJS {
	
	public final ScriptObjectMirror o;
	public final ScriptObjectMirror d;
	public final StaticClass c;
	public final StaticClass s;
	public final StaticClass i;
	public final short
		UP		= 0,
		DOWN		= 1,
		LEFT		= 2,
		RIGHT		= 3;
	public final short
			DEFAULT		= 0,
			DIFFUSE		= 1,
			DEPTH		= 2,
			NORMALS		= 3,
			BRIGHTNESS	= 4,
			HDR			= 5,
			DEFERRED	= 10,
			GUI			= 20;
	public final short
			SINGLE = 0,
			COMPOSITE = 1,
			DEBUG = 	2,
			HORSPLIT = 	3,
			VERTSPLIT = 4,
			QUADSPLIT = 5,
			VR = 		6;
	public final short
			ZYX = 0,
			ZXY = 1,
			YXZ = 2,
			YZX = 3,
			XYZ = 4,
			XZY = 5;
	
	public KJS() throws ScriptException {
		System.out.println("KJS initialization");
		s = (StaticClass) Kondion.getNashorn().eval("Java.type(\"vendalenger.kondion.SceneLoader\")");
		c = (StaticClass) Kondion.getNashorn().eval("Java.type(\"vendalenger.kondion.js.KJS\")");
		i = (StaticClass) Kondion.getNashorn().eval("Java.type(\"vendalenger.kondion.KInput\")");
		Kondion.getNashorn().eval("var thisislong = {"
				+ "example_poop: \"neat\","
				+ "example_pole: \"neat\""
				+ "}");
		d = (ScriptObjectMirror) Kondion.getNashorn().eval("thisislong");
		Kondion.getNashorn().eval("var thisisabitlong = {}");
		o = (ScriptObjectMirror) Kondion.getNashorn().eval("thisisabitlong");

	}
	
	//public void duck() {
	//	Window.poopy();
	//}
	
	public Object executeScript(String path) {
		try {
			return Kondion.getNashorn().eval(new InputStreamReader(KLoader.get(path)));
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public KTexture texture(String name) {
		KTexture t = KLoader.textures.get(name);
		if (t == null)
			t = KLoader.getMissingTexture();
		return t;
	}
	
	public KModel obj(String name) {
		KModel t = KLoader.obj.get(name);
		return t;
	}
	
	public KAudio aud(String name) {
		KAudio t = KLoader.aud.get(name);
		return t;
	}
	
	public KObj_Solid raycast(Vector3f result, float sx, float sy, float sz, float tx, float ty, float tz, int steps, int types) {
		
		Vector3f testPoint = new Vector3f();
		
		for (int i = 0; i < steps; i ++) {
			result.set(sx + tx * i, sy + ty * i, sz + tz * i);
			KObj_Solid f = Kondion.getCurrentScene().checkPointCollision(result.x, result.y, result.z, types);
			
			if (f != null) {
				
				return f;
			}
			
		}
		
		return null;
	}
	
	public long currentTime() {
		return Kondion.msTime() - Kondion.startTime();
	}
	
	public long currentTick() {
		return Kondion.getFrame();
	}

	public void freeCam() {
		Kondion.getCurrentCamera().setFreeMode(true);
	}

	public void issueCommand(String msg) {
		Command.issue(msg, false);
	}
	
	public float fps() {
		return Kondion.getFramerate();
	}
	
	public int width() {
		return Window.getWidth();
	}
	
	public int height() {
		return Window.getHeight();
	}
}
