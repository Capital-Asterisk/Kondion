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

import java.util.ArrayList;
import java.util.List;

public class Traits {

	private static List<String> traits = new ArrayList<String>();

	public static short getTraitIndex(String t) {
		return (short) traits.indexOf(t);
	}

	public static boolean hasTrait(short[] array, short trait) {
		for (short s : array)
			if (s == trait)
				return true;
		return false;
	}

	public static boolean hasTrait(Short[] array, short trait) {
		for (Short s : array)
			if (s.equals(trait))
				return true;
		return false;
	}

	public static void initDefault() {

		traits.add("et_static");
		traits.add("et_physic");
		traits.add("et_alive");

		traits.add("kg_control");
		traits.add("kg_particle");

		traits.add("gl_billboard");
		traits.add("gl_sprite");

		traits.add("ph_gravity");
	}
}
