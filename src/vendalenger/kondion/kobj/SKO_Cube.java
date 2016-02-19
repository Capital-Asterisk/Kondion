package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import org.lwjgl.BufferUtils;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionShader;
import vendalenger.kondion.materials.KMat_FlatColor;
import vendalenger.kondion.materials.KMat_MultiTexture;
import vendalenger.kondion.materials.KMat_Strange;
import vendalenger.kondion.objectbase.CollisionData;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Solid;

public class SKO_Cube extends KObj_Solid {
	
	KondionShader eggs;
	FloatBuffer buffer;
	
	public SKO_Cube() {
		super();
		pointer = false;
		//eggs = KondionLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
		material = new KMat_FlatColor();
		buffer = null;
	}

	@Override
	public void update() {
		defaultUpdate();
		if (!(parent instanceof KObj_Oriented)) {
			transform.rotate(rotVelocity);
			rotVelocity.w *= 1.02f;
			//rotVelocity.x *= 0.992;
			//rotVelocity.y *= 0.992;
			//rotVelocity.z *= 0.992;
			velocity.y -= Kondion.getDelta() * 10;
			rotVelocity.normalize();
			transform.m30 += velocity.x * Kondion.getDelta();
			transform.m31 += velocity.y * Kondion.getDelta();
			transform.m32 += velocity.z * Kondion.getDelta();
			//rotVelocity.
		}
		
	}
	

	@Override
	public CollisionData collisionCheck(KObj_Solid kobj) {
                Matrix4f temp0 = new Matrix4f();
                Vector3f temp1 = new Vector3f();
                Vector3f temp2 = new Vector3f();
                float f;
		if (kobj instanceof SKO_InfinitePlane) {
                    //actTransform.
                    temp1.set(actTransform.m30, actTransform.m31, actTransform.m32);
                    //temp1.mulPoint(actTransform);
                    temp1.mulPoint(kobj.actTransform.invert(temp0), temp2);
                    f = temp2.z;
                    //System.out.println("eee: " + f);
                    if (f < 0) {
                        // get up
                        temp2.set(0, 0, 1);
                        temp2.mulPoint(temp0);
                        temp2.sub(temp0.m30, temp0.m31, temp0.m32);
                        temp1.set(1, 1, 0);
                        temp1.mulPoint(temp0);
                        temp1.sub(temp0.m30, temp0.m31, temp0.m32);
                        
                        System.out.println(temp1.x + " " + temp1.y + " " + temp1.z + " " + Kondion.getFrame());
                        transform.m30 -= f * temp2.x;
                        transform.m31 += f * temp2.y;
                        transform.m32 -= f * temp2.z;
                        velocity.x *= Math.abs(temp1.x);
                        velocity.y *= Math.abs(temp1.y);
                        velocity.z *= Math.abs(temp1.z);
                    }
                    
		}
		GKO_Scene.immaEatYourChildren(this);
		return null;
	}

	@Override
	public void render(int type) {
		if (buffer == null)
			buffer = BufferUtils.createFloatBuffer(16);
		
		buffer.clear();
		actTransform.get(buffer);
		
		glPushMatrix();
		glLoadIdentity();
		glMultMatrixf(buffer);
		//eggs.useProgram();
		if (material != null)
			material.bind(type);
		if (fogIntensity > 0.0f)
			material.fogOverride(fogIntensity);
		GLDrawing.renderCube(1, KondionLoader.textures.get("K_Cube"));
		//GLDrawing.renderQuad(1, 1, KondionLoader.textures.get("K_Cube"));
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
