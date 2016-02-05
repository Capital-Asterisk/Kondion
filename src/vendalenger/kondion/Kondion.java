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

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import vendalenger.kondion.js.KJS;
import vendalenger.kondion.kobj.GKO_Scene;
import vendalenger.kondion.kobj.OKO_Camera_;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.port.FileShortcuts;
import vendalenger.port.VD_FlConsole;

public class Kondion {
	
	public static final String version = "0.0.2-ACACIA-DEV";

	private static OKO_Camera_ dummyCamera;
	private static JFrame loadingScreen;
	private static KondionGame game;
	private static ScriptEngine jsEngine;
	private static Thread gameThread;
	private static KondionWorld world;
	private static KJS kjs;

	private static float fps;
	private static long ticks;
	private static float delta;

	public static boolean showPrespective = true;
	public static boolean showHud = false;
	
	private static void gameLoop() {
		
		world = new KondionWorld();

		KInput.setMouseLock(true);
		
		try {
			jsEngine.put("World", world);
			jsEngine.put("SCN", world.Scene);
			((Invocable) jsEngine).invokeFunction("kondionInit");
			((Invocable) jsEngine).invokeFunction("start");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		ticks = 0;

		long time = 0l;
		long prevTime = 0l;

		Window.setWindowVisible(true);
		
		while (glfwWindowShouldClose(Window.getWindow()) == GL_FALSE) {
			
			prevTime = System.nanoTime();
			delta = time / 1000000000.0f;
			ticks ++;

			KInput.update();
			
			//currentCamera.update();

			// Rendering

			TTT.three();

			glClearColor(world.clearColor.x, world.clearColor.y,
					world.clearColor.z, world.clearColor.w);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
												// framebuffer
			//currentCamera.gluLookAt();
			
			//glTranslatef(0, 0, 1);
			//FlatDrawing.renderBillboard(1, 1, KondionLoader.textures.get("noah"));
			
			for (int i = 0; i < world.Layers.size(); i++) {
				world.Layers.get(i).update();
			}
			
			ticks++;
			Window.update();
			
			fps = 1 / delta;
			time = System.nanoTime() - prevTime;
		}

		Window.end();
		System.exit(0);
	}

	public static OKO_Camera_ getCurrentCamera() {
		// Something getters and setters are useful for
		if (world.camera != null)
			return world.camera;
		else {
			return dummyCamera;
		}
	}
	
	public static GKO_Scene getCurrentScene() {
		return world.Scene;
	}

	public static KondionGame getGame() {
		return game;
	}
	
	public static float getFramerate() {
		return fps;
	}

	public static float getDelta() {
		return delta;
	}

	public static Thread getGameThread() {
		return gameThread;
	}
	
	public static ScriptEngine getNashorn() {
		return jsEngine;
	}
	
	public static KondionWorld getWorld() {
		return world;
	}

	public static void run(KondionGame g) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadingScreen = new JFrame("KONDION Game Engine");
				loadingScreen.setLayout(new BorderLayout());
				loadingScreen.setUndecorated(false);
				loadingScreen
						.setIconImage(VD_FlConsole.consoleWindow.consoleWindow
								.getIconImage());
				loadingScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				ImageIcon img = new ImageIcon(Kondion.class
						.getResource("kondion.png"));
				final JLabel imgl = new JLabel(img);
				imgl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				imgl.setOpaque(true);
				imgl.setBackground(java.awt.Color.decode("#080808"));
				imgl.setPreferredSize(new Dimension(800, 600));

				loadingScreen.add(imgl);
				loadingScreen.pack();
				loadingScreen.setLocationRelativeTo(null);
				loadingScreen.setVisible(true);
			}

		}).start();
		gameThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Window.setNatives();
					Window.initGL(1280, 720, false, false, g.getGameInfo()
							.getStringValue("GameName"));
					GL.createCapabilities();
					GLDrawing.setup();
					///FlatDrawing.
					
					jsEngine = new ScriptEngineManager()
							.getEngineByName("nashorn");

					System.out.print("Loading Javascript...");

					jsEngine.eval(new BufferedReader(
							new InputStreamReader(getClass()
									.getResourceAsStream("kondiondefault.js"))));

					System.out.print("Parsing game information...");
					JsonRootNode rootNode = g.getGameInfo();
					// reusable variable
					List<JsonNode> array;
					// Buttons
					JsonNode node = rootNode.getNode("Buttons");
					for (int i = 0; i < node.getFieldList().size(); i++) {
						array = node.getFieldList().get(i).getValue()
								.getArrayNode();
						KInput.regButton(node.getFieldList().get(i).getName()
								.getStringValue(), array.get(0)
								.getStringValue(), (array.get(1)
								.getStringValue().equals("key")) ? (0) : (1),
								KInput.toGLFWCode(array.get(2).getStringValue()
										.charAt(0)));
					}

					// Graphics
					KondionLoader.init();
					node = rootNode.getNode("Graphics");

					try {
						for (int i = 0; i < node.getFieldList().size(); i++) {
							array = node.getFieldList().get(i).getValue()
									.getArrayNode();
							for (int j = 0; j < array.size() - 1; j++) {
								System.out.println(array.get(j + 1));

							}

							KondionLoader.queueTexture(
									FileShortcuts.getChild(g.getGameDir(),
											"graphics"
													+ File.separator
													+ array.get(0)
															.getStringValue()),
									node.getFieldList().get(i).getName()
											.getStringValue(),
									GL11.class.getField(
											array.get(1).getStringValue())
											.getInt(0),
									GL11.class.getField(
											array.get(2).getStringValue())
											.getInt(0),
									GL11.class.getField(
											array.get(3).getStringValue())
											.getInt(0),
									GL11.class.getField(
											array.get(4).getStringValue())
											.getInt(0));
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}

					System.out.println("Doing other stuff...");
					GLDrawing.setup();

					System.out.print("Loading game scripts...");

					jsEngine.eval(new FileReader(FileShortcuts.getChild(
							g.getGameDir(), "masterscript.js")));
					
					dummyCamera = new OKO_Camera_();
					dummyCamera.look(0, 0, 5, 0, 0, 0);
					//dummyCamera.setFreeMode(true);
					
					kjs = new KJS();
					jsEngine.put("KJS", kjs);
					((Invocable) jsEngine).invokeFunction("init");
					Window.update();
					KondionLoader.load();

					loadingScreen.setVisible(false);
					loadingScreen.dispose();

					gameLoop();
				} catch (ScriptException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		gameThread.start();
	}
	
	public static void eggs() {
		System.out.println("EGGS");
	}

}
