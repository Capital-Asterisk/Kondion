package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.nio.FloatBuffer;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KShader;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Renderable;

public class RKO_Light extends KObj_Renderable {
	
	protected KShader shader; // could have been static
	protected int colorUni = -1;
	public final Vector4f color;
	
	public RKO_Light() {
		shader = KLoader.shaders.get("K_AmbientLight");
		color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		if (colorUni == -1) {
			colorUni = shader.uniformLocation("color");
		}
	}
	
	public void apply(int width, int height) {
		shader.useProgram();
		glUniform4f(colorUni, color.x, color.y, color.z, color.z);
		GLDrawing.renderQuad(width, height);
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
		
	}

	@Override
	public void render(int type, GKO_RenderPass pass) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isLight() {
		return true;
	}
}
