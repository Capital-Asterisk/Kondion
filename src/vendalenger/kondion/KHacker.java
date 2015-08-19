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
import vendalenger.port.FileShortcuts;
import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

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
		try {
			KondionGame game = new KondionGame() {};
			String text = FileShortcuts.readTextFile(new File(path));
			JsonRootNode rootNode = new JdomParser().parse(text);
			game.setGameInfo(rootNode);
			game.setGameDir(new File(path).getParentFile());
			hacking = true;
			Kondion.run(game);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
