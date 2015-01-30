package de.dakror.wseminar.graph.api;

import java.util.List;

/**
 * @author Dakror
 */
public interface Node<T> {
	public boolean isWalkable();
	
	public List<Edge<T>> getConnections();
	
	public T getStorage();
	
	public void addConnection(Edge<T> connection);
	
	/**
	 * @return the individual cost from <code>this</code> to <code>o</code>. if not connected return 0
	 */
	public float getCost(Node<T> o);
}
