package vendalenger.kondion.lwjgl.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import vendalenger.port.FileShortcuts;

public class FolderResource implements KResource {

	private String name, nicename, version;
	private File directory;
	private File config;
	private JsonRootNode json;
	
	public FolderResource(File folder) {
		directory = folder;
		config = FileShortcuts.getChild(folder, "kondion.json");
		json = null;
		if (config.exists()) {
			//config = FileShortcuts.getChild(folder, "kondion.ini");
			try {
				json = new JdomParser().parse(FileShortcuts.readTextFile(config));
				name = json.getNode("Id").getStringValue();
				nicename = json.getNode("GameName").getStringValue();
				version = json.getNode("Version").getStringValue();
				
			} catch (InvalidSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			System.err.println("Missing config file in " + folder.getPath());
	}

	@Override
	public InputStream get(String path) {
		if (path.equals("masterjs")) {
			try {
				return new FileInputStream(FileShortcuts.getChild(directory, "masterscript.js"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// String parsing
			//System.out.println("NAV: " + path);
			File dir = directory;
			String[] split = path.split(Pattern.quote(System.getProperty("file.separator")));
			for (int i = 0; i < split.length; i++) {
				File[] f = dir.listFiles();
				//System.out.println("FILE: " + dir.getName() + " NEED: " + split[i]);
				for (int j = 0; j < f.length; j++) {
					if (f[j].getName().startsWith(split[i]) && (f[j].isDirectory() || split.length - 1 == i)) {
						//System.out.println("ENTER: " + f[j].getName());
						dir = f[j];
						j = f.length + 4;
					}
				}
			}
			try {
				System.out.println("Using file " + dir.getName());
				System.out.println("    for " + name + ":" + path);
				return new FileInputStream(dir);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getNeatName() {
		return nicename;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonRootNode getConfig() {
		return json;
	}

}
