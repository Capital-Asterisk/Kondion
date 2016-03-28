package vendalenger.kondion.lwjgl.resource;

import java.io.InputStream;

import argo.jdom.JsonRootNode;
import vendalenger.kondion.Kondion;

public class DefaultResource implements KResource {

	@Override
	public InputStream get(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		return Kondion.version;
	}

	@Override
	public String getNeatName() {
		return "Kondion Default resources";
	}

	@Override
	public String getName() {
		return "kondion";
	}

	@Override
	public void init() {
		
	}

	@Override
	public JsonRootNode getConfig() {
		// TODO Auto-generated method stub
		return null;
	}

}
