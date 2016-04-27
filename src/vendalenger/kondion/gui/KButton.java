package vendalenger.kondion.gui;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.lwjgl.OpenGL2DContext;

public class KButton extends KComponent {

	public KButton(ScriptObjectMirror som) {
		super(som);
	}

	@Override
	public void draw(OpenGL2DContext ctx, int state) {
		Object obj = s.get("draw");
		if (obj instanceof ScriptObjectMirror && ((ScriptObjectMirror) obj).isFunction()) {
			((ScriptObjectMirror) obj).call(this, ctx, state);
		} else {
			
			switch (state) {
				case NORMAL:
					ctx.fillRgb(1.0f, 0.0f, 0.0f);
					break;
				case SELECT:
					ctx.fillRgb(0.0f, 1.0f, 0.0f);
					break;
				default:
				break;
			}
			ctx.fillRect(-width / 2, -height / 2, width, height);
			obj = s.get("text");
			if (obj != null) {
				
				ctx.fillRgb(1.0f, 1.0f, 1.0f);
				ctx.textAlign = "center";
				ctx.fillText(obj.toString(), 0, 0);
			}
		}
		//y += 0.001f;
		//System.out.println("butts");
		
	}

}
