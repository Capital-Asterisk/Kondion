package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.io.File;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.resource.KLoader;
import vendalenger.kondion.lwjgl.resource.KShader;
import vendalenger.kondion.materials.KMat_FlatColor;
import vendalenger.kondion.materials.KMat_Monotexture;
import vendalenger.kondion.materials.KMat_Strange;
import vendalenger.kondion.objectbase.CollisionData;
import vendalenger.kondion.objectbase.KObj_Solid;

public class SKO_InfinitePlane extends KObj_Solid {
	
	private FloatBuffer buffer;
	public int size;
	public float textureSize;
	private static Matrix4f temp0;
	private static Vector3f temp1;
	
	KShader eggs;
	
	public SKO_InfinitePlane() {
		super();
		size = 10000;
		textureSize = 1f;
		temp0 = new Matrix4f();
		temp1 = new Vector3f();
		buffer = null;
	}
	
	@Override
	public void render(int type, GKO_RenderPass pass) {
		if (buffer == null)
			buffer = BufferUtils.createFloatBuffer(16);
		
		Kondion.getWorld().zFar = Float.MAX_VALUE / 20 - 1;
		Kondion.getWorld().zNear = 0.1f;
		
		Vector3f temp1 = new Vector3f(); // Transformed camera position
		Vector3f temp2 = new Vector3f(); // Transformed this position
		Kondion.getCurrentCamera().getEye().mulPoint(actTransform.invert(temp0), temp1);
		
		temp2.mulPoint(temp0);
		
		glPushMatrix();
		glLoadIdentity();
		//glTranslatef(0, 0, -temp1.z + temp2.z);
		//glTranslatef(pass.getCamera().actTransform.m30, pass.getCamera().actTransform.m31, pass.getCamera().actTransform.m32);
		buffer.clear();
		actTransform.get(buffer);
		glMultMatrixf(buffer);
		glTranslatef(0, 0, -temp1.z + temp2.z);
		
		//eggs.useProgram();
		if (material != null)
			material.bind(type);
		else
			material = new KMat_Monotexture();
		if (fogIntensity > 0.0f)
			material.fogOverride(fogIntensity);
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
		//if (material == null)
			GLDrawing.renderQuad(size, -size, KLoader.getMissingTexture());
		//else
			//GLDrawing.renderQuad(size, -size);
		//System.out.println(textureSize);
		if (material != null)
			material.unbind();
		glPopMatrix();
	}

	@Override
	public void update() {
		defaultUpdate();
	}

	@Override
	public CollisionData collisionCheck(KObj_Solid kobj) {
		return null;
	}

	@Override
	public void updateB() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkPoint(float x, float y, float z) {

		temp1.set(x, y, z);
		actTransform.invert(temp0);
		temp1.mulPoint(temp0);
		//System.out.println(temp1.z);
		return (temp1.z < 0);
	}
}
