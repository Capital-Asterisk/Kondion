/*
 * (C) Copyright 2015-2016 Richard Greenlees
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */

package vendalenger.kondion.math;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.joml.AxisAngle4f;
import org.joml.Matrix3d;
import org.joml.Matrix3f;
import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Contains the definition of a 4x4 Matrix of floats, and associated functions
 * to transform it. The matrix is column-major to match OpenGL's interpretation,
 * and it looks like this:
 * <p>
 * m00 m10 m20 m30<br>
 * m01 m11 m21 m31<br>
 * m02 m12 m22 m32<br>
 * m03 m13 m23 m33<br>
 * 
 * @author Richard Greenlees
 * @author Kai Burjack
 */
public class Matrix4klf {

	private static final long serialVersionUID = 1L;
	public static final long meter = 1000l;
	public float m00, m10, m20;
	public float m01, m11, m21;
	public float m02, m12, m22;
	public float m03, m13, m23, m33;
	public long m30, m31, m32;

	/**
	 * Create a new {@link Matrix4klf} and set it to {@link #identity()
	 * identity}.
	 */
	public Matrix4klf() {
		m00 = 1.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = 1.0f;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 1.0f;
		m23 = 0.0f;
		m30 = 1l;
		m31 = 1l;
		m32 = 1l;
		m33 = 1.0f;
	}

	/**
	 * Create a new {@link Matrix4klf} by setting its uppper left 3x3 submatrix
	 * to the values of the given {@link Matrix3f} and the rest to identity.
	 * 
	 * @param mat
	 *            the {@link Matrix3f}
	 */
	public Matrix4klf(Matrix3f mat) {
		m00 = mat.m00;
		m01 = mat.m01;
		m02 = mat.m02;
		m10 = mat.m10;
		m11 = mat.m11;
		m12 = mat.m12;
		m20 = mat.m20;
		m21 = mat.m21;
		m22 = mat.m22;
		m33 = 1.0f;
	}

	/**
	 * Create a new {@link Matrix4klf} and make it a copy of the given matrix.
	 * 
	 * @param mat
	 *            the {@link Matrix4klf} to copy the values from
	 */
	public Matrix4klf(Matrix4klf mat) {
		m00 = mat.m00;
		m01 = mat.m01;
		m02 = mat.m02;
		m03 = mat.m03;
		m10 = mat.m10;
		m11 = mat.m11;
		m12 = mat.m12;
		m13 = mat.m13;
		m20 = mat.m20;
		m21 = mat.m21;
		m22 = mat.m22;
		m23 = mat.m23;
		m30 = mat.m30;
		m31 = mat.m31;
		m32 = mat.m32;
		m33 = mat.m33;
	}

	/**
	 * Create a new {@link Matrix4klf} and make it a copy of the given matrix.
	 * <p>
	 * Note that due to the given {@link Matrix4d} storing values in
	 * double-precision and the constructed {@link Matrix4klf} storing them in
	 * single-precision, there is the possibility of losing precision.
	 * 
	 * @param mat
	 *            the {@link Matrix4d} to copy the values from
	 */
	public Matrix4klf(Matrix4d mat) {
		m00 = (float) mat.m00;
		m01 = (float) mat.m01;
		m02 = (float) mat.m02;
		m03 = (float) mat.m03;
		m10 = (float) mat.m10;
		m11 = (float) mat.m11;
		m12 = (float) mat.m12;
		m13 = (float) mat.m13;
		m20 = (float) mat.m20;
		m21 = (float) mat.m21;
		m22 = (float) mat.m22;
		m23 = (float) mat.m23;
		m30 = (long) mat.m30 * meter;
		m31 = (long) mat.m31 * meter;
		m32 = (long) mat.m32 * meter;
		m33 = (float) mat.m33;
	}

	/**
	 * Create a new 4x4 matrix using the supplied float values.
	 * 
	 * @param m00
	 *            the value of m00
	 * @param m01
	 *            the value of m01
	 * @param m02
	 *            the value of m02
	 * @param m03
	 *            the value of m03
	 * @param m10
	 *            the value of m10
	 * @param m11
	 *            the value of m11
	 * @param m12
	 *            the value of m12
	 * @param m13
	 *            the value of m13
	 * @param m20
	 *            the value of m20
	 * @param m21
	 *            the value of m21
	 * @param m22
	 *            the value of m22
	 * @param m23
	 *            the value of m23
	 * @param m30
	 *            the value of m30
	 * @param m31
	 *            the value of m31
	 * @param m32
	 *            the value of m32
	 * @param m33
	 *            the value of m33
	 */
	public Matrix4klf(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20,
			float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = (long) (m30 * meter);
		this.m31 = (long) (m31 * meter);
		this.m32 = (long) (m32 * meter);
		this.m33 = m33;
	}

	/**
	 * Reset this matrix to the identity.
	 * <p>
	 * Please note that if a call to {@link #identity()} is immediately followed
	 * by a call to: {@link #translate(float, float, float) translate},
	 * {@link #rotate(float, float, float, float) rotate},
	 * {@link #scale(float, float, float) scale},
	 * {@link #perspective(float, float, float, float) perspective},
	 * {@link #frustum(float, float, float, float, float, float) frustum},
	 * {@link #ortho(float, float, float, float, float, float) ortho},
	 * {@link #ortho2D(float, float, float, float) ortho2D},
	 * {@link #lookAt(float, float, float, float, float, float, float, float, float)
	 * lookAt}, {@link #lookAlong(float, float, float, float, float, float)
	 * lookAlong}, or any of their overloads, then the call to
	 * {@link #identity()} can be omitted and the subsequent call replaced with:
	 * {@link #translation(float, float, float) translation},
	 * {@link #rotation(float, float, float, float) rotation},
	 * {@link #scaling(float, float, float) scaling},
	 * {@link #setPerspective(float, float, float, float) setPerspective},
	 * {@link #setFrustum(float, float, float, float, float, float) setFrustum},
	 * {@link #setOrtho(float, float, float, float, float, float) setOrtho},
	 * {@link #setOrtho2D(float, float, float, float) setOrtho2D},
	 * {@link #setLookAt(float, float, float, float, float, float, float, float, float)
	 * setLookAt},
	 * {@link #setLookAlong(float, float, float, float, float, float)
	 * setLookAlong}, or any of their overloads.
	 * 
	 * @return this
	 */
	public Matrix4klf identity() {
		m00 = 1.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = 1.0f;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 1.0f;
		m23 = 0.0f;
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Store the values of the given matrix <code>m</code> into
	 * <code>this</code> matrix.*
	 * 
	 * @see #Matrix4f(Matrix4klf)
	 * @see #get(Matrix4klf)
	 * 
	 * @param m
	 *            the matrix to copy the values from
	 * @return this
	 */
	public Matrix4klf set(Matrix4klf m) {
		m00 = m.m00;
		m01 = m.m01;
		m02 = m.m02;
		m03 = m.m03;
		m10 = m.m10;
		m11 = m.m11;
		m12 = m.m12;
		m13 = m.m13;
		m20 = m.m20;
		m21 = m.m21;
		m22 = m.m22;
		m23 = m.m23;
		m30 = m.m30;
		m31 = m.m31;
		m32 = m.m32;
		m33 = m.m33;
		return this;
	}

	/**
	 * Store the values of the given matrix <code>m</code> into
	 * <code>this</code> matrix.
	 * <p>
	 * Note that due to the given matrix <code>m</code> storing values in
	 * double-precision and <code>this</code> matrix storing them in
	 * single-precision, there is the possibility to lose precision.
	 * 
	 * @see #Matrix4f(Matrix4d)
	 * @see #get(Matrix4d)
	 * 
	 * @param m
	 *            the matrix to copy the values from
	 * @return this
	 */
	public Matrix4klf set(Matrix4d m) {
		m00 = (float) m.m00;
		m01 = (float) m.m01;
		m02 = (float) m.m02;
		m03 = (float) m.m03;
		m10 = (float) m.m10;
		m11 = (float) m.m11;
		m12 = (float) m.m12;
		m13 = (float) m.m13;
		m20 = (float) m.m20;
		m21 = (float) m.m21;
		m22 = (float) m.m22;
		m23 = (float) m.m23;
		m30 = (long) (m.m30 * meter);
		m31 = (long) (m.m31 * meter);
		m32 = (long) (m.m32 * meter);
		m33 = (float) m.m33;
		return this;
	}

	/**
	 * Set the upper left 3x3 submatrix of this {@link Matrix4klf} to the given
	 * {@link Matrix3f} and the rest to identity.
	 * 
	 * @see #Matrix4f(Matrix3f)
	 * 
	 * @param mat
	 *            the {@link Matrix3f}
	 * @return this
	 */
	public Matrix4klf set(Matrix3f mat) {
		m00 = mat.m00;
		m01 = mat.m01;
		m02 = mat.m02;
		m03 = 0.0f;
		m10 = mat.m10;
		m11 = mat.m11;
		m12 = mat.m12;
		m13 = 0.0f;
		m20 = mat.m20;
		m21 = mat.m21;
		m22 = mat.m22;
		m23 = 0.0f;
		// m30 = 0.0f;
		// m31 = 0.0f;
		// m32 = 0.0f;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set this matrix to be equivalent to the rotation specified by the given
	 * {@link Quaterniond}.
	 * 
	 * @see Quaterniond#get(Matrix4klf)
	 * 
	 * @param q
	 *            the {@link Quaterniond}
	 * @return this
	 */
	public Matrix4klf set(Quaterniond q) {
		// q.get(this);
		return this;
	}

	/**
	 * Set the upper left 3x3 submatrix of this {@link Matrix4klf} to that of
	 * the given {@link Matrix4klf} and the rest to identity.
	 * 
	 * @param mat
	 *            the {@link Matrix4klf}
	 * @return this
	 */
	public Matrix4klf set3x3(Matrix4klf mat) {
		m00 = mat.m00;
		m01 = mat.m01;
		m02 = mat.m02;
		m03 = 0.0f;
		m10 = mat.m10;
		m11 = mat.m11;
		m12 = mat.m12;
		m13 = 0.0f;
		m20 = mat.m20;
		m21 = mat.m21;
		m22 = mat.m22;
		m23 = 0.0f;
		// m30 = 0.0f;
		// m31 = 0.0f;
		// m32 = 0.0f;
		// m33 = 1.0f;
		return this;
	}

	/**
	 * Multiply this matrix by the supplied <code>right</code> matrix and store
	 * the result in <code>this</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * <code>right</code> matrix, then the new matrix will be <code>M * R</code>
	 * . So when transforming a vector <code>v</code> with the new matrix by
	 * using <code>M * R * v</code>, the transformation of the right matrix will
	 * be applied first!
	 *
	 * @param right
	 *            the right operand of the matrix multiplication
	 * @return this
	 */
	public Matrix4klf mul(Matrix4klf right) {
		return mul(right, this);
	}

	/**
	 * Multiply this matrix by the supplied <code>right</code> matrix and store
	 * the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * <code>right</code> matrix, then the new matrix will be <code>M * R</code>
	 * . So when transforming a vector <code>v</code> with the new matrix by
	 * using <code>M * R * v</code>, the transformation of the right matrix will
	 * be applied first!
	 *
	 * @param right
	 *            the right operand of the matrix multiplication
	 * @param dest
	 *            the destination matrix, which will hold the result
	 * @return dest
	 */
	public Matrix4klf mul(Matrix4klf right, Matrix4klf dest) {
		long tmpx = m30;
		long tmpy = m31;
		long tmpz = m32;
		//m30 -= tmpx;
		//m31 -= tmpy;
		//m32 -= tmpz;
		//right.m30 -= tmpx;
		//right.m31 -= tmpy;
		//right.m32 -= tmpz;
		dest.set(m00 * right.m00 + m10 * right.m01 + m20 * right.m02 + m30 * right.m03,
				m01 * right.m00 + m11 * right.m01 + m21 * right.m02 + m31 * right.m03,
				m02 * right.m00 + m12 * right.m01 + m22 * right.m02 + m32 * right.m03,
				m03 * right.m00 + m13 * right.m01 + m23 * right.m02 + m33 * right.m03,
				m00 * right.m10 + m10 * right.m11 + m20 * right.m12 + m30 * right.m13,
				m01 * right.m10 + m11 * right.m11 + m21 * right.m12 + m31 * right.m13,
				m02 * right.m10 + m12 * right.m11 + m22 * right.m12 + m32 * right.m13,
				m03 * right.m10 + m13 * right.m11 + m23 * right.m12 + m33 * right.m13,
				m00 * right.m20 + m10 * right.m21 + m20 * right.m22 + m30 * right.m23,
				m01 * right.m20 + m11 * right.m21 + m21 * right.m22 + m31 * right.m23,
				m02 * right.m20 + m12 * right.m21 + m22 * right.m22 + m32 * right.m23,
				m03 * right.m20 + m13 * right.m21 + m23 * right.m22 + m33 * right.m23,
				m00 * right.m30 + m10 * right.m31 + m20 * right.m32 + m30 * right.m33,
				m01 * right.m30 + m11 * right.m31 + m21 * right.m32 + m31 * right.m33,
				m02 * right.m30 + m12 * right.m31 + m22 * right.m32 + m32 * right.m33,
				m03 * right.m30 + m13 * right.m31 + m23 * right.m32 + m33 * right.m33);
		//m30 += tmpx;
		//m31 += tmpy;
		//m32 += tmpz;
		//right.m30 += tmpx;
		//right.m31 += tmpy;
		//right.m32 += tmpz;
		return dest;
	}

	/**
	 * Component-wise add <code>this</code> and <code>other</code> and store the
	 * result in <code>dest</code>.
	 * 
	 * @param other
	 *            the other addend
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf add(Matrix4klf other, Matrix4klf dest) {
		dest.m00 = m00 + other.m00;
		dest.m01 = m01 + other.m01;
		dest.m02 = m02 + other.m02;
		dest.m03 = m03 + other.m03;
		dest.m10 = m10 + other.m10;
		dest.m11 = m11 + other.m11;
		dest.m12 = m12 + other.m12;
		dest.m13 = m13 + other.m13;
		dest.m20 = m20 + other.m20;
		dest.m21 = m21 + other.m21;
		dest.m22 = m22 + other.m22;
		dest.m23 = m23 + other.m23;
		dest.m30 = m30 + other.m30;
		dest.m31 = m31 + other.m31;
		dest.m32 = m32 + other.m32;
		dest.m33 = m33 + other.m33;
		return dest;
	}

	/**
	 * Component-wise subtract <code>subtrahend</code> from <code>this</code>.
	 * 
	 * @param subtrahend
	 *            the subtrahend
	 * @return this
	 */
	public Matrix4klf sub(Matrix4klf subtrahend) {
		return sub(subtrahend, this);
	}

	/**
	 * Component-wise subtract <code>subtrahend</code> from <code>this</code>
	 * and store the result in <code>dest</code>.
	 * 
	 * @param subtrahend
	 *            the subtrahend
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf sub(Matrix4klf subtrahend, Matrix4klf dest) {
		dest.m00 = m00 - subtrahend.m00;
		dest.m01 = m01 - subtrahend.m01;
		dest.m02 = m02 - subtrahend.m02;
		dest.m03 = m03 - subtrahend.m03;
		dest.m10 = m10 - subtrahend.m10;
		dest.m11 = m11 - subtrahend.m11;
		dest.m12 = m12 - subtrahend.m12;
		dest.m13 = m13 - subtrahend.m13;
		dest.m20 = m20 - subtrahend.m20;
		dest.m21 = m21 - subtrahend.m21;
		dest.m22 = m22 - subtrahend.m22;
		dest.m23 = m23 - subtrahend.m23;
		dest.m30 = m30 - subtrahend.m30;
		dest.m31 = m31 - subtrahend.m31;
		dest.m32 = m32 - subtrahend.m32;
		dest.m33 = m33 - subtrahend.m33;
		return dest;
	}

	/**
	 * Component-wise multiply <code>this</code> by <code>other</code>.
	 * 
	 * @param other
	 *            the other matrix
	 * @return this
	 */
	public Matrix4klf mulComponentWise(Matrix4klf other) {
		return mulComponentWise(other, this);
	}

	/**
	 * Component-wise multiply <code>this</code> by <code>other</code> and store
	 * the result in <code>dest</code>.
	 * 
	 * @param other
	 *            the other matrix
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf mulComponentWise(Matrix4klf other, Matrix4klf dest) {
		dest.m00 = m00 * other.m00;
		dest.m01 = m01 * other.m01;
		dest.m02 = m02 * other.m02;
		dest.m03 = m03 * other.m03;
		dest.m10 = m10 * other.m10;
		dest.m11 = m11 * other.m11;
		dest.m12 = m12 * other.m12;
		dest.m13 = m13 * other.m13;
		dest.m20 = m20 * other.m20;
		dest.m21 = m21 * other.m21;
		dest.m22 = m22 * other.m22;
		dest.m23 = m23 * other.m23;
		dest.m30 = m30 * other.m30;
		dest.m31 = m31 * other.m31;
		dest.m32 = m32 * other.m32;
		dest.m33 = m33 * other.m33;
		return dest;
	}

	/**
	 * Component-wise add the upper 4x3 submatrices of <code>this</code> and
	 * <code>other</code>.
	 * 
	 * @param other
	 *            the other addend
	 * @return this
	 */
	public Matrix4klf add4x3(Matrix4klf other) {
		return add4x3(other, this);
	}

	/**
	 * Component-wise add the upper 4x3 submatrices of <code>this</code> and
	 * <code>other</code> and store the result in <code>dest</code>.
	 * <p>
	 * The other components of <code>dest</code> will be set to the ones of
	 * <code>this</code>.
	 * 
	 * @param other
	 *            the other addend
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf add4x3(Matrix4klf other, Matrix4klf dest) {
		dest.m00 = m00 + other.m00;
		dest.m01 = m01 + other.m01;
		dest.m02 = m02 + other.m02;
		dest.m03 = m03;
		dest.m10 = m10 + other.m10;
		dest.m11 = m11 + other.m11;
		dest.m12 = m12 + other.m12;
		dest.m13 = m13;
		dest.m20 = m20 + other.m20;
		dest.m21 = m21 + other.m21;
		dest.m22 = m22 + other.m22;
		dest.m23 = m23;
		dest.m30 = m30 + other.m30;
		dest.m31 = m31 + other.m31;
		dest.m32 = m32 + other.m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Component-wise subtract the upper 4x3 submatrices of
	 * <code>subtrahend</code> from <code>this</code>.
	 * 
	 * @param subtrahend
	 *            the subtrahend
	 * @return this
	 */
	public Matrix4klf sub4x3(Matrix4klf subtrahend) {
		return sub4x3(subtrahend, this);
	}

	/**
	 * Component-wise subtract the upper 4x3 submatrices of
	 * <code>subtrahend</code> from <code>this</code> and store the result in
	 * <code>dest</code>.
	 * <p>
	 * The other components of <code>dest</code> will be set to the ones of
	 * <code>this</code>.
	 * 
	 * @param subtrahend
	 *            the subtrahend
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf sub4x3(Matrix4klf subtrahend, Matrix4klf dest) {
		dest.m00 = m00 - subtrahend.m00;
		dest.m01 = m01 - subtrahend.m01;
		dest.m02 = m02 - subtrahend.m02;
		dest.m03 = m03;
		dest.m10 = m10 - subtrahend.m10;
		dest.m11 = m11 - subtrahend.m11;
		dest.m12 = m12 - subtrahend.m12;
		dest.m13 = m13;
		dest.m20 = m20 - subtrahend.m20;
		dest.m21 = m21 - subtrahend.m21;
		dest.m22 = m22 - subtrahend.m22;
		dest.m23 = m23;
		dest.m30 = m30 - subtrahend.m30;
		dest.m31 = m31 - subtrahend.m31;
		dest.m32 = m32 - subtrahend.m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Component-wise multiply the upper 4x3 submatrices of <code>this</code> by
	 * <code>other</code>.
	 * 
	 * @param other
	 *            the other matrix
	 * @return this
	 */
	public Matrix4klf mul4x3ComponentWise(Matrix4klf other) {
		return mul4x3ComponentWise(other, this);
	}

	/**
	 * Component-wise multiply the upper 4x3 submatrices of <code>this</code> by
	 * <code>other</code> and store the result in <code>dest</code>.
	 * <p>
	 * The other components of <code>dest</code> will be set to the ones of
	 * <code>this</code>.
	 * 
	 * @param other
	 *            the other matrix
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf mul4x3ComponentWise(Matrix4klf other, Matrix4klf dest) {
		dest.m00 = m00 * other.m00;
		dest.m01 = m01 * other.m01;
		dest.m02 = m02 * other.m02;
		dest.m03 = m03;
		dest.m10 = m10 * other.m10;
		dest.m11 = m11 * other.m11;
		dest.m12 = m12 * other.m12;
		dest.m13 = m13;
		dest.m20 = m20 * other.m20;
		dest.m21 = m21 * other.m21;
		dest.m22 = m22 * other.m22;
		dest.m23 = m23;
		dest.m30 = m30 * other.m30;
		dest.m31 = m31 * other.m31;
		dest.m32 = m32 * other.m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Set the values within this matrix to the supplied float values. The
	 * matrix will look like this:<br>
	 * <br>
	 *
	 * m00, m10, m20, m30<br>
	 * m01, m11, m21, m31<br>
	 * m02, m12, m22, m32<br>
	 * m03, m13, m23, m33
	 * 
	 * @param m00
	 *            the new value of m00
	 * @param m01
	 *            the new value of m01
	 * @param m02
	 *            the new value of m02
	 * @param m03
	 *            the new value of m03
	 * @param m10
	 *            the new value of m10
	 * @param m11
	 *            the new value of m11
	 * @param m12
	 *            the new value of m12
	 * @param m13
	 *            the new value of m13
	 * @param m20
	 *            the new value of m20
	 * @param m21
	 *            the new value of m21
	 * @param m22
	 *            the new value of m22
	 * @param m23
	 *            the new value of m23
	 * @param m30
	 *            the new value of m30
	 * @param m31
	 *            the new value of m31
	 * @param m32
	 *            the new value of m32
	 * @param m33
	 *            the new value of m33
	 * @return this
	 */
	public Matrix4klf set(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13,
			float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = (long) (m30 * meter);
		this.m31 = (long) (m31 * meter);
		this.m32 = (long) (m32 * meter);
		this.m33 = m33;
		return this;
	}

	/**
	 * Set the values in the matrix using a float array that contains the matrix
	 * elements in column-major order.
	 * <p>
	 * The results will look like this:<br>
	 * <br>
	 * 
	 * 0, 4, 8, 12<br>
	 * 1, 5, 9, 13<br>
	 * 2, 6, 10, 14<br>
	 * 3, 7, 11, 15<br>
	 * 
	 * @see #set(float[])
	 * 
	 * @param m
	 *            the array to read the matrix values from
	 * @param off
	 *            the offset into the array
	 * @return this
	 */
	public Matrix4klf set(float m[], int off) {
		m00 = m[off + 0];
		m01 = m[off + 1];
		m02 = m[off + 2];
		m03 = m[off + 3];
		m10 = m[off + 4];
		m11 = m[off + 5];
		m12 = m[off + 6];
		m13 = m[off + 7];
		m20 = m[off + 8];
		m21 = m[off + 9];
		m22 = m[off + 10];
		m23 = m[off + 11];
		m30 = (long) (m[off + 12] * meter);
		m31 = (long) (m[off + 13] * meter);
		m32 = (long) (m[off + 14] * meter);
		m33 = m[off + 15];
		return this;
	}

	/**
	 * Set the values in the matrix using a float array that contains the matrix
	 * elements in column-major order.
	 * <p>
	 * The results will look like this:<br>
	 * <br>
	 * 
	 * 0, 4, 8, 12<br>
	 * 1, 5, 9, 13<br>
	 * 2, 6, 10, 14<br>
	 * 3, 7, 11, 15<br>
	 * 
	 * @see #set(float[], int)
	 * 
	 * @param m
	 *            the array to read the matrix values from
	 * @return this
	 */
	public Matrix4klf set(float m[]) {
		return set(m, 0);
	}

	/**
	 * Return the determinant of this matrix.
	 * <p>
	 * If <code>this</code> matrix represents an {@link #isAffine() affine}
	 * transformation, such as translation, rotation, scaling and shearing, and
	 * thus its last row is equal to <tt>(0, 0, 0, 1)</tt>, then
	 * {@link #determinantAffine()} can be used instead of this method.
	 * 
	 * @see #determinantAffine()
	 * 
	 * @return the determinant
	 */
	public float determinant() {
		return (m00 * m11 - m01 * m10) * (m22 * m33 - m23 * m32) + (m02 * m10 - m00 * m12) * (m21 * m33 - m23 * m31)
				+ (m00 * m13 - m03 * m10) * (m21 * m32 - m22 * m31) + (m01 * m12 - m02 * m11) * (m20 * m33 - m23 * m30)
				+ (m03 * m11 - m01 * m13) * (m20 * m32 - m22 * m30) + (m02 * m13 - m03 * m12) * (m20 * m31 - m21 * m30);
	}

	/**
	 * Return the determinant of the upper left 3x3 submatrix of this matrix.
	 * 
	 * @return the determinant
	 */
	public float determinant3x3() {
		return (m00 * m11 - m01 * m10) * m22 + (m02 * m10 - m00 * m12) * m21 + (m01 * m12 - m02 * m11) * m20;
	}

	/**
	 * Invert this matrix and write the result into <code>dest</code>.
	 * <p>
	 * If <code>this</code> matrix represents an {@link #isAffine() affine}
	 * transformation, such as translation, rotation, scaling and shearing, and
	 * thus its last row is equal to <tt>(0, 0, 0, 1)</tt>, then
	 * {@link #invertAffine(Matrix4klf)} can be used instead of this method.
	 * 
	 * @see #invertAffine(Matrix4klf)
	 * 
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf invert(Matrix4klf dest) {
		float a = m00 * m11 - m01 * m10;
		float b = m00 * m12 - m02 * m10;
		float c = m00 * m13 - m03 * m10;
		float d = m01 * m12 - m02 * m11;
		float e = m01 * m13 - m03 * m11;
		float f = m02 * m13 - m03 * m12;
		float g = m20 * m31 - m21 * m30;
		float h = m20 * m32 - m22 * m30;
		float i = m20 * m33 - m23 * m30;
		float j = m21 * m32 - m22 * m31;
		float k = m21 * m33 - m23 * m31;
		float l = m22 * m33 - m23 * m32;
		float det = a * l - b * k + c * j + d * i - e * h + f * g;
		det = 1.0f / det;
		dest.set((m11 * l - m12 * k + m13 * j) * det, (-m01 * l + m02 * k - m03 * j) * det,
				(m31 * f - m32 * e + m33 * d) * det, (-m21 * f + m22 * e - m23 * d) * det,
				(-m10 * l + m12 * i - m13 * h) * det, (m00 * l - m02 * i + m03 * h) * det,
				(-m30 * f + m32 * c - m33 * b) * det, (m20 * f - m22 * c + m23 * b) * det,
				(m10 * k - m11 * i + m13 * g) * det, (-m00 * k + m01 * i - m03 * g) * det,
				(m30 * e - m31 * c + m33 * a) * det, (-m20 * e + m21 * c - m23 * a) * det,
				(-m10 * j + m11 * h - m12 * g) * det, (m00 * j - m01 * h + m02 * g) * det,
				(-m30 * d + m31 * b - m32 * a) * det, (m20 * d - m21 * b + m22 * a) * det);
		return dest;
	}

	/**
	 * Invert this matrix.
	 * <p>
	 * If <code>this</code> matrix represents an {@link #isAffine() affine}
	 * transformation, such as translation, rotation, scaling and shearing, and
	 * thus its last row is equal to <tt>(0, 0, 0, 1)</tt>, then
	 * {@link #invertAffine()} can be used instead of this method.
	 * 
	 * @see #invertAffine()
	 * 
	 * @return this
	 */
	public Matrix4klf invert() {
		return invert(this);
	}

	/**
	 * Get the scaling factors of <code>this</code> matrix for the three base
	 * axes.
	 * 
	 * @param dest
	 *            will hold the scaling factors for <tt>x</tt>, <tt>y</tt> and
	 *            <tt>z</tt>
	 * @return dest
	 */
	public Vector3f getScale(Vector3f dest) {
		dest.x = (float) Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02);
		dest.y = (float) Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12);
		dest.z = (float) Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22);
		return dest;
	}

	/**
	 * Return a string representation of this matrix.
	 * <p>
	 * This method creates a new {@link DecimalFormat} on every invocation with
	 * the format string "<tt>  0.000E0; -</tt>".
	 * 
	 * @return the string representation
	 */
	public String toString() {
		DecimalFormat formatter = new DecimalFormat("  0.000E0; -"); //$NON-NLS-1$
		return toString(formatter).replaceAll("E(\\d+)", "E+$1"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Return a string representation of this matrix by formatting the matrix
	 * elements with the given {@link NumberFormat}.
	 * 
	 * @param formatter
	 *            the {@link NumberFormat} used to format the matrix values with
	 * @return the string representation
	 */
	public String toString(NumberFormat formatter) {
		return formatter.format(m00) + formatter.format(m10) + formatter.format(m20) + formatter.format(m30) + "\n" //$NON-NLS-1$
				+ formatter.format(m01) + formatter.format(m11) + formatter.format(m21) + formatter.format(m31) + "\n" //$NON-NLS-1$
				+ formatter.format(m02) + formatter.format(m12) + formatter.format(m22) + formatter.format(m32) + "\n" //$NON-NLS-1$
				+ formatter.format(m03) + formatter.format(m13) + formatter.format(m23) + formatter.format(m33) + "\n"; //$NON-NLS-1$
	}

	/**
	 * Get the current values of <code>this</code> matrix and store them into
	 * <code>dest</code>.
	 * <p>
	 * This is the reverse method of {@link #set(Matrix4klf)} and allows to
	 * obtain intermediate calculation results when chaining multiple
	 * transformations.
	 * 
	 * @see #set(Matrix4klf)
	 * 
	 * @param dest
	 *            the destination matrix
	 * @return the passed in destination
	 */
	public Matrix4klf get(Matrix4klf dest) {
		return dest.set(this);
	}

	/**
	 * Get the current values of <code>this</code> matrix and store them into
	 * <code>dest</code>.
	 * <p>
	 * This is the reverse method of {@link #set(Matrix4d)} and allows to obtain
	 * intermediate calculation results when chaining multiple transformations.
	 * 
	 * @see #set(Matrix4d)
	 * 
	 * @param dest
	 *            the destination matrix
	 * @return the passed in destination
	 */
	public Matrix4d get(Matrix4d dest) {
		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m03 = m03;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m13 = m13;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		dest.m23 = m23;
		dest.m30 = ((double) m30) / meter;
		dest.m31 = ((double) m31) / meter;
		dest.m32 = ((double) m32) / meter;
		dest.m33 = m30;
		return dest;
	}

	/**
	 * Get the current values of the upper left 3x3 submatrix of
	 * <code>this</code> matrix and store them into <code>dest</code>.
	 * 
	 * @param dest
	 *            the destination matrix
	 * @return the passed in destination
	 */
	public Matrix3f get3x3(Matrix3f dest) {
		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		return dest;
	}

	/**
	 * Get the current values of the upper left 3x3 submatrix of
	 * <code>this</code> matrix and store them into <code>dest</code>.
	 * 
	 * @param dest
	 *            the destination matrix
	 * @return the passed in destination
	 */
	public Matrix3d get3x3(Matrix3d dest) {
		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		return dest;
	}
	
	public void get(FloatBuffer buffer) {
		buffer.put(m00);
		buffer.put(m10);
		buffer.put(m20);
		buffer.put(((float) m30) / meter);
		
		buffer.put(m01);
		buffer.put(m11);
		buffer.put(m21);
		buffer.put(((float) m31) / meter);
		
		buffer.put(m02);
		buffer.put(m12);
		buffer.put(m22);
		buffer.put(((float) m32) / meter);
		
		buffer.put(m03);
		buffer.put(m13);
		buffer.put(m23);
		buffer.put(m33);
		
		buffer.flip();
	}

	/**
	 * Set all the values within this matrix to <code>0</code>.
	 * 
	 * @return this
	 */
	public Matrix4klf zero() {
		m00 = 0.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = 0.0f;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 0.0f;
		m23 = 0.0f;
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 0.0f;
		return this;
	}

	/**
	 * Set this matrix to be a simple scale matrix, which scales all axes
	 * uniformly by the given factor.
	 * <p>
	 * The resulting matrix can be multiplied against another transformation
	 * matrix to obtain an additional scaling.
	 * <p>
	 * In order to post-multiply a scaling transformation directly to a matrix,
	 * use {@link #scale(float) scale()} instead.
	 * 
	 * @see #scale(float)
	 * 
	 * @param factor
	 *            the scale factor in x, y and z
	 * @return this
	 */
	public Matrix4klf scaling(float factor) {
		m00 = factor;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = factor;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = factor;
		m23 = 0.0f;
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set this matrix to be a simple scale matrix.
	 * <p>
	 * The resulting matrix can be multiplied against another transformation
	 * matrix to obtain an additional scaling.
	 * <p>
	 * In order to post-multiply a scaling transformation directly to a matrix,
	 * use {@link #scale(float, float, float) scale()} instead.
	 * 
	 * @see #scale(float, float, float)
	 * 
	 * @param x
	 *            the scale in x
	 * @param y
	 *            the scale in y
	 * @param z
	 *            the scale in z
	 * @return this
	 */
	public Matrix4klf scaling(float x, float y, float z) {
		m00 = x;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = y;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = z;
		m23 = 0.0f;
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set this matrix to be a simple scale matrix which scales the base axes by
	 * <tt>xyz.x</tt>, <tt>xyz.y</tt> and <tt>xyz.z</tt> respectively.
	 * <p>
	 * The resulting matrix can be multiplied against another transformation
	 * matrix to obtain an additional scaling.
	 * <p>
	 * In order to post-multiply a scaling transformation directly to a matrix
	 * use {@link #scale(Vector3f) scale()} instead.
	 * 
	 * @see #scale(Vector3f)
	 * 
	 * @param xyz
	 *            the scale in x, y and z respectively
	 * @return this
	 */
	public Matrix4klf scaling(Vector3f xyz) {
		return scaling(xyz.x, xyz.y, xyz.z);
	}

	/**
	 * Set this matrix to a rotation matrix which rotates the given radians
	 * about a given axis.
	 * <p>
	 * The resulting matrix can be multiplied against another transformation
	 * matrix to obtain an additional rotation.
	 * <p>
	 * In order to post-multiply a rotation transformation directly to a matrix,
	 * use {@link #rotate(float, Vector3f) rotate()} instead.
	 * 
	 * @see #rotate(float, Vector3f)
	 * 
	 * @param angle
	 *            the angle in radians
	 * @param axis
	 *            the axis to rotate about (needs to be
	 *            {@link Vector3f#normalize() normalized})
	 * @return this
	 */
	public Matrix4klf rotation(float angle, Vector3f axis) {
		return rotation(angle, axis.x, axis.y, axis.z);
	}

	/**
	 * Set this matrix to a rotation transformation using the given
	 * {@link AxisAngle4f}.
	 * <p>
	 * The resulting matrix can be multiplied against another transformation
	 * matrix to obtain an additional rotation.
	 * <p>
	 * In order to apply the rotation transformation to an existing
	 * transformation, use {@link #rotate(AxisAngle4f) rotate()} instead.
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Axis_and_angle">
	 * http://en.wikipedia.org</a>
	 *
	 * @see #rotate(AxisAngle4f)
	 * 
	 * @param axisAngle
	 *            the {@link AxisAngle4f} (needs to be
	 *            {@link AxisAngle4f#normalize() normalized})
	 * @return this
	 */
	public Matrix4klf rotation(AxisAngle4f axisAngle) {
		return rotation(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
	}

	/**
	 * Set this matrix to a rotation matrix which rotates the given radians
	 * about a given axis.
	 * <p>
	 * The axis described by the three components needs to be a unit vector.
	 * <p>
	 * The resulting matrix can be multiplied against another transformation
	 * matrix to obtain an additional rotation.
	 * <p>
	 * In order to apply the rotation transformation to an existing
	 * transformation, use {@link #rotate(float, float, float, float) rotate()}
	 * instead.
	 * <p>
	 * Reference: <a href=
	 * "http://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle">
	 * http://en.wikipedia.org</a>
	 * 
	 * @see #rotate(float, float, float, float)
	 * 
	 * @param angle
	 *            the angle in radians
	 * @param x
	 *            the x-component of the rotation axis
	 * @param y
	 *            the y-component of the rotation axis
	 * @param z
	 *            the z-component of the rotation axis
	 * @return this
	 */
	public Matrix4klf rotation(float angle, float x, float y, float z) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float C = 1.0f - cos;
		float xy = x * y, xz = x * z, yz = y * z;
		m00 = cos + x * x * C;
		m10 = xy * C - z * sin;
		m20 = xz * C + y * sin;
		m30 = 0l;
		m01 = xy * C + z * sin;
		m11 = cos + y * y * C;
		m21 = yz * C - x * sin;
		m31 = 0l;
		m02 = xz * C - y * sin;
		m12 = yz * C + x * sin;
		m22 = cos + z * z * C;
		m32 = 0l;
		m03 = 0.0f;
		m13 = 0.0f;
		m23 = 0.0f;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set this matrix to a rotation transformation about the X axis.
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">
	 * http://en.wikipedia.org</a>
	 * 
	 * @param ang
	 *            the angle in radians
	 * @return this
	 */
	public Matrix4klf rotationX(float ang) {
		float sin, cos;
		if (ang == (float) Math.PI || ang == -(float) Math.PI) {
			cos = -1.0f;
			sin = 0.0f;
		} else if (ang == (float) Math.PI * 0.5f || ang == -(float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = 1.0f;
		} else if (ang == (float) -Math.PI * 0.5f || ang == (float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = -1.0f;
		} else {
			cos = (float) Math.cos(ang);
			sin = (float) Math.sin(ang);
		}
		m00 = 1.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = cos;
		m12 = sin;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = -sin;
		m22 = cos;
		m23 = 0.0f;
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set this matrix to a rotation transformation about the Y axis.
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">
	 * http://en.wikipedia.org</a>
	 * 
	 * @param ang
	 *            the angle in radians
	 * @return this
	 */
	public Matrix4klf rotationY(float ang) {
		float sin, cos;
		if (ang == (float) Math.PI || ang == -(float) Math.PI) {
			cos = -1.0f;
			sin = 0.0f;
		} else if (ang == (float) Math.PI * 0.5f || ang == -(float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = 1.0f;
		} else if (ang == (float) -Math.PI * 0.5f || ang == (float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = -1.0f;
		} else {
			cos = (float) Math.cos(ang);
			sin = (float) Math.sin(ang);
		}
		m00 = cos;
		m01 = 0.0f;
		m02 = -sin;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = 1.0f;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = sin;
		m21 = 0.0f;
		m22 = cos;
		m23 = 0.0f;
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set this matrix to a rotation transformation about the Z axis.
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">
	 * http://en.wikipedia.org</a>
	 * 
	 * @param ang
	 *            the angle in radians
	 * @return this
	 */
	public Matrix4klf rotationZ(float ang) {
		float sin, cos;
		if (ang == (float) Math.PI || ang == -(float) Math.PI) {
			cos = -1.0f;
			sin = 0.0f;
		} else if (ang == (float) Math.PI * 0.5f || ang == -(float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = 1.0f;
		} else if (ang == (float) -Math.PI * 0.5f || ang == (float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = -1.0f;
		} else {
			cos = (float) Math.cos(ang);
			sin = (float) Math.sin(ang);
		}
		m00 = cos;
		m01 = sin;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = -sin;
		m11 = cos;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 1.0f;
		m23 = 0.0f;
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set this matrix to a rotation of <code>angleX</code> radians about the X
	 * axis, followed by a rotation of <code>angleY</code> radians about the Y
	 * axis and followed by a rotation of <code>angleZ</code> radians about the
	 * Z axis.
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>rotationX(angleX).rotateY(angleY).rotateZ(angleZ)</tt>
	 * 
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @return this
	 */
	public Matrix4klf rotationXYZ(float angleX, float angleY, float angleZ) {
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float m_sinX = -sinX;
		float m_sinY = -sinY;
		float m_sinZ = -sinZ;

		// rotateX
		float nm11 = cosX;
		float nm12 = sinX;
		float nm21 = m_sinX;
		float nm22 = cosX;
		// rotateY
		float nm00 = cosY;
		float nm01 = nm21 * m_sinY;
		float nm02 = nm22 * m_sinY;
		m20 = sinY;
		m21 = nm21 * cosY;
		m22 = nm22 * cosY;
		m23 = 0.0f;
		// rotateZ
		m00 = nm00 * cosZ;
		m01 = nm01 * cosZ + nm11 * sinZ;
		m02 = nm02 * cosZ + nm12 * sinZ;
		m03 = 0.0f;
		m10 = nm00 * m_sinZ;
		m11 = nm01 * m_sinZ + nm11 * cosZ;
		m12 = nm02 * m_sinZ + nm12 * cosZ;
		m13 = 0.0f;
		// set last column to identity
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set this matrix to a rotation of <code>angleZ</code> radians about the Z
	 * axis, followed by a rotation of <code>angleY</code> radians about the Y
	 * axis and followed by a rotation of <code>angleX</code> radians about the
	 * X axis.
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>rotationZ(angleZ).rotateY(angleY).rotateX(angleX)</tt>
	 * 
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @return this
	 */
	public Matrix4klf rotationZYX(float angleZ, float angleY, float angleX) {
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float m_sinZ = -sinZ;
		float m_sinY = -sinY;
		float m_sinX = -sinX;

		// rotateZ
		float nm00 = cosZ;
		float nm01 = sinZ;
		float nm10 = m_sinZ;
		float nm11 = cosZ;
		// rotateY
		float nm20 = nm00 * sinY;
		float nm21 = nm01 * sinY;
		float nm22 = cosY;
		m00 = nm00 * cosY;
		m01 = nm01 * cosY;
		m02 = m_sinY;
		m03 = 0.0f;
		// rotateX
		m10 = nm10 * cosX + nm20 * sinX;
		m11 = nm11 * cosX + nm21 * sinX;
		m12 = nm22 * sinX;
		m13 = 0.0f;
		m20 = nm10 * m_sinX + nm20 * cosX;
		m21 = nm11 * m_sinX + nm21 * cosX;
		m22 = nm22 * cosX;
		m23 = 0.0f;
		// set last column to identity
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set this matrix to a rotation of <code>angleY</code> radians about the Y
	 * axis, followed by a rotation of <code>angleX</code> radians about the X
	 * axis and followed by a rotation of <code>angleZ</code> radians about the
	 * Z axis.
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>rotationY(angleY).rotateX(angleX).rotateZ(angleZ)</tt>
	 * 
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @return this
	 */
	public Matrix4klf rotationYXZ(float angleY, float angleX, float angleZ) {
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float m_sinY = -sinY;
		float m_sinX = -sinX;
		float m_sinZ = -sinZ;

		// rotateY
		float nm00 = cosY;
		float nm02 = m_sinY;
		float nm20 = sinY;
		float nm22 = cosY;
		// rotateX
		float nm10 = nm20 * sinX;
		float nm11 = cosX;
		float nm12 = nm22 * sinX;
		m20 = nm20 * cosX;
		m21 = m_sinX;
		m22 = nm22 * cosX;
		m23 = 0.0f;
		// rotateZ
		m00 = nm00 * cosZ + nm10 * sinZ;
		m01 = nm11 * sinZ;
		m02 = nm02 * cosZ + nm12 * sinZ;
		m03 = 0.0f;
		m10 = nm00 * m_sinZ + nm10 * cosZ;
		m11 = nm11 * cosZ;
		m12 = nm02 * m_sinZ + nm12 * cosZ;
		m13 = 0.0f;
		// set last column to identity
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set only the upper left 3x3 submatrix of this matrix to a rotation of
	 * <code>angleX</code> radians about the X axis, followed by a rotation of
	 * <code>angleY</code> radians about the Y axis and followed by a rotation
	 * of <code>angleZ</code> radians about the Z axis.
	 * 
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @return this
	 */
	public Matrix4klf setRotationXYZ(float angleX, float angleY, float angleZ) {
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float m_sinX = -sinX;
		float m_sinY = -sinY;
		float m_sinZ = -sinZ;

		// rotateX
		float nm11 = cosX;
		float nm12 = sinX;
		float nm21 = m_sinX;
		float nm22 = cosX;
		// rotateY
		float nm00 = cosY;
		float nm01 = nm21 * m_sinY;
		float nm02 = nm22 * m_sinY;
		m20 = sinY;
		m21 = nm21 * cosY;
		m22 = nm22 * cosY;
		// rotateZ
		m00 = nm00 * cosZ;
		m01 = nm01 * cosZ + nm11 * sinZ;
		m02 = nm02 * cosZ + nm12 * sinZ;
		m10 = nm00 * m_sinZ;
		m11 = nm01 * m_sinZ + nm11 * cosZ;
		m12 = nm02 * m_sinZ + nm12 * cosZ;
		return this;
	}

	/**
	 * Set only the upper left 3x3 submatrix of this matrix to a rotation of
	 * <code>angleZ</code> radians about the Z axis, followed by a rotation of
	 * <code>angleY</code> radians about the Y axis and followed by a rotation
	 * of <code>angleX</code> radians about the X axis.
	 * 
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @return this
	 */
	public Matrix4klf setRotationZYX(float angleZ, float angleY, float angleX) {
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float m_sinZ = -sinZ;
		float m_sinY = -sinY;
		float m_sinX = -sinX;

		// rotateZ
		float nm00 = cosZ;
		float nm01 = sinZ;
		float nm10 = m_sinZ;
		float nm11 = cosZ;
		// rotateY
		float nm20 = nm00 * sinY;
		float nm21 = nm01 * sinY;
		float nm22 = cosY;
		m00 = nm00 * cosY;
		m01 = nm01 * cosY;
		m02 = m_sinY;
		// rotateX
		m10 = nm10 * cosX + nm20 * sinX;
		m11 = nm11 * cosX + nm21 * sinX;
		m12 = nm22 * sinX;
		m20 = nm10 * m_sinX + nm20 * cosX;
		m21 = nm11 * m_sinX + nm21 * cosX;
		m22 = nm22 * cosX;
		return this;
	}

	/**
	 * Set only the upper left 3x3 submatrix of this matrix to a rotation of
	 * <code>angleY</code> radians about the Y axis, followed by a rotation of
	 * <code>angleX</code> radians about the X axis and followed by a rotation
	 * of <code>angleZ</code> radians about the Z axis.
	 * 
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @return this
	 */
	public Matrix4klf setRotationYXZ(float angleY, float angleX, float angleZ) {
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float m_sinY = -sinY;
		float m_sinX = -sinX;
		float m_sinZ = -sinZ;

		// rotateY
		float nm00 = cosY;
		float nm02 = m_sinY;
		float nm20 = sinY;
		float nm22 = cosY;
		// rotateX
		float nm10 = nm20 * sinX;
		float nm11 = cosX;
		float nm12 = nm22 * sinX;
		m20 = nm20 * cosX;
		m21 = m_sinX;
		m22 = nm22 * cosX;
		// rotateZ
		m00 = nm00 * cosZ + nm10 * sinZ;
		m01 = nm11 * sinZ;
		m02 = nm02 * cosZ + nm12 * sinZ;
		m10 = nm00 * m_sinZ + nm10 * cosZ;
		m11 = nm11 * cosZ;
		m12 = nm02 * m_sinZ + nm12 * cosZ;
		return this;
	}

	/**
	 * Set this matrix to the rotation transformation of the given
	 * {@link Quaternionf}.
	 * <p>
	 * The resulting matrix can be multiplied against another transformation
	 * matrix to obtain an additional rotation.
	 * <p>
	 * In order to apply the rotation transformation to an existing
	 * transformation, use {@link #rotate(Quaternionf) rotate()} instead.
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Quaternion">http://
	 * en.wikipedia.org</a>
	 * 
	 * @see #rotate(Quaternionf)
	 * 
	 * @param quat
	 *            the {@link Quaternionf}
	 * @return this
	 */
	public Matrix4klf rotation(Quaternionf quat) {
		float dqx = quat.x + quat.x;
		float dqy = quat.y + quat.y;
		float dqz = quat.z + quat.z;
		float q00 = dqx * quat.x;
		float q11 = dqy * quat.y;
		float q22 = dqz * quat.z;
		float q01 = dqx * quat.y;
		float q02 = dqx * quat.z;
		float q03 = dqx * quat.w;
		float q12 = dqy * quat.z;
		float q13 = dqy * quat.w;
		float q23 = dqz * quat.w;

		m00 = 1.0f - q11 - q22;
		m01 = q01 + q23;
		m02 = q02 - q13;
		m03 = 0.0f;
		m10 = q01 - q23;
		m11 = 1.0f - q22 - q00;
		m12 = q12 + q03;
		m13 = 0.0f;
		m20 = q02 + q13;
		m21 = q12 - q03;
		m22 = 1.0f - q11 - q00;
		m23 = 0.0f;
		m30 = 0l;
		m31 = 0l;
		m32 = 0l;
		m33 = 1.0f;

		return this;
	}

	/**
	 * Set <code>this</code> matrix to <tt>T * R * S</tt>, where <tt>T</tt> is a
	 * translation by the given <tt>(tx, ty, tz)</tt>, <tt>R</tt> is a rotation
	 * transformation specified by the quaternion <tt>(qx, qy, qz, qw)</tt>, and
	 * <tt>S</tt> is a scaling transformation which scales the three axes x, y
	 * and z by <tt>(sx, sy, sz)</tt>.
	 * <p>
	 * When transforming a vector by the resulting matrix the scaling
	 * transformation will be applied first, then the rotation and at last the
	 * translation.
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>translation(tx, ty, tz).rotate(quat).scale(sx, sy, sz)</tt>
	 * 
	 * @see #translation(float, float, float)
	 * @see #rotate(Quaternionf)
	 * @see #scale(float, float, float)
	 * 
	 * @param tx
	 *            the number of units by which to translate the x-component
	 * @param ty
	 *            the number of units by which to translate the y-component
	 * @param tz
	 *            the number of units by which to translate the z-component
	 * @param qx
	 *            the x-coordinate of the vector part of the quaternion
	 * @param qy
	 *            the y-coordinate of the vector part of the quaternion
	 * @param qz
	 *            the z-coordinate of the vector part of the quaternion
	 * @param qw
	 *            the scalar part of the quaternion
	 * @param sx
	 *            the scaling factor for the x-axis
	 * @param sy
	 *            the scaling factor for the y-axis
	 * @param sz
	 *            the scaling factor for the z-axis
	 * @return this
	 */
	public Matrix4klf translateRotateScale(long tx, long ty, long tz, float qx, float qy, float qz, float qw, float sx,
			float sy, float sz) {
		float dqx = qx + qx;
		float dqy = qy + qy;
		float dqz = qz + qz;
		float q00 = dqx * qx;
		float q11 = dqy * qy;
		float q22 = dqz * qz;
		float q01 = dqx * qy;
		float q02 = dqx * qz;
		float q03 = dqx * qw;
		float q12 = dqy * qz;
		float q13 = dqy * qw;
		float q23 = dqz * qw;
		m00 = sx - (q11 + q22) * sx;
		m01 = (q01 + q23) * sx;
		m02 = (q02 - q13) * sx;
		m03 = 0.0f;
		m10 = (q01 - q23) * sy;
		m11 = sy - (q22 + q00) * sy;
		m12 = (q12 + q03) * sy;
		m13 = 0.0f;
		m20 = (q02 + q13) * sz;
		m21 = (q12 - q03) * sz;
		m22 = sz - (q11 + q00) * sz;
		m23 = 0.0f;
		m30 = tx;
		m31 = ty;
		m32 = tz;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set <code>this</code> matrix to <tt>T * R * S</tt>, where <tt>T</tt> is a
	 * translation by the given <tt>(tx, ty, tz)</tt>, <tt>R</tt> is a rotation
	 * transformation specified by the quaternion <tt>(qx, qy, qz, qw)</tt>, and
	 * <tt>S</tt> is a scaling transformation which scales the three axes x, y
	 * and z by <tt>(sx, sy, sz)</tt>.
	 * <p>
	 * When transforming a vector by the resulting matrix the scaling
	 * transformation will be applied first, then the rotation and at last the
	 * translation.
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>translation(tx, ty, tz).rotate(quat).scale(sx, sy, sz)</tt>
	 * 
	 * @see #translation(float, float, float)
	 * @see #rotate(Quaternionf)
	 * @see #scale(float, float, float)
	 * 
	 * @param tx
	 *            the number of units by which to translate the x-component
	 * @param ty
	 *            the number of units by which to translate the y-component
	 * @param tz
	 *            the number of units by which to translate the z-component
	 * @param qx
	 *            the x-coordinate of the vector part of the quaternion
	 * @param qy
	 *            the y-coordinate of the vector part of the quaternion
	 * @param qz
	 *            the z-coordinate of the vector part of the quaternion
	 * @param qw
	 *            the scalar part of the quaternion
	 * @param sx
	 *            the scaling factor for the x-axis
	 * @param sy
	 *            the scaling factor for the y-axis
	 * @param sz
	 *            the scaling factor for the z-axis
	 * @return this
	 */
	public Matrix4klf translateRotateScalef(float tx, float ty, float tz, float qx, float qy, float qz, float qw,
			float sx, float sy, float sz) {
		float dqx = qx + qx;
		float dqy = qy + qy;
		float dqz = qz + qz;
		float q00 = dqx * qx;
		float q11 = dqy * qy;
		float q22 = dqz * qz;
		float q01 = dqx * qy;
		float q02 = dqx * qz;
		float q03 = dqx * qw;
		float q12 = dqy * qz;
		float q13 = dqy * qw;
		float q23 = dqz * qw;
		m00 = sx - (q11 + q22) * sx;
		m01 = (q01 + q23) * sx;
		m02 = (q02 - q13) * sx;
		m03 = 0.0f;
		m10 = (q01 - q23) * sy;
		m11 = sy - (q22 + q00) * sy;
		m12 = (q12 + q03) * sy;
		m13 = 0.0f;
		m20 = (q02 + q13) * sz;
		m21 = (q12 - q03) * sz;
		m22 = sz - (q11 + q00) * sz;
		m23 = 0.0f;
		m30 = (long) (tx * meter);
		m31 = (long) (ty * meter);
		m32 = (long) (tz * meter);
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set <code>this</code> matrix to <tt>T * R</tt>, where <tt>T</tt> is a
	 * translation by the given <tt>(tx, ty, tz)</tt> and <tt>R</tt> is a
	 * rotation transformation specified by the given quaternion.
	 * <p>
	 * When transforming a vector by the resulting matrix the rotation
	 * transformation will be applied first and then the translation.
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>translation(tx, ty, tz).rotate(quat)</tt>
	 * 
	 * @see #translation(float, float, float)
	 * @see #rotate(Quaternionf)
	 * 
	 * @param tx
	 *            the number of units by which to translate the x-component
	 * @param ty
	 *            the number of units by which to translate the y-component
	 * @param tz
	 *            the number of units by which to translate the z-component
	 * @param quat
	 *            the quaternion representing a rotation
	 * @return this
	 */
	public Matrix4klf translateRotatef(float tx, float ty, float tz, Quaternionf quat) {
		float dqx = quat.x + quat.x;
		float dqy = quat.y + quat.y;
		float dqz = quat.z + quat.z;
		float q00 = dqx * quat.x;
		float q11 = dqy * quat.y;
		float q22 = dqz * quat.z;
		float q01 = dqx * quat.y;
		float q02 = dqx * quat.z;
		float q03 = dqx * quat.w;
		float q12 = dqy * quat.z;
		float q13 = dqy * quat.w;
		float q23 = dqz * quat.w;
		m00 = 1.0f - (q11 + q22);
		m01 = q01 + q23;
		m02 = q02 - q13;
		m03 = 0.0f;
		m10 = q01 - q23;
		m11 = 1.0f - (q22 + q00);
		m12 = q12 + q03;
		m13 = 0.0f;
		m20 = q02 + q13;
		m21 = q12 - q03;
		m22 = 1.0f - (q11 + q00);
		m23 = 0.0f;
		m30 = (long) (tx * meter);
		m31 = (long) (ty * meter);
		m32 = (long) (tz * meter);
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set <code>this</code> matrix to <tt>T * R</tt>, where <tt>T</tt> is a
	 * translation by the given <tt>(tx, ty, tz)</tt> and <tt>R</tt> is a
	 * rotation transformation specified by the given quaternion.
	 * <p>
	 * When transforming a vector by the resulting matrix the rotation
	 * transformation will be applied first and then the translation.
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>translation(tx, ty, tz).rotate(quat)</tt>
	 * 
	 * @see #translation(float, float, float)
	 * @see #rotate(Quaternionf)
	 * 
	 * @param tx
	 *            the number of units by which to translate the x-component
	 * @param ty
	 *            the number of units by which to translate the y-component
	 * @param tz
	 *            the number of units by which to translate the z-component
	 * @param quat
	 *            the quaternion representing a rotation
	 * @return this
	 */
	public Matrix4klf translateRotate(long tx, long ty, long tz, Quaternionf quat) {
		float dqx = quat.x + quat.x;
		float dqy = quat.y + quat.y;
		float dqz = quat.z + quat.z;
		float q00 = dqx * quat.x;
		float q11 = dqy * quat.y;
		float q22 = dqz * quat.z;
		float q01 = dqx * quat.y;
		float q02 = dqx * quat.z;
		float q03 = dqx * quat.w;
		float q12 = dqy * quat.z;
		float q13 = dqy * quat.w;
		float q23 = dqz * quat.w;
		m00 = 1.0f - (q11 + q22);
		m01 = q01 + q23;
		m02 = q02 - q13;
		m03 = 0.0f;
		m10 = q01 - q23;
		m11 = 1.0f - (q22 + q00);
		m12 = q12 + q03;
		m13 = 0.0f;
		m20 = q02 + q13;
		m21 = q12 - q03;
		m22 = 1.0f - (q11 + q00);
		m23 = 0.0f;
		m30 = tx;
		m31 = ty;
		m32 = tz;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Set the upper 3x3 matrix of this {@link Matrix4klf} to the given
	 * {@link Matrix3f} and the rest to the identity.
	 * 
	 * @param mat
	 *            the 3x3 matrix
	 * @return this
	 */
	public Matrix4klf set3x3(Matrix3f mat) {
		m00 = mat.m00;
		m01 = mat.m01;
		m02 = mat.m02;
		m03 = 0.0f;
		m10 = mat.m10;
		m11 = mat.m11;
		m12 = mat.m12;
		m13 = 0.0f;
		m20 = mat.m20;
		m21 = mat.m21;
		m22 = mat.m22;
		m23 = 0.0f;
		m33 = 1.0f;
		return this;
	}

	/**
	 * Transform/multiply the given 3D-vector, as if it was a 4D-vector with
	 * w=1, by this matrix and store the result in that vector.
	 * <p>
	 * The given 3D-vector is treated as a 4D-vector with its w-component being
	 * 1.0, so it will represent a position/location in 3D-space rather than a
	 * direction. This method is therefore not suited for perspective projection
	 * transformations as it will not save the <tt>w</tt> component of the
	 * transformed vector. For perspective projection use
	 * {@link #transform(Vector4f)} or {@link #transformProject(Vector3f)} when
	 * perspective divide should be applied, too.
	 * <p>
	 * In order to store the result in another vector, use
	 * {@link #transformPosition(Vector3f, Vector3f)}.
	 * 
	 * @see #transformPosition(Vector3f, Vector3f)
	 * @see #transform(Vector4f)
	 * @see #transformProject(Vector3f)
	 * 
	 * @param v
	 *            the vector to transform and to hold the final result
	 * @return v
	 */
	public Vector3f transformf(Vector3f v) {
		v.set(m00 * v.x + m10 * v.y + m20 * v.z + m30, m01 * v.x + m11 * v.y + m21 * v.z + m31,
				m02 * v.x + m12 * v.y + m22 * v.z + m32);
		return v;
	}

	/**
	 * Transform/multiply the given 3D-vector, as if it was a 4D-vector with
	 * w=1, by this matrix and store the result in <code>dest</code>.
	 * <p>
	 * The given 3D-vector is treated as a 4D-vector with its w-component being
	 * 1.0, so it will represent a position/location in 3D-space rather than a
	 * direction. This method is therefore not suited for perspective projection
	 * transformations as it will not save the <tt>w</tt> component of the
	 * transformed vector. For perspective projection use
	 * {@link #transform(Vector4f, Vector4f)} or
	 * {@link #transformProject(Vector3f, Vector3f)} when perspective divide
	 * should be applied, too.
	 * <p>
	 * In order to store the result in the same vector, use
	 * {@link #transformPosition(Vector3f)}.
	 * 
	 * @see #transformPosition(Vector3f)
	 * @see #transform(Vector4f, Vector4f)
	 * @see #transformProject(Vector3f, Vector3f)
	 * 
	 * @param v
	 *            the vector to transform
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Vector3f transformf(Vector3f v, Vector3f dest) {
		dest.set(m00 * v.x + m10 * v.y + m20 * v.z + ((float) m30) / meter, m01 * v.x + m11 * v.y + m21 * v.z + ((float) m31) / meter,
				m02 * v.x + m12 * v.y + m22 * v.z + ((float) m32) / meter);
		return dest;
	}

	/**
	 * Transform/multiply the given 3D-vector, as if it was a 4D-vector with
	 * w=0, by this matrix and store the result in that vector.
	 * <p>
	 * The given 3D-vector is treated as a 4D-vector with its w-component being
	 * <tt>0.0</tt>, so it will represent a direction in 3D-space rather than a
	 * position. This method will therefore not take the translation part of the
	 * matrix into account.
	 * <p>
	 * In order to store the result in another vector, use
	 * {@link #transformDirection(Vector3f, Vector3f)}.
	 * 
	 * @see #transformDirection(Vector3f, Vector3f)
	 * 
	 * @param v
	 *            the vector to transform and to hold the final result
	 * @return v
	 */
	public Vector3f transformDir(Vector3f v) {
		v.set(m00 * v.x + m10 * v.y + m20 * v.z, m01 * v.x + m11 * v.y + m21 * v.z, m02 * v.x + m12 * v.y + m22 * v.z);
		return v;
	}

	/**
	 * Transform/multiply the given 3D-vector, as if it was a 4D-vector with
	 * w=0, by this matrix and store the result in <code>dest</code>.
	 * <p>
	 * The given 3D-vector is treated as a 4D-vector with its w-component being
	 * <tt>0.0</tt>, so it will represent a direction in 3D-space rather than a
	 * position. This method will therefore not take the translation part of the
	 * matrix into account.
	 * <p>
	 * In order to store the result in the same vector, use
	 * {@link #transformDirection(Vector3f)}.
	 * 
	 * @see #transformDirection(Vector3f)
	 * 
	 * @param v
	 *            the vector to transform and to hold the final result
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Vector3f transformDir(Vector3f v, Vector3f dest) {
		dest.set(m00 * v.x + m10 * v.y + m20 * v.z, m01 * v.x + m11 * v.y + m21 * v.z,
				m02 * v.x + m12 * v.y + m22 * v.z);
		return dest;
	}

	/**
	 * Apply scaling to the this matrix by scaling the base axes by the given
	 * <tt>xyz.x</tt>, <tt>xyz.y</tt> and <tt>xyz.z</tt> factors, respectively
	 * and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>S</code> the
	 * scaling matrix, then the new matrix will be <code>M * S</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * S * v</code> , the scaling will be applied first!
	 * 
	 * @param xyz
	 *            the factors of the x, y and z component, respectively
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf scale(Vector3f xyz, Matrix4klf dest) {
		return scale(xyz.x, xyz.y, xyz.z, dest);
	}

	/**
	 * Apply scaling to this matrix by scaling the base axes by the given
	 * <tt>xyz.x</tt>, <tt>xyz.y</tt> and <tt>xyz.z</tt> factors, respectively.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>S</code> the
	 * scaling matrix, then the new matrix will be <code>M * S</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * S * v</code>, the scaling will be applied first!
	 * 
	 * @param xyz
	 *            the factors of the x, y and z component, respectively
	 * @return this
	 */
	public Matrix4klf scale(Vector3f xyz) {
		return scale(xyz.x, xyz.y, xyz.z, this);
	}

	/**
	 * Apply scaling to this matrix by uniformly scaling all base axes by the
	 * given <code>xyz</code> factor and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>S</code> the
	 * scaling matrix, then the new matrix will be <code>M * S</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * S * v</code>, the scaling will be applied first!
	 * <p>
	 * Individual scaling of all three axes can be applied using
	 * {@link #scale(float, float, float, Matrix4klf)}.
	 * 
	 * @see #scale(float, float, float, Matrix4klf)
	 * 
	 * @param xyz
	 *            the factor for all components
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf scale(float xyz, Matrix4klf dest) {
		return scale(xyz, xyz, xyz, dest);
	}

	/**
	 * Apply scaling to this matrix by uniformly scaling all base axes by the
	 * given <code>xyz</code> factor.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>S</code> the
	 * scaling matrix, then the new matrix will be <code>M * S</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * S * v</code>, the scaling will be applied first!
	 * <p>
	 * Individual scaling of all three axes can be applied using
	 * {@link #scale(float, float, float)}.
	 * 
	 * @see #scale(float, float, float)
	 * 
	 * @param xyz
	 *            the factor for all components
	 * @return this
	 */
	public Matrix4klf scale(float xyz) {
		return scale(xyz, xyz, xyz);
	}

	/**
	 * Apply scaling to the this matrix by scaling the base axes by the given x,
	 * y and z factors and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>S</code> the
	 * scaling matrix, then the new matrix will be <code>M * S</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * S * v</code> , the scaling will be applied first!
	 * 
	 * @param x
	 *            the factor of the x component
	 * @param y
	 *            the factor of the y component
	 * @param z
	 *            the factor of the z component
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf scale(float x, float y, float z, Matrix4klf dest) {
		// scale matrix elements:
		// m00 = x, m11 = y, m22 = z
		// m33 = 1
		// all others = 0
		dest.m00 = m00 * x;
		dest.m01 = m01 * x;
		dest.m02 = m02 * x;
		dest.m03 = m03 * x;
		dest.m10 = m10 * y;
		dest.m11 = m11 * y;
		dest.m12 = m12 * y;
		dest.m13 = m13 * y;
		dest.m20 = m20 * z;
		dest.m21 = m21 * z;
		dest.m22 = m22 * z;
		dest.m23 = m23 * z;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Apply scaling to this matrix by scaling the base axes by the given x, y
	 * and z factors.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>S</code> the
	 * scaling matrix, then the new matrix will be <code>M * S</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * S * v</code>, the scaling will be applied first!
	 * 
	 * @param x
	 *            the factor of the x component
	 * @param y
	 *            the factor of the y component
	 * @param z
	 *            the factor of the z component
	 * @return this
	 */
	public Matrix4klf scale(float x, float y, float z) {
		return scale(x, y, z, this);
	}

	/**
	 * Apply rotation about the X axis to this matrix by rotating the given
	 * amount of radians and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">
	 * http://en.wikipedia.org</a>
	 * 
	 * @param ang
	 *            the angle in radians
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotateX(float ang, Matrix4klf dest) {
		float sin, cos;
		if (ang == (float) Math.PI || ang == -(float) Math.PI) {
			cos = -1.0f;
			sin = 0.0f;
		} else if (ang == (float) Math.PI * 0.5f || ang == -(float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = 1.0f;
		} else if (ang == (float) -Math.PI * 0.5f || ang == (float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = -1.0f;
		} else {
			cos = (float) Math.cos(ang);
			sin = (float) Math.sin(ang);
		}
		float rm11 = cos;
		float rm12 = sin;
		float rm21 = -sin;
		float rm22 = cos;

		// add temporaries for dependent values
		float nm10 = m10 * rm11 + m20 * rm12;
		float nm11 = m11 * rm11 + m21 * rm12;
		float nm12 = m12 * rm11 + m22 * rm12;
		float nm13 = m13 * rm11 + m23 * rm12;
		// set non-dependent values directly
		dest.m20 = m10 * rm21 + m20 * rm22;
		dest.m21 = m11 * rm21 + m21 * rm22;
		dest.m22 = m12 * rm21 + m22 * rm22;
		dest.m23 = m13 * rm21 + m23 * rm22;
		// set other values
		dest.m10 = nm10;
		dest.m11 = nm11;
		dest.m12 = nm12;
		dest.m13 = nm13;
		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m03 = m03;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Apply rotation about the X axis to this matrix by rotating the given
	 * amount of radians.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">
	 * http://en.wikipedia.org</a>
	 * 
	 * @param ang
	 *            the angle in radians
	 * @return this
	 */
	public Matrix4klf rotateX(float ang) {
		return rotateX(ang, this);
	}

	/**
	 * Apply rotation about the Y axis to this matrix by rotating the given
	 * amount of radians and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">
	 * http://en.wikipedia.org</a>
	 * 
	 * @param ang
	 *            the angle in radians
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotateY(float ang, Matrix4klf dest) {
		float cos, sin;
		if (ang == (float) Math.PI || ang == -(float) Math.PI) {
			cos = -1.0f;
			sin = 0.0f;
		} else if (ang == (float) Math.PI * 0.5f || ang == -(float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = 1.0f;
		} else if (ang == (float) -Math.PI * 0.5f || ang == (float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = -1.0f;
		} else {
			cos = (float) Math.cos(ang);
			sin = (float) Math.sin(ang);
		}
		float rm00 = cos;
		float rm02 = -sin;
		float rm20 = sin;
		float rm22 = cos;

		// add temporaries for dependent values
		float nm00 = m00 * rm00 + m20 * rm02;
		float nm01 = m01 * rm00 + m21 * rm02;
		float nm02 = m02 * rm00 + m22 * rm02;
		float nm03 = m03 * rm00 + m23 * rm02;
		// set non-dependent values directly
		dest.m20 = m00 * rm20 + m20 * rm22;
		dest.m21 = m01 * rm20 + m21 * rm22;
		dest.m22 = m02 * rm20 + m22 * rm22;
		dest.m23 = m03 * rm20 + m23 * rm22;
		// set other values
		dest.m00 = nm00;
		dest.m01 = nm01;
		dest.m02 = nm02;
		dest.m03 = nm03;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m13 = m13;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Apply rotation about the Y axis to this matrix by rotating the given
	 * amount of radians.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">
	 * http://en.wikipedia.org</a>
	 * 
	 * @param ang
	 *            the angle in radians
	 * @return this
	 */
	public Matrix4klf rotateY(float ang) {
		return rotateY(ang, this);
	}

	/**
	 * Apply rotation about the Z axis to this matrix by rotating the given
	 * amount of radians and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">
	 * http://en.wikipedia.org</a>
	 * 
	 * @param ang
	 *            the angle in radians
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotateZ(float ang, Matrix4klf dest) {
		float sin, cos;
		if (ang == (float) Math.PI || ang == -(float) Math.PI) {
			cos = -1.0f;
			sin = 0.0f;
		} else if (ang == (float) Math.PI * 0.5f || ang == -(float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = 1.0f;
		} else if (ang == (float) -Math.PI * 0.5f || ang == (float) Math.PI * 1.5f) {
			cos = 0.0f;
			sin = -1.0f;
		} else {
			cos = (float) Math.cos(ang);
			sin = (float) Math.sin(ang);
		}
		float rm00 = cos;
		float rm01 = sin;
		float rm10 = -sin;
		float rm11 = cos;

		// add temporaries for dependent values
		float nm00 = m00 * rm00 + m10 * rm01;
		float nm01 = m01 * rm00 + m11 * rm01;
		float nm02 = m02 * rm00 + m12 * rm01;
		float nm03 = m03 * rm00 + m13 * rm01;
		// set non-dependent values directly
		dest.m10 = m00 * rm10 + m10 * rm11;
		dest.m11 = m01 * rm10 + m11 * rm11;
		dest.m12 = m02 * rm10 + m12 * rm11;
		dest.m13 = m03 * rm10 + m13 * rm11;
		// set other values
		dest.m00 = nm00;
		dest.m01 = nm01;
		dest.m02 = nm02;
		dest.m03 = nm03;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		dest.m23 = m23;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Apply rotation about the Z axis to this matrix by rotating the given
	 * amount of radians.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">
	 * http://en.wikipedia.org</a>
	 * 
	 * @param ang
	 *            the angle in radians
	 * @return this
	 */
	public Matrix4klf rotateZ(float ang) {
		return rotateZ(ang, this);
	}

	/**
	 * Apply rotation of <code>angleX</code> radians about the X axis, followed
	 * by a rotation of <code>angleY</code> radians about the Y axis and
	 * followed by a rotation of <code>angleZ</code> radians about the Z axis.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>rotateX(angleX).rotateY(angleY).rotateZ(angleZ)</tt>
	 * 
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @return this
	 */
	public Matrix4klf rotateXYZ(float angleX, float angleY, float angleZ) {
		return rotateXYZ(angleX, angleY, angleZ, this);
	}

	/**
	 * Apply rotation of <code>angleX</code> radians about the X axis, followed
	 * by a rotation of <code>angleY</code> radians about the Y axis and
	 * followed by a rotation of <code>angleZ</code> radians about the Z axis
	 * and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>rotateX(angleX, dest).rotateY(angleY).rotateZ(angleZ)</tt>
	 * 
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotateXYZ(float angleX, float angleY, float angleZ, Matrix4klf dest) {
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float m_sinX = -sinX;
		float m_sinY = -sinY;
		float m_sinZ = -sinZ;

		// rotateX
		float nm10 = m10 * cosX + m20 * sinX;
		float nm11 = m11 * cosX + m21 * sinX;
		float nm12 = m12 * cosX + m22 * sinX;
		float nm13 = m13 * cosX + m23 * sinX;
		float nm20 = m10 * m_sinX + m20 * cosX;
		float nm21 = m11 * m_sinX + m21 * cosX;
		float nm22 = m12 * m_sinX + m22 * cosX;
		float nm23 = m13 * m_sinX + m23 * cosX;
		// rotateY
		float nm00 = m00 * cosY + nm20 * m_sinY;
		float nm01 = m01 * cosY + nm21 * m_sinY;
		float nm02 = m02 * cosY + nm22 * m_sinY;
		float nm03 = m03 * cosY + nm23 * m_sinY;
		dest.m20 = m00 * sinY + nm20 * cosY;
		dest.m21 = m01 * sinY + nm21 * cosY;
		dest.m22 = m02 * sinY + nm22 * cosY;
		dest.m23 = m03 * sinY + nm23 * cosY;
		// rotateZ
		dest.m00 = nm00 * cosZ + nm10 * sinZ;
		dest.m01 = nm01 * cosZ + nm11 * sinZ;
		dest.m02 = nm02 * cosZ + nm12 * sinZ;
		dest.m03 = nm03 * cosZ + nm13 * sinZ;
		dest.m10 = nm00 * m_sinZ + nm10 * cosZ;
		dest.m11 = nm01 * m_sinZ + nm11 * cosZ;
		dest.m12 = nm02 * m_sinZ + nm12 * cosZ;
		dest.m13 = nm03 * m_sinZ + nm13 * cosZ;
		// copy last column from 'this'
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Apply rotation of <code>angleX</code> radians about the X axis, followed
	 * by a rotation of <code>angleY</code> radians about the Y axis and
	 * followed by a rotation of <code>angleZ</code> radians about the Z axis.
	 * <p>
	 * This method assumes that <code>this</code> matrix represents an
	 * {@link #isAffine() affine} transformation (i.e. its last row is equal to
	 * <tt>(0, 0, 0, 1)</tt>) and can be used to speed up matrix multiplication
	 * if the matrix only represents affine transformations, such as
	 * translation, rotation, scaling and shearing (in any combination).
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>rotateX(angleX).rotateY(angleY).rotateZ(angleZ)</tt>
	 * 
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @return this
	 */
	public Matrix4klf rotateAffineXYZ(float angleX, float angleY, float angleZ) {
		return rotateAffineXYZ(angleX, angleY, angleZ, this);
	}

	/**
	 * Apply rotation of <code>angleX</code> radians about the X axis, followed
	 * by a rotation of <code>angleY</code> radians about the Y axis and
	 * followed by a rotation of <code>angleZ</code> radians about the Z axis
	 * and store the result in <code>dest</code>.
	 * <p>
	 * This method assumes that <code>this</code> matrix represents an
	 * {@link #isAffine() affine} transformation (i.e. its last row is equal to
	 * <tt>(0, 0, 0, 1)</tt>) and can be used to speed up matrix multiplication
	 * if the matrix only represents affine transformations, such as
	 * translation, rotation, scaling and shearing (in any combination).
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * 
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotateAffineXYZ(float angleX, float angleY, float angleZ, Matrix4klf dest) {
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float m_sinX = -sinX;
		float m_sinY = -sinY;
		float m_sinZ = -sinZ;

		// rotateX
		float nm10 = m10 * cosX + m20 * sinX;
		float nm11 = m11 * cosX + m21 * sinX;
		float nm12 = m12 * cosX + m22 * sinX;
		float nm20 = m10 * m_sinX + m20 * cosX;
		float nm21 = m11 * m_sinX + m21 * cosX;
		float nm22 = m12 * m_sinX + m22 * cosX;
		// rotateY
		float nm00 = m00 * cosY + nm20 * m_sinY;
		float nm01 = m01 * cosY + nm21 * m_sinY;
		float nm02 = m02 * cosY + nm22 * m_sinY;
		dest.m20 = m00 * sinY + nm20 * cosY;
		dest.m21 = m01 * sinY + nm21 * cosY;
		dest.m22 = m02 * sinY + nm22 * cosY;
		dest.m23 = 0.0f;
		// rotateZ
		dest.m00 = nm00 * cosZ + nm10 * sinZ;
		dest.m01 = nm01 * cosZ + nm11 * sinZ;
		dest.m02 = nm02 * cosZ + nm12 * sinZ;
		dest.m03 = 0.0f;
		dest.m10 = nm00 * m_sinZ + nm10 * cosZ;
		dest.m11 = nm01 * m_sinZ + nm11 * cosZ;
		dest.m12 = nm02 * m_sinZ + nm12 * cosZ;
		dest.m13 = 0.0f;
		// copy last column from 'this'
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Apply rotation of <code>angleZ</code> radians about the Z axis, followed
	 * by a rotation of <code>angleY</code> radians about the Y axis and
	 * followed by a rotation of <code>angleX</code> radians about the X axis.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>rotateZ(angleZ).rotateY(angleY).rotateX(angleX)</tt>
	 * 
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @return this
	 */
	public Matrix4klf rotateZYX(float angleZ, float angleY, float angleX) {
		return rotateZYX(angleZ, angleY, angleX, this);
	}

	/**
	 * Apply rotation of <code>angleZ</code> radians about the Z axis, followed
	 * by a rotation of <code>angleY</code> radians about the Y axis and
	 * followed by a rotation of <code>angleX</code> radians about the X axis
	 * and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>rotateZ(angleZ, dest).rotateY(angleY).rotateX(angleX)</tt>
	 * 
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotateZYX(float angleZ, float angleY, float angleX, Matrix4klf dest) {
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float m_sinZ = -sinZ;
		float m_sinY = -sinY;
		float m_sinX = -sinX;

		// rotateZ
		float nm00 = m00 * cosZ + m10 * sinZ;
		float nm01 = m01 * cosZ + m11 * sinZ;
		float nm02 = m02 * cosZ + m12 * sinZ;
		float nm03 = m03 * cosZ + m13 * sinZ;
		float nm10 = m00 * m_sinZ + m10 * cosZ;
		float nm11 = m01 * m_sinZ + m11 * cosZ;
		float nm12 = m02 * m_sinZ + m12 * cosZ;
		float nm13 = m03 * m_sinZ + m13 * cosZ;
		// rotateY
		float nm20 = nm00 * sinY + m20 * cosY;
		float nm21 = nm01 * sinY + m21 * cosY;
		float nm22 = nm02 * sinY + m22 * cosY;
		float nm23 = nm03 * sinY + m23 * cosY;
		dest.m00 = nm00 * cosY + m20 * m_sinY;
		dest.m01 = nm01 * cosY + m21 * m_sinY;
		dest.m02 = nm02 * cosY + m22 * m_sinY;
		dest.m03 = nm03 * cosY + m23 * m_sinY;
		// rotateX
		dest.m10 = nm10 * cosX + nm20 * sinX;
		dest.m11 = nm11 * cosX + nm21 * sinX;
		dest.m12 = nm12 * cosX + nm22 * sinX;
		dest.m13 = nm13 * cosX + nm23 * sinX;
		dest.m20 = nm10 * m_sinX + nm20 * cosX;
		dest.m21 = nm11 * m_sinX + nm21 * cosX;
		dest.m22 = nm12 * m_sinX + nm22 * cosX;
		dest.m23 = nm13 * m_sinX + nm23 * cosX;
		// copy last column from 'this'
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Apply rotation of <code>angleZ</code> radians about the Z axis, followed
	 * by a rotation of <code>angleY</code> radians about the Y axis and
	 * followed by a rotation of <code>angleX</code> radians about the X axis.
	 * <p>
	 * This method assumes that <code>this</code> matrix represents an
	 * {@link #isAffine() affine} transformation (i.e. its last row is equal to
	 * <tt>(0, 0, 0, 1)</tt>) and can be used to speed up matrix multiplication
	 * if the matrix only represents affine transformations, such as
	 * translation, rotation, scaling and shearing (in any combination).
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * 
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @return this
	 */
	public Matrix4klf rotateAffineZYX(float angleZ, float angleY, float angleX) {
		return rotateAffineZYX(angleZ, angleY, angleX, this);
	}

	/**
	 * Apply rotation of <code>angleZ</code> radians about the Z axis, followed
	 * by a rotation of <code>angleY</code> radians about the Y axis and
	 * followed by a rotation of <code>angleX</code> radians about the X axis
	 * and store the result in <code>dest</code>.
	 * <p>
	 * This method assumes that <code>this</code> matrix represents an
	 * {@link #isAffine() affine} transformation (i.e. its last row is equal to
	 * <tt>(0, 0, 0, 1)</tt>) and can be used to speed up matrix multiplication
	 * if the matrix only represents affine transformations, such as
	 * translation, rotation, scaling and shearing (in any combination).
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * 
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotateAffineZYX(float angleZ, float angleY, float angleX, Matrix4klf dest) {
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float m_sinZ = -sinZ;
		float m_sinY = -sinY;
		float m_sinX = -sinX;

		// rotateZ
		float nm00 = m00 * cosZ + m10 * sinZ;
		float nm01 = m01 * cosZ + m11 * sinZ;
		float nm02 = m02 * cosZ + m12 * sinZ;
		float nm10 = m00 * m_sinZ + m10 * cosZ;
		float nm11 = m01 * m_sinZ + m11 * cosZ;
		float nm12 = m02 * m_sinZ + m12 * cosZ;
		// rotateY
		float nm20 = nm00 * sinY + m20 * cosY;
		float nm21 = nm01 * sinY + m21 * cosY;
		float nm22 = nm02 * sinY + m22 * cosY;
		dest.m00 = nm00 * cosY + m20 * m_sinY;
		dest.m01 = nm01 * cosY + m21 * m_sinY;
		dest.m02 = nm02 * cosY + m22 * m_sinY;
		dest.m03 = 0.0f;
		// rotateX
		dest.m10 = nm10 * cosX + nm20 * sinX;
		dest.m11 = nm11 * cosX + nm21 * sinX;
		dest.m12 = nm12 * cosX + nm22 * sinX;
		dest.m13 = 0.0f;
		dest.m20 = nm10 * m_sinX + nm20 * cosX;
		dest.m21 = nm11 * m_sinX + nm21 * cosX;
		dest.m22 = nm12 * m_sinX + nm22 * cosX;
		dest.m23 = 0.0f;
		// copy last column from 'this'
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Apply rotation of <code>angleY</code> radians about the Y axis, followed
	 * by a rotation of <code>angleX</code> radians about the X axis and
	 * followed by a rotation of <code>angleZ</code> radians about the Z axis.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>rotateY(angleY).rotateX(angleX).rotateZ(angleZ)</tt>
	 * 
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @return this
	 */
	public Matrix4klf rotateYXZ(float angleY, float angleX, float angleZ) {
		return rotateYXZ(angleY, angleX, angleZ, this);
	}

	/**
	 * Apply rotation of <code>angleY</code> radians about the Y axis, followed
	 * by a rotation of <code>angleX</code> radians about the X axis and
	 * followed by a rotation of <code>angleZ</code> radians about the Z axis
	 * and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * This method is equivalent to calling:
	 * <tt>rotateY(angleY, dest).rotateX(angleX).rotateZ(angleZ)</tt>
	 * 
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotateYXZ(float angleY, float angleX, float angleZ, Matrix4klf dest) {
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float m_sinY = -sinY;
		float m_sinX = -sinX;
		float m_sinZ = -sinZ;

		// rotateY
		float nm20 = m00 * sinY + m20 * cosY;
		float nm21 = m01 * sinY + m21 * cosY;
		float nm22 = m02 * sinY + m22 * cosY;
		float nm23 = m03 * sinY + m23 * cosY;
		float nm00 = m00 * cosY + m20 * m_sinY;
		float nm01 = m01 * cosY + m21 * m_sinY;
		float nm02 = m02 * cosY + m22 * m_sinY;
		float nm03 = m03 * cosY + m23 * m_sinY;
		// rotateX
		float nm10 = m10 * cosX + nm20 * sinX;
		float nm11 = m11 * cosX + nm21 * sinX;
		float nm12 = m12 * cosX + nm22 * sinX;
		float nm13 = m13 * cosX + nm23 * sinX;
		dest.m20 = m10 * m_sinX + nm20 * cosX;
		dest.m21 = m11 * m_sinX + nm21 * cosX;
		dest.m22 = m12 * m_sinX + nm22 * cosX;
		dest.m23 = m13 * m_sinX + nm23 * cosX;
		// rotateZ
		dest.m00 = nm00 * cosZ + nm10 * sinZ;
		dest.m01 = nm01 * cosZ + nm11 * sinZ;
		dest.m02 = nm02 * cosZ + nm12 * sinZ;
		dest.m03 = nm03 * cosZ + nm13 * sinZ;
		dest.m10 = nm00 * m_sinZ + nm10 * cosZ;
		dest.m11 = nm01 * m_sinZ + nm11 * cosZ;
		dest.m12 = nm02 * m_sinZ + nm12 * cosZ;
		dest.m13 = nm03 * m_sinZ + nm13 * cosZ;
		// copy last column from 'this'
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Apply rotation of <code>angleY</code> radians about the Y axis, followed
	 * by a rotation of <code>angleX</code> radians about the X axis and
	 * followed by a rotation of <code>angleZ</code> radians about the Z axis.
	 * <p>
	 * This method assumes that <code>this</code> matrix represents an
	 * {@link #isAffine() affine} transformation (i.e. its last row is equal to
	 * <tt>(0, 0, 0, 1)</tt>) and can be used to speed up matrix multiplication
	 * if the matrix only represents affine transformations, such as
	 * translation, rotation, scaling and shearing (in any combination).
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * 
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @return this
	 */
	public Matrix4klf rotateAffineYXZ(float angleY, float angleX, float angleZ) {
		return rotateAffineYXZ(angleY, angleX, angleZ, this);
	}

	/**
	 * Apply rotation of <code>angleY</code> radians about the Y axis, followed
	 * by a rotation of <code>angleX</code> radians about the X axis and
	 * followed by a rotation of <code>angleZ</code> radians about the Z axis
	 * and store the result in <code>dest</code>.
	 * <p>
	 * This method assumes that <code>this</code> matrix represents an
	 * {@link #isAffine() affine} transformation (i.e. its last row is equal to
	 * <tt>(0, 0, 0, 1)</tt>) and can be used to speed up matrix multiplication
	 * if the matrix only represents affine transformations, such as
	 * translation, rotation, scaling and shearing (in any combination).
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * 
	 * @param angleY
	 *            the angle to rotate about Y
	 * @param angleX
	 *            the angle to rotate about X
	 * @param angleZ
	 *            the angle to rotate about Z
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotateAffineYXZ(float angleY, float angleX, float angleZ, Matrix4klf dest) {
		float cosY = (float) Math.cos(angleY);
		float sinY = (float) Math.sin(angleY);
		float cosX = (float) Math.cos(angleX);
		float sinX = (float) Math.sin(angleX);
		float cosZ = (float) Math.cos(angleZ);
		float sinZ = (float) Math.sin(angleZ);
		float m_sinY = -sinY;
		float m_sinX = -sinX;
		float m_sinZ = -sinZ;

		// rotateY
		float nm20 = m00 * sinY + m20 * cosY;
		float nm21 = m01 * sinY + m21 * cosY;
		float nm22 = m02 * sinY + m22 * cosY;
		float nm00 = m00 * cosY + m20 * m_sinY;
		float nm01 = m01 * cosY + m21 * m_sinY;
		float nm02 = m02 * cosY + m22 * m_sinY;
		// rotateX
		float nm10 = m10 * cosX + nm20 * sinX;
		float nm11 = m11 * cosX + nm21 * sinX;
		float nm12 = m12 * cosX + nm22 * sinX;
		dest.m20 = m10 * m_sinX + nm20 * cosX;
		dest.m21 = m11 * m_sinX + nm21 * cosX;
		dest.m22 = m12 * m_sinX + nm22 * cosX;
		dest.m23 = 0.0f;
		// rotateZ
		dest.m00 = nm00 * cosZ + nm10 * sinZ;
		dest.m01 = nm01 * cosZ + nm11 * sinZ;
		dest.m02 = nm02 * cosZ + nm12 * sinZ;
		dest.m03 = 0.0f;
		dest.m10 = nm00 * m_sinZ + nm10 * cosZ;
		dest.m11 = nm01 * m_sinZ + nm11 * cosZ;
		dest.m12 = nm02 * m_sinZ + nm12 * cosZ;
		dest.m13 = 0.0f;
		// copy last column from 'this'
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		return dest;
	}

	/**
	 * Apply rotation to this matrix by rotating the given amount of radians
	 * about the specified <tt>(x, y, z)</tt> axis and store the result in
	 * <code>dest</code>.
	 * <p>
	 * The axis described by the three components needs to be a unit vector.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * In order to set the matrix to a rotation matrix without post-multiplying
	 * the rotation transformation, use
	 * {@link #rotation(float, float, float, float) rotation()}.
	 * <p>
	 * Reference: <a href=
	 * "http://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle">
	 * http://en.wikipedia.org</a>
	 * 
	 * @see #rotation(float, float, float, float)
	 * 
	 * @param ang
	 *            the angle in radians
	 * @param x
	 *            the x component of the axis
	 * @param y
	 *            the y component of the axis
	 * @param z
	 *            the z component of the axis
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotate(float ang, float x, float y, float z, Matrix4klf dest) {
		float s = (float) Math.sin(ang);
		float c = (float) Math.cos(ang);
		float C = 1.0f - c;

		// rotation matrix elements:
		// m30, m31, m32, m03, m13, m23 = 0
		// m33 = 1
		float xx = x * x, xy = x * y, xz = x * z;
		float yy = y * y, yz = y * z;
		float zz = z * z;
		float rm00 = xx * C + c;
		float rm01 = xy * C + z * s;
		float rm02 = xz * C - y * s;
		float rm10 = xy * C - z * s;
		float rm11 = yy * C + c;
		float rm12 = yz * C + x * s;
		float rm20 = xz * C + y * s;
		float rm21 = yz * C - x * s;
		float rm22 = zz * C + c;

		// add temporaries for dependent values
		float nm00 = m00 * rm00 + m10 * rm01 + m20 * rm02;
		float nm01 = m01 * rm00 + m11 * rm01 + m21 * rm02;
		float nm02 = m02 * rm00 + m12 * rm01 + m22 * rm02;
		float nm03 = m03 * rm00 + m13 * rm01 + m23 * rm02;
		float nm10 = m00 * rm10 + m10 * rm11 + m20 * rm12;
		float nm11 = m01 * rm10 + m11 * rm11 + m21 * rm12;
		float nm12 = m02 * rm10 + m12 * rm11 + m22 * rm12;
		float nm13 = m03 * rm10 + m13 * rm11 + m23 * rm12;
		// set non-dependent values directly
		dest.m20 = m00 * rm20 + m10 * rm21 + m20 * rm22;
		dest.m21 = m01 * rm20 + m11 * rm21 + m21 * rm22;
		dest.m22 = m02 * rm20 + m12 * rm21 + m22 * rm22;
		dest.m23 = m03 * rm20 + m13 * rm21 + m23 * rm22;
		// set other values
		dest.m00 = nm00;
		dest.m01 = nm01;
		dest.m02 = nm02;
		dest.m03 = nm03;
		dest.m10 = nm10;
		dest.m11 = nm11;
		dest.m12 = nm12;
		dest.m13 = nm13;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;

		return dest;
	}

	/**
	 * Apply rotation to this matrix by rotating the given amount of radians
	 * about the specified <tt>(x, y, z)</tt> axis.
	 * <p>
	 * The axis described by the three components needs to be a unit vector.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the
	 * rotation matrix, then the new matrix will be <code>M * R</code>. So when
	 * transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * R * v</code>, the rotation will be applied first!
	 * <p>
	 * In order to set the matrix to a rotation matrix without post-multiplying
	 * the rotation transformation, use
	 * {@link #rotation(float, float, float, float) rotation()}.
	 * <p>
	 * Reference: <a href=
	 * "http://en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle">
	 * http://en.wikipedia.org</a>
	 * 
	 * @see #rotation(float, float, float, float)
	 * 
	 * @param ang
	 *            the angle in radians
	 * @param x
	 *            the x component of the axis
	 * @param y
	 *            the y component of the axis
	 * @param z
	 *            the z component of the axis
	 * @return this
	 */
	public Matrix4klf rotate(float ang, float x, float y, float z) {
		return rotate(ang, x, y, z, this);
	}

	/**
	 * Apply a translation to this matrix by translating by the given number of
	 * units in x, y and z.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>T</code> the
	 * translation matrix, then the new matrix will be <code>M * T</code>. So
	 * when transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * T * v</code>, the translation will be applied first!
	 * <p>
	 * In order to set the matrix to a translation transformation without
	 * post-multiplying it, use {@link #translation(Vector3f)}.
	 * 
	 * @see #translation(Vector3f)
	 * 
	 * @param offset
	 *            the number of units in x, y and z by which to translate
	 * @return this
	 */
	public Matrix4klf translatef(Vector3f offset) {
		return translatef(offset.x, offset.y, offset.z);
	}

	/**
	 * Apply a translation to this matrix by translating by the given number of
	 * units in x, y and z and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>T</code> the
	 * translation matrix, then the new matrix will be <code>M * T</code>. So
	 * when transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * T * v</code>, the translation will be applied first!
	 * <p>
	 * In order to set the matrix to a translation transformation without
	 * post-multiplying it, use {@link #translation(float, float, float)}.
	 * 
	 * @see #translation(float, float, float)
	 * 
	 * @param x
	 *            the offset to translate in x
	 * @param y
	 *            the offset to translate in y
	 * @param z
	 *            the offset to translate in z
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf translate(long x, long y, long z, Matrix4klf dest) {
		// translation matrix elements:
		// m00, m11, m22, m33 = 1
		// m30 = x, m31 = y, m32 = z
		// all others = 0
		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m03 = m03;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m13 = m13;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		dest.m23 = m23;
		dest.m30 = (long) (m00 * x + m10 * y + m20 * z + m30);
		dest.m31 = (long) (m01 * x + m11 * y + m21 * z + m31);
		dest.m32 = (long) (m02 * x + m12 * y + m22 * z + m32);
		dest.m33 = m03 * x + m13 * y + m23 * z + m33;
		return dest;
	}

	/**
	 * Apply a translation to this matrix by translating by the given number of
	 * units in x, y and z.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>T</code> the
	 * translation matrix, then the new matrix will be <code>M * T</code>. So
	 * when transforming a vector <code>v</code> with the new matrix by using
	 * <code>M * T * v</code>, the translation will be applied first!
	 * <p>
	 * In order to set the matrix to a translation transformation without
	 * post-multiplying it, use {@link #translation(float, float, float)}.
	 * 
	 * @see #translation(float, float, float)
	 * 
	 * @param x
	 *            the offset to translate in x
	 * @param y
	 *            the offset to translate in y
	 * @param z
	 *            the offset to translate in z
	 * @return this
	 */
	public Matrix4klf translatef(float x, float y, float z) {
		x *= meter;
		y *= meter;
		z *= meter;
		// translation matrix elements:
		// m00, m11, m22, m33 = 1
		// m30 = x, m31 = y, m32 = z
		// all others = 0
		m30 += m00 * x + m10 * y + m20 * z;
		m31 += m01 * x + m11 * y + m21 * z;
		m32 += m02 * x + m12 * y + m22 * z;
		m33 = m03 * x + m13 * y + m23 * z + m33;
		return this;
	}

	/**
	 * Apply a rotation transformation, rotating about the given
	 * {@link AxisAngle4f}, to this matrix.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>A</code> the
	 * rotation matrix obtained from the given {@link AxisAngle4f}, then the new
	 * matrix will be <code>M * A</code>. So when transforming a vector
	 * <code>v</code> with the new matrix by using <code>M * A * v</code>, the
	 * {@link AxisAngle4f} rotation will be applied first!
	 * <p>
	 * In order to set the matrix to a rotation transformation without
	 * post-multiplying, use {@link #rotation(AxisAngle4f)}.
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Axis_and_angle">
	 * http://en.wikipedia.org</a>
	 * 
	 * @see #rotate(float, float, float, float)
	 * @see #rotation(AxisAngle4f)
	 * 
	 * @param axisAngle
	 *            the {@link AxisAngle4f} (needs to be
	 *            {@link AxisAngle4f#normalize() normalized})
	 * @return this
	 */
	public Matrix4klf rotate(AxisAngle4f axisAngle) {
		return rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z);
	}

	/**
	 * Apply a rotation transformation, rotating about the given
	 * {@link AxisAngle4f} and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>A</code> the
	 * rotation matrix obtained from the given {@link AxisAngle4f}, then the new
	 * matrix will be <code>M * A</code>. So when transforming a vector
	 * <code>v</code> with the new matrix by using <code>M * A * v</code>, the
	 * {@link AxisAngle4f} rotation will be applied first!
	 * <p>
	 * In order to set the matrix to a rotation transformation without
	 * post-multiplying, use {@link #rotation(AxisAngle4f)}.
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Axis_and_angle">
	 * http://en.wikipedia.org</a>
	 * 
	 * @see #rotate(float, float, float, float)
	 * @see #rotation(AxisAngle4f)
	 * 
	 * @param axisAngle
	 *            the {@link AxisAngle4f} (needs to be
	 *            {@link AxisAngle4f#normalize() normalized})
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotate(AxisAngle4f axisAngle, Matrix4klf dest) {
		return rotate(axisAngle.angle, axisAngle.x, axisAngle.y, axisAngle.z, dest);
	}
	
	public Matrix4klf rotate(Quaternionf quat, Matrix4klf dest) {
        float dqx = quat.x + quat.x;
        float dqy = quat.y + quat.y;
        float dqz = quat.z + quat.z;
        float q00 = dqx * quat.x;
        float q11 = dqy * quat.y;
        float q22 = dqz * quat.z;
        float q01 = dqx * quat.y;
        float q02 = dqx * quat.z;
        float q03 = dqx * quat.w;
        float q12 = dqy * quat.z;
        float q13 = dqy * quat.w;
        float q23 = dqz * quat.w;

        float rm00 = 1.0f - q11 - q22;
        float rm01 = q01 + q23;
        float rm02 = q02 - q13;
        float rm10 = q01 - q23;
        float rm11 = 1.0f - q22 - q00;
        float rm12 = q12 + q03;
        float rm20 = q02 + q13;
        float rm21 = q12 - q03;
        float rm22 = 1.0f - q11 - q00;

        float nm00 = m00 * rm00 + m10 * rm01 + m20 * rm02;
        float nm01 = m01 * rm00 + m11 * rm01 + m21 * rm02;
        float nm02 = m02 * rm00 + m12 * rm01 + m22 * rm02;
        float nm03 = m03 * rm00 + m13 * rm01 + m23 * rm02;
        float nm10 = m00 * rm10 + m10 * rm11 + m20 * rm12;
        float nm11 = m01 * rm10 + m11 * rm11 + m21 * rm12;
        float nm12 = m02 * rm10 + m12 * rm11 + m22 * rm12;
        float nm13 = m03 * rm10 + m13 * rm11 + m23 * rm12;
        dest.m20 = m00 * rm20 + m10 * rm21 + m20 * rm22;
        dest.m21 = m01 * rm20 + m11 * rm21 + m21 * rm22;
        dest.m22 = m02 * rm20 + m12 * rm21 + m22 * rm22;
        dest.m23 = m03 * rm20 + m13 * rm21 + m23 * rm22;
        dest.m00 = nm00;
        dest.m01 = nm01;
        dest.m02 = nm02;
        dest.m03 = nm03;
        dest.m10 = nm10;
        dest.m11 = nm11;
        dest.m12 = nm12;
        dest.m13 = nm13;
        dest.m30 = m30;
        dest.m31 = m31;
        dest.m32 = m32;
        dest.m33 = m33;

        return dest;
    }
	
	/**
     * Apply the rotation transformation of the given {@link Quaternionf} to this matrix.
     * <p>
     * If <code>M</code> is <code>this</code> matrix and <code>Q</code> the rotation matrix obtained from the given quaternion,
     * then the new matrix will be <code>M * Q</code>. So when transforming a
     * vector <code>v</code> with the new matrix by using <code>M * Q * v</code>,
     * the quaternion rotation will be applied first!
     * <p>
     * In order to set the matrix to a rotation transformation without post-multiplying,
     * use {@link #rotation(Quaternionf)}.
     * <p>
     * Reference: <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Quaternion">http://en.wikipedia.org</a>
     * 
     * @see #rotation(Quaternionf)
     * 
     * @param quat
     *          the {@link Quaternionf}
     * @return this
     */
    public Matrix4klf rotate(Quaternionf quat) {
        return rotate(quat, this);
    }

	/**
	 * Apply a rotation transformation, rotating the given radians about the
	 * specified axis, to this matrix.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>A</code> the
	 * rotation matrix obtained from the given axis-angle, then the new matrix
	 * will be <code>M * A</code>. So when transforming a vector <code>v</code>
	 * with the new matrix by using <code>M * A * v</code>, the axis-angle
	 * rotation will be applied first!
	 * <p>
	 * In order to set the matrix to a rotation transformation without
	 * post-multiplying, use {@link #rotation(float, Vector3f)}.
	 * <p>
		m30 = (long) (m.m30 * meter);
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Axis_and_angle">
	 * http://en.wikipedia.org</a>
	 * 
	 * @see #rotate(float, float, float, float)
	 * @see #rotation(float, Vector3f)
	 * 
	 * @param angle
	 *            the angle in radians
	 * @param axis
	 *            the rotation axis (needs to be {@link Vector3f#normalize()
	 *            normalized})
	 * @return this
	 */
	public Matrix4klf rotate(float angle, Vector3f axis) {
		return rotate(angle, axis.x, axis.y, axis.z);
	}

	/**
	 * Apply a rotation transformation, rotating the given radians about the
	 * specified axis and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>A</code> the
	 * rotation matrix obtained from the given axis-angle, then the new matrix
	 * will be <code>M * A</code>. So when transforming a vector <code>v</code>
	 * with the new matrix by using <code>M * A * v</code>, the axis-angle
	 * rotation will be applied first!
	 * <p>
	 * In order to set the matrix to a rotation transformation without
	 * post-multiplying, use {@link #rotation(float, Vector3f)}.
	 * <p>
	 * Reference:
	 * <a href="http://en.wikipedia.org/wiki/Rotation_matrix#Axis_and_angle">
	 * http://en.wikipedia.org</a>
	 * 
	 * @see #rotate(float, float, float, float)
	 * @see #rotation(float, Vector3f)
	 * 
	 * @param angle
	 *            the angle in radians
	 * @param axis
	 *            the rotation axis (needs to be {@link Vector3f#normalize()
	 *            normalized})
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf rotate(float angle, Vector3f axis, Matrix4klf dest) {
		return rotate(angle, axis.x, axis.y, axis.z, dest);
	}

	/**
	 * Normalize the upper left 3x3 submatrix of this matrix.
	 * <p>
	 * The resulting matrix will map unit vectors to unit vectors, though a pair
	 * of orthogonal input unit vectors need not be mapped to a pair of
	 * orthogonal output vectors if the original matrix was not orthogonal
	 * itself (i.e. had <i>skewing</i>).
	 * 
	 * @return this
	 */
	public Matrix4klf normalize3x3() {
		return normalize3x3(this);
	}

	/**
	 * Normalize the upper left 3x3 submatrix of this matrix and store the
	 * result in <code>dest</code>.
	 * <p>
	 * The resulting matrix will map unit vectors to unit vectors, though a pair
	 * of orthogonal input unit vectors need not be mapped to a pair of
	 * orthogonal output vectors if the original matrix was not orthogonal
	 * itself (i.e. had <i>skewing</i>).
	 * 
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix4klf normalize3x3(Matrix4klf dest) {
		float invXlen = (float) (1.0 / Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02));
		float invYlen = (float) (1.0 / Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12));
		float invZlen = (float) (1.0 / Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22));
		dest.m00 = m00 * invXlen;
		dest.m01 = m01 * invXlen;
		dest.m02 = m02 * invXlen;
		dest.m10 = m10 * invYlen;
		dest.m11 = m11 * invYlen;
		dest.m12 = m12 * invYlen;
		dest.m20 = m20 * invZlen;
		dest.m21 = m21 * invZlen;
		dest.m22 = m22 * invZlen;
		return dest;
	}

	/**
	 * Normalize the upper left 3x3 submatrix of this matrix and store the
	 * result in <code>dest</code>.
	 * <p>
	 * The resulting matrix will map unit vectors to unit vectors, though a pair
	 * of orthogonal input unit vectors need not be mapped to a pair of
	 * orthogonal output vectors if the original matrix was not orthogonal
	 * itself (i.e. had <i>skewing</i>).
	 * 
	 * @param dest
	 *            will hold the result
	 * @return dest
	 */
	public Matrix3f normalize3x3(Matrix3f dest) {
		float invXlen = (float) (1.0 / Math.sqrt(m00 * m00 + m01 * m01 + m02 * m02));
		float invYlen = (float) (1.0 / Math.sqrt(m10 * m10 + m11 * m11 + m12 * m12));
		float invZlen = (float) (1.0 / Math.sqrt(m20 * m20 + m21 * m21 + m22 * m22));
		dest.m00 = m00 * invXlen;
		dest.m01 = m01 * invXlen;
		dest.m02 = m02 * invXlen;
		dest.m10 = m10 * invYlen;
		dest.m11 = m11 * invYlen;
		dest.m12 = m12 * invYlen;
		dest.m20 = m20 * invZlen;
		dest.m21 = m21 * invZlen;
		dest.m22 = m22 * invZlen;
		return dest;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(m00);
		result = prime * result + Float.floatToIntBits(m01);
		result = prime * result + Float.floatToIntBits(m02);
		result = prime * result + Float.floatToIntBits(m03);
		result = prime * result + Float.floatToIntBits(m10);
		result = prime * result + Float.floatToIntBits(m11);
		result = prime * result + Float.floatToIntBits(m12);
		result = prime * result + Float.floatToIntBits(m13);
		result = prime * result + Float.floatToIntBits(m20);
		result = prime * result + Float.floatToIntBits(m21);
		result = prime * result + Float.floatToIntBits(m22);
		result = prime * result + Float.floatToIntBits(m23);
		result = prime * result + Float.floatToIntBits(m30);
		result = prime * result + Float.floatToIntBits(m31);
		result = prime * result + Float.floatToIntBits(m32);
		result = prime * result + Float.floatToIntBits(m33);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Matrix4klf))
			return false;
		Matrix4klf other = (Matrix4klf) obj;
		if (Float.floatToIntBits(m00) != Float.floatToIntBits(other.m00))
			return false;
		if (Float.floatToIntBits(m01) != Float.floatToIntBits(other.m01))
			return false;
		if (Float.floatToIntBits(m02) != Float.floatToIntBits(other.m02))
			return false;
		if (Float.floatToIntBits(m03) != Float.floatToIntBits(other.m03))
			return false;
		if (Float.floatToIntBits(m10) != Float.floatToIntBits(other.m10))
			return false;
		if (Float.floatToIntBits(m11) != Float.floatToIntBits(other.m11))
			return false;
		if (Float.floatToIntBits(m12) != Float.floatToIntBits(other.m12))
			return false;
		if (Float.floatToIntBits(m13) != Float.floatToIntBits(other.m13))
			return false;
		if (Float.floatToIntBits(m20) != Float.floatToIntBits(other.m20))
			return false;
		if (Float.floatToIntBits(m21) != Float.floatToIntBits(other.m21))
			return false;
		if (Float.floatToIntBits(m22) != Float.floatToIntBits(other.m22))
			return false;
		if (Float.floatToIntBits(m23) != Float.floatToIntBits(other.m23))
			return false;
		if (Float.floatToIntBits(m30) != Float.floatToIntBits(other.m30))
			return false;
		if (Float.floatToIntBits(m31) != Float.floatToIntBits(other.m31))
			return false;
		if (Float.floatToIntBits(m32) != Float.floatToIntBits(other.m32))
			return false;
		if (Float.floatToIntBits(m33) != Float.floatToIntBits(other.m33))
			return false;
		return true;
	}

	/**
	 * Exchange the values of <code>this</code> matrix with the given
	 * <code>other</code> matrix.
	 * 
	 * @param other
	 *            the other matrix to exchange the values with
	 * @return this
	 */
	public Matrix4klf swap(Matrix4klf other) {
		float tmp;
		long tmpb;
		tmp = m00;
		m00 = other.m00;
		other.m00 = tmp;
		tmp = m01;
		m01 = other.m01;
		other.m01 = tmp;
		tmp = m02;
		m02 = other.m02;
		other.m02 = tmp;
		tmp = m03;
		m03 = other.m03;
		other.m03 = tmp;
		tmp = m10;
		m10 = other.m10;
		other.m10 = tmp;
		tmp = m11;
		m11 = other.m11;
		other.m11 = tmp;
		tmp = m12;
		m12 = other.m12;
		other.m12 = tmp;
		tmp = m13;
		m13 = other.m13;
		other.m13 = tmp;
		tmp = m20;
		m20 = other.m20;
		other.m20 = tmp;
		tmp = m21;
		m21 = other.m21;
		other.m21 = tmp;
		tmp = m22;
		m22 = other.m22;
		other.m22 = tmp;
		tmp = m23;
		m23 = other.m23;
		other.m23 = tmp;
		tmpb = m30;
		m30 = other.m30;
		other.m30 = tmpb;
		tmpb = m31;
		m31 = other.m31;
		other.m31 = tmpb;
		tmpb = m32;
		m32 = other.m32;
		other.m32 = tmpb;
		tmp = m33;
		m33 = other.m33;
		other.m33 = tmp;
		return this;
	}

}
