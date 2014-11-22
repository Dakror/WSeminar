package de.dakror.wseminar.graph.impl.abstr;

import de.dakror.wseminar.graph.Connection;
import de.dakror.wseminar.graph.Node;

/**
 * @author Dakror
 */
public class AbstractConnection<T> implements Connection<T> {
	float cost;
	
	Node<T> from, to;
	
	public AbstractConnection(float cost, Node<T> from, Node<T> to) {
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
