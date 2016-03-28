package vendalenger.kondion;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.GL11.*;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.joml.Vector4f;

import com.sun.management.OperatingSystemMXBean;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import vendalenger.kondion.kobj.GKO_RenderPass;
import vendalenger.kondion.kobj.GKO_Scene;
import vendalenger.kondion.kobj.OKO_Camera_;
import vendalenger.kondion.lwjgl.GLDrawing;
import vendalenger.kondion.lwjgl.OpenGL2DContext;
import vendalenger.kondion.lwjgl.TTT;
import vendalenger.kondion.lwjgl.Window;
import vendalenger.kondion.lwjgl.resource.KLoader;

public class KondionWorld {
	
	private final OpenGL2DContext ctx;

	public final List<GKO_RenderPass> passes;
	public final Vector4f clearColor;
	public final Vector4f skyColor;
	
	public ScriptObjectMirror compositor;
	public ScriptObjectMirror s;
	public OKO_Camera_ camera;
	public GKO_Scene Scene;
	
	public short compMode = 0;
	
	public float zNear = 0.001f;
	public float zFar = 100.0f;
	public float fogIntensity = 0.0f;
	
	public KondionWorld() {
		ctx = new OpenGL2DContext();
		clearColor = new Vector4f(0.02f, 0.02f, 0.02f, 0.0f);
		skyColor = new Vector4f(0.02f, 0.02f, 0.02f, 0.0f);
		passes = new ArrayList<GKO_RenderPass>();
		Scene = new GKO_Scene();
	}
	
	float rot = 0;
	
	public void composite() {
		//passes.get(0).
		
		int j = 0;
		//System.out.println("FPS: " + Kondion.getFramerate() + " # of render passes: " + passes.size());
		switch (compMode) {
			case 1:
				if (compositor != null && compositor.isFunction())
					compositor.call(this, ctx, passes);
				
			break;
			case 2:
				// Debug mode
				for (int i = 0; i < passes.size(); i++) {
					passes.get(i).render();
				}
				TTT.two();
				//System.out.println("# of render passes: " + passes.size() + " FPS: " + Kondion.getFramerate());
				j = (int) Math.ceil(Math.sqrt(passes.size() + 1));
				for (int i = 0; i < passes.size() + 1; i++) {
					
					//TTT.two(clearColor);
					glLoadIdentity();
					glTranslatef((i % j) * (Window.getWidth() / j) + (Window.getWidth() / j / 2),
							(int)(i / j) * (Window.getHeight() / j) + (Window.getHeight() / j / 2), 0.0f);
					
					if (passes.size() != i) {
						glBindTexture(GL_TEXTURE_2D, passes.get(i).getTextureId());
						GLDrawing.renderQuad(Window.getWidth() / j, Window.getHeight() / j);
						glTranslatef(-(Window.getWidth() / j / 2 - 4), -(Window.getHeight() / j / 2), 0.0f);
						glScalef(1.0f / j, 1.0f / j, 1.0f);
						glTranslatef(0.0f, 32, 0.0f);
						GLDrawing.drawText("Render Mode: " + passes.get(i).getType());
						glTranslatef(0.0f, 32, 0.0f);
						GLDrawing.drawText(passes.get(i).itemCount() + " objects");
						
					} else if (Kondion.kjs.d != null) {
						OperatingSystemMXBean b = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
						glTranslatef(-(Window.getWidth() / j / 2 - 4), -(Window.getHeight() / j / 2), 0.0f);
						glScalef(1.0f / j, 1.0f / j, 1.0f);
						glTranslatef(0.0f, 32, 0.0f);
						GLDrawing.drawText("KONDION NOT SO USEFUL DEBUG VIEW");
						glTranslatef(0.0f, 32, 0.0f);
						GLDrawing.drawText("VERSION " + Kondion.version + " ON " + b.getName() + " (" + b.getVersion() + ")");
						glTranslatef(0.0f, 32, 0.0f);
						GLDrawing.drawText("Render Passes: " + passes.size() + " FPS " + Kondion.getFramerate());
						glTranslatef(0.0f, 32, 0.0f);
						GLDrawing.drawText("CPU Usage (" + b.getAvailableProcessors() + " cores, " + b.getArch() + "): " + (int) (b.getProcessCpuLoad() * 100) + "%");
						glTranslatef(0.0f, 32, 0.0f);
						GLDrawing.drawText("Free RAM: " + (int) (b.getFreePhysicalMemorySize() * 100));
						
						glTranslatef(0.0f, 32, 0.0f);
						GLDrawing.drawText("Free Swap: " + (int) (b.getFreeSwapSpaceSize() * 100));
						//glTranslatef(0.0f, 32, 0.0f);
						//GLDrawing.drawText("Average FPS: " + passes.size() + " FPS " + Kondion.getFramerate());
						glTranslatef(0.0f, 32, 0.0f);
						String[] eggs = Kondion.kjs.d.getOwnKeys(true);
						for (int k = 0; k < eggs.length; k++) {
								glTranslatef(0.0f, 32, 0.0f);
								GLDrawing.drawText(eggs[k] + ": " + Kondion.kjs.d.get(eggs[k]));
						}
					}
				}
			break;
			case 3:
				// Horizontal Split (Right and left)
				if (passes.size() >= 2) {
					passes.get(0).render();
					passes.get(1).render();
					TTT.two();
					
					glBindTexture(GL_TEXTURE_2D, passes.get(0).getTextureId());
					glTranslatef(Window.getWidth() / 4, Window.getHeight() / 2, 0.0f);
					GLDrawing.renderQuad(Window.getWidth() / 2, Window.getHeight());
					
					glBindTexture(GL_TEXTURE_2D, passes.get(1).getTextureId());
					glTranslatef(Window.getWidth() / 2, 0, 0.0f);
					GLDrawing.renderQuad(Window.getWidth() / 2, Window.getHeight());
				}
			break;
			case 4:
				// Vertical Split (Top and Bot)
				if (passes.size() >= 2) {
					passes.get(0).render();
					passes.get(1).render();
					TTT.two();
					
					glBindTexture(GL_TEXTURE_2D, passes.get(0).getTextureId());
					glTranslatef(Window.getWidth() / 2, Window.getHeight() / 4, 0.0f);
					GLDrawing.renderQuad(Window.getWidth(), Window.getHeight() / 2);
					
					glBindTexture(GL_TEXTURE_2D, passes.get(1).getTextureId());
					glTranslatef(0.0f, Window.getHeight() / 2, 0.0f);
					GLDrawing.renderQuad(Window.getWidth(), Window.getHeight() / 2);
				}
			break;
			case 5:
				// Split
				//System.out.println("# of render passes: " + passes.size() + " FPS: " + Kondion.getFramerate());
				j = (int) Math.ceil(Math.sqrt(passes.size() + 1));
				for (int i = 0; i < passes.size() + 1; i++) {
					
					glLoadIdentity();
					glTranslatef((i % j) * (Window.getWidth() / j) + (Window.getWidth() / j / 2),
							(int)(i / j) * (Window.getHeight() / j) + (Window.getHeight() / j / 2), 0.0f);
					
					if (passes.size() != i) {
						glBindTexture(GL_TEXTURE_2D, passes.get(i).getTextureId());
						GLDrawing.renderQuad(Window.getWidth() / j, Window.getHeight() / j);
						glTranslatef(-(Window.getWidth() / j / 2 - 4), -(Window.getHeight() / j / 2), 0.0f);
					}
				}
			break;
			case 6:
				// Stereoscopic 3d
				
			break;
				// anaglyph etc...
			default:
				// Draw first single pass
				passes.get(0).render();
				TTT.two();
				
				glBindTexture(GL_TEXTURE_2D, passes.get(0).getTextureId());
				glTranslatef(Window.getWidth() / 2, Window.getHeight() / 2, 0.0f);
				GLDrawing.renderQuad(Window.getWidth(), Window.getHeight());
				//GLDrawing.renderQuad(128, 128, KondionLoader.getMissingTexture());
			break;
		}
		
		/*
		TTT.three();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMatrixMode(GL_MODELVIEW);
		//glRotatef(rot, 0, 1, 0);
		//rot += 0.3f;
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		glLoadIdentity();
		glBindTexture(GL_TEXTURE_2D, passes.get(0).getTextureId());
		GLDrawing.setCoords(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
		
		//glTranslatef(0.5f, 0f, 0);
		GLDrawing.renderQuad(2.0f, 2.0f);
		glBindTexture(GL_TEXTURE_2D, passes.get(1).getTextureId());
		glTranslatef(-0.9f, 0.9f, 0);
		GLDrawing.renderQuad(0.2f, 0.2f);
		
		glBindTexture(GL_TEXTURE_2D, passes.get(2).getTextureId());
		//glLoadIdentity();
		glTranslatef(0.0f, -0.2f, 0.0f);
		GLDrawing.renderQuad(0.2f, 0.2f);
		//glTranslatef(-1.0f, 0, 0);
		//GLDrawing.renderQuad(0.9f, 0.9f);
		//glTranslatef(0.0f, -1.0f, 0);
		//GLDrawing.renderQuad(0.9f, 0.9f);
		//glTranslatef(1.0f, 0.0f, 0);
		//GLDrawing.renderQuad(0.9f, 0.9f);
		glBindTexture(GL_TEXTURE_2D, 0);
		*/
	}
}
