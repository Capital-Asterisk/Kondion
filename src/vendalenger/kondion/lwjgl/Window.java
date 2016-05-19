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

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVersionString;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Random;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALContext;
import org.lwjgl.openal.ALDevice;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.objectbase.KObj_Solid;

public class Window {
	
	private static int windowWidth, windowHeight;
	private static long window;
	private static ALContext ac;
	private static GLFWKeyCallback keyCallback;

	public static void end() {
		//ac.destroy();
		AL.destroy(ac);
		glfwDestroyWindow(Window.getWindow());
	}

	public static int getHeight() {
		return windowHeight;
	}

	public static int getWidth() {
		return windowWidth;
	}

	public static long getWindow() {
		return window;
	}

	public static void setWindowVisible(boolean b) {
		if (b)
			glfwShowWindow(window);
		else
			glfwHideWindow(window);
	}

	/**
	 * The stock way of initializing lwjgl
	 *
	 * @param width
	 *            Width of the game window.
	 * @param height
	 *            Height of the game window.
	 * @param resizable
	 *            determine whenever the window is resizable.
	 * @param fullscreen
	 *            determine whenever the window is full screen.
	 * @param title
	 *            The window title.
	 */
	public static void initGL(int width, int height, boolean resizable,
			boolean fullscreen, String title) {
		
		double f = 6000007.600;
		System.out.println("Double Presishon: " + String.format("%.12f", f));
		System.out.println("Initialize...");
		System.out.println("Create window:");
		windowWidth = width;
		windowHeight = height;
		GLFWErrorCallback errorCallback;
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		if (glfwInit() != GL_TRUE) {
			throw new IllegalStateException(
					"Error in initializing GLFW Error callback");
		}

		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
												// after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
		
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) {
			System.err.println("Error in initializing GLFW Window");
		}

		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action,
					int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, GL_TRUE); // We will detect
																// this in our
																// rendering
																// loop
			}
		};
		glfwSetKeyCallback(window, keyCallback);

		GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		glfwSetWindowPos(window, (mode.width() - width) / 2,
				(mode.height() - height) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		
		System.out.println("GLFW Version: " + glfwGetVersionString());
		
		System.out.println("Initialize OpenAL...");
		
		//ALC.create();
		//if (devnotsdaorsdb == NULL) {
		//	System.err.println("Unable to open sound device");
		//}
		ac = ALContext.create();
		System.out.println(ac.getCapabilities().OpenAL10);
		System.out.println(ac.getCapabilities().OpenAL11);
		System.out.println(ac.isCurrent());
		
		//long devnotsdaorsdb = ALC10.alcOpenDevice((ByteBuffer) null);
		//ALC10.alcOpenDevice(deviceSpecifier)
		System.out.println(AL10.AL_NO_ERROR);
		/*int buff = AL10.alGenBuffers();
		//ByteBuffer buffy = BufferUtils.createByteBuffer(80000);
		int size = 3000;
		ByteBuffer buffy = BufferUtils.createByteBuffer(size);
		for (int t = 0; t < size; t++) {
			buffy.put((byte) ((Math.random() * 255 * ((double) (t - size) / size)) - 127));
			
			//buffy.put((byte)(((i / 6) % 2) * Byte.MAX_VALUE - 127));
			//buffy.put((byte) ((t / 2 * (((t >> 4 | t) >> 8) % 11) & 255) / 4 + (t * (((t >> 5 | t) >> 8) % 11) & 100) / 2));
			//new Random().
		}
		//byte[] f = new byte[1000];
		//for (int i = 0; i < f.length; i++) {
		//	f[i] *= i / 1000;
		//}
		//new Random().nextBytes(f);
		//buffy.put(f);
		buffy.flip();
		AL10.alBufferData(buff, AL10.AL_FORMAT_MONO8, buffy, 16000);
		System.out.println(AL10.alGetError());
		src = AL10.alGenSources();
		AL10.alSourcei(src, AL10.AL_BUFFER, buff);
		AL10.alSourcef(src, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(src, AL10.AL_GAIN, 1.0f);
		AL10.alSource3f(src, AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
		AL10.alSource3f(src, AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
		AL10.alSourcePlay(src);
		System.out.println("Sources: " + ALC11.ALC_MONO_SOURCES);
		System.out.println(AL10.alGetError());
		// TTT.Two();*/
	}
	/*public static int src;
	public static void poopy() {
		AL10.alSourcePlay(src);
		//Matrix4f f = ((KObj_Solid) Kondion.getCurrentScene().get("car")).actTransform;
		//AL10.alListener3f(AL10.AL_POSITION, f.m30, f.m31, f.m32);
		System.out.println(AL10.alGetError());
	}*/
	
	
	/**
	 * Sets LWJGL native library path.
	 */
	public static void setNatives() {
		String dataModel = System.getProperty("sun.arch.data.model");
		// new File("").getAbsolutePath() is getting the directory
		String path = new File("").getAbsolutePath();
		if (!path.endsWith(File.separator))
			path += File.separator;
		path += "lwjgl";
		
		System.out.println("-------------------SOME INFORMATION-----------------");
		System.out.println("Kondion Version:  " + Kondion.version);
		System.out.println("Today's Date:     " + new Date().toString());
		System.out.println("On directory:     " + new File("").getAbsolutePath());
		System.out.println("OS                " + System.getProperty("os.name")
				+ " " + System.getProperty("os.version"));
		System.out.println("Architecture:     " + System.getProperty("os.arch"));
		System.out.println("Data model:       " + dataModel + " bit");
		System.out.println("CPU Cores:        "
				+ Runtime.getRuntime().availableProcessors());
		System.out.println("LWJGL version:    " + org.lwjgl.Version.getVersion());
		
		//System.
		//String os = System.getProperty("os.name").toLowerCase();
		/*
		// Detect the Operating system then load the appropriate lwjgl natives
		if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
			System.out.println("Natives to use:   Loading Linux LWJGL Natives, finally.");
			path += "/linux";
		} else if (os.toLowerCase().contains("win")) {
			System.out
					.println("Natives to use:   Loading Windows LWJGL Natives. Windows is fine, but still. get a Linux.");
			path += "\\windows";
		} else if (os.contains("mac")) {
			System.out
					.println("Natives to use:   Loading Macintosh Apples LWJGL Natives and Linux is better that this expensible OS, so get a Linux.");
			path += "/getalinux";
		}
		
		// 64, or 32 bit?
		if (dataModel.equals("64")) {
			// If amd64
			path += File.separator + "x64";
		} else {
			// If x86 or i386
			path += File.separator + "x86";
		}
		*/
		
		path += File.separator + "natives";
		
		System.out.println("LWJGL Path:       " + path);
		
		System.out.println("----------------------------------------------------");
		
		System.setProperty("org.lwjgl.librarypath", path);
	}

	public static void update() {
		glfwSwapBuffers(window); // swap the color buffers
		glfwPollEvents();
	}
}
