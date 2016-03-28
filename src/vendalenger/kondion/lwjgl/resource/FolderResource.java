package vendalenger.kondion.lwjgl.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
