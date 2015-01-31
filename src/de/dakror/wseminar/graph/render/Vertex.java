package de.dakror.wseminar.graph.render;

import de.dakror.wseminar.math.Vector2;

/**
 * @author Maximilian Stark | Dakror
 */
public interface Vertex<T> {
	public Vector2 getPos();
	
	public T getStorage();
}
