/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
 

package de.dakror.wseminar;

import javafx.animation.Interpolator;
import javafx.scene.paint.Color;

/**
 * @author Maximilian Stark | Dakror
 */
public class Const {
	// -- Graph Generation-- //
	public static int cellSize = 24;
	public static int nodeAmount = 20;
	public static int edgeAmount = 3;
	public static int edgesMaxCost = 30;
	
	// -- VFX / Animation -- //
	/**
	 * Used http://cubic-bezier.com/
	 */
	public static Interpolator overlyEaseIn = Interpolator.SPLINE(.43, .65, .19, 1);
	public static int visualModeTimeout = 250;
	// -- graph visualization -- //
	public static int defaultCycles = 500;
	
	// -- edge colors -- //
	public static enum State {
		DEFAULT,
		ACTIVE(Color.valueOf("#2279e5"), Color.valueOf("#2279e5").darker()),
		OPEN_LIST(Color.valueOf("#00ff00"), Color.valueOf("#00ff00").darker()),
		
		;
		
		private Color lineColor;
		private Color textColor;
		
		private State() {}
		
		private State(Color lineColor, Color textColor) {
			this.lineColor = lineColor;
			this.textColor = textColor;
		}
		
		public Color getLineColor() {
			return lineColor;
		}
		
		public Color getTextColor() {
			return textColor;
		}
	}
}
