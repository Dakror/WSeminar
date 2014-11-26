package de.dakror.wseminar.graph.api;

import java.util.List;

/**
 * @author Dakror
 */
public interface Graph<T> {
	public List<Node<T>> getNodes();
	
	public boolean areConnected(Node<T> n1, Node<T> n2);
	
	public List<Node<T>> getNeighbors(Node<T> n);
	
	public void addNode(Node<T> n);
	
	public boolean removeNode(Node<T> n);
	
	/**
	 * Should be null-safe
	 */
	public Node<T> getNode(T l);
}
