package vendalenger.kondion.materials;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KShader;

/**
 * 
 * 
 */
public class KMat_Strange implements KMat_erial {
	
	private KShader shader;
	private int eggs = 0;
	private int fuk = 0;
	
	public KMat_Strange() {
		shader = KLoader.shaders.get("K_Strange");
		eggs = shader.uniformLocation("eggs");
		fuk = shader.uniformLocation("fuk");
	}

	public void setFUK(boolean a) {
		glUniform1i(fuk, a ? 1 : 0);
	}
	
	@Override
	public int bind(int type) {
		shader.useProgram();
		glUniform1i(eggs, (int) Kondion.getFrame());
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
		// TODO Auto-generated method stub
		
	}
}
