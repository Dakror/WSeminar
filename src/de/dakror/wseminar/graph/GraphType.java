package de.dakror.wseminar.graph;

/**
 * @author Dakror
 */
public enum GraphType {
	ABSTRACT_GRAPH("Abstrakter Graph"),
	GRID("Raster-Netz"),
	
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
