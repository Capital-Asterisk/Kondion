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

package vendalenger.kondion.kobj;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.objectbase.KObj_Node;
import vendalenger.kondion.objectbase.KObj_Oriented;
import vendalenger.kondion.objectbase.KObj_Renderable;
import vendalenger.kondion.objectbase.KObj_Solid;

public class GKO_RenderPass extends KObj_Node {
	
	public static final int
		DEFAULT		= 0,
		DIFFUSE		= 1,
		DEPTH		= 2,
		NORMALS		= 3,
		BRIGHTNESS	= 4,
		HDR			= 5,
		CUSTOM		= 9,
		DEFERRED	= 10,
		HRD_RESULT	= 11;
	
	private List<KObj_Renderable> items;
	private boolean framebuffered = false;
	public boolean disable = false;
	public boolean auto = true;
	public int type;
	public int id;
	
	public GKO_RenderPass() {
		items = new ArrayList<KObj_Renderable>();
		type = 0;
	}
	
	public GKO_RenderPass(int t) {
		items = new ArrayList<KObj_Renderable>();
		type = t;
	}
	
	public void addItem(KObj_Renderable f) {
		items.add(f);
	}

	public void render() {
		for (KObj_Renderable kobj : items) {
			if (kobj instanceof KObj_Oriented) {
				((KObj_Oriented) kobj).updateB();
				if (Kondion.showPrespective && kobj instanceof KObj_Renderable)
					((KObj_Renderable) kobj).render(type);
			}
		}
	}

	@Override
	public void update() {
		defaultUpdate();
	}
}
