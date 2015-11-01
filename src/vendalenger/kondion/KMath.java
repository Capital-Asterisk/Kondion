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

public class KMath {
	
	/**
	 * Check collision between 2 axis aligned rectangles
	 * @param aX Rect A X
	 * @param aY Rect A Y
	 * @param aW Rect A Width
	 * @param aH Rect A Height
	 * @param bX Rect B X
	 * @param bY Rect B Y
	 * @param bW Rect B Width
	 * @param bH Rect B Height
	 * @return
	 */
	public static boolean axisAlignedCollision(float aX, float aY, float aW, float aH,
			float bX, float bY, float bW, float bH) {
		return (aX < bX + bW
			&& aX + aW > bX
			&& aY < bY + bH
			&& aH + aY > bY);
	}
	
	/**
	 * Check if Rect B is inside Rect A
	 * @param aX Rect A X
	 * @param aY Rect A Y
	 * @param aW Rect A Width
	 * @param aH Rect A Height
	 * @param bX Rect B X
	 * @param bY Rect B Y
	 * @param bW Rect B Width
	 * @param bH Rect B Height
	 * @return
	 */
	public static boolean axisAlignedInside(float aX, float aY, float aW, float aH,
			float bX, float bY, float bW, float bH) {
		return (bX + bW <= aX + aW
			&& bY + bH <= aY + aH
			&& bX >= aX
			&& bY >= aY);
	}
	
}
