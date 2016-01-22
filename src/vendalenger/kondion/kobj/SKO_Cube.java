package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMultMatrix;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
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
		defaultUpdate();
	}

	@Override
	public void collisionCheck(KObj_Solid kobj) {
		
	}

	@Override
	public void render() {
		if (buffer == null)
			buffer = BufferUtils.createFloatBuffer(16);
		
		buffer.clear();
		actTransform.get(buffer);
		
		glPushMatrix();
		glLoadIdentity();
		glMultMatrix(buffer);
		//eggs.useProgram();
		if (material != null)
			material.bind();
		GLDrawing.renderCube(1, KondionLoader.textures.get("K_Cube"));
		//KondionShader.unbind();
		if (material != null)
			material.unbind();
		glPopMatrix();
	}

	@Override
	public void updateB() {
		// TODO Auto-generated method stub
		
	}
}
