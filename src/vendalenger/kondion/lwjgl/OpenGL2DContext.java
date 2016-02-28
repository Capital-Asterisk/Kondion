package vendalenger.kondion.lwjgl;

import static org.lwjgl.opengl.GL11.*;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.js.JSContext2D;
import vendalenger.kondion.js.JSDrawable;

public class OpenGL2DContext implements JSContext2D {

	@Override
	public int createLinearGradient(float x0, float y0, float x1, float y1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int createPattern(JSDrawable img, String r) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int createPattern(JSDrawable img, boolean repeat, boolean repeatX, boolean repeatY) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int createRadialGradient(float x0, float y0, float r0, float x1, float y1, float r1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addColorStop(float stop, ScriptObjectMirror color) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void rect(float x, float y, float width, float height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fillRect(float x, float y, float width, float height) {
		
	}

	@Override
	public void strokeRect(float x, float y, float width, float height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearRect(float x, float y, float width, float height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fill() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stroke() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beginPath() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveTo(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closePath() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lineTo(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clip() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quadraticCurveTo(float cpx, float cpy, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bezierCurveTo(float cp1x, float cp1y, float cp2x, float cp2y, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void arc(float x, float y, float r, float sAngle, float eAngle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void arc(float x, float y, float r, float sAngle, float eAngle, boolean counterclockwise) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void arcTo(float x1, float y1, float x2, float y2, float r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isPointInPath(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void scale(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotate(float angle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void translate(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transform(float a, float b, float c, float d, float e, float f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTransform(float a, float b, float c, float d, float e, float f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void identity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fillText(String text, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void strokeText(String text, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void strokeText(String text, float x, float y, float maxWidth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void measureText(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawImage(JSDrawable img, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawImage(JSDrawable img, float x, float y, float width, float height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawImage(JSDrawable img, float sx, float sy, float swidth, float sheight, float x, float y,
			float width, float height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ScriptObjectMirror createImageData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptObjectMirror getImageData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptObjectMirror putImageData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save() {
		glPushMatrix();
	}

	@Override
	public void restore() {
		glPopMatrix();
	}

}
