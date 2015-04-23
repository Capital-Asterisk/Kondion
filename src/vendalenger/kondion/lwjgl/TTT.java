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

package vendalenger.kondion.lwjgl;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glAlphaFunc;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import javax.swing.JOptionPane;

import org.lwjgl.util.glu.GLU;

/*
 * TTT, the same class used in my first 3d lwjgl game. (with modifications) TTT
 * serves the same purpose. as before.
 * 
 * =======================HISTORY OF THE VENDALENGER LOGO=======================
 * Once upon a time while working on my first lwjgl game. I modified NeHe lesson
 * 5 to a point where I had a cube class with a constructor (x, y z). I made 3
 * cubes (0, 2, 0), (1, 0, 0), and (2, 1, 0) creating this when the Camera_ was
 * put a distance: █ █ █ I decided it was a good logo so I made this: █
 * SHET █ A █ BYTE It was too informal, and by mistake I made this: █ █
 * GENERICO █ Generico sounded generic and Spanish so I went back in time when
 * I made up a name similar to Vendalenger (i was like 7 in daycare) and came up
 * with the name. So as a final I made: █ █ VENDALENGER █
 * 
 * btw: There was more before shetabyte. There was 776 and Separate Functions
 * (The names I used back in 2008 Roblox Lua scripting). There was also
 * Nyanbytes (I was such a meme master and got my Steam account id as nyanbyte5)
 * thats it?
 * =============================================================================
 */
public class TTT {

	public static boolean dark = false;
	public static float fov = 50;
	// public static FloatBuffer fogcolor = BufferUtils.createFloatBuffer(4);
	public static boolean fs = false;

	public static int Decide(String message, String objective, int type) {
		int d = JOptionPane.showConfirmDialog(null, message, objective,
				JOptionPane.YES_NO_OPTION, type);
		if (d == JOptionPane.OK_OPTION) {
			return 0;
		} else if (d == JOptionPane.NO_OPTION) {
			return 1;
		} else {
			return 2;
		}

	}

	public static void Error(String message) {
		JOptionPane.showMessageDialog(null, message, "Error Message",
				JOptionPane.ERROR_MESSAGE);

	}

	public static void Inform(String message) {
		JOptionPane.showMessageDialog(null, message, "Information",
				JOptionPane.INFORMATION_MESSAGE);

	}

	/** 3D mode (1/2) */
	public static void Three() {// not mine. (A bunch of code snippets bunched
		// up together :3)
		glViewport(0, 0, Window.getWidth(), Window.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(fov,
				((float) (Window.getWidth()) / (float) (Window.getHeight())),
				0.1f, 200.0f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glCullFace(GL_BACK);
		glEnable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_POINT_SMOOTH);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDepthFunc(GL_LEQUAL);
		glClearDepth(10.0f);
	}

	/** 2D mode */
	/*
	 * public static void Two() { glViewport(0, 0, getDisplayMode().getWidth(),
	 * getDisplayMode() .getHeight()); glMatrixMode(GL_PROJECTION);
	 * glLoadIdentity(); glOrtho(0, getDisplayMode().getWidth(),
	 * getDisplayMode().getHeight(), 0, 6, -6); glMatrixMode(GL_MODELVIEW);
	 * 
	 * glAlphaFunc(GL_GREATER, 0.0f); glEnable(GL_ALPHA_TEST);
	 * 
	 * glEnable(GL_TEXTURE_2D); glEnable(GL_DEPTH_TEST); glEnable(GL_BLEND);
	 * glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); glClearColor(0.0f,
	 * 0.0f, 0.0f, 0.0f); }
	 */
}