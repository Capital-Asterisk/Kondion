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

package vendalenger.kondion.objects;

import java.util.Arrays;
import java.util.List;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import vendalenger.kondion.lwjgl.FlatDrawing;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionTexture;

import com.sun.javafx.geom.Vec3f;

public class Entity {

	protected enum states {};

	protected float scale = 1.0f;
	protected Vector3f position;
	protected ProtoEntity prototype;
	protected ScriptObjectMirror mirror;
	protected KondionTexture t;

	// public abstract void draw();
	// public abstract void tick(String msg);

	public Entity(ProtoEntity p, ScriptObjectMirror m) {
		prototype = p;
		mirror = m;
		position = new Vector3f();
	};

	/*
	 * Getters and setters Without setStates because it's a list Everything else
	 * is pretty obvious
	 */

	public float getScale() {
		return scale;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setScale(float s) {
		scale = s;
	}

	public void setPosition(Vector3f p) {
		position = p;
	}

	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
	}

	public void render() {
		GL11.glPushMatrix();
		GL11.glTranslatef(position.x, position.y, position.z);
		t = KondionLoader.textures.get("human");
		FlatDrawing.renderBillboard(2.0f, 2.0f, t);
		GL11.glPopMatrix();
	}
}
