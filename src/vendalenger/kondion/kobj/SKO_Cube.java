package vendalenger.kondion.kobj;

import java.nio.FloatBuffer;
import java.util.List;

import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Solid;

public class SKO_Cube extends KObj_Solid {
	
	KondionShader eggs;
	FloatBuffer buffer;
	
	public SKO_Cube() {
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

	@Override
	public void collisionCheck(KObj_Solid kobj) {
		
	}

	@Override
	public void render() {
		// Draw a cube
		
	}

	@Override
	public void updateB() {
		// TODO Auto-generated method stub
		
	}
}
