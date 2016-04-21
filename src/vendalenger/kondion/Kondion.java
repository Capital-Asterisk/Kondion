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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import jdk.nashorn.internal.parser.JSONParser;
import vendalenger.kondion.js.KJS;
import vendalenger.kondion.kobj.GKO_Scene;
import vendalenger.kondion.kobj.OKO_Camera_;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KModel;
import vendalenger.port.FileShortcuts;
import vendalenger.port.VD_FlConsole;

public class Kondion {
	
	public static final String version = "0.0.2-ACACIA-DEV";

	private static OKO_Camera_ dummyCamera;
	private static JFrame loadingScreen;
	private static ScriptEngine jsEngine;
	private static Thread gameThread;
	private static KondionWorld world;
	public static KJS kjs;

	private static int width = 0, height = 0;
	private static float delta;
	private static float fps;
	private static long ticks;
	private static long startTime;
	private static long currentTime;

	public static boolean showPrespective = true;
	public static boolean showHud = false;
	//public static KModel km; // tests only
	
	private static void gameLoop() {
		
		world = new KondionWorld();

		KInput.setMouseLock(true);
		
		try {
			jsEngine.put("World", world);
			jsEngine.put("SCN", world.scene);
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
		
		//km = null;
		//try {
		//	km = KLoader.loadObj(new FileInputStream(new File("/home/neal/Desktop/kaytrav.obj")));
		//} catch (FileNotFoundException e) {
		//	e.printStackTrace();
		//}
		//km.createVbo();
		
		startTime = System.currentTimeMillis();
		
		while (glfwWindowShouldClose(Window.getWindow()) == GL_FALSE) {
			
			prevTime = System.nanoTime();
			delta = time / 1000000000.0f;
			ticks ++;

			KInput.update();
			
			//currentCamera.update();

			// Rendering

			TTT.three();

			//currentCamera.gluLookAt();
			
			//glTranslatef(0, 0, 1);
			//FlatDrawing.renderBillboard(1, 1, KondionLoader.textures.get("noah"));
			
			currentTime = System.currentTimeMillis();
			
			world.scene.update();
			
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
		return world.scene;
	}

	public static float getFramerate() {
		return fps;
	}
	
	public static long msTime() {
		return currentTime;
	}
	
	public static long startTime() {
		return startTime;
	}

	public static float getDelta() {
		return delta;
	}
	
	/**
	 * This number is added to every frame.
	 * @return
	 */
	public static long getFrame() {
		return ticks;
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
	
	public static void setSize(int w, int h) {
		width = w;
		height = h;
	}

	public static void run(String identifier) {
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
					
					if (width * height == 0) {
						// No width or height has been specified
						//width = 1280;
						//height = 720;
						width = 800;
						height = 600;
					}
					
					Window.initGL(width, height, false, false, KLoader.getKResource(identifier).getNeatName());
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
					JsonRootNode rootNode = KLoader.getKResource(identifier).getConfig();
					
					// reusable variable
					List<JsonNode> array;
					
					// Buttons
					
					System.out.println("Loading buttons...");
					
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
					
					node = rootNode.getNode("Graphics");

					System.out.println("Loading textures (" + node.getFieldList().size() + ")");
					
					try {
						for (int i = 0; i < node.getFieldList().size(); i++) {
							array = node.getFieldList().get(i).getValue()
									.getArrayNode();
							
							System.out.println("TEXTURE: " + node.getFieldList().get(i).getName()
									.getStringValue());

							KLoader.registerTexture(identifier + ":" + array.get(0).getStringValue(),
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
											.getInt(0), true);
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
					
					// OBJ
					
					node = rootNode.getNode("OBJ");
					
					System.out.println("Loading OBJ models (" + node.getFieldList().size() + ")");

					try {
						for (int i = 0; i < node.getFieldList().size(); i++) {
							array = node.getFieldList().get(i).getValue()
									.getArrayNode();

							KLoader.registerObj(identifier + ":" + array.get(0).getStringValue(),
									node.getFieldList().get(i).getName().getStringValue(),
									array.get(1).getStringValue(), true);
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}

					// Audio
					
					node = rootNode.getNode("Audio");
					
					System.out.println("Loading Audio (" + node.getFieldList().size() + ")");

					try {
						for (int i = 0; i < node.getFieldList().size(); i++) {
							array = node.getFieldList().get(i).getValue()
									.getArrayNode();

							KLoader.registerAudio("ol:" + array.get(0).getStringValue(), node.getFieldList().get(i).getName().getStringValue(), true);
							
							//KLoader.registerObj(identifier + ":" + array.get(0).getStringValue(),
							//		node.getFieldList().get(i).getName().getStringValue(),
							//		array.get(1).getStringValue(), true);
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}

					System.out.println("Doing other stuff...");
					
					GLDrawing.setup();

					System.out.print("Loading game scripts...");

					jsEngine.eval(new InputStreamReader(KLoader.get(identifier + ":masterjs")));
					
					dummyCamera = new OKO_Camera_();
					dummyCamera.look(0, 0, 5, 0, 0, 0);
					//dummyCamera.setFreeMode(true);
					
					kjs = new KJS();
					jsEngine.put("KJS", kjs);
					((Invocable) jsEngine).invokeFunction("init");
					Window.update();
					KLoader.load();

					loadingScreen.setVisible(false);
					loadingScreen.dispose();

					gameLoop();
				} catch (ScriptException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
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
