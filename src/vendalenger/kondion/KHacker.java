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

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KResource;
import vendalenger.port.FileShortcuts;

public class KHacker {

	private static boolean hacking = false;

	/**
	 * Run a game directly from a folder, without a .kog file. For debug
	 * purposes.
	 * 
	 * @param path
	 *            Path to your game folder
	 */
	public static void runGame(String path) {
		//KondionGame game = new KondionGame() {};
		KLoader.init();
		KResource res = KLoader.addFolderResource(new File(path));
		//String text = FileShortcuts.readInputStream(res.get("kondion"))
		hacking = true;
		Kondion.run(res.getName());
	}
}
