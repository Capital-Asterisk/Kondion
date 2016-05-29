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
import org.lwjgl.opengl.GL11;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.TTT;
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
	private static Matrix4f temp0 = new Matrix4f();
	private static Vector3f temp1 = new Vector3f();
	private static Vector3f temp2 = new Vector3f();
	private static Vector3f temp3 = new Vector3f();
	private static Matrix4f temp4 = new Matrix4f();
	private static byte[] dirs = new byte[3];
	//private static Vector3f temp4 = new Vector3f();

	public SKO_Cube() {
		this(1);
	}

	public SKO_Cube(int eggs) {
		super(eggs);
		pointer = false;
		// eggs = KondionLoader.loadNashShader(new
		// File("KondionTestGame_0/testshader.nash"));
		material = new KMat_Monotexture();
		buffer = null;
	}

	@Override
	public void update() {
		
		defaultUpdate();
		
		prevTransform.set(actTransform);
		
		if (!(parent instanceof KObj_Oriented)) {
			transform.rotate(rotVelocity);
			
			rotVelocity.w *= 1.02f;
			// rotVelocity.x *= 0.992;
			// rotVelocity.y *= 0.992;
			// rotVelocity.z *= 0.992;
			rotVelocity.normalize();
			if (!anchor) {
				if (!gravityOverride)
					velocity.y -= Kondion.getDelta() * 9.806;
				transform.m30 += velocity.x * Kondion.getDelta();
				transform.m31 += velocity.y * Kondion.getDelta();
				transform.m32 += velocity.z * Kondion.getDelta();
			}
			// rotVelocity.
		}

	}

	@Override
	public CollisionData collisionCheck(KObj_Solid kobj) {

		if (kobj instanceof SKO_InfinitePlane && !anchor) {
			// actTransform.
			/*temp1.set(actTransform.m30, actTransform.m31, actTransform.m32);
			// temp1.mulPoint(actTransform);
			temp3 = actTransform.getScale(temp3);
			temp1.mulPoint(kobj.actTransform.invert(temp0), temp2);
			f = temp2.z - 0.5f * temp3.y;

			// System.out.println("eee: " + f);
			if (f < 0) {
				// get up
				temp2.set(0, 0, 1);
				temp2.mulPoint(temp0);
				temp2.sub(temp0.m30, temp0.m31, temp0.m32);
				temp1.set(1, 1, 0);
				temp1.mulPoint(temp0);
				temp1.sub(temp0.m30, temp0.m31, temp0.m32);

				// System.out.println(temp1.x + " " + temp1.y + " " + temp1.z +
				// " " + Kondion.getFrame());
				transform.m30 -= f * temp2.x;
				transform.m31 += f * temp2.y;
				transform.m32 -= f * temp2.z;
				velocity.x *= Math.abs(temp1.x);
				velocity.y *= Math.abs(temp1.y);
				velocity.z *= Math.abs(temp1.z);

				if (s != null && s.containsKey("collide")) {
					s.callMember("collide", kobj, temp2);
				}
			}*/
			
			// set temp0 to invert of plane
			kobj.actTransform.invert(temp0);
			// multiply by transform and store result in temp4
			temp0.mul(transform, temp4);
			// use x as some variable
			temp3.x = temp4.m32 - (Math.abs(temp4.m02) + Math.abs(temp4.m12) + Math.abs(temp4.m22)) / 2;
			
			if (temp3.x < 0) {
				temp1.set(1, 1, 0);
				temp1.mulDirection(kobj.actTransform);
				temp2.set(0, 0, -1);
				temp2.mulDirection(kobj.actTransform);
				temp2.normalize();

				// System.out.println(temp1.x + " " + temp1.y + " " + temp1.z +
				// " " + Kondion.getFrame());
				if ((kobj.collideType & collideMove) == kobj.collideType) {
					transform.m30 += temp3.x * temp2.x;
					transform.m31 += temp3.x * temp2.y;
					transform.m32 += temp3.x * temp2.z;
					velocity.x *= Math.abs(temp1.x);
					velocity.y *= Math.abs(temp1.y);
					velocity.z *= Math.abs(temp1.z);
					// get deceleration velocity by
					// get direction of current velocity and scale it down
					//float mag = velocity.length();
					dirs[0] = (velocity.x == 0) ? 0 : ((velocity.x < 0) ? (byte) 1 : (byte) -1);
					dirs[1] = (velocity.y == 0) ? 0 : ((velocity.y < 0) ? (byte) 1 : (byte) -1);
					dirs[2] = (velocity.z == 0) ? 0 : ((velocity.z < 0) ? (byte) 1 : (byte) -1);
					if (!(dirs[0] == 0 && dirs[1] == 0 && dirs[2] == 0)) {
						velocity.normalize(temp1);
						temp1.div(divFriction * kobj.divFriction);
						//temp1.mul();
						//System.out.println(temp1.x);
						temp1.mul(1 - Math.min(Kondion.getDelta(), 1));
						velocity.sub(temp1);
						if (((dirs[0] >= 0) ^ (velocity.x < 0)))
							velocity.x = 0;
						if (((dirs[1] >= 0) ^ (velocity.y < 0)))
							velocity.y = 0;
						if (((dirs[2] >= 0) ^ (velocity.z < 0)))
							velocity.z = 0;
					}
					
				}
				
				if (((kobj.collideType & collideCall) == kobj.collideType) && s != null && s.containsKey("collide")) {
					s.callMember("collide", kobj, temp2);
				}
			}
			
		} else if (kobj instanceof SKO_Cube) {
			//if (kobj.axisAlign) {
				
			//}
			// boom
			// erm.... absolute value of an absolute value??? wat
			temp1.set(
					(Math.abs(actTransform.m00) + Math.abs(actTransform.m10) + Math.abs(actTransform.m20)) / 2.0f,
					(Math.abs(actTransform.m01) + Math.abs(actTransform.m11) + Math.abs(actTransform.m21)) / 2.0f,
					(Math.abs(actTransform.m02) + Math.abs(actTransform.m12) + Math.abs(actTransform.m22)) / 2.0f);
			temp2.set(
					(Math.abs(kobj.actTransform.m00) + Math.abs(kobj.actTransform.m10) + Math.abs(kobj.actTransform.m20)) / 2.0f,
					(Math.abs(kobj.actTransform.m01) + Math.abs(kobj.actTransform.m11) + Math.abs(kobj.actTransform.m21)) / 2.0f,
					(Math.abs(kobj.actTransform.m02) + Math.abs(kobj.actTransform.m12) + Math.abs(kobj.actTransform.m22)) / 2.0f);
			
			if (Math.abs(actTransform.m30 - kobj.actTransform.m30) < temp1.x + temp2.x
				&& Math.abs(actTransform.m31 - kobj.actTransform.m31) < temp1.y + temp2.y
				&& Math.abs(actTransform.m32 - kobj.actTransform.m32) < temp1.z + temp2.z) {
				//System.out.println("beet wise: " + kobj.collideType + "&" + collideMove + "=" + (kobj.collideType & collideMove));
				if (kobj.anchor && ((kobj.collideType & collideMove) == kobj.collideType)) {
					// slopes
					temp3.set(actTransform.m30 - prevTransform.m30,
							actTransform.m31 - prevTransform.m31,
							actTransform.m32 - prevTransform.m32);
					
					dirs[0] = (temp3.x == 0) ? 0 : ((temp3.x < 0) ? (byte) 1 : (byte) -1);
					dirs[1] = (temp3.y == 0) ? 0 : ((temp3.y < 0) ? (byte) 1 : (byte) -1);
					dirs[2] = (temp3.z == 0) ? 0 : ((temp3.z < 0) ? (byte) 1 : (byte) -1);
					
					//System.out.println(dirs[0] + ", " + dirs[1] + ", " + dirs[2]);
					
					//System.out.println("Slopes: " + temp3.x + ", " + temp3.y + ", " + temp3.z);
					// solve for x and z
					/*if (dirs[1] != 0) {
						float targety = kobj.transform.m31 + temp2.y + temp1.y;
						float multiplier = kobj.transform.m31 / targety;
						System.out.println("fukb: " + multiplier);
						transform.m30 = transform.m30 + temp3.x * multiplier;
						transform.m31 = targety;
						transform.m32 = transform.m32 + temp3.z * multiplier;
						velocity.y = 0;
						
					}*/
					
					// Check for Y
					// use matrix to store temporary variables
					temp0.m00 = kobj.transform.m31 + (temp2.y + temp1.y) * dirs[1];
					temp0.m01 = (temp0.m00 - prevTransform.m31) / temp3.y;
					transform.m30 = temp3.x * temp0.m01 + prevTransform.m30;
					transform.m31 = temp0.m00;
					transform.m32 = temp3.z * temp0.m01 + prevTransform.m32;
					temp0.m10 = 0;
					temp0.m11 = -dirs[1];
					temp0.m12 = 0;
					
					//velocity.y = 0;
					
					if (Math.abs(transform.m30 - kobj.transform.m30) <= temp1.x + temp2.x
							&& Math.abs(transform.m32 - kobj.transform.m32) <= temp1.z + temp2.z) {
						//System.out.println("birds: " + temp0.m01);
						transform.m30 = temp3.x + prevTransform.m30;
						transform.m32 = temp3.z + prevTransform.m32;
						velocity.y = 0;
					} else {
						
						// Check for X
						// use matrix to store temporary variables
						temp0.m00 = kobj.transform.m30 + (temp2.x + temp1.x) * dirs[0];
						temp0.m01 = (temp0.m00 - prevTransform.m30) / temp3.x;
						transform.m30 = temp0.m00;
						transform.m31 = temp3.y * temp0.m01 + prevTransform.m31;
						transform.m32 = temp3.z * temp0.m01 + prevTransform.m32;
						temp0.m10 = -dirs[0];
						temp0.m11 = 0;
						temp0.m12 = 0;
						
						//velocity.x = 0;
						if (Math.abs(transform.m31 - kobj.transform.m31) <= temp1.y + temp2.y
								&& Math.abs(transform.m32 - kobj.transform.m32) <= temp1.z + temp2.z) {
							transform.m31 = temp3.y + prevTransform.m31;
							transform.m32 = temp3.z + prevTransform.m32;
							velocity.x = 0;
						} else {
							// Check for Z
							// use matrix to store temporary variables
							temp0.m00 = kobj.transform.m32 + (temp2.z + temp1.z) * dirs[2];
							temp0.m01 = (temp0.m00 - prevTransform.m32) / temp3.z;
							transform.m30 = temp3.x * temp0.m01 + prevTransform.m30;
							transform.m31 = temp3.y * temp0.m01 + prevTransform.m31;
							transform.m32 = temp0.m00;
							temp0.m10 = 0;
							temp0.m11 = 0;
							temp0.m12 = -dirs[2];
							if (Math.abs(transform.m30 - kobj.transform.m30) <= temp1.x + temp2.x
									&& Math.abs(transform.m31 - kobj.transform.m31) <= temp1.y + temp2.y) {
								velocity.z = 0;
								transform.m30 = temp3.x + prevTransform.m30;
								transform.m31 = temp3.y + prevTransform.m31;
							} else {
								// none?
								//System.out.print("oh no");
								//transform.m30 = prevTransform.m30;
								//transform.m31 = prevTransform.m31;
								//transform.m32 = prevTransform.m32;
							}
							
						}
						
					}
					
					dirs[0] = (velocity.x == 0) ? 0 : ((velocity.x < 0) ? (byte) 1 : (byte) -1);
					dirs[1] = (velocity.y == 0) ? 0 : ((velocity.y < 0) ? (byte) 1 : (byte) -1);
					dirs[2] = (velocity.z == 0) ? 0 : ((velocity.z < 0) ? (byte) 1 : (byte) -1);
					if (!(dirs[0] == 0 && dirs[1] == 0 && dirs[2] == 0)) {
						velocity.normalize(temp1);
						temp1.div(divFriction * kobj.divFriction);
						//temp1.mul();
						//System.out.println(temp1.x);
						temp1.mul(1 - Math.min(Kondion.getDelta(), 1));
						velocity.sub(temp1);
						//System.out.println(temp1.x);
						velocity.sub(temp1);
						if (((dirs[0] >= 0) ^ (velocity.x < 0)))
							velocity.x = 0;
						if (((dirs[1] >= 0) ^ (velocity.y < 0)))
							velocity.y = 0;
						if (((dirs[2] >= 0) ^ (velocity.z < 0)))
							velocity.z = 0;
						
						
					}
					//velocity.x *= dirs[0] * -1;
					//velocity.y *= dirs[1] * -1;
					//velocity.z *= dirs[2] * -1;
					/*if (temp3.x * temp3.y * temp3.z == 0) {
						// 0 somewhere
						
						dirs[0] = 
						
					*/
					
					temp3.set(temp0.m10, temp0.m11, temp0.m12);
					
				} else {
					
					temp3.set(0, 0, 0);
					
				}
				
				if (((kobj.collideType & collideCall) == kobj.collideType) && s != null && s.containsKey("collide")) {
					s.callMember("collide", kobj, temp3);
					//System.out.println("COLLIDE " + collideType + " vs " + kobj.collideType);
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

		glTranslatef(-pass.getCamera().actTransform.m30, -pass.getCamera().actTransform.m31,
				-pass.getCamera().actTransform.m32);
		//actTransform.getRotation(dest)
		buffer.clear();
		//actTransform.transform(v)
		//GL11.glLoadMatrix
		actTransform.get(buffer);
		glMultMatrixf(buffer);
		
		//GL11.glLoadMatrixf(buffer);
		// eggs.useProgram();
		if (material != null)
			material.bind(type);
		if (fogIntensity > 0.0f)
			material.fogOverride(fogIntensity);
		GLDrawing.renderCube(1);
		// GLDrawing.renderQuad(1, 1, KondionLoader.textures.get("K_Cube"));
		// KondionShader.unbind();
		if (material != null)
			material.unbind();
		glPopMatrix();
		
		/*glPushMatrix();
		// Render bounding box
		GL11.glLineWidth(1.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		//GL11.glColor3f(0.42f, 1.0f, 0.0f);
		GL11.glColor3f(1.0f, 0.0f, 0.0f);
		
		glTranslatef(-pass.getCamera().actTransform.m30, -pass.getCamera().actTransform.m31,
				-pass.getCamera().actTransform.m32);
		GL11.glTranslatef(actTransform.m30, actTransform.m31, actTransform.m32);
		GL11.glScalef(
				Math.abs(actTransform.m00) + Math.abs(actTransform.m10) + Math.abs(actTransform.m20), 
				Math.abs(actTransform.m01) + Math.abs(actTransform.m11) + Math.abs(actTransform.m21),
				Math.abs(actTransform.m02) + Math.abs(actTransform.m12) + Math.abs(actTransform.m22));
		GLDrawing.renderCube(1);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		glPopMatrix();
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);*/
	}

	@Override
	public void updateB() {
		defaultUpdateB();
	}

	@Override
	public boolean checkPoint(float x, float y, float z) {
		temp1.set(
				(Math.abs(actTransform.m00) + Math.abs(actTransform.m10) + Math.abs(actTransform.m20)) / 2.0f,
				(Math.abs(actTransform.m01) + Math.abs(actTransform.m11) + Math.abs(actTransform.m21)) / 2.0f,
				(Math.abs(actTransform.m02) + Math.abs(actTransform.m12) + Math.abs(actTransform.m22)) / 2.0f);
		
		return (temp1.x > Math.abs(x - actTransform.m30)
				&& temp1.y > Math.abs(y - actTransform.m31)
				&& temp1.z > Math.abs(z - actTransform.m32));
	}
}
