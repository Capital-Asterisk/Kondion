package vendalenger.kondion.gui;

import org.joml.Vector3f;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.lwjgl.OpenGL2DContext;

public class KButton extends KComponent {
	
	public Vector3f normalColor;
	public Vector3f selectColor;
	public Vector3f hoverColor;
	public Vector3f downColor;

	public KButton(ScriptObjectMirror som) {
		super(som);
	}
	
	@Override
	public void click(int button, boolean mouse) {
		Object obj = s.get("click");
		if (obj instanceof ScriptObjectMirror && ((ScriptObjectMirror) obj).isFunction()) {
			((ScriptObjectMirror) obj).call(this, button, mouse);
		}
	}

	@Override
	public void draw(OpenGL2DContext ctx, int state) {
		Object obj = s.get("draw");
		if (obj instanceof ScriptObjectMirror && ((ScriptObjectMirror) obj).isFunction()) {
			((ScriptObjectMirror) obj).call(this, ctx, state);
		} else {
			
			switch (state) {
				case NORMAL:
					ctx.fillRgba(1.0f, 1.0f, 1.0f, 0.1f);
					break;
				case SELECT:
					ctx.fillRgba(1.0f, 1.0f, 1.0f, 0.2f);
					break;
				default:
				break;
			}
			//ctx.rotate(state);
			ctx.fillRect(-width / 2, -height / 2, width, height);
			obj = s.get("text");
			if (obj != null) {
				
				ctx.fillRgb(1.0f, 1.0f, 1.0f);
				ctx.textAlign = "center";
				ctx.fillText(obj.toString(), 0, 5);
			}
		}
		//y += 0.001f;
		//System.out.println("butts");
		
	}

}
