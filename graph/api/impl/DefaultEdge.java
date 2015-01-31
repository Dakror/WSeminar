package de.dakror.wseminar.graph.api.impl;

import de.dakror.wseminar.graph.api.Edge;
import de.dakror.wseminar.graph.api.Node;

/**
 * @author Dakror
 */
public class DefaultEdge<T> implements Edge<T> {
	float cost;
	
	protected Node<T> v, u;
	
	public DefaultEdge(float cost, Node<T> v, Node<T> u) {
		this.cost = cost;
		this.v = v;
		this.u = u;
	}
	
	@Override
	public float getCost() {
		return cost;
	}
	
	@Override
	public Node<T> getV() {
		return v;
	}
	
	@Override
	public Node<T> getU() {
		return u;
	}
	
	@Override
	public String toString() {
		return "E[" + v.getStorage() + " <-> " + u.getStorage() + ", " + cost + "]";
	}
}
