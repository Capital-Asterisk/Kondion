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

import javax.script.ScriptException;

import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.Kondion;
import vendalenger.port.Command;

public class KJS {
	
	public final ScriptObjectMirror o;
	public final ScriptObjectMirror d;
	public final StaticClass i;
	public final StaticClass c;
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
}
