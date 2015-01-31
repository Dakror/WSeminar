package de.dakror.wseminar.graph.api.impl;

import de.dakror.wseminar.graph.api.Node;

/**
 * @author Maximilian Stark | Dakror
 */
public class DefaultDirectedEdge<T> extends DefaultEdge<T> {
	public DefaultDirectedEdge(float cost, Node<T> from, Node<T> to) {
		super(cost, from, to);
	}
	
	@Override
	public String toString() {
		return "DE[" + v.getStorage() + " -> " + u.getStorage() + ", " + cost + "]";
	}
}
