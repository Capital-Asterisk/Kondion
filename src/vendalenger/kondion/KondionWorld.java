package vendalenger.kondion;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.kobj.GKO_RenderPass;
import vendalenger.kondion.kobj.GKO_Scene;
import vendalenger.kondion.kobj.OKO_Camera_;

public class KondionWorld {

	public final List<GKO_RenderPass> Layers;
	public final Vector4f clearColor;
	
	public GKO_Scene Scene;
	public ScriptObjectMirror s;
	public OKO_Camera_ camera;
	
	public float zNear = 0.001f;
	public float zFar = 100.0f;
	
	public KondionWorld() {
		clearColor = new Vector4f(0.02f, 0.02f, 0.02f, 0.0f);
		Layers = new ArrayList<GKO_RenderPass>();
		Scene = new GKO_Scene();
	}
	
}
