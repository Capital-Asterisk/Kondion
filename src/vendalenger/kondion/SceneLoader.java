package vendalenger.kondion;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;
import vendalenger.kondion.kobj.GKO_RenderPass;
import vendalenger.kondion.kobj.GKO_Scene;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.objectbase.KObj_Node;

public class SceneLoader {
	
	public static void load(String s) {
		try {
			InputStream input = KLoader.get(s);
			JsonRootNode jrn = new JdomParser().parse(new InputStreamReader(input));
			List<JsonNode> scripts = jrn.getArrayNode("Scripts");
			for (JsonNode node : scripts) {
				//System.out.println(node.toString());
				String str = node.getStringValue();
				if (str.contains(":")) {
					// Specific game is specified
					Kondion.kjs.executeScript(str);
				} else {
					// Use scene's game
					Kondion.kjs.executeScript(s.split(":")[0] + ":" + str);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static void clear() {
		KondionWorld w = Kondion.getWorld();
		KInput.setMouseLock(false);
		w.scene.rescan();
		for (KObj_Node node : w.scene.everything()) {
			node.delete();
		}
		w.scene.clear();
		w.scene.s.clear();
		
		w.passes.clear();
		w.camera = null;
		w.compositor = null;
		w.compMode = 0;
		
		System.gc();
		System.out.println("World cleared");
		//Kondion.getNashorn().getContext().
		//for (int i = 0; i < s.size(); i++) {
		//	s.get(i);
		//}
	}
}
