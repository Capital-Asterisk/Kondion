package vendalenger.kondion;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.kobj.GKO_RenderPass;
import vendalenger.kondion.kobj.GKO_Scene;
import vendalenger.kondion.kobj.OKO_Camera_;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.Window;

public class KondionWorld {

	public final List<GKO_RenderPass> passes;
	public final Vector4f clearColor;
	public final Vector4f skyColor;
	
	public ScriptObjectMirror compositor;
	public ScriptObjectMirror s;
	public OKO_Camera_ camera;
	public GKO_Scene Scene;
	
	public float zNear = 0.001f;
	public float zFar = 100.0f;
	public float fogIntensity = 0.0f;
	
	public KondionWorld() {
		clearColor = new Vector4f(0.02f, 0.02f, 0.02f, 0.0f);
		skyColor = new Vector4f(0.02f, 0.02f, 0.02f, 0.0f);
		passes = new ArrayList<GKO_RenderPass>();
		Scene = new GKO_Scene();
	}
	
	float rot = 0;
	
	public void composite() {
		//passes.get(0).
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		glClearColor(clearColor.x, clearColor.y,
				clearColor.z, clearColor.w);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
		glViewport(0, 0, Window.getWidth(), Window.getHeight());
		TTT.three();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glTranslatef(0, 0, 0);
		//glRotatef(rot, 0, 1, 0);
		//rot += 0.3f;
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		glBindTexture(GL_TEXTURE_2D, passes.get(0).getTextureId());
		GLDrawing.setCoords(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
		GLDrawing.renderQuad(2.0f, 2.0f);
		glBindTexture(GL_TEXTURE_2D, passes.get(1).getTextureId());
		glTranslatef(0.5f, 0.5f, 0);
		GLDrawing.renderQuad(0.3f, 0.3f);
		//glTranslatef(-1.0f, 0, 0);
		//GLDrawing.renderQuad(0.9f, 0.9f);
		//glTranslatef(0.0f, -1.0f, 0);
		//GLDrawing.renderQuad(0.9f, 0.9f);
		//glTranslatef(1.0f, 0.0f, 0);
		//GLDrawing.renderQuad(0.9f, 0.9f);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
