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

import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.ReadableVector4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import vendalenger.kondion.KInput;

import com.sun.javafx.geom.Matrix3f;

/*
 * Camera_ is a traditional name... my first 3d game had a class called Camera_.
 * it stored x, y, z, yaw, and pitch roll of the camera.
 */
public class Camera_ {

	private int[] upDownLeftRight = {0, 0, 0, 0};

	// For Camera position and movement
	private boolean freeMode = false;
	private float yaw, pitch, roll;
	private Vector3f center;
	private Vector3f eye;
	private Vector3f up;

	// For calculations, no new objects are created for speed.
	private Vector3f tempVector0;
	private Vector3f tempVector1;
	private Vector4f tempVector2;
	private Vector4f tempVector3;
	private Matrix4f tempMatrix;

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

		tempVector0 = new Vector3f();
		tempVector1 = new Vector3f();
		tempVector2 = new Vector4f();
		tempVector3 = new Vector4f();
		tempMatrix = new Matrix4f();
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

	/**
	 * Get the up vector (A normal vector pointing up relative to the camera)
	 * 
	 * @return a Vector3f
	 */
	public Vector3f getUp() {
		return up;
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
		tempVector2.set(0.0f, 1.0f, 0.0f);
		tempMatrix.setIdentity();
		tempVector1.set(0.0f, 0.0f, 1.0f); // set rotate mode to z axis
		tempMatrix.rotate(roll, tempVector1); // rotate it
		Matrix4f.transform(tempMatrix, tempVector2, tempVector2); // rotate tv2

		// set up to tv2
		// very upsetting...
		up.set(tempVector2.x, tempVector2.y, tempVector2.z);
	}

	/**
	 * Calls GLU.gluLookAt(...) with eye, center, and up
	 */
	public void gluLookAt() {
		GLU.gluLookAt(eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x,
				up.y, up.z);
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
	 * Point towards a vector. Eye is unchanged.
	 */
	public void look(float desx, float desy, float desz) {
		center.set(desx, desy, desz);
	}

	/**
	 * Center is normalized relative to eye.
	 */
	public void normalizeCenter() {
		Vector3f.sub(center, eye, tempVector0);
		tempVector0.normalise();
		Vector3f.add(tempVector0, eye, center);
	}

	public void setFreeMode(boolean b) {
		freeMode = b;
	}

	public void update() {
		if (freeMode) {
			calculateAngle();
			tempVector0.set(0, 0, 0);
			if (KInput.buttonIsDown(upDownLeftRight[0])) {
				// forward
				eye.x += (center.x - eye.x) * 0.04;
				eye.y += (center.y - eye.y) * 0.04;
				eye.z += (center.z - eye.z) * 0.04;
			}
			if (KInput.buttonIsDown(upDownLeftRight[1])) {
				// backwards
				eye.x -= (center.x - eye.x) * 0.04;
				eye.y -= (center.y - eye.y) * 0.04;
				eye.z -= (center.z - eye.z) * 0.04;
			}
			if (KInput.buttonIsDown(upDownLeftRight[2])) {
				// left
				tempVector0.x = (float) Math.cos(yaw + Math.PI / 2);
				tempVector0.y = (float) Math.sin(yaw + Math.PI / 2);
				eye.x -= tempVector0.x * 0.04;
				eye.z -= tempVector0.y * 0.04;
				center.x -= tempVector0.x * 0.04;
				center.z -= tempVector0.y * 0.04;
			}
			if (KInput.buttonIsDown(upDownLeftRight[3])) {
				// right
				tempVector0.x = (float) Math.cos(yaw + Math.PI / 2);
				tempVector0.y = (float) Math.sin(yaw + Math.PI / 2);
				eye.x += tempVector0.x * 0.04;
				eye.z += tempVector0.y * 0.04;
				center.x += tempVector0.x * 0.04;
				center.z += tempVector0.y * 0.04;
			}
			normalizeCenter();
			calculateUp();
		}
	}
}
