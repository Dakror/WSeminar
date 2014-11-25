package de.dakror.wseminar.graph.api;

/**
 * @author Dakror
 */
public enum GraphType {
	ABSTRACT_GRAPH("Abstrakter Graph"),
	GRID("Raster-Netz"),
	LABYRINTH("Raster-Labyrinth"),
	
	;
	
	String title;
	
	private GraphType(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return title;
	}
}
