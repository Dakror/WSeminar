package de.dakror.wseminar.graph;

/**
 * @author Dakror
 */
public interface Connection<T> {
	public float getCost();
	
	public Node<T> getFrom();
	
	public Node<T> getTo();
}
