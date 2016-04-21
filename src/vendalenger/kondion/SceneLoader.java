package vendalenger.kondion;

import vendalenger.kondion.kobj.GKO_RenderPass;
import vendalenger.kondion.kobj.GKO_Scene;
import vendalenger.kondion.objectbase.KObj_Node;

public class SceneLoader {
	
	public static void load(String s) {
		
	}
	
	public static void clear() {
		KondionWorld w = Kondion.getWorld();
		w.scene.rescan();
		for (KObj_Node node : w.scene.everything()) {
			node.delete();
		}
		w.scene.clear();
		w.scene.s.clear();
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
