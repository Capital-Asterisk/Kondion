package vendalenger.kondion.materials;

import static org.lwjgl.opengl.GL11.*;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionShader;

/**
 * 
 * 
 */
public class KMat_Strange implements KMat_erial {
	
	private KondionShader shader;
	private int eggs = 0;
	private int fuk = 0;
	
	public KMat_Strange() {
		shader = KondionLoader.shaders.get("K_Strange");
		eggs = shader.uniformLocation("eggs");
		fuk = shader.uniformLocation("fuk");
	}

	public void setFUK(boolean a) {
		KondionShader.uniform1i(fuk, a ? 1 : 0);
	}
	
	@Override
	public int bind() {
		shader.useProgram();
		KondionShader.uniform1i(eggs, (int) Kondion.getFrame());
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
}
