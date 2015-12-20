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

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import sun.font.Script;
import vendalenger.kondion.kobj.Scene;
import vendalenger.kondion.lwjgl.Camera_;
import vendalenger.kondion.lwjgl.FlatDrawing;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.port.FileShortcuts;
import vendalenger.port.VD_FlConsole;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;

public class Kondion {

	private static JFrame loadingScreen;

	private static Camera_ currentCamera;
	private static KondionGame game;
	private static Scene currentScene;
	private static ScriptEngine jsEngine;
	private static Thread gameThread;

	private static long ticks;
	private static float delta;

	private static void gameLoop() {
		// if (GLFW.glfwGetKey(Initializer.window, GLFW.) == GL_TRUE) {
		// currentCamera.getEye().z += 0.1;
		// }
		currentCamera = new Camera_();
		currentCamera.look(0, 0, 8, 0, 0, 0);
		// currentScene.doGlBuffers();
		
		// DEBUG
		currentScene = new Scene();
		// END

		try {
			((Invocable) jsEngine).invokeFunction("kondionInit");
			((Invocable) jsEngine).invokeFunction("start");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		ticks = 0;

		long time = 0;
		long prevTime = 0l;

		Window.setWindowVisible(true);
		
		while (glfwWindowShouldClose(Window.getWindow()) == GL_FALSE) {
			prevTime = System.nanoTime();
			delta = time / 1000000000.0f;

			currentCamera.update();

			// Rendering

			TTT.three();

			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
																// framebuffer
			// currentCamera.gluLookAt();

			/*for (int i = 0; i < currentScene.getRenders().size(); i++) {
				currentScene.getRenders().get(i).render();
				
			}*/
			
			ticks++;
			Window.update();
			
			time = System.nanoTime() - prevTime;

		}

		Window.end();
		System.exit(0);
	}

	public static Camera_ getCurrentCamera() {
		return currentCamera;
	}
	
	public static Scene getCurrentScene() {
		return currentScene;
	}

	public static KondionGame getGame() {
		return game;
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
					Window.initGL(800, 600, false, false, g.getGameInfo()
							.getStringValue("GameName"));
					GLContext.createFromCurrent();
					
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
					FlatDrawing.setup();

					System.out.print("Loading game scripts...");

					jsEngine.eval(new FileReader(FileShortcuts.getChild(
							g.getGameDir(), "masterscript.js")));

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
