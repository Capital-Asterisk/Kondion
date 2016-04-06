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

public class RKO_DirLight extends RKO_Light {
	
	//private static KShader ambient;
	
	public RKO_DirLight() {
		shader = KLoader.shaders.get("K_DirectionLight");
		//color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		//if (colorUni == -1) {
		//	colorUni = ambient.uniformLocation("color");
		//}
	}
}
