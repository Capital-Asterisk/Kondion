package vendalenger.kondion.gui;

import org.joml.Vector2f;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.lwjgl.OpenGL2DContext;

public abstract class KComponent {
	
	public static final int 
		NORMAL = 0,
		HOVER = 1,
		SELECT = 2,
		DOWN = 3;
	public ScriptObjectMirror s;
	public KComponent up, down, left, right;
	public float x, y, width, height;
	public float angle = 0.0f;
	//public KComponent[] directions = null;
	public KComponent parent = null;
	
	public KComponent(ScriptObjectMirror som) {
		
		up = null;
		down = null;
		left = null;
		right = null;
		
		if (som.containsKey("x")) {
			x = Float.parseFloat(som.get("x").toString());
			if (x == Float.NaN)
				x = 0;
			som.remove("x");
		}
		
		if (som.containsKey("y")) {
			y = Float.parseFloat(som.get("y").toString());
			if (y == Float.NaN)
				y = 0;
			som.remove("y");
		}
		
		if (som.containsKey("width")) {
			width = Float.parseFloat(som.get("width").toString());
			if (width == Float.NaN)
				width = 0;
			som.remove("width");
		}
		
		if (som.containsKey("height")) {
			height = Float.parseFloat(som.get("height").toString());
			if (height == Float.NaN)
				height = 0;
			som.remove("height");
		}
		
		s = som;
		//vertex = new Vector2f();
	}
	
	public abstract void draw(OpenGL2DContext ctx, int state);
	
	public void applyTransforms(Vector2f vertex) {
		if (parent != null) {
			parent.applyTransforms(vertex);
		}
		vertex.x += x;
		vertex.y += y;
		//ctx.rotate(angle);
	}
	
	public void applyTransforms(OpenGL2DContext ctx) {
		if (parent != null) {
			parent.applyTransforms(ctx);
		}
		ctx.translate(x, y);
		ctx.rotate(angle);
	}
	
	public boolean inside(int x, int y) {
		return false;
	};
	
	public void click(int button, boolean mouse) {
		
	}
}
