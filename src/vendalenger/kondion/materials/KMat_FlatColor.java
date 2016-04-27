package vendalenger.kondion.materials;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KShader;

/**
 * 
 * 
 */
public class KMat_FlatColor implements KMat_erial {
	
	private KShader shader;
	private float r, g, b, a;
	private int uni_type, uni_fog, uni_color;
	private boolean alpha = false;
	
	public KMat_FlatColor() {
		shader = KLoader.shaders.get("K_FlatCol");
		uni_type = shader.uniformLocation("type");
		uni_color = shader.uniformLocation("color");
		uni_fog = shader.uniformLocation("fog");
		//setColorf(0.4f, 1.0f, 0.0f);
		setColorf(0.6f, 0.6f, 0.6f); 
	}
	
	/**
	 * Set the colo(u)r, from 0.0f - 1.0f
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColorf(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Set the colo(u)r, from 0 - 255
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColori(int r, int g, int b) {
		this.r = ((float) r) / 255;
		this.g = ((float) g) / 255;
		this.b = ((float) b) / 255;
	}
	
	/**
	 * Set the alpha, 0.0f - 1.0f
	 * @param a
	 */
	public void setAlphaf(float a) {
		alpha = true;
		this.a = a;
	}
	
	public void disableAlpha() {
		alpha = false;
	}
	
	public float getR() {
		return r;
	}
	
	public float getG() {
		return r;
	}
	
	public float getB() {
		return r;
	}
	
	public float getA() {
		return a;
	}

	@Override
	public int bind(int type) {
		
		shader.useProgram();
		//KondionShader.uniform1i(eggs, (int) Kondion.getFrame());
		glUniform4f(uni_color, r, g, b, alpha ? a : 1.0f);
		glUniform1f(uni_fog, Kondion.getWorld().fogIntensity);
		glUniform1i(uni_type, type);
		
		return 0;
	}

	@Override
	public int prepare() {
		
		return 0;
	}

	@Override
	public int unbind() {
		shader.unbind();
		return 0;
	}

	@Override
	public void fogOverride(float fog) {
		glUniform1f(uni_fog, fog);
	}
}
