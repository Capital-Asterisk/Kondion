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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector3f;

import vendalenger.kondion.lwjgl.Camera_;
import vendalenger.kondion.lwjgl.FlatDrawing;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.objects.Entity;
import vendalenger.kondion.objects.PhysicEntity;
import vendalenger.kondion.objects.ProtoEntity;
import vendalenger.kondion.scene.Scene;

public class Kondion {

	private static Camera_ currentCamera;
	private static KondionGame game;
	private static Thread gameThread;
	private static ScriptEngine jsEngine;
	private static Scene currentScene;
	private static List<ProtoEntity> pEntityList;
	private static List<ScriptObjectMirror> mirrorList;
	private static List<Entity> entityList;

	public static Camera_ getCurrentCamera() {
		return currentCamera;
	}

	public static Thread getGameThread() {
		return gameThread;
	}

	public static KondionGame getGame() {
		return game;
	}

	public static List<Entity> getEntityList() {
		return entityList;
	}

	public static List<ScriptObjectMirror> getMirrorList() {
		return mirrorList;
	}

	public static List<ProtoEntity> getProtoEntityList() {
		return pEntityList;
	}

	public static void eggs() {
		System.out.println("EGGS");
		for (int i = 0; i < entityList.size(); i++) {
			System.out.println(entityList.get(i));
		}
	}

	public static void run(KondionGame g) {
		gameThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Window.setNatives();
					Window.initGL(800, 600, false, false, g.getGameInfo()
							.getStringValue("GameName"));
					entityList = new ArrayList<Entity>();
					mirrorList = new ArrayList<ScriptObjectMirror>();
					pEntityList = new ArrayList<ProtoEntity>();
					jsEngine = new ScriptEngineManager()
							.getEngineByName("nashorn");
					jsEngine.eval(new BufferedReader(
							new InputStreamReader(getClass()
									.getResourceAsStream("kondiondefault.js"))));
					for (int i = 0; i < 2; i++) {
						((Invocable) jsEngine).invokeFunction("init");
					}
					GLContext.createFromCurrent();
					KondionLoader.load();
					FlatDrawing.setup();
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

	public static void gameLoop() {
		// if (GLFW.glfwGetKey(Initializer.window, GLFW.) == GL_TRUE) {
		// currentCamera.getEye().z += 0.1;
		// }
		currentCamera = new Camera_();
		currentCamera.look(0, 0, 8, 0, 0, 0);

		currentScene = new Scene();
		currentScene.addAABlock(new Vector3f(0, 0, 0), 1, 1, 4, 4, 4, 4);
		currentScene.doGlBuffers();

		try {
			((Invocable) jsEngine).invokeFunction("start");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		byte t = 0;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		while (glfwWindowShouldClose(Window.getWindow()) == GL_FALSE) {

			// Updating

			currentCamera.update();
			KInput.update();

			if (t == 5) {
				t = 0;
				for (int i = 0; i < mirrorList.size(); i++) {
					mirrorList.get(i).callMember("tick", mirrorList.get(i));
				}
			} else
				t++;
			for (Entity entity : entityList) {
				if (entity instanceof PhysicEntity)
					((PhysicEntity) entity).move();
			}

			// Rendering

			TTT.three();

			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
																// framebuffer
			currentCamera.gluLookAt();

			currentScene.render();

			for (int i = 0; i < entityList.size(); i++) {
				entityList.get(i).render();
			}

			Window.update();
		}

		Window.end();
		System.exit(0);
	}
}
