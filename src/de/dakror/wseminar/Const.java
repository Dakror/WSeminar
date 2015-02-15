package de.dakror.wseminar;

import javafx.animation.Interpolator;
import javafx.scene.paint.Color;

/**
 * @author Maximilian Stark | Dakror
 */
public class Const {
	public static int gridColumns = 40;
	public static int gridRows = 25;
	
	// -- Graph Generation-- //
	public static int cellSize = 24;
	public static int nodeAmount = 20;
	public static int edgeAmount = 3;
	public static int edgesMaxCost = 30;
	
	// -- VFX / Animation -- //
	/**
	 * Sadly values above 1.0 are useless but who cares, amirite?
	 * Used http://cubic-bezier.com/
	 */
	public static Interpolator overlyEaseIn = Interpolator.SPLINE(.43, .65, .19, 1);
	
	// -- graph visualization -- //
	public static int defaultCycles = 3000;
	
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
