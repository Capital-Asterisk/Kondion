package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.FlatDrawing;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.objectbase.KObj_Renderable;

public class RKO_InfinitePlane extends KObj_Renderable {
	
	KondionShader eggs;
	FloatBuffer buffer;
	
	public RKO_InfinitePlane() {
		//eggs = KondionLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
		buffer = null;
	}
	
	@Override
	public void render() {
		if (buffer == null)
			buffer = BufferUtils.createFloatBuffer(16);
		glPushMatrix();
		Matrix4f temp0 = new Matrix4f();
		temp0.rotate(-rot.z / TTT.converter, 0, 0, 1);
		temp0.rotate(-rot.y / TTT.converter, 0, 1, 0);
		temp0.rotate(-rot.x / TTT.converter, 1, 0, 0);
		Vector3f temp1 = new Vector3f();
		Vector3f temp2 = new Vector3f();
		Kondion.getCurrentCamera().pos.mul(temp0, temp1);
		//System.out.println("Distance: " + (temp2.z - temp1.z));
		pos.mul(temp0, temp2);
		//glTranslatef(pos.x, pos.y, pos.z);
		//glTranslatef(0, -6, print("EGGS");	-5);
		glTranslatef(Kondion.getCurrentCamera().pos.x, Kondion.getCurrentCamera().pos.y, Kondion.getCurrentCamera().pos.z);
		glRotatef(rot.z, 0, 0, 1);
		glRotatef(rot.y, 0, 1, 0);
		glRotatef(rot.x, 1, 0, 0);
		//glTranslatef(0, 0, (temp2.y - temp1.y));
		glTranslatef(0, 0, -temp1.z + temp2.z);
		//glTranslatef(0, 0, 5);
		//transform.identity();
		//transform.lookAlong(rot, up);
		//transform.translate(pos);
		//buffer.clear();
		//transform.get(buffer);
		//buffer.rewind();
		//buffer.position(15);
		//glMultMatrix(buffer);
		//glTranslatef(pos.x, pos.y, pos.z);
		//eggs.useProgram();
		if (material != null)
			material.bind();
		//System.out.println("RENDER!");
		FlatDrawing.setCoords(new float[] {200, 200, 0, 200, 0, 0, 200, 0});
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
