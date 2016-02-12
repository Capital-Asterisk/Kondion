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

package vendalenger.kondion.lwjgl.resource;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.ARBShaderObjects;

public class KondionShader {

	private int frag;
	private int vert;
	
	private int prog;

	public KondionShader(int v, int f, int p) {
		vert = v;
		frag = f;
		prog = p;
	}

	public int uniformLocation(CharSequence name) {
		return glGetUniformLocation(prog, name);
	}
	
	public void useProgram() {
		ARBShaderObjects.glUseProgramObjectARB(prog);
	}

	public static void unbind() {
		ARBShaderObjects.glUseProgramObjectARB(0);
	}
}
