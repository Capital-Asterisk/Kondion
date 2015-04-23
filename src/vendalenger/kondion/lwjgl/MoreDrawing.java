/*
 * Copyright 2015 Neal Nicdao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vendalenger.kondion.lwjgl;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static vendalenger.kondion.lwjgl.FlatDrawing.setCoords;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

/**
 *
 * @author 555016
 */
public class MoreDrawing {

	/*
	 * public static void drawCorruptImage(Texture t, Color c, float x, float y,
	 * float width, float height, float layer) {
	 * 
	 * glPushMatrix(); glTranslatef(x, y, layer); glScalef(width, height, 0);
	 * glBindTexture(GL_TEXTURE_2D, t.getTextureID()); c.bind();
	 * 
	 * setCoords(new float[] {1, 1, 0, 1, 0, 0, 1, 0});
	 * GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, FlatDrawing.vbo_unitSquare);
	 * GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0l);
	 * GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	 * 
	 * GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, FlatDrawing.vbo_texCoords);
	 * GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0l);
	 * GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	 * 
	 * GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
	 * GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	 * 
	 * GL11.glDrawArrays(GL11.GL_QUADS, 0, 3 * 4);
	 * 
	 * GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	 * GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	 * 
	 * glPopMatrix(); }
	 */

}
