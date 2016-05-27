package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KShader;
import vendalenger.kondion.objectbase.KObj_Renderable;

public class RKO_Board extends KObj_Renderable {
	
	private FloatBuffer buffer;
	
	public RKO_Board() {
		//eggs = KLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
	}
	
	public RKO_Board(int id) {
		super(id);
		//eggs = KLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
	}
	
	@Override
	public void render(int type, GKO_RenderPass pass) {
		if (buffer == null)
			buffer = BufferUtils.createFloatBuffer(16);
		
		glPushMatrix();
		glLoadIdentity();

		glTranslatef(-pass.getCamera().actTransform.m30, -pass.getCamera().actTransform.m31,
				-pass.getCamera().actTransform.m32);
		//actTransform.getRotation(dest)
		buffer.clear();
		//actTransform.transform(v)
		//GL11.glLoadMatrix
		actTransform.get(buffer);
		glMultMatrixf(buffer);
		
		//GL11.glLoadMatrixf(buffer);
		// eggs.useProgram();
		if (material != null)
			material.bind(type);
		if (fogIntensity > 0.0f)
			material.fogOverride(fogIntensity);
		GLDrawing.setCoords(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
		GLDrawing.renderQuad(1, 1);
		// GLDrawing.renderQuad(1, 1, KondionLoader.textures.get("K_Cube"));
		// KondionShader.unbind();
		if (material != null)
			material.unbind();
		glPopMatrix();
	}

	@Override
	public void update() {
		if (this.s != null) {
			this.s.setMember("obj", this);
			if (this.s.containsKey("onupdate")) {
				this.s.callMember("onupdate");
			}
		}
	}

	@Override
	public void updateB() {
		// TODO Auto-generated method stub
		
	}
}
