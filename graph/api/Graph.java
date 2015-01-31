package de.dakror.wseminar.graph.api;

import java.util.List;

/**
 * @author Dakror
 */
public interface Graph<T> {
	public List<Node<T>> getNodes();
	
	public List<Edge<T>> getEdges();
	
	public boolean areConnected(Node<T> n1, Node<T> n2);
	
	public List<Node<T>> getNeighbors(Node<T> n);
	
	public void addNode(Node<T> n);
	
	public boolean removeNode(Node<T> n);
	
	public void addEdge(Edge<T> edge);
	
	/**
	 * Should be null-safe
	 */
	public Node<T> getNode(T l);
}
