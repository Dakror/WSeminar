package de.dakror.wseminar.graph;

import java.util.List;

/**
 * @author Maximilian Stark | Dakror
 */
public interface Graph<V> {
	public List<Edge<V>> getEdges();
	
	public List<Edge<V>> getEdgesFrom(V from);
	
	public List<Edge<V>> getEdgesTo(V to);
	
	public List<V> getVertices();
	
	public void addEdge(Edge<V> edge);
	
	public boolean removeEdge(V from, V to);
	
	public void addVertex(V v);
	
	public boolean removeVertex(V v);
	
	public boolean areConnected(V from, V to);
	
	public Edge<V> getEdge(V from, V to);
	
	public float getWeight(V from, V to);
	
	public Graph<Vertex<V>> getVertexGraph(Class<?>... vertexData);
}
