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
import vendalenger.kondion.lwjgl.resource.KModel;
import vendalenger.kondion.lwjgl.resource.KShader;
import vendalenger.kondion.objectbase.KObj_Renderable;

public class RKO_Obj extends KObj_Renderable {
	
	private KModel obj = null;
	private FloatBuffer buffer;
	
	public RKO_Obj(KModel model) {
		super();
		obj = model;
		//eggs = KLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
	}
	
	public RKO_Obj(KModel model, int id) {
		super(id);
		obj = model;
		//eggs = KLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
	}
	
	@Override
	public void render(int type, GKO_RenderPass pass) {
		if (obj != null) {
			if (buffer == null)
				buffer = BufferUtils.createFloatBuffer(16);
	
			glPushMatrix();
			glLoadIdentity();
			
			glTranslatef(-pass.getCamera().actTransform.m30, -pass.getCamera().actTransform.m31, -pass.getCamera().actTransform.m32);
			
			buffer.clear();
			actTransform.get(buffer);
			glMultMatrixf(buffer);
			
			//eggs.useProgram();
			if (material != null)
				material.bind(type);
			if (fogIntensity > 0.0f)
				material.fogOverride(fogIntensity);
			//GLDrawing.renderCube(1);
			//System.out.println("wula");
			obj.draw();
			//GLDrawing.renderQuad(1, 1, KondionLoader.textures.get("K_Cube"));
			//KondionShader.unbind();
			if (material != null)
				material.unbind();
			glPopMatrix();
		}
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
