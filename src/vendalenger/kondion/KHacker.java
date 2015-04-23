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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeType;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import vendalenger.kondion.lwjgl.FlatDrawing;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.port.Command;
import vendalenger.port.FileShortcuts;
import vendalenger.port.VD_FlConsole;

public class KHacker {

	private static boolean hacking = false;

	public static void runGame(KondionGame k) {
		hacking = true;
		VD_FlConsole.initConsole(500, 500, true);
		Command.commandSetup();
		VD_FlConsole.showConsole();
		new Thread(() -> {
			// Initializer.setNatives();
			// Initializer.initGL(800, 600, false, false, k.getWindowName());
			// FlatDrawing.setup();
			// KondionLoader.init();
			// Command.issue("^fc.fcis (kondion/" + k.getGameId() +
			// "/init.fcis)", false);

			}).start();
	}

	public static void runGame(String path) {
		try {
			KondionGame game = new KondionGame() {};
			Map<String, String> info = new LinkedHashMap<String, String>();
			String text = FileShortcuts.readTextFile(new File(path));
			JsonRootNode rootNode = new JdomParser().parse(text);

			// Game info
			for (int i = 0; i < rootNode.getFieldList().size(); i++) {
				// System.out.println(rootNode.getFieldList().get(i));
				if (rootNode.getFieldList().get(i).getValue().getType() == JsonNodeType.STRING) {
					info.put(
							rootNode.getFieldList().get(i).getName().getText(),
							rootNode.getFieldList().get(i).getValue().getText());
				}
			}

			List<Object> list = Arrays.asList(info.keySet().toArray());
			for (int i = 0; i < info.size(); i++) {
				System.out.println(list.get(i) + ": " + info.get(list.get(i)));
			}
			info.put("gameDir", path);

			game.setGameInfo(rootNode);

			// reusable variable
			List<JsonNode> array;

			// Buttons
			JsonNode node = rootNode.getNode("Buttons");
			for (int i = 0; i < node.getFieldList().size(); i++) {
				array = node.getFieldList().get(i).getValue().getArrayNode();
				KInput.regButton(node.getFieldList().get(i).getName()
						.getStringValue(), array.get(0).getStringValue(),
						(array.get(1).getStringValue().equals("key")) ? (0)
								: (1), KInput.toGLFWCode(array.get(2)
								.getStringValue().charAt(0)));
			}

			// Graphics
			KondionLoader.init();
			node = rootNode.getNode("Graphics");
			for (int i = 0; i < node.getFieldList().size(); i++) {
				array = node.getFieldList().get(i).getValue().getArrayNode();
				for (int j = 0; j < array.size() - 1; j++) {
					System.out.println(array.get(j + 1));

				}

				KondionLoader.queueTexture(new File(new File(path)
						.getParentFile().getAbsolutePath()
						+ "/graphics/"
						+ array.get(0).getStringValue()), node.getFieldList()
						.get(i).getName().getStringValue(), GL11.GL_NEAREST,
						GL11.GL_NEAREST, GL11.GL_REPEAT, GL11.GL_REPEAT);
			}

			hacking = true;
			Kondion.run(game);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
