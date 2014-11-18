package de.dakror.wseminar.graph;

import java.util.List;

/**
 * @author Dakror
 */
public interface Node<T> {
	public boolean isWalkable();
	
	public List<Node<T>> getNeighbors();
	
	public T getLocation();
	
	public float getWeight();
}
