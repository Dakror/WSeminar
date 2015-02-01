package de.dakror.wseminar.graph.layout;

import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;

/**
 * @author Maximilian Stark | Dakror
 */
public interface Layout<V> {
	public Graph<Vertex<V>> render(Graph<V> sourceGraph);
	
	public Graph<Vertex<V>> render(Graph<V> sourceGraph, int maxCycles);
}
