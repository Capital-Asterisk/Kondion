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

import static argo.jdom.JsonNodeFactories.array;
import static argo.jdom.JsonNodeFactories.booleanNode;
import static argo.jdom.JsonNodeFactories.field;
import static argo.jdom.JsonNodeFactories.number;
import static argo.jdom.JsonNodeFactories.object;
import static argo.jdom.JsonNodeFactories.string;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonField;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class VD_Keydata {

	public static boolean console = true;

	public static JsonRootNode JSON = null;

	public static List<String> keys;

	public static List<String> tags;

	public static List<JsonNode> values;

	/**
	 * Determine whenever a key exists.
	 *
	 * @param key
	 *            The key to check existence.
	 * @return True if the entry exists.
	 */
	public static boolean exists(String key) {
		return (keys.contains(key));
	}

	public static void firstSetup() {
		VD_Keydata.set("Debvi.Appearance.theme", "default", "file:zip");
		VD_Keydata.set("Debvi.FluffyConsole.enable", "true", "boolean");
		VD_Keydata.set("Debvi.FileOp.Open.png", "^sd.img_view (#)",
				"menuitem.array");
		VD_Keydata.set("Debvi.FileOp.Open.gif", "^sd.img_view (#)",
				"menuitem.array");
		VD_Keydata.set("Debvi.FileOp.Open.dspt", "^sprt_edit (#)",
				"menuitem.array");
		VD_Keydata.set("Webvi.FileOp.Open.dspt", "^sprt_edit (#)",
				"menuitem.array");
		VD_Keydata
				.set("Debvi.FileOp.Context.*",
						"Open by Desktop^open (#) false||-divider-||Cut^f.cut (#)||Copy^f.copy (#)||Paste^f.paste (#)||Rename^f.rename (#) true||Delete^f.del (#)",
						"menuitem.array");
		VD_Keydata.set("Debvi.Store.lastLocation", "default", "hide");
	}

	public static JsonNode get(String key) {
		return values.get(keys.indexOf(key));
	}

	public static String getTag(String key) {
		return tags.get(keys.indexOf(key));
	}

	public static void init() {
		keys = new ArrayList<String>();
		tags = new ArrayList<String>();
		values = new ArrayList<JsonNode>();
	}

	public static String[] listStart(String start) {
		String[] ret = new String[] {};
		for (String key : keys) {
			if (key.startsWith(start)) {
				String[] ret2 = new String[ret.length + 1];
				System.arraycopy(ret, 0, ret2, 0, ret.length);
				ret2[ret.length] = key;
				ret = ret2;
			}
		}
		return ret;
	}

	/**
	 * Load a VD_Keydata JSON file.
	 *
	 * @param file
	 *            The location of the keydata file.
	 * @throws IOException
	 */
	public static void load(File prefs) {
		try {
			String text = FileShortcuts.readTextFile(prefs);
			JsonRootNode result = new JdomParser().parse(text);
			String cats = "";
			/* Skip the first 4 fields */
			for (int i = 4; i < result.getFieldList().size(); i++) {
				if (result.getFieldList().get(i).getValue().isArrayNode()) {
					System.out.println("Terrible preference entry!: "
							+ result.getFieldList().get(i).getName());
					set(result.getFieldList().get(i).getName().getText(),
							result.getFieldList().get(i).getValue()
									.getArrayNode().get(0), result
									.getFieldList().get(i).getValue()
									.getArrayNode().get(1).getText());
				} else {
					cats = result.getFieldList().get(i).getName().getText();
					fromElement(result.getFieldList().get(i).getValue(), cats);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to load preference file: "
					+ prefs.getAbsolutePath());
			System.err
					.println("Possible: No permissions, File doesn't exist, File system is corrupt...");
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
			System.err.println("Failed to load preference file: "
					+ prefs.getAbsolutePath());
			System.err
					.println("Possible: File is not a json, File is corrupt, File was badly edited...");
		}
	}

	/**
	 * Arrange the entries alphabetically by their keys.
	 */
	public static void organize() {
		ArrayList<String> newKeys = new ArrayList<String>(keys);
		ArrayList<String> newTags = new ArrayList<String>();
		ArrayList<JsonNode> newValues = new ArrayList<JsonNode>();
		Collections.sort(newKeys, String.CASE_INSENSITIVE_ORDER);
		for (int i = 0; i < keys.size(); i++) {
			newValues.add(get(newKeys.get(i)));
			newTags.add(getTag(newKeys.get(i)));
		}
		keys = newKeys;
		tags = newTags;
		values = newValues;
	}

	public static void remove(String key) {

	}

	/**
	 * Create or set an entry as a Boolean.
	 *
	 * @param key
	 *            Key of the entry, written as category.sub.name
	 * @param value
	 *            The (new) value of the specified entry
	 * @param tag
	 *            hidden, debvi, specific plugin, anything you want (comma
	 *            delimited & no spaces)
	 */
	public static void set(String key, boolean value, String tag) {
		if (exists(key)) {
			/* Set an existing entry */
			int index = keys.indexOf(key);
			tags.set(index, tag);
		} else {
			/* Create a new one */
			keys.add(key);
			values.add(booleanNode(value));
			tags.add(tag);
		}
	}

	/**
	 * Create or set an entry as any JsonNode.
	 *
	 * @param key
	 *            Key of the entry, written as category.sub.name
	 * @param value
	 *            The (new) value of the specified entry
	 * @param tag
	 *            hidden, debvi, specific plugin, anything you want (comma
	 *            delimited & no spaces)
	 */
	public static void set(String key, JsonNode value, String tag) {
		if (exists(key)) {
			/* Set an existing entry */
			int index = keys.indexOf(key);
			tags.set(index, tag);
		} else {
			/* Create a new one */
			keys.add(key);
			values.add(value);
			tags.add(tag);
		}
	}

	/**
	 * Create or set an entry as an Array.
	 *
	 * @param key
	 *            Key of the entry, written as category.sub.name
	 * @param value
	 *            The (new) value of the specified entry
	 * @param tag
	 *            hidden, debvi, specific plugin, anything you want (comma
	 *            delimited & no spaces)
	 */
	public static void set(String key, List<JsonNode> value, String tag) {
		if (exists(key)) {
			/* Set an existing entry */
			int index = keys.indexOf(key);
			tags.set(index, tag);
		} else {
			/* Create a new one */
			keys.add(key);
			values.add(array(value));
			tags.add(tag);
		}
	}

	/**
	 * Create or set an entry as a Number.
	 *
	 * @param key
	 *            Key of the entry, written as category.sub.name
	 * @param value
	 *            The (new) value of the specified entry
	 * @param tag
	 *            hidden, debvi, specific plugin, anything you want (comma
	 *            delimited & no spaces)
	 */
	public static void set(String key, Number value, String tag) {
		if (exists(key)) {
			/* Set an existing entry */
			int index = keys.indexOf(key);
			tags.set(index, tag);
		} else {
			/* Create a new one */
			keys.add(key);
			values.add(number(value.toString()));
			tags.add(tag);
		}
	}

	/**
	 * Create or set an entry as a String.
	 *
	 * @param key
	 *            Key of the entry, written as category.sub.name
	 * @param value
	 *            The (new) value of the specified entry
	 * @param tag
	 *            hidden, debvi, specific plugin, anything you want (comma
	 *            delimited & no spaces)
	 */
	public static void set(String key, String value, String tag) {
		if (exists(key)) {
			/* Set an existing entry */
			int index = keys.indexOf(key);
			tags.set(index, tag);
		} else {
			/* Create a new one */
			keys.add(key);
			values.add(string(value));
			tags.add(tag);
		}
	}

	@SuppressWarnings("unchecked")
	public static void write(File prefs) {
		organize();
		List<JsonField> fieldList = new ArrayList<JsonField>();
		fieldList.add(field("Program", string("Electric Fence")));
		fieldList.add(field("Desc", string("Preferences file for Debvi")));
		fieldList.add(field("Date", string(new Date().toString())));
		fieldList.add(field("OS", string(System.getProperty("os.name"))));
		/*
		 * Make Tree (Very complicated and looks like low level objective-c)
		 * Object list is a branch String is a leaf as key First object in a
		 * branch is the name of the category
		 */
		List<Object> root = new ArrayList<Object>();
		for (int i = 0; i < keys.size(); i++) {// category.length() -
												// category.replace(".",
												// "").length();
			String[] split = keys.get(i).split("\\.");
			if (!(split.length == 1)) {
				/* Entry in categories */
				List<Object> cd = root;
				for (int j = 0; j < split.length - 1; j++) {
					/*
					 * Cat name is split[j] Check for category existence
					 */
					int catIndex = -1;
					for (int k = 0; k < cd.size(); k++) {
						if (cd.get(k) instanceof ArrayList) {
							/* Its a category! */
							if (((ArrayList<Object>) cd.get(k)).get(0).equals(
									split[j])) {
								/* Category Exists! */
								catIndex = k / 1;
							}
						}
					}
					if (catIndex != -1) {
						if (j == split.length - 2) {
							// System.out.println("Put " + keys.get(i) + " in "
							// + cd.get(0));
							((ArrayList<Object>) cd.get(catIndex)).add(keys
									.get(i));
						} else {
							cd = (List<Object>) cd.get(catIndex);
						}
					} else {
						/* Make a category and put file */
						// System.out.println("Created category " + split[j]);
						ArrayList<Object> newCat = new ArrayList<Object>();
						newCat.add(split[j]);
						if (j == split.length - 2) {
							// System.out.println("Put " + keys.get(i) + " in "
							// + newCat.get(0));
							newCat.add(keys.get(i));
						}
						cd.add(newCat);
						cd = newCat;
					}
				}
			} else {
				/*
				 * Entry in default category (bad, like bogo sort, and
				 * spagettiatic code break in loopz) Very formal...
				 */
				root.add(keys.get(i));
			}
		}
		fieldList.addAll(toField(root, true));

		JSON = object(fieldList);
		System.out.println(new PrettyJsonFormatter().format(JSON));
		try {
			PrintWriter pw = new PrintWriter(prefs);
			pw.print(new PrettyJsonFormatter().format(JSON));
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Error in writing json file.");
		}
	}

	private static void fromElement(JsonNode list, String cats) {
		for (int i = 0; i < list.getFieldList().size(); i++) {
			if (list.getFieldList().get(i).getValue().isArrayNode()) {
				set(cats + "." + list.getFieldList().get(i).getName().getText(),
						list.getFieldList().get(i).getValue().getArrayNode()
								.get(0), list.getFieldList().get(i).getValue()
								.getArrayNode().get(1).getText());
			} else {
				fromElement(list.getFieldList().get(i).getValue(), cats + "."
						+ list.getFieldList().get(i).getName().getText());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static List<JsonField> toField(List<Object> branch, boolean root) {
		if (root) {
			List<JsonField> catList = new ArrayList<JsonField>();

			/* root does not have a name, i is set to 0 */
			for (int i = 0; i < branch.size(); i++) {
				if (branch.get(i) instanceof List) {
					/* [i] is a category */
					catList.add(toField((List<Object>) branch.get(i), false)
							.get(0));
				} else {
					/* [i] is a leaf (bad for root) */
					String[] split = ((String) branch.get(i)).split("\\.");
					catList.add(field(
							split[split.length - 1],
							array(get((String) branch.get(i)),
									string(getTag((String) branch.get(i))))));
				}
			}
			return catList;
		} else {
			List<JsonField> list = new ArrayList<JsonField>();

			/* i is set to 1 because [0] is the name of the category */
			for (int i = 1; i < branch.size(); i++) {
				if (branch.get(i) instanceof List) {
					/* [i] is a category */
					list.add(toField((List<Object>) branch.get(i), false)
							.get(0));
				} else {
					/* [i] is a leaf */
					String[] split = ((String) branch.get(i)).split("\\.");
					list.add(field(
							split[split.length - 1],
							array(get((String) branch.get(i)),
									string(getTag((String) branch.get(i))))));
				}
			}
			return Arrays.asList(field((String) branch.get(0), object(list)));
		}
	}
}
