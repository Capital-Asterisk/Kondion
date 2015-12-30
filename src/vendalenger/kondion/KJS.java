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

package vendalenger.kondion;

import javax.script.ScriptException;

import jdk.internal.dynalink.beans.StaticClass;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.joml.Vector3f;

import vendalenger.port.Command;

public class KJS {
	
	public final ScriptObjectMirror o;
	public final StaticClass i;
	
	public KJS() throws ScriptException {
		i = (StaticClass) Kondion.getNashorn().eval("Java.type(\"vendalenger.kondion.KInput\")");
		o = (ScriptObjectMirror) Kondion.getNashorn().eval("{"
				/*+ "  node: Java.type(\"vendalenger.kondion.objectbase.KObj_Node\"),"
				+ "  oriented: Java.type(\"vendalenger.kondion.objectbase.KObj_Oriented\"),"
				+ "  render: Java.type(\"vendalenger.kondion.objectbase.KObj_Renderable\"),"
				+ "  solid: Java.type(\"vendalenger.kondion.objectbase.KObj_Solid\"),"
				+ "  extend: function(from, with) {"
				+ "    var java"
				+ "  }"*/
				+ "}");

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
