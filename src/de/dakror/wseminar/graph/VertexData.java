package de.dakror.wseminar.graph;

import de.dakror.wseminar.math.Vector2;

public class VertexData {
	public static class Delay {
		public float delay;
	}
	
	public static class Disposition {
		public Vector2 disp = new Vector2();
	}
	
	public static class Force {
		public Vector2 Fe = new Vector2(), Fr = new Vector2(), F = new Vector2();
	}
	
	public static class Position {
		public Vector2 pos = new Vector2();
	}
}
