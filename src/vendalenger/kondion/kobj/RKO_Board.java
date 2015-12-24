package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.glTranslatef;

import java.io.File;

import vendalenger.kondion.lwjgl.FlatDrawing;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.objectbase.KObj_Renderable;

public class RKO_Board extends KObj_Renderable {
	
	KondionShader eggs;
	
	public RKO_Board() {
		eggs = KondionLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
	}
	
	@Override
	public void render() {
		glTranslatef(pos.x, pos.y, pos.z);
		//eggs.useProgram();
		if (material != null)
			material.bind();
		//System.out.println("RENDER!");
		FlatDrawing.renderBillboard(2, 2, KondionLoader.getMissingTexture());
		//KondionShader.unbind();
		if (material != null)
			material.unbind();
	}

	@Override
	public void update() {
		
	}
}