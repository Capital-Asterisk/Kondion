package vendalenger.kondion.kobj;

import java.nio.FloatBuffer;

import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.objectbase.KObj_Solid;

public class SKO_Ground extends KObj_Solid {
	
	KondionShader eggs;
	FloatBuffer buffer;
	
	public SKO_Ground() {
		//eggs = KondionLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
		buffer = null;
	}

	@Override
	public void update() {
		if (this.s != null) {
			if (this.s.containsKey("onupdate")) {
				this.s.callMember("onupdate");
			}
		}
	}
}
