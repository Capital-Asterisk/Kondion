package vendalenger.kondion.kobj;


import static org.lwjgl.opengl.GL11.*;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.js.JSContext2D;
import vendalenger.kondion.js.JSDrawable;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.objectbase.KObj_Renderable;

public class RKO_GUI extends KObj_Renderable {
 
	@Override
	public void render(int type, GKO_RenderPass pass) {
		if (type == 20) {
			// TODO render shit on the screene
			
			glViewport(0, 0, Window.getWidth(), Window.getHeight());
			glMatrixMode(GL_PROJECTION);
			//glLoadIdentity(); glOrtho(0, getDisplayMode().getWidth(),
			//getDisplayMode().getHeight(), 0, 6, -6); glMatrixMode(GL_MODELVIEW);
			glAlphaFunc(GL_GREATER, 0.0f); glEnable(GL_ALPHA_TEST);
			
			glEnable(GL_TEXTURE_2D); glEnable(GL_DEPTH_TEST); glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glClearColor(0.0f,0.0f, 0.0f, 0.0f); 
		}
	}

	@Override
	public void updateB() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
