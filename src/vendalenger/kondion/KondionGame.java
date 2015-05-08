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

import argo.jdom.JsonRootNode;

public abstract class KondionGame {

	private File gameDir;
	private JsonRootNode gameInfo;

	public File getGameDir() {
		return gameDir;
	}

	public JsonRootNode getGameInfo() {
		return gameInfo;
	}

	public void setGameDir(File dir) {
		gameDir = dir;
	}

	public void setGameInfo(JsonRootNode info) {
		gameInfo = info;
	}
}
