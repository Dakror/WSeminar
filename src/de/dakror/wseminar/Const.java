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
	
	// -- graph visualization -- //
	public static int defaultCycles = 300;
	
	// -- edge colors -- //
	public static enum State {
		DEFAULT,
		ACTIVE(Color.valueOf("#2279e5")),
		
		;
		
		private Color color;
		
		private State() {}
		
		private State(Color color) {
			this.color = color;
		}
		
		public Color getColor() {
			return color;
		}
	}
}
