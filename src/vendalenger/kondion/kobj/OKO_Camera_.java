/*
 * Copyright 2015 Neal Nicdao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vendalenger.kondion.kobj;

import static org.lwjgl.opengl.GL11.glMultMatrixf;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import vendalenger.kondion.KInput;
import vendalenger.kondion.Kondion;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.objectbase.KObj_Oriented;

/*
 * Camera_ is a traditional name... my first 3d game had a class called Camera_.
 * it stored x, y, z, yaw, and pitch roll of the camera.
 */
public class OKO_Camera_ extends KObj_Oriented {
	
	public static final int
		FORWARD = 0,
		TRACK = 1,
		FREE = 2;

	// For Camera position and movement
	public int mode = 0;
	private int fov = 50;
	private float cameraSpeed = 0.2f;
	private float yaw, pitch, roll;

	private final Vector3f center;
	private final Vector3f up;

	private Matrix4f prespectiveMatrix;

	// Camera control
	//private Entity bind = null;
	private Vector3f rotLock = null;

	private int[] upDownLeftRight = {0, 0, 0, 0};

	// For calculations, no new objects are created for speed.
	private Matrix4f tempMatrix;
	private Vector3f tempVector0;
	private Vector3f tempVector1;
	private Vector4f tempVector2;
	
	public float moveSpeed = 6.0f;

	public OKO_Camera_() {

		upDownLeftRight[0] = KInput.getButtonIndex("up");
		upDownLeftRight[1] = KInput.getButtonIndex("down");
		upDownLeftRight[2] = KInput.getButtonIndex("left");
		upDownLeftRight[3] = KInput.getButtonIndex("right");

		yaw = 0;
		pitch = 0;
		roll = 0;

		center = new Vector3f();
		up = new Vector3f();

		prespectiveMatrix = new Matrix4f();

		tempVector0 = new Vector3f();
		tempVector1 = new Vector3f();
		tempVector2 = new Vector4f();
		tempMatrix = new Matrix4f();
	}
	
	public void aim(float y, float p) {
		yaw = y;
		pitch = p;
		transform.setRotationYXZ(y, p, 0);
		//transform.setLookAt(transform.m30, transform.m31, transform.m32,
		//		0, 0, -1, 0, 1, 0);
		//transform.rotateY(y);
		//transform.rotateX(p);
		//calculateCenter();
	}

	/**
	 * Calculate yaw and pitch, center must be normal
	 */
	private void calculateAngles() {
		tempVector0.set(0, 0, -1);
		tempVector0.mulPoint(transform);
		System.out.println(tempVector0.z - transform.m32);
		yaw = (float) Math.atan2(tempVector0.z - transform.m32, tempVector0.x - transform.m30);
		pitch = (float) Math.asin(tempVector0.y - transform.m31);
	}
	
	private void calculateCenter() {
		center.set(0, 0, -1);
		center.mulPoint(actTransform);
	}
	
	private void calculateUp() {
		// Very upsetting eh??
		up.set(0, 1, 0);
		up.mulPoint(actTransform);
		up.x -= actTransform.m30;
		up.y -= actTransform.m31;
		up.z -= actTransform.m32;
	}

	/**
	 * Get the center (The xyz of where you want to look)
	 * 
	 * @return a Vector3f
	 */
	public Vector3f getCenter() {
		return center;
	}
	
	/**
	 * Get the up vector (A normal vector pointing up relative to the camera)
	 * 
	 * @return a Vector3f
	 */
	public Vector3f getUp() {
		return up;
	}

	public float getYaw() {
		return yaw;
	}

	/**
	 * Calls GLU.gluLookAt(...) with eye, center, and up
	 */
	public void gluLookAt() {
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		if (fov < 1) {
			prespectiveMatrix
					.identity()
					.perspective(
							(float) Math.toRadians(TTT.getPrefFov()),
							((float) (Window.getWidth()) / (float) (Window
									.getHeight())), Kondion.getWorld().zNear, Kondion.getWorld().zFar)
					.lookAt(actTransform.m30, actTransform.m31, actTransform.m32, center.x, center.y, center.z,
							up.x, up.y, up.z).get(fb);
		} else {
			prespectiveMatrix
					.identity()
					.perspective(
							(float) Math.toRadians(fov),
							((float) (Window.getWidth()) / (float) (Window
									.getHeight())), Kondion.getWorld().zNear, Kondion.getWorld().zFar)
					.lookAt(actTransform.m30, actTransform.m31, actTransform.m32, center.x, center.y, center.z,
							up.x, up.y, up.z).get(fb);
		}
		glMultMatrixf(fb);
		// GL20.glUniformMatrix4(0, true, fb);
	}

	/**
	 * Point towards a vector. Eye is unchanged. Only works in track mode.
	 */
	public void look(float desx, float desy, float desz) {
		center.set(desx, desy, desz);
	}

	/**
	 * Look at a vector from another vector. Center, eye, and up are modified.
	 */
	public void look(float posx, float posy, float posz, float desx,
			float desy, float desz) {
		actTransform.m30 = posx;
		actTransform.m31 = posy;
		actTransform.m32 = posz;
		center.set(desx, desy, desz);
	}

	/**
	 * Center is normalized relative to eye.
	 */
	public void normalizeCenter() {
		tempVector1.set(actTransform.m30, actTransform.m31, actTransform.m32);
		center.sub(tempVector1, tempVector0);
		tempVector0.normalize();
		tempVector0.add(tempVector1, center);
	}

	public void setFreeMode(boolean b) {
		if (b && mode != FREE) {
			mode = FREE;
		}
	}
	
	public Vector3f getEye() {
		tempVector1.set(actTransform.m30, actTransform.m31, actTransform.m32);
		return tempVector1;
	}

	/**
	 * Roll the camera
	 * 
	 * @param r
	 *            New roll
	 */
	public void setRoll(float r) {
		roll = r;
	}

	public void unbind() {
		//bind = null;
	}

	public void update() {
		defaultUpdate();
		//mode = FREE;
		//System.out.println("Up");
		if (mode == FREE) {
			
			//calculateAngles();
			//calculateCenter();
			
			yaw = (float) (yaw - KInput.getMouseDX() / 300);
			yaw %= Math.PI * 2;
			
			pitch = (float) (pitch - KInput.getMouseDY() / 300);
			pitch = (float) Math.min(Math.max(pitch, -Math.PI / 2), Math.PI / 2);
			
			aim(yaw, pitch);
			
			tempVector0.set(0, 0, 0);
			cameraSpeed = Kondion.getDelta() * moveSpeed;
			//bind = null;
			if (KInput.buttonIsDown(upDownLeftRight[0])) {
				// forward
				transform.translate(0.0f, 0.0f, -cameraSpeed);
			}
			if (KInput.buttonIsDown(upDownLeftRight[1])) {
				// backwards
				transform.translate(0.0f, 0.0f, cameraSpeed);
			}
			if (KInput.buttonIsDown(upDownLeftRight[2])) {
				// left
				transform.translate(-cameraSpeed, 0.0f, 0.0f);
			}
			if (KInput.buttonIsDown(upDownLeftRight[3])) {
				// right
				transform.translate(cameraSpeed, 0.0f, 0.0f);
			}
		}
	}
	
	@Override
	public void applyTransform() {
		super.applyTransform();
		calculateCenter();
		calculateUp();
	}

	@Override
	public void updateB() {
		//System.out.println("wulawula");
		//if (mode == FORWARD) {
			
		//}
	}
}
