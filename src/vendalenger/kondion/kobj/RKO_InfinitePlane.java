package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import vendalenger.kondion.lwjgl.FlatDrawing;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.objectbase.KObj_Renderable;

public class RKO_InfinitePlane extends KObj_Renderable {
	
	KondionShader eggs;
	FloatBuffer buffer;
	
	public RKO_InfinitePlane() {
		eggs = KondionLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
		buffer = BufferUtils.createFloatBuffer(16);
	}
	
	@Override
	public void render() {
		glPushMatrix();
		transform.identity();
		//transform.
		buffer.clear();
		glMultMatrix(transform.get(buffer));
		//glTranslatef(pos.x, pos.y, pos.z);
		//eggs.useProgram();
		if (material != null)
			material.bind();
		//System.out.println("RENDER!");
		FlatDrawing.renderBillboard(300, 300);
		//KondionShader.unbind();
		if (material != null)
			material.unbind();
		glPopMatrix();
	}

	@Override
	public void update() {
		
	}
}
