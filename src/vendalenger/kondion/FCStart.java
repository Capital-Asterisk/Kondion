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

import javax.swing.JOptionPane;

import vendalenger.port.Command;
import vendalenger.port.VD_FlConsole;

public class FCStart {

	/**
	 * Start with fluffy console only
	 * 
	 * @param args
	 *            Command line arguments, what else?
	 */
	public static void main(String args[]) {
		VD_FlConsole.initConsole(600, 400, Boolean.parseBoolean(args[0]));
		Command.commandSetup();
		VD_FlConsole.showConsole();

		/* Run the startup script */
		Command.fcis(new File("startup.fcis"), "startup");
	}
}
