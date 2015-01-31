package de.dakror.wseminar.graph;

import java.util.Random;

/**
 * @author Dakror
 */
public enum GraphType {
	ABSTRACT_GRAPH("Abstrakter Graph") {
		@Override
		public Object supply(int nodeIndex, int totalNodes, Random r) {
			return nodeIndex;
		}
	},
	GRID("Raster-Netz"),
	LABYRINTH("Raster-Labyrinth"),
	
	;
	
	String title;
	
	private GraphType(String title) {
		this.title = title;
	}
	
	public Object supply(int nodeIndex, int totalNodes, Random r) {
		throw new UnsupportedOperationException("Graph type not supported yet!");
	}
	
	@Override
	public String toString() {
		return title;
	}
}
