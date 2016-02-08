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

package vendalenger.kondion.objectbase;

import vendalenger.kondion.Kondion;
import vendalenger.kondion.kobj.GKO_RenderPass;
import vendalenger.kondion.lwjgl.resource.KondionTexture;
import vendalenger.kondion.materials.KMat_erial;


public abstract class KObj_Renderable extends KObj_Oriented {
	
	protected KondionTexture texture;
	protected KMat_erial material;
	
	public KObj_Renderable() {
		for (GKO_RenderPass rp : Kondion.getWorld().passes) {
			if (rp.auto) {
				rp.addItem(this);
			}
		}
	}
	
	public KMat_erial getMaterial() {
		return material;
	}

	public void setMaterial(KMat_erial material) {
		this.material = material;
	}

	public abstract void render(int type);
	
}
