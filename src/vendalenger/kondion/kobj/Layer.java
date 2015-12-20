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

import vendalenger.kondion.objectbase.KObj_Node;

public class Layer extends KObj_Node {
	
	protected static final boolean pointer = false;
	
	public boolean physic;
	public boolean render;
	public boolean update;
	
	public Layer() {
		physic = false;
		render = false;
		update = false;
		parent = this;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
