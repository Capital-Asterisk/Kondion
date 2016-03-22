package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.nio.FloatBuffer;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Renderable;

public class RKO_Light extends KObj_Renderable {
	
	private static KondionShader ambient;
	private static int colorUni = -1;
	public final Vector4f color;
	
	public RKO_Light() {
		ambient = KondionLoader.shaders.get("K_AmbientLight");
		color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		if (colorUni == -1) {
			colorUni = ambient.uniformLocation("color");
		}
	}
	
	public void apply(int width, int height) {
		ambient.useProgram();
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
}
