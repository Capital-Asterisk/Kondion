package vendalenger.kondion.lwjgl.resource;

import java.io.InputStream;

import argo.jdom.JsonRootNode;

public interface KResource {
	
	public InputStream get(String path);
	public JsonRootNode getConfig();
	public String getName();
	public String getNeatName();
	public String getVersion();
	public void init();
}
