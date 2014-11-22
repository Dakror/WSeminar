package de.dakror.wseminar.graph;

import java.util.List;

/**
 * @author Dakror
 */
public interface Node<T> {
	public boolean isWalkable();
	
	public List<Connection<T>> getConnections();
	
	public T getStorage();
	
	/**
	 * @return the uniform weight of all connections with this node
	 */
	public float getWeight();
	
	public void addConnection(Connection<T> connection);
	
	/**
	 * @return the individual cost from <code>this</code> to <code>o</code>. if not connected return 0
	 */
	public float getCost(Node<T> o);
}
