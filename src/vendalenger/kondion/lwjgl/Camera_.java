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

package vendalenger.kondion.lwjgl;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import vendalenger.kondion.KInput;
import vendalenger.kondion.Kondion;
import vendalenger.kondion.objects.Entity;

/*
 * Camera_ is a traditional name... my first 3d game had a class called Camera_.
 * it stored x, y, z, yaw, and pitch roll of the camera.
 */
public class Camera_ {

	// For Camera position and movement
	private boolean freeMode = false;
	private int fov = 50;
	private float cameraSpeed = 0.2f;
	private float yaw, pitch, roll;

	private Vector3f center;
	private Vector3f eye;
	private Vector3f up;

	private Matrix4f prespectiveMatrix;

	// Camera control
	private Entity bind = null;
	private Vector3f rotLock = null;

	private int[] upDownLeftRight = {0, 0, 0, 0};

	// For calculations, no new objects are created for speed.
	private Matrix4f tempMatrix;
	private Vector3f tempVector0;
	private Vector3f tempVector1;
	private Vector4f tempVector2;
	private Vector4f tempVector3;

	public Camera_() {

		upDownLeftRight[0] = KInput.getButtonIndex("up");
		upDownLeftRight[1] = KInput.getButtonIndex("down");
		upDownLeftRight[2] = KInput.getButtonIndex("left");
		upDownLeftRight[3] = KInput.getButtonIndex("right");

		yaw = 0;
		pitch = 0;
		roll = 0;

		center = new Vector3f();
		eye = new Vector3f();
		up = new Vector3f();

		prespectiveMatrix = new Matrix4f();

		tempVector0 = new Vector3f();
		tempVector1 = new Vector3f();
		tempVector2 = new Vector4f();
		tempVector3 = new Vector4f();
		tempMatrix = new Matrix4f();
	}

	/**
	 * Aim towards angle, you can use setRoll();
	 * 
	 * @param y
	 *            Yaw
	 * @param p
	 *            Pitch
	 */
	public void aim(float y, float p) {
		yaw = y;
		pitch = p;
		center.y = (float) Math.sin(p) + eye.y;
		center.x = (float) (Math.cos(yaw) * Math.cos(p)) + eye.x;
		center.z = (float) (Math.sin(yaw) * Math.cos(p)) + eye.z;
	}

	public void bindToEntity(Entity e) {
		bind = e;
		freeMode = false;
	}

	/**
	 * Calculate yaw and pitch, center must be normal
	 */
	public void calculateAngle() {
		yaw = (float) Math.atan2(center.z - eye.z, center.x - eye.x);
		pitch = (float) Math.asin(center.y - eye.y);
		// System.out.println(yaw + " " + pitch);
	}

	/**
	 * Sets up vector to up relative to the camera and roll
	 */
	public void calculateUp() {
		// TODO add up calculations
		/*
		 * tempMatrix.setIdentity(); tempVector1.set(1.0f, 0.0f, 0.0f); //
		 * rotate the x (pitch) Vector3f.sub(center, eye, tempVector0); // set
		 * tv0 relative center tempVector2.set(tempVector0.x, tempVector0.y,
		 * tempVector0.z); System.out.println(tempVector2.x + " " +
		 * tempVector2.y + " " + tempVector2.z); tempMatrix.rotate(90.0f,
		 * tempVector1); // rotate matrix pitch up
		 * Matrix4f.transform(tempMatrix, tempVector2, tempVector2); // make tv2
		 * pitch up //tempVector2.normalise(); // paranoia up.set(tempVector2.x,
		 * tempVector2.y, tempVector2.z); // well... im upset...
		 */
		// set template up vector
		tempVector2.set(0.0f, 1.0f, 0.0f, 1.0f);
		tempMatrix.identity();
		tempVector1.set(0.0f, 0.0f, 1.0f); // set rotate mode to z axis
		tempMatrix.rotate(roll, tempVector1); // rotate it
		tempMatrix.transform(tempVector2); // rotate tv2
		// set up to tv2
		// very upsetting...
		up.set(tempVector2.x, tempVector2.y, tempVector2.z);
	}

	public Entity getBindEntity() {
		return bind;
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
	 * Get the eye (The position of the camera)
	 * 
	 * @return a Vector3f
	 */
	public Vector3f getEye() {
		return eye;
	}

	public float getPitch() {
		return pitch;
	}

	public float getRoll() {
		return roll;
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
									.getHeight())), 0.01f, 100.0f)
					.lookAt(eye.x, eye.y, eye.z, center.x, center.y, center.z,
							up.x, up.y, up.z).get(fb);
		} else {
			prespectiveMatrix
					.identity()
					.perspective(
							(float) Math.toRadians(fov),
							((float) (Window.getWidth()) / (float) (Window
									.getHeight())), 0.01f, 100.0f)
					.lookAt(eye.x, eye.y, eye.z, center.x, center.y, center.z,
							up.x, up.y, up.z).get(fb);
		}
		GL11.glMultMatrix(fb);
		// GL20.glUniformMatrix4(0, true, fb);
	}

	public boolean isFree() {
		return freeMode;
	}

	public void lockRotation(Vector3f rot) {
		rotLock = rot;
	}

	/**
	 * Point towards a vector. Eye is unchanged.
	 */
	public void look(float desx, float desy, float desz) {
		center.set(desx, desy, desz);
	}

	/**
	 * Look at a vector from another vector. Center, eye, and up are modified.
	 */
	public void look(float posx, float posy, float posz, float desx,
			float desy, float desz) {
		eye.set(posx, posy, posz);
		center.set(desx, desy, desz);
		calculateUp();
	}

	/**
	 * Center is normalized relative to eye.
	 */
	public void normalizeCenter() {
		center.sub(eye, tempVector0);
		tempVector0.normalize();
		tempVector0.add(eye, center);
	}

	public void setFreeMode(boolean b) {
		freeMode = b;
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
		bind = null;
	}

	public void update() {
		if (freeMode) {
			calculateAngle();
			tempVector0.set(0, 0, 0);
			cameraSpeed = Kondion.getDelta() * 6;
			bind = null;
			if (KInput.buttonIsDown(upDownLeftRight[0])) {
				// forward
				eye.x += (center.x - eye.x) * cameraSpeed;
				eye.y += (center.y - eye.y) * cameraSpeed;
				eye.z += (center.z - eye.z) * cameraSpeed;
			}
			if (KInput.buttonIsDown(upDownLeftRight[1])) {
				// backwards
				eye.x -= (center.x - eye.x) * cameraSpeed;
				eye.y -= (center.y - eye.y) * cameraSpeed;
				eye.z -= (center.z - eye.z) * cameraSpeed;
			}
			if (KInput.buttonIsDown(upDownLeftRight[2])) {
				// left
				tempVector0.x = (float) Math.cos(yaw + Math.PI / 2);
				tempVector0.y = (float) Math.sin(yaw + Math.PI / 2);
				eye.x -= tempVector0.x * cameraSpeed;
				eye.z -= tempVector0.y * cameraSpeed;
				center.x -= tempVector0.x * cameraSpeed;
				center.z -= tempVector0.y * cameraSpeed;
			}
			if (KInput.buttonIsDown(upDownLeftRight[3])) {
				// right
				tempVector0.x = (float) Math.cos(yaw + Math.PI / 2);
				tempVector0.y = (float) Math.sin(yaw + Math.PI / 2);
				eye.x += tempVector0.x * cameraSpeed;
				eye.z += tempVector0.y * cameraSpeed;
				center.x += tempVector0.x * cameraSpeed;
				center.z += tempVector0.y * cameraSpeed;
			}
		} else if (bind != null) {
			eye.x = bind.getPosition().x;
			eye.y = bind.getPosition().y;
			eye.z = bind.getPosition().z;
		}
		if (rotLock != null) {
			yaw = rotLock.x;
			pitch = rotLock.y;
			roll = rotLock.z;
			aim(yaw, pitch);
		} else {
			aim((float) (yaw + KInput.getMouseDX() / 300),
					(float) (pitch - KInput.getMouseDY() / 300));
		}
		if (true) {
			normalizeCenter();
			calculateUp();
		}
	}
}
