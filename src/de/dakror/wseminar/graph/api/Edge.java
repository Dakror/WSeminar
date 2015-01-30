package de.dakror.wseminar.graph.api;

/**
 * @author Dakror
 */
public interface Edge<T> {
	public float getCost();
	
	public Node<T> getFrom();
	
	public Node<T> getTo();
}
