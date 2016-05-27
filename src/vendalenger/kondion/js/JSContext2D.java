package vendalenger.kondion.js;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 * A canvas interface that matches the HTML5 canvas element with some customs added
 * http://www.w3schools.com/tags/ref_canvas.asp
 */
public abstract class JSContext2D {
	
	public Object fillStyle = null;
	public Object strokeStyle = null;
	public Object shadowColor = null;
	public float shadowBlur = 0.0f;
	public float shadowOffsetX = 0.0f;
	public float shadowOffsetY = 0.0f;
	public abstract int createLinearGradient(float x0, float y0, float x1, float y1);
	public abstract int createPattern(JSDrawable img, String r);
	public abstract int createPattern(JSDrawable img, boolean repeat, boolean repeatX, boolean repeatY);
	public abstract int createRadialGradient(float x0, float y0, float r0, float x1, float y1, float r1);
	public abstract int addColorStop(float stop, ScriptObjectMirror color);
	
	public ScriptObjectMirror lineCap = null;
	public ScriptObjectMirror lineJoin = null;
	public float lineWidth = 1.0f;
	public float miterLimit = 10.0f;
	
	public abstract void rect(float x, float y, float width, float height);
	public abstract void fillRect(float x, float y, float width, float height);
	public abstract void strokeRect(float x, float y, float width, float height);
	public abstract void clearRect(float x, float y, float width, float height);
	
	public abstract void fill();
	public abstract void stroke();
	public abstract void beginPath();
	public abstract void moveTo(float x, float y);
	public abstract void closePath();
	public abstract void lineTo(float x, float y);
	public abstract void clip();
	public abstract void quadraticCurveTo(float cpx, float cpy, float x, float y);
	public abstract void bezierCurveTo(float cp1x, float cp1y, float cp2x, float cp2y, float x, float y);
	public abstract void arc(float x, float y, float r, float sAngle, float eAngle);
	public abstract void arc(float x, float y, float r, float sAngle, float eAngle, boolean counterclockwise);
	public abstract void arcTo(float x1, float y1, float x2, float y2, float r);
	public abstract boolean isPointInPath(float x, float y);
	
	public abstract void scale(float x, float y);
	public abstract void rotate(float angle);
	public abstract void translate(float x, float y);
	public abstract void transform(float a, float b, float c, float d, float e, float f);
	public abstract void setTransform(float a, float b, float c, float d, float e, float f);
	public abstract void identity(); // Added
	
	public ScriptObjectMirror font = null;
	public Object textAlign = null;
	public ScriptObjectMirror textBaseline = null;
	public abstract void fillText(String text, float x, float y);
	public abstract void strokeText(String text, float x, float y);
	public abstract void strokeText(String text, float x, float y, float maxWidth);
	public abstract float measureText(String text);
	
	public abstract void drawImage(JSDrawable img, float x, float y);
	public abstract void drawImage(JSDrawable img, float x, float y, float width, float height);
	public abstract void drawImage(JSDrawable img, float sx, float sy, float swidth, float sheight, float x, float y, float width, float height);
	
	public float width = 0;
	public float height = 0;
	public ScriptObjectMirror data = null;
	public abstract ScriptObjectMirror createImageData();
	public abstract ScriptObjectMirror getImageData();
	public abstract ScriptObjectMirror putImageData();
	
	public float globalAlpha = 1.0f;
	public Object globalCompositeOperation = null;
	
	public abstract void save();
	public abstract void restore();
}
