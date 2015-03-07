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
	
	public Edge<V> setFrom(V from) {
		this.from = from;
		return this;
	}
	
	public V getTo() {
		return to;
	}
	
	public Edge<V> setTo(V to) {
		this.to = to;
		return this;
	}
	
	public Edge<V> setDirected(boolean directed) {
		this.directed = directed;
		return this;
	}
	
	public V getOtherEnd(V end) {
		if (end.equals(from)) return to;
		else return from;
	}
	
	public boolean isDirected() {
		return directed;
	}
	
	@Override
	public String toString() {
		return "E[" + from + " " + (directed ? "" : "<") + "-> " + to + "]";
	}
}
