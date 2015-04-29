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

package vendalenger.kondion;

import vendalenger.kondion.objects.ProtoEntity;
import vendalenger.port.Command;

public class KJS {

	public static void freeCam() {
		Kondion.getCurrentCamera().setFreeMode(true);
	}

	public static void issueCommand(String msg) {
		Command.issue(msg, false);
	}

	public static ProtoEntity getProtoEntity(String id) {
		for (int i = 0; i < Kondion.getProtoEntityList().size(); i++) {
			if (id.equals(Kondion.getProtoEntityList().get(i).getId())) {
				return Kondion.getProtoEntityList().get(i);
			}
		}
		return null;
	}
}