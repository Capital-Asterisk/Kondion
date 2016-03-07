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

import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POINT_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import poly2Tri.Triangulation;
import vendalenger.kondion.Kondion;
import vendalenger.kondion.KondionWorld;
import vendalenger.kondion.kobj.OKO_Camera_;

public class TTT {

	/*
	 * TTT, the same class used in my first 3d lwjgl game. (with modifications)
	 * TTT serves the same purpose. as before.
	 */

	private static int prefFov = 50;
	public static final float converter = 57.295779513f;
	
	public void printVector(String name, Vector3f v) {
		System.out.println(name + ": (" + v.x + ", " + v.y + ", " + v.z + ")");
	}

	public static void addVect(List<Float> list, Vector3f v) {
		list.add(v.x);
		list.add(v.y);
		list.add(v.z);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Turn an area into a list of Vector2f triangles. The 2nd hardest peice of code I have ever written.
	 * @param area Your awt area class
	 * @return A list of Vector2f. 
	 */
	public static List<Vector2f> areaToTriangles(Area area)
			throws java.lang.ArrayIndexOutOfBoundsException {
		// not mine, it's modified from
		// http://stackoverflow.com/questions/8144156/using-pathiterator-to-return-all-line-segments-that-constrain-an-area
		List<double[]> areaPoints = new ArrayList<double[]>();
		List<List<double[]>> areaSegments = new ArrayList<List<double[]>>();
		double[] coords = new double[6];

		for (PathIterator pi = area.getPathIterator(null); !pi.isDone(); pi
				.next()) {
			// The type will be SEG_LINETO, SEG_MOVETO, or SEG_CLOSE
			// Because the Area is composed of straight lines
			int type = pi.currentSegment(coords);
			// We record a double array of {segment type, x coord, y coord}
			double[] pathIteratorCoords = {type, coords[0], coords[1]};
			areaPoints.add(pathIteratorCoords);
		}

		for (int i = 0; i < areaPoints.size(); i++) {
			// If we're not on the last point, return a line from this point to
			// the next
			double[] currentElement = areaPoints.get(i);

			// We need a default value in case we've reached the end of the
			// ArrayList
			double[] nextElement = {-1, -1, -1};
			if (i < areaPoints.size() - 1) {
				nextElement = areaPoints.get(i + 1);
			}

			// Make the lines
			if (currentElement[0] == PathIterator.SEG_MOVETO) {
				// System.out.println("New shape");
				areaSegments.add(new ArrayList<double[]>());
			}

			if (nextElement[0] == PathIterator.SEG_LINETO) {
				areaSegments.get(areaSegments.size() - 1).add(
						new double[] {currentElement[1], currentElement[2]});
				// System.out.println("Add point: (" + currentElement[1] + ", "
				// + currentElement[2] + ")");
			} else if (nextElement[0] == PathIterator.SEG_CLOSE) {
				areaSegments.get(areaSegments.size() - 1).add(
						new double[] {currentElement[1], currentElement[2]});
				// System.out.println("Close: (" + currentElement[1] + ", " +
				// currentElement[2] + ")");
			}
		}

		// *** done code snippet

		// now triangulate

		List<double[]> vertices = new ArrayList<double[]>();
		int[] contours = new int[areaSegments.size()];

		// Reverse last array in areaSegments, then add it first
		Collections.reverse(areaSegments.get(areaSegments.size() - 1));
		for (int j = 0; j < areaSegments.get(areaSegments.size() - 1).size(); j++) {
			vertices.add(areaSegments.get(areaSegments.size() - 1).get(j));
		}
		contours[0] = areaSegments.get(areaSegments.size() - 1).size();

		// add the rest, -1 to skip last element
		for (int i = 0; i < areaSegments.size() - 1; i++) {
			Collections.reverse(areaSegments.get(i));
			for (int j = 0; j < areaSegments.get(i).size(); j++) {
				vertices.add(areaSegments.get(i).get(j));
			}
			contours[i + 1] = areaSegments.get(i).size();
		}
		ArrayList<ArrayList<Integer>> res = Triangulation.triangulate(
				contours.length, contours,
				vertices.toArray(new double[vertices.size()][]));

		List<Vector2f> result = new ArrayList<Vector2f>();

		for (ArrayList<Integer> ds : res) {
			for (Integer inte : ds) {
				// System.out.println("(" + vertices.get(inte)[0] + ", " +
				// vertices.get(inte)[1] + ")");
				result.add(new Vector2f((float) vertices.get(inte)[0],
						(float) vertices.get(inte)[1]));
			}
		}
		return result;
	}

	public static Matrix3f rotateByUnitVector(Matrix3f tgt, float cos, float sin, float x, float y, float z) {
		float C = 1.0f - cos;
        float xy = x * y, xz = x * z, yz = y * z;
        tgt.m00 = cos + x * x * C;
        tgt.m10 = xy * C - z * sin;
        tgt.m20 = xz * C + y * sin;
        tgt.m01 = xy * C + z * sin;
        tgt.m11 = cos + y * y * C;
        tgt.m21 = yz * C - x * sin;
        tgt.m02 = xz * C - y * sin;
        tgt.m12 = yz * C + x * sin;
        tgt.m22 = cos + z * z * C;
		return tgt;
	}
	
	public static int Decide(String message, String objective, int type) {
		int d = JOptionPane.showConfirmDialog(null, message, objective,
				JOptionPane.YES_NO_OPTION, type);
		if (d == JOptionPane.OK_OPTION) {
			return 0;
		} else if (d == JOptionPane.NO_OPTION) {
			return 1;
		} else {
			return 2;
		}
	}

	public static void Error(String message) {
		JOptionPane.showMessageDialog(null, message, "Error Message",
				JOptionPane.ERROR_MESSAGE);
	}

	public static void Inform(String message) {
		JOptionPane.showMessageDialog(null, message, "Information",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Put elements from a float list into a float buffer
	 * 
	 * @param list
	 *            The Float List
	 * @param buffer
	 *            The FloatBuffer
	 */
	public static void listPutFloatBuffer(List<Float> list, FloatBuffer buffer) {
		for (Float float1 : list) {
			buffer.put(float1.floatValue());
		}
	}

	public static int getPrefFov() {
		return prefFov;
	}

	/**
	 * Quad shortcut for a triangle vertex or normal array.
	 * <p>
	 * (0, 1) a-------b (1, 1)
	 * </p>
	 * <p>
	 * (0, 0) c-------d (1, 0)
	 * </p>
	 * 
	 * @param list
	 * @param a
	 *            point a
	 * @param b
	 *            point b
	 * @param c
	 *            point c
	 * @param d
	 *            point d
	 */
	public static void quad(List<Float> list, Vector3f a, Vector3f b,
			Vector3f c, Vector3f d) {
		list.add(a.x);
		list.add(a.y);
		list.add(a.z);

		list.add(b.x);
		list.add(b.y);
		list.add(b.z);

		list.add(c.x);
		list.add(c.y);
		list.add(c.z);

		list.add(c.x);
		list.add(c.y);
		list.add(c.z);

		list.add(b.x);
		list.add(b.y);
		list.add(b.z);

		list.add(d.x);
		list.add(d.y);
		list.add(d.z);
	}

	public static void three() {
		three(Kondion.getCurrentCamera());
	}
	
	/**
	 * Set to 3d mode
	 */
	public static void three(OKO_Camera_ cam) {
		glViewport(0, 0, Window.getWidth(), Window.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		cam.gluLookAt();
		// GLU.gluPerspective(fov,
		// ((float) (Window.getWidth()) / (float) (Window.getHeight())),
		// 0.1f, 200.0f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		// glCullFace(GL_BACK);
		glEnable(GL_BLEND);
		// glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_POINT_SMOOTH);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDepthFunc(GL_LEQUAL);
		glClearDepth(10.0f);
	}
	
	public static void two() {
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		glClearColor(Kondion.getWorld().clearColor.x, Kondion.getWorld().clearColor.y,
				Kondion.getWorld().clearColor.z, Kondion.getWorld().clearColor.w);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Window.getWidth(), Window.getHeight(),
				0, 6.0f, -6.0f);
		glMatrixMode(GL_MODELVIEW);
		//glScalef(1.0f, -1.0f, 1.0f);
		GLDrawing.setCoords(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
	}

	/** 2D mode */
	/*
	 * public static void Two() { glViewport(0, 0, getDisplayMode().getWidth(),
	 * getDisplayMode() .getHeight()); glMatrixMode(GL_PROJECTION);
	 * glLoadIdentity(); glOrtho(0, getDisplayMode().getWidth(),
	 * getDisplayMode().getHeight(), 0, 6, -6); glMatrixMode(GL_MODELVIEW);
	 * 
	 * glAlphaFunc(GL_GREATER, 0.0f); glEnable(GL_ALPHA_TEST);
	 * 
	 * glEnable(GL_TEXTURE_2D); glEnable(GL_DEPTH_TEST); glEnable(GL_BLEND);
	 * glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); glClearColor(0.0f,
	 * 0.0f, 0.0f, 0.0f); }
	 */
}