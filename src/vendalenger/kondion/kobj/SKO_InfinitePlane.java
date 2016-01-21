package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Solid;

public class SKO_InfinitePlane extends KObj_Solid {
	
	private FloatBuffer buffer;
	public int size;
	public float textureSize;
	private Matrix4f temp0;
	
	KondionShader eggs;
	
	public SKO_InfinitePlane() {
		eggs = KondionLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
		size = 10000;
		textureSize = 1f;
		temp0 = new Matrix4f();
		buffer = null;
	}
	
	@Override
	public void render() {
		if (buffer == null)
			buffer = BufferUtils.createFloatBuffer(16);
		
		Kondion.getWorld().zFar = Float.MAX_VALUE / 5 - 1;
		
		Vector3f temp1 = new Vector3f(); // Transformed camera position
		Vector3f temp2 = new Vector3f(); // Transformed this position
		Kondion.getCurrentCamera().pos.mul(actTransform.invert(temp0), temp1);
		
		temp2.mul(temp0);
		
		buffer.clear();
		actTransform.get(buffer);
		
		
		glPushMatrix();
		glLoadIdentity();
		//glTranslatef(0, 0, -temp1.z + temp2.z);
		glTranslatef(Kondion.getCurrentCamera().pos.x, Kondion.getCurrentCamera().pos.y, Kondion.getCurrentCamera().pos.z);
		glMultMatrix(buffer);
		glTranslatef(0, 0, -temp1.z + temp2.z);
		
		//eggs.useProgram();
		if (material != null)
			material.bind();
		//System.out.println("RENDER!");
		float addx = -(-temp1.x + temp2.x) / textureSize;
		float addy = -(-temp1.y + temp2.y) / textureSize;
		addx %= textureSize;
		addy %= textureSize;
		GLDrawing.setCoords(new float[] {
				size / textureSize + addx, size / textureSize + addy,
				addx, size / textureSize + addy,
				addx, addy,
				size / textureSize + addx, addy});
		GLDrawing.renderQuad(size, size, KondionLoader.getMissingTexture());
		System.out.println(textureSize);
		if (material != null)
			material.unbind();
		glPopMatrix();
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
	public void updateB() {
		// TODO Auto-generated method stub
		
	}
}
