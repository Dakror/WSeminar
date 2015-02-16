package de.dakror.wseminar.graph;


/**
 * @author Dakror
 */
public enum GraphType {
	ABSTRACT_GRAPH("Abstrakter Graph") {
		@Override
		public Object supply(int nodeIndex, int totalNodes) {
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
	
	public Object supply(int nodeIndex, int totalNodes) {
		throw new UnsupportedOperationException("Graph type not supported yet!");
	}
	
	@Override
	public String toString() {
		return title;
	}
}
