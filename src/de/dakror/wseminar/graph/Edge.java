package de.dakror.wseminar.graph;

/**
 * @author Maximilian Stark | Dakror
 */
public class Edge<V> {
	V from, to;
	boolean directed;
	
	public Edge(V from, V to) {
		this.from = from;
		this.to = to;
	}
	
	public Edge(V from, V to, boolean directed) {
		this.from = from;
		this.to = to;
		this.directed = directed;
	}
	
	public V getFrom() {
		return from;
	}
	
	public V getTo() {
		return to;
	}
	
	public void setDirected(boolean directed) {
		this.directed = directed;
	}
	
	public boolean isDirected() {
		return directed;
	}
}
