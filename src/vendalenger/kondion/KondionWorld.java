package vendalenger.kondion;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.kobj.GKO_Layer;
import vendalenger.kondion.kobj.GKO_Scene;
import vendalenger.kondion.kobj.OKO_Camera_;

public class KondionWorld {

	public final List<GKO_Layer> Layers;
	public final Vector4f clearColor;
	
	public GKO_Scene Scene;
	public ScriptObjectMirror s;
	public OKO_Camera_ camera;
	
	public float zNear = 0.1f;
	public float zFar = 100.0f;
	
	public KondionWorld() {
		clearColor = new Vector4f(0.02f, 0.02f, 0.02f, 0.0f);
		Layers = new ArrayList<GKO_Layer>();
		Scene = new GKO_Scene();
	}
	
}
