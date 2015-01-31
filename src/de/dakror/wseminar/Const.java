package de.dakror.wseminar;

import javafx.animation.Interpolator;

/**
 * @author Maximilian Stark | Dakror
 */
public class Const {
	public static int gridColumns = 30;
	public static int gridRows = 18;
	
	// -- Graph Generation-- //
	public static int cellSize = 24;
	public static int nodeAmount = 25;
	public static int edges = 4;
	public static int edgesMaxCost = 30;
	
	// -- VFX / Animation -- //
	/**
	 * Sadly values above 1.0 are useless but who cares, amirite?
	 * Used http://cubic-bezier.com/
	 */
	public static Interpolator overlyEaseIn = Interpolator.SPLINE(.43, .65, .19, 1);
	
	// -- graph visualization -- //
	public static int defaultCycles = 75;
}