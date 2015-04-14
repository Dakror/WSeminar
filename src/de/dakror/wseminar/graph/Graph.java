package de.dakror.wseminar.graph;

import java.util.List;

/**
 * @author Maximilian Stark | Dakror
 */
public interface Graph<V> {
	public static enum GraphType {
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
	
	public List<Edge<V>> getEdges();
	
	public List<Edge<V>> getEdgesFrom(V from);
	
	public List<Edge<V>> getEdgesTo(V to);
	
	public List<Edge<V>> getEdges(V v);
	
	public List<V> getVertices();
	
	public void addEdge(Edge<V> edge);
	
	public boolean removeEdge(V from, V to);
	
	public void addVertex(V v);
	
	public boolean removeVertex(V v);
	
	public boolean areConnected(V from, V to);
	
	@SuppressWarnings("hiding")
	public <V> boolean isConnected(V v, Edge<V> edge);
	
	public Edge<V> getEdge(V from, V to);
	
	public float getWeight(V from, V to);
	
	public Graph<Vertex<V>> getVertexGraph(Class<?>... vertexData);
}
