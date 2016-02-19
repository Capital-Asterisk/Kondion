package vendalenger.kondion.js;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * A canvas interface that matches the HTML5 canvas element with some customs added
 * http://www.w3schools.com/tags/ref_canvas.asp
 */
public interface JSContext2D {
	
	public ScriptObjectMirror fillStyle = null;
	public ScriptObjectMirror strokeStyle = null;
	public ScriptObjectMirror shadowColor = null;
	public float shadowBlur = 0.0f;
	public float shadowOffsetX = 0.0f;
	public float shadowOffsetY = 0.0f;
	public int createLinearGradient(float x0, float y0, float x1, float y1);
	public int createPattern(JSDrawable img, String r);
	public int createPattern(JSDrawable img, boolean repeat, boolean repeatX, boolean repeatY);
	public int createRadialGradient(float x0, float y0, float r0, float x1, float y1, float r1);
	public int addColorStop(float stop, ScriptObjectMirror color);
	
	public ScriptObjectMirror lineCap = null;
	public ScriptObjectMirror lineJoin = null;
	public float lineWidth = 1.0f;
	public float miterLimit = 10.0f;
	
	public void rect(float x, float y, float width, float height);
	public void fillRect(float x, float y, float width, float height);
	public void strokeRect(float x, float y, float width, float height);
	public void clearRect(float x, float y, float width, float height);
	
	public void fill();
	public void stroke();
	public void beginPath();
	public void moveTo(float x, float y);
	public void closePath();
	public void lineTo(float x, float y);
	public void clip();
	public void quadraticCurveTo(float cpx, float cpy, float x, float y);
	public void bezierCurveTo(float cp1x, float cp1y, float cp2x, float cp2y, float x, float y);
	public void arc(float x, float y, float r, float sAngle, float eAngle);
	public void arc(float x, float y, float r, float sAngle, float eAngle, boolean counterclockwise);
	public void arcTo(float x1, float y1, float x2, float y2, float r);
	public boolean isPointInPath(float x, float y);
	
	public void scale(float x, float y);
	public void rotate(float angle);
	public void translate(float x, float y);
	public void transform(float a, float b, float c, float d, float e, float f);
	public void setTransform(float a, float b, float c, float d, float e, float f);
	public void identity(); // Added
	
	public ScriptObjectMirror font = null;
	public ScriptObjectMirror textAlign = null;
	public ScriptObjectMirror textBaseline = null;
	public void fillText(String text, float x, float y);
	public void strokeText(String text, float x, float y);
	public void strokeText(String text, float x, float y, float maxWidth);
	public void measureText(String text);
	
	public void drawImage(JSDrawable img, float x, float y);
	public void drawImage(JSDrawable img, float x, float y, float width, float height);
	public void drawImage(JSDrawable img, float sx, float sy, float swidth, float sheight, float x, float y, float width, float height);
	
	public float width = 0;
	public float height = 0;
	public ScriptObjectMirror data = null;
	public ScriptObjectMirror createImageData();
	public ScriptObjectMirror getImageData();
	public ScriptObjectMirror putImageData();
	
	public float globalAlpha = 1.0f;
	public ScriptObjectMirror globalCompositeOperation = null;
	
	public void save();
	public void restore();
}
