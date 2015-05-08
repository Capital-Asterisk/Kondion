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

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.lwjgl.util.vector.Vector3f;

import vendalenger.kondion.lwjgl.FlatDrawing;
import vendalenger.kondion.lwjgl.resource.KondionLoader;
import vendalenger.kondion.lwjgl.resource.KondionTexture;

public class Entity {
	
	private int tickInterval;

	protected float scale = 1.0f;
	protected KondionTexture t;
	protected ProtoEntity prototype;
	protected ScriptObjectMirror mirror;
	protected short[] xTraits = null;
	protected Vector3f position;
	protected Vector3f rotation;

	// public abstract void draw();
	// public abstract void tick(String msg);

	public Entity(ProtoEntity p, ScriptObjectMirror m) {
		prototype = p;
		mirror = m;
		position = new Vector3f();
		rotation = new Vector3f();
	};

	/*
	 * Getters and setters Without setStates because it's a list Everything else
	 * is pretty obvious
	 */

	public Vector3f getPosition() {
		return position;
	}

	/**
	 * 
	 * @return Yaw, Pitch, and Roll. Modifiable
	 */
	public Vector3f getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public int getTickInterval() {
		return tickInterval;
	}

	public void render() {
		glPushMatrix();
		glTranslatef(position.x, position.y, position.z);
		t = KondionLoader.textures.get("human");
		FlatDrawing.renderBillboard(2.0f, 2.0f, t);
		glPopMatrix();
	}

	public void setExtraTraits(short[] array) {
		xTraits = array;
	}

	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
	}

	public void setPosition(Vector3f p) {
		position = p;
	}

	public void setScale(float s) {
		scale = s;
	}

	public void setTickInterval(int tickInterval) {
		this.tickInterval = tickInterval;
	}
}
