package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

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
import vendalenger.kondion.materials.KMat_MultiTexture;
import vendalenger.kondion.materials.KMat_Strange;
import vendalenger.kondion.objectbase.CollisionData;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Solid;

public class SKO_Cube extends KObj_Solid {
	
	private FloatBuffer buffer;
	private Matrix4f temp0 = new Matrix4f();
	private Vector3f temp1 = new Vector3f();
	private Vector3f temp2 = new Vector3f();
	private Vector3f temp3 = new Vector3f();
	
	public SKO_Cube() {
		this(0);
	}
	
	public SKO_Cube(int eggs) {
		super(eggs);
		pointer = false;
		//eggs = KondionLoader.loadNashShader(new File("KondionTestGame_0/testshader.nash"));
		material = new KMat_Monotexture();
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
			velocity.y -= Kondion.getDelta() * 9.806;
			rotVelocity.normalize();
			transform.m30 += velocity.x * Kondion.getDelta();
			transform.m31 += velocity.y * Kondion.getDelta();
			transform.m32 += velocity.z * Kondion.getDelta();
			//rotVelocity.
		}
		
	}
	

	@Override
	public CollisionData collisionCheck(KObj_Solid kobj) {
                
                float f;
		if (kobj instanceof SKO_InfinitePlane) {
                    //actTransform.
                    temp1.set(actTransform.m30, actTransform.m31, actTransform.m32);
                    //temp1.mulPoint(actTransform);
                    temp1.mulPoint(kobj.actTransform.invert(temp0), temp2);
                    f = temp2.z - 0.5f;
                    
                    //System.out.println("eee: " + f);
                    if (f < 0) {
                        // get up
                        temp2.set(0, 0, 1);
                        temp2.mulPoint(temp0);
                        temp2.sub(temp0.m30, temp0.m31, temp0.m32);
                        temp1.set(1, 1, 0);
                        temp1.mulPoint(temp0);
                        temp1.sub(temp0.m30, temp0.m31, temp0.m32);
                        
                        //System.out.println(temp1.x + " " + temp1.y + " " + temp1.z + " " + Kondion.getFrame());
                        transform.m30 -= f * temp2.x;
                        transform.m31 += f * temp2.y;
                        transform.m32 -= f * temp2.z;
                        velocity.x *= Math.abs(temp1.x);
                        velocity.y *= Math.abs(temp1.y);
                        velocity.z *= Math.abs(temp1.z);
  
                        if (s != null && s.containsKey("collide")) {
            				s.callMember("collide", kobj, temp2);
            			}
                    }
                    
		}
		GKO_Scene.immaEatYourChildren(this);
		return null;
	}

	@Override
	public void render(int type, GKO_RenderPass pass) {
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
		GLDrawing.renderCube(1, KLoader.textures.get("K_Cube"));
		//GLDrawing.renderQuad(1, 1, KondionLoader.textures.get("K_Cube"));
		//KondionShader.unbind();
		if (material != null)
			material.unbind();
		glPopMatrix();
	}

	@Override
	public void updateB() {
		defaultUpdateB();
	}
}
