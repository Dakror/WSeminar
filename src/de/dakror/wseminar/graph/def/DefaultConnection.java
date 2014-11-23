package de.dakror.wseminar.graph.def;

import de.dakror.wseminar.graph.Connection;
import de.dakror.wseminar.graph.Node;

/**
 * @author Dakror
 */
public class DefaultConnection<T> implements Connection<T> {
	float cost;
	
	Node<T> from, to;
	
	public DefaultConnection(float cost, Node<T> from, Node<T> to) {
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
}
