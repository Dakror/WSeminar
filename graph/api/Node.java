package de.dakror.wseminar.graph.api;

import java.util.List;

/**
 * @author Dakror
 */
public interface Node<T> {
	public boolean isWalkable();
	
	public List<Edge<T>> getEdges();
	
	public T getStorage();
	
	public void addEdge(Edge<T> edge);
	
	/**
	 * Removes the first edge found between this and otherNode
	 * 
	 * @param otherNode
	 * @return
	 */
	public boolean removeEdge(Node<T> otherNode);
	
	/**
	 * @return the individual cost from <code>this</code> to <code>o</code>. if not connected return 0
	 */
	public float getCost(Node<T> o);
}
