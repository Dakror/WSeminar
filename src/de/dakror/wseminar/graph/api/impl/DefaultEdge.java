package de.dakror.wseminar.graph.api.impl;

import de.dakror.wseminar.graph.api.Edge;
import de.dakror.wseminar.graph.api.Node;

/**
 * @author Dakror
 */
public class DefaultEdge<T> implements Edge<T> {
	float cost;
	
	Node<T> from, to;
	
	public DefaultEdge(float cost, Node<T> from, Node<T> to) {
		this.cost = cost;
		this.from = from;
		this.to = to;
	}
	
	@Override
	public float getCost() {
		return cost;
	}
	
	@Override
	public Node<T> getFrom() {
		return from;
	}
	
	@Override
	public Node<T> getTo() {
		return to;
	}
	
	@Override
	public String toString() {
		return "C[" + from.getStorage() + " -> " + to.getStorage() + ", " + cost + "]";
	}
}
