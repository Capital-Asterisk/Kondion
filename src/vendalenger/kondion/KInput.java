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

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_G;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_H;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_I;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_J;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_N;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_O;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_T;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_U;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_V;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Y;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import vendalenger.kondion.lwjgl.Window;

public class KInput {

	private static final short[] aToZ = new short[] {GLFW_KEY_A, GLFW_KEY_B,
			GLFW_KEY_C, GLFW_KEY_D, GLFW_KEY_E, GLFW_KEY_F, GLFW_KEY_G,
			GLFW_KEY_H, GLFW_KEY_I, GLFW_KEY_J, GLFW_KEY_K, GLFW_KEY_L,
			GLFW_KEY_M, GLFW_KEY_N, GLFW_KEY_O, GLFW_KEY_P, GLFW_KEY_Q,
			GLFW_KEY_R, GLFW_KEY_S, GLFW_KEY_T, GLFW_KEY_U, GLFW_KEY_V,
			GLFW_KEY_W, GLFW_KEY_X, GLFW_KEY_Y, GLFW_KEY_Z};

	private static List<KButton> buttonList = new ArrayList<KButton>();
	private static double mouseDX = 0, mouseDY = 0, mousePX = 0, mousePY = 0;
	private static boolean mouseLocked = false;
	private static DoubleBuffer mouseX = BufferUtils.createDoubleBuffer(1);
	private static DoubleBuffer mouseY = BufferUtils.createDoubleBuffer(1);

	public static boolean buttonIsDown(int buttonIndex) {
		if (buttonList.get(buttonIndex).device == 0) {
			// keyboard test
			return (GLFW.glfwGetKey(Window.getWindow(),
					buttonList.get(buttonIndex).key) == GL_TRUE);
		} else if (buttonList.get(buttonIndex).device == 1) {
			// mouse test
			return (GLFW.glfwGetMouseButton(Window.getWindow(),
					buttonList.get(buttonIndex).key) == GL_TRUE);
		}
		return false;
	}

	/**
	 * Get a button's index by its id. Its recommended that you use this to
	 * store the index in a variable then use buttonIsDown();
	 * 
	 * @param id
	 *            Id of the button defined in kondion.json
	 * @return The button's index
	 */
	public static int getButtonIndex(String id) {
		for (int i = 0; i < buttonList.size(); i++) {
			if (buttonList.get(i).id.equals(id))
				return i;
		}
		return -1;
	}

	/**
	 * @return The list of all the buttons
	 */
	public static List<KButton> getButtonList() {
		return buttonList;
	}

	public static double getMouseDX() {
		return mouseDX;
	}

	public static double getMouseDY() {
		return mouseDY;
	}

	public static int getMouseX() {
		return (int) mouseX.get(0);
	}

	public static int getMouseY() {
		return (int) mouseY.get(0);
	}

	public static boolean keyboardDown(int key) {
		return (GLFW.glfwGetKey(Window.getWindow(), key) == GL_TRUE);
	}

	public static boolean mouseDown(int button) {
		return (GLFW.glfwGetMouseButton(Window.getWindow(), button) == GL_TRUE);
	}

	public static void regButton(String id, String name, int device, int key) {
		buttonList.add(new KButton(id, name, device, key));
		((ScriptObjectMirror) ((ScriptObjectMirror) Kondion.getNashorn().get(
				"KJS")).get("b")).put(id, buttonList.size() - 1);
		// System.out.println(buttonList.toArray());
	}

	public static void setMouseLock(boolean b) {
		mouseLocked = b;
		if (b) {
			// TODO: hide cursor
		} else {
			// TODO: show cursor
		}

	}

	/**
	 * Makes a char into a GLFW key code. 'A' becomes GLFW_KEY_A, 'B' becomes
	 * GLFW_KEY_B, and so on...
	 * 
	 * @param c
	 *            The char
	 */
	public static int toGLFWCode(char c) {
		if (Character.isLetter(c)) {
			return aToZ[(Character.toUpperCase(c)) - 65];
		} else {
			return 0;
		}
	}

	public static void update() {
		// setMouseLock(true);
		mouseX.clear();
		mouseY.clear();
		GLFW.glfwGetCursorPos(Window.getWindow(), mouseX, mouseY);
		mouseDX = mouseX.get(0) - mousePX;
		mouseDY = mouseY.get(0) - mousePY;
		if (mouseLocked) {
			// Set mouse position to center
			GLFW.glfwSetCursorPos(Window.getWindow(), Window.getWidth() / 2,
					Window.getHeight() / 2);
			mousePX = Window.getWidth() / 2;
			mousePY = Window.getHeight() / 2;
		} else {
			mousePX = mouseX.get(0);
			mousePY = mouseY.get(0);
		}
		// System.out.println(mouseDX + " " + mouseDY);
	}
}

class KButton {

	// Mouse, keyboard
	public int device;

	public String id;
	public int key;
	public String name;

	public KButton(String i, String n, int d, int k) {
		System.out.println(i);
		id = i;
		name = n;
		device = d;
		key = k;
	}
}
