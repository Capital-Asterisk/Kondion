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

package vendalenger.port;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import javax.script.ScriptException;

import org.lwjgl.opengl.GL11;

import vendalenger.kondion.KHacker;
import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.resource.KondionLoader;

public class Command {

	public static ArrayList<Command> commandList = new ArrayList<Command>();

	public boolean hidden;

	public String key, help, usage;

	/**
	 * Create a command that could be accessed through the console, player chat,
	 * and so on.
	 *
	 * @param keyword
	 *            the name of the command example: keyword()
	 * @param quickhelp
	 *            the quick statement shown when help is called.
	 * @param use
	 *            the message given, usually an example, of how to use this
	 *            method. Automatically begins with "Usage for command: keyword"
	 */
	public Command(String keyword, String quickhelp, String use) {
		key = keyword;
		help = quickhelp;
		usage = use;

	}

	/**
	 * Called when the command is issued.
	 *
	 * @param args
	 *            arguments included in the issue example: keyword(arg1, arg2)
	 */
	public Object action(String[] args) {
		return "";
	}

	@Override
	public String toString() {
		return key;
	}

	/**
	 * Add a command to the command list so it could be used.
	 *
	 * @param c
	 *            The command to be added.
	 * @param hidden
	 *            Hides the command from help.
	 */
	public static void addCommand(Command c, boolean hidden) {
		c.hidden = hidden;
		commandList.add(c);
		sort();
	}

	/**
	 * Setup default commands and Editors for Fluffy Console
	 */
	public static void commandSetup() {

		Command.addCommand(new Command("calculate", "Do some math",
				"^calculate [MATH]" + "\nReturns zK,FVC  VC") {
			@Override
			public String action(String[] args) {
				double sum = 0;
				for (String s : args) {
					try {
						sum += Double.parseDouble(s);
					} catch (NumberFormatException e) {
						VD_FlConsole.println("addition: " + s
								+ " is not a valid number.");
					}
				}
				return String.valueOf(sum);
			}
		}, false);

		Command.addCommand(new Command("clr", "Clear the console screen.",
				"I don't like that.") {
			@Override
			public String action(String[] args) {
				VD_FlConsole.clear();
				return "";
			}
		}, false);

		Command.addCommand(
				new Command(
						"ef.regtex",
						"Register a texture",
						"^ef.regtex [id] [file]"
								+ "\nid: Id - The Id the texture will be identified by. "
								+ "\nfile: path - Path to image in electricfence folder. eg, /img/living/human.png"
								+ "\n\nThis command will replace a texture if the same id exists") {
					@Override
					public String action(String[] args) {

						KondionLoader.registerTexture(new File(args[1]),
								args[0], GL11.GL_NEAREST, GL11.GL_NEAREST,
								GL11.GL_CLAMP, GL11.GL_CLAMP);
						return null;
					}
				}, false);

		Command.addCommand(new Command("f.copy",
				"Copy a files to the system clipboard",
				"^f.copy [file0] [file1] [file2] ..."
						+ "\nfile: file(s) - Files to copy") {
			@Override
			public String action(final String[] args) {
				Platform.runLater(() -> {
					Clipboard sc = Clipboard.getSystemClipboard();
					ClipboardContent cbc = new ClipboardContent();
					ArrayList<String> files = new ArrayList<String>();
					for (int i = 0; i < args.length; i++) {
						VD_FlConsole.println("Clipboard: " + args[i]);
						files.add(args[i]);
					}
					cbc.putFilesByPath(files);
					sc.setContent(cbc);
				});
				return "";
			}
		}, false);

		Command.addCommand(
				new Command(
						"fc.color",
						"Change the foreground and background",
						"\n^color [hex] [hex]"
								+ "\nhex: The hex value of the desired background color."
								+ "\nhex: The hex value of the desired foreground color."
								+ "\n\nBoth can be left as null for no change."
								+ "\nDo not include # or 0x") {
					@Override
					public String action(String[] args) {
						if (args[1].equals("null")) {
							VD_FlConsole.setColor(null,
									Color.decode("0x" + args[0]));
						} else if (args[0].equals("null")) {
							VD_FlConsole.setColor(Color.decode("0x" + args[1]),
									null);
						} else if (!args[0].equals("null")
								&& !args[1].equals("null")) {
							VD_FlConsole.setColor(Color.decode("0x" + args[1]),
									Color.decode("0x" + args[0]));
						}
						return "";
					}
				}, false);

		Command.addCommand(new Command("fc.fcis", "Run an FCIS",
				"^fc.fcis [file] [arg#0] [arg#1] [arg#2] ..."
						+ "\nfile: fcis - Path to the file containing commands"
						+ "\nargs: arguments - Values to be passed on."
						+ "\n\nPlease see FCIS help for more info.") {
			@Override
			public String action(String[] args) {
				return fcis(new File(args[0]),
						Arrays.copyOfRange(args, 1, args.length));
			}
		}, false);

		Command.addCommand(new Command("fc.testerror", "Cause an error",
				"Connect to the invalid port 235325 with a blank host.") {
			@Override
			public String action(String[] args) {
				VD_FlConsole.testError();
				return "";
			}
		}, false);

		Command.addCommand(
				new Command(
						"fc.title",
						"Set or return the title of the Debvi screen or this.",
						"^title [string(console,debvi)] [bool] [string]"
								+ "\nstring console : Set mode to the title of this Console"
								+ "\nstring debvi : Set mode to the title of the Debvi screen"
								+ "\nbool: return Return the title of console or debvi"
								+ "\nstring: title The title to be set.") {
					@Override
					public String action(String[] args) {
						String re = "";
						if (Boolean.parseBoolean(args[1])) {
							if (args[0].equals("console")) {
								re = VD_FlConsole.consoleWindow.consoleWindow
										.getTitle();
							}
							if (args[0].equals("debvi")) {
								// re = DebviFrame..getTitle();
							}
						} else {
							if (args[0].equals("console")) {
								VD_FlConsole.consoleWindow.consoleWindow
										.setTitle(args[2]);
							}
							if (args[0].equals("debvi")) {
								// DebviFrame.mainWindow.setTitle(args[2]);
							}
						}
						return re;
					}
				}, false);

		Command.addCommand(
				new Command(
						"help",
						"Show help.",
						"How to use this console."
								+ "\nThis whole console is command based. Syntax follows:"
								+ "\n\n ^command arg0 arg1 arg2 arg3 arg4 and so on..."
								+ "\n\nSome commands like '^m.sum' have return values."
								+ "\nReturn values could be used in Commands with other commands"
								+ "\nin them using brackets as so. Think of your BEDMAS, brackets are first."
								+ "\n\n ^m.sum (^m.sum 1 4) 1 Returns 6"
								+ "\n\nA series of commands could be stored in a .fcis"
								+ "\n(Fluffy Console Isd.welcomenterpreted Script)"
								+ "\nAn fcis could be created by a simple text editor."
								+ "\nIn fact, Debvi has an fcis editor built in.") {
					@Override
					public String action(String[] args) {
						if (args[0].equalsIgnoreCase("null")) {
							VD_FlConsole.println("Showing list of commands: ");
							System.out
									.println("For more details, use help key\n");
							for (int i = 0; i < Command.commandList.size(); i++) {
								if (!commandList.get(i).hidden) {
									VD_FlConsole.println(Command.commandList
											.get(i).key
											+ " - "
											+ Command.commandList.get(i).help);
								}
							}
							System.out
									.println("Have no clue of what your doing? try \"^help help\"");
						} else {
							System.out
									.println("Showing long help for command: "
											+ args[0] + "\n");
							for (int i = 0; i < Command.commandList.size(); i++) {
								if (commandList.get(i).key
										.equalsIgnoreCase(args[0])) {
									System.out
											.println(commandList.get(i).usage);
								}
							}
						}
						return "";
					}
				}, false);

		Command.addCommand(
				new Command(
						"jvm.exit",
						"System.exit() (WARNING)",
						"^jvm.exit [int]"
								+ "\nint: status - 0 for normal. (default 0) see System.exit(); in Java.") {
					@Override
					public String action(String[] args) {
						if (args[0] != null) {
							System.exit(Integer.parseInt(args[0]));
						} else {
							System.exit(0);
						}
						return "";
					}
				}, false);

		Command.addCommand(new Command("jvm.gc", "Free memory from JVM",
				"Free memory from the JVM"
						+ "\nIn java, System.gc(); is called."
						+ "\njavaw will still use as much memory.") {
			@Override
			public String action(String[] args) {
				System.gc();
				return "";
			}
		}, false);

		Command.addCommand(new Command("jvm.tcount",
				"List the number of active threads",
				"In Java, Thread.activeCount();" + "\nIs called") {
			@Override
			public String action(String[] args) {
				VD_FlConsole.println(Thread.activeCount());
				return Thread.activeCount() + "";
			}
		}, false);

		Command.addCommand(new Command("kdion.js",
				"Run some javascript using Nashorn", "kdion.js [string]"
						+ "\nstring: string - Javascript code") {
			@Override
			public String action(String[] args) {
				try {
					Kondion.getNashorn().eval(args[0]);
				} catch (ScriptException e) {
					e.printStackTrace();
				}
				return "";
			}
		}, false);

		Command.addCommand(new Command("kdion.rungamedir",
				"Run a game from a folder", "kdion.rungamefolder [file]"
						+ "\nfile: json - Path to kondion.json") {
			@Override
			public String action(String[] args) {
				KHacker.runGame(args[0]);
				return "";
			}
		}, false);

		Command.addCommand(
				new Command(
						"pref.clear",
						"Removes all the entries",
						"^pref.clear"
								+ "\nThis command will clear all pref entries in the memory."
								+ "\nIt will be saved to a file when write is called.") {
					@Override
					public String action(String[] args) {
						VD_Keydata.remove(args[0]);
						return "";
					}
				}, false);

		Command.addCommand(new Command("pref.list",
				"List all entries in Preferences", "A preferences operation.") {
			@Override
			public String action(String[] args) {
				String p = "Listing preference entries...";
				p += "\ntag :: key :: value";

				for (int i = 0; i < VD_Keydata.keys.size(); i++) {
					p += "\n" + VD_Keydata.tags.get(i) + " :: "
							+ VD_Keydata.keys.get(i) + " :: "
							+ VD_Keydata.values.get(i).getText();
				}

				return p;
			}
		}, false);

		Command.addCommand(
				new Command(
						"pref.load",
						"Load a preferences file.",
						"^pref.load [file]"
								+ "\nfile: pref - Path to the desired pref file."
								+ "\n\nThe preferences file for this session is changed to this file.") {
					@Override
					public String action(String[] args) {
						VD_Keydata.load(new File(args[0]));
						return "";
					}
				}, false);

		Command.addCommand(new Command("pref.remove", "Remove an entry",
				"^pref.remove [string]"
						+ "\nstring: key - The key of the entry to remove") {
			@Override
			public String action(String[] args) {
				VD_Keydata.remove(args[0]);
				return "";
			}
		}, false);

		Command.addCommand(
				new Command(
						"pref.set",
						"Create or change an entry contained in preferences.",
						"^pref.set [string] [string] [string]"
								+ "\nstring: key - The key of the entry to create or change."
								+ "\nstring: tag - The tag of the entry to create or change. see Pref tags."
								+ "\nstring: value - The value to be set to the entry.") {
					@Override
					public String action(String[] args) {
						VD_Keydata.set(args[0], args[2], args[1]);
						return "";
					}
				}, false);

		Command.addCommand(
				new Command(
						"pref.write",
						"Write Preferences to a file",
						"^pref.write [file]"
								+ "\nfile: pref - Path to overwrite or create a file."
								+ "\n\nPref files come in .pref and the Debvi default is debvi.pref"
								+ "\n^pref.write (debvi.pref)") {
					@Override
					public String action(String[] args) {
						VD_Keydata.write(new File(args[0]));
						return "";
					}
				}, false);

		/*
		 * Command.addCommand(new Command("debvi.reset",
		 * "Total Reset (WARNING)", "^debvi.reset [string]" +
		 * "\nstring: confirm - Confirm the reset by typing CoNfirMthEREseT123455678912"
		 * + "\n\nThis will erase Prefs ") {
		 * 
		 * @Override public String action(String[] args) { new
		 * File("debvi.pref").delete(); System.exit(0); return ""; } }, false);
		 */

		Command.addCommand(new Command("print", "Print a message.",
				"^print [string] " + "\nHELLO WORLD!!!!!") {
			@Override
			public String action(String[] args) {
				VD_FlConsole.println(args[0]);
				return args[0];
			}
		}, false);

		// Eggs

		Command.addCommand(new Command("eggs", "Show your eggs.",
				"Do you like eggs?") {
			@Override
			public String action(String[] args) {
				// The \u2588 character is the full block character.
				VD_FlConsole
						.println("\u2588\u2588\u2588\u2588  \u2588\u2588\u2588\u2588\u2588   \u2588   \u2588 \u2588\u2588\u2588\u2588\u2588 \u2588   \u2588   \u2588     \u2588\u2588\u2588\u2588\u2588 \u2588 \u2588   \u2588 \u2588\u2588\u2588\u2588\u2588");
				VD_FlConsole
						.println("\u2588   \u2588 \u2588   \u2588   \u2588   \u2588 \u2588   \u2588 \u2588   \u2588   \u2588     \u2588     \u2588 \u2588  \u2588  \u2588");
				VD_FlConsole
						.println("\u2588   \u2588 \u2588   \u2588    \u2588 \u2588  \u2588   \u2588 \u2588   \u2588   \u2588     \u2588\u2588\u2588\u2588\u2588 \u2588 \u2588\u2588\u2588   \u2588\u2588\u2588\u2588\u2588");
				VD_FlConsole
						.println("\u2588   \u2588 \u2588   \u2588     \u2588   \u2588   \u2588 \u2588   \u2588   \u2588     \u2588     \u2588 \u2588  \u2588  \u2588");
				VD_FlConsole
						.println("\u2588\u2588\u2588\u2588  \u2588\u2588\u2588\u2588\u2588     \u2588   \u2588\u2588\u2588\u2588\u2588 \u2588\u2588\u2588\u2588\u2588   \u2588\u2588\u2588\u2588\u2588 \u2588\u2588\u2588\u2588\u2588 \u2588 \u2588   \u2588 \u2588\u2588\u2588\u2588\u2588");
				VD_FlConsole.println("");
				VD_FlConsole
						.println("\u2588\u2588\u2588\u2588\u2588 \u2588\u2588\u2588\u2588\u2588 \u2588\u2588\u2588\u2588\u2588 \u2588\u2588\u2588\u2588\u2588 \u2588\u2588\u2588\u2588");
				VD_FlConsole
						.println("\u2588     \u2588     \u2588     \u2588         \u2588");
				VD_FlConsole
						.println("\u2588\u2588\u2588\u2588\u2588 \u2588  \u2588\u2588 \u2588  \u2588\u2588 \u2588\u2588\u2588\u2588\u2588    \u2588");
				VD_FlConsole
						.println("\u2588     \u2588   \u2588 \u2588   \u2588     \u2588   ");
				VD_FlConsole
						.println("\u2588\u2588\u2588\u2588\u2588 \u2588\u2588\u2588\u2588\u2588 \u2588\u2588\u2588\u2588\u2588 \u2588\u2588\u2588\u2588\u2588   \u2588");
				return "Eggs";
			}
		}, true);

		Command.addCommand(new Command("vendalenger", "Do you leike eggs?",
				"Do you leike eggs?") {
			@Override
			public String action(String[] args) {
				Command.issue("^fc.color 010101 67ff00", false);
				Command.issue("^clr", false);
				String vendalenger = "\n       █████▒      ▒███      ▒███"
						+ "\n    ███████████▒   ▒███     ▒███"
						+ "\n   ████████▒  ██▒  ▒███    ▒███"
						+ "\n  █████████▒  ███▒ ▒███   ▒███"
						+ "\n  ███▒  █████████▒ ▒███  ▒███"
						+ "\n  ███▒  █████████▒ ▒███ ▒███"
						+ "\n  ██████▒  ██████▒ ▒███▒███"
						+ "\n   █████▒  █████▒  ▒██████"
						+ "\n    ███████████▒   ▒█████"
						+ "\n       █████▒      ▒████"
						+ "\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"
						+ "\n   ▖       █"
						+ "\n  ▚     █       VENDALENGER 2014" + "\n         █";
				return vendalenger;
			}
		}, true);
	}

	public static String fcis(File commands, String... args) {
		long time = System.currentTimeMillis();
		VD_FlConsole.println("FCIS: " + commands.getAbsolutePath());
		String ret = "";
		// ^fc.fcis /home/neal/Desktop/eggs.fcis (John Smith) Fiona 12
		try {
			String text = FileShortcuts.readTextFile(commands);
			for (int i = 0; i < args.length; i++) {
				text = text.replaceAll("#arg" + i, args[i]);
			}
			String[] script = text.split(System.getProperty("line.separator"));
			for (int i = 0; i < script.length; i++) {
				script[i].trim();
				Command.issue(script[i], false);
			}
		} catch (IOException e) {
			ret = "Failed to read file: " + e.getCause();
			e.printStackTrace();
		}
		VD_FlConsole.println("FCIS: Finished in "
				+ (System.currentTimeMillis() - time) + "ms");
		return ret;
	}

	/**
	 * Issue a command.
	 *
	 * @param said
	 *            The command
	 */
	public static void issue(String said, boolean typed) {
		if (said.startsWith(String.valueOf('^'))) {
			boolean error = false;
			boolean exist = false;
			String errorMessage = "No errors";
			String raw = said.substring(1);
			for (int i = 0; i < commandList.size(); i++) {
				if (commandList.get(i).key.equalsIgnoreCase(raw.split(" ")[0])) {
					exist = true;
					boolean asterisk = false;
					boolean innerCommand = false;
					char[] pargs = raw
							.substring(raw.split(" ")[0].length(), raw.length())
							.trim().toCharArray();
					int bracket = 0;
					int comBracket = 0;
					ArrayList<String> args = new ArrayList<String>();
					String arg = "";
					String ict = "";
					for (int j = 0; j < pargs.length; j++) {
						if (!asterisk) {
							if (pargs[j] == '*' && !innerCommand) {
								asterisk = true;
							} else if (pargs[j] == '^' && !innerCommand) {
								innerCommand = true;
								comBracket = bracket / 1;
								ict += pargs[j];
							} else if (pargs[j] == '(') {
								bracket++;
								if (innerCommand) {
									ict += pargs[j];
								}
							} else if (pargs[j] == ')') {
								if (comBracket == bracket && innerCommand) {
									comBracket = 0;
									innerCommand = false;
									arg += jellyIssue(ict);
								}
								bracket--;
								if (innerCommand) {
									ict += pargs[j];
								}
								if (bracket < 0) {
									error = true;
									errorMessage = "Unbalanced Brackets, \ntoo many ')'s";
									break;
								}
							} else if (pargs[j] == ' ' && bracket == 0) {
								args.add(arg + "");
								arg = "";
							} else if (innerCommand) {
								ict += pargs[j];
							} else {
								arg += pargs[j];
							}
						} else {
							arg += pargs[j];
							asterisk = false;
						}
					}
					if (!arg.equals("")) {
						args.add(arg);
					}
					if (bracket != 0) {
						error = true;
						errorMessage = "Unbalanced Brackets, \ntoo many '('s";
					}
					if (args.size() == 0) {
						args.add("null");
					}
					String[] a = new String[args.size()];
					for (int j = 0; j < args.size(); j++) {
						a[j] = args.get(j);
					}
					Object res = "error";
					try {
						res = commandList.get(i).action(a);
					} catch (ArrayIndexOutOfBoundsException e) {
						VD_FlConsole.println("Fluffy Console error:");
						VD_FlConsole.println("Missing arguments");
					}

					if (typed && !res.equals("")) {
						VD_FlConsole.println(res);
					}
				}
			}
			if (!exist) {
				error = true;
				errorMessage = "Command: " + raw.split(" ")[0]
						+ " does not exist." + "\nPlugin not installed?";
			}

			if (error) {
				VD_FlConsole.println("Fluffy Console error:");
				VD_FlConsole.println(errorMessage);
			}
		}
	}

	/**
	 * Sort the commands by their keyword alphabetically
	 */
	public static void sort() {
		commandList.sort((o1, o2) -> o1.key.compareToIgnoreCase(o2.key));
	}

	/**
	 * Issue a command inside a command.
	 *
	 * @param said
	 *            The command
	 */
	private static Object jellyIssue(String said) {
		Object ret = "";
		if (said.startsWith(String.valueOf('^'))) {
			boolean error = false;
			boolean exist = false;
			String errorMessage = "No errors";
			String raw = said.substring(1);
			for (int i = 0; i < commandList.size(); i++) {
				if (commandList.get(i).key.equalsIgnoreCase(raw.split(" ")[0])) {
					exist = true;
					boolean asterisk = false;
					boolean innerCommand = false;
					char[] pargs = raw
							.substring(raw.split(" ")[0].length(), raw.length())
							.trim().toCharArray();
					int bracket = 0;
					int comBracket = 0;
					ArrayList<String> args = new ArrayList<String>();
					String arg = "";
					String ict = "";
					for (int j = 0; j < pargs.length; j++) {
						if (!asterisk) {
							if (pargs[j] == '*' && !innerCommand) {
								asterisk = true;
							} else if (pargs[j] == '^' && !innerCommand) {
								innerCommand = true;
								comBracket = bracket / 1;
								ict += pargs[j];
							} else if (pargs[j] == '(') {
								bracket++;
								if (innerCommand) {
									ict += pargs[j];
								}
							} else if (pargs[j] == ')') {
								if (comBracket == bracket && innerCommand) {
									comBracket = 0;
									innerCommand = false;
									arg += jellyIssue(ict);
								}
								bracket--;
								if (innerCommand) {
									ict += pargs[j];
								}
								if (bracket < 0) {
									error = true;
									errorMessage = "Unbalanced Brackets, \ntoo many ')'s";
									break;
								}
							} else if (pargs[j] == ' ' && bracket == 0) {
								args.add(arg + "");
								arg = "";
							} else if (innerCommand) {
								ict += pargs[j];
							} else {
								arg += pargs[j];
							}
						} else {
							arg += pargs[j];
							asterisk = false;
						}
					}
					if (!arg.equals("")) {
						args.add(arg);
					}
					if (bracket != 0) {
						error = true;
						errorMessage = "Unbalanced Brackets, \ntoo many '('s";
					}
					if (args.size() == 0) {
						args.add("null");
					}
					String[] a = new String[args.size()];
					for (int j = 0; j < args.size(); j++) {
						a[j] = args.get(j);
					}
					try {
						ret = commandList.get(i).action(a);
					} catch (ArrayIndexOutOfBoundsException e) {
						VD_FlConsole.println("Fluffy Console error:");
						VD_FlConsole.println("Missing arguments");
					}

				}
			}
			if (!exist) {
				error = true;
				errorMessage = "Command: " + raw.split(" ")[0]
						+ " does not exist." + "\nPlugin not installed?";
			} else {
				VD_FlConsole.println("Fluffy Console error:");
				VD_FlConsole.println(errorMessage);
			}
		}
		return ret;
	}

}