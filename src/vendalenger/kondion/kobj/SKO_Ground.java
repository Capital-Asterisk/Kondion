package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import vendalenger.kondion.lwjgl.FlatDrawing;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.objectbase.KObj_Renderable;

public class SKO_Ground extends KObj_Renderable {
	
	KondionShader eggs;
	FloatBuffer buffer;
	
	public SKO_Ground() {
		//eggs = KondionLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
		buffer = null;
	}
	
	@Override
	public void render() {
		if (buffer == null)
			buffer = BufferUtils.createFloatBuffer(16);
		glPushMatrix();
		transform.identity();
		//transform.lookAlong(rot, up);
		transform.translate(pos);
		transform.rotate(rot.z / TTT.converter, 0, 0, 1);
		transform.rotate(rot.y / TTT.converter, 0, 1, 0);
		transform.rotate(rot.x / TTT.converter, 1, 0, 0);
		buffer.clear();
		transform.get(buffer);
		buffer.rewind();
		//buffer.position(15);
		glMultMatrix(buffer);
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
