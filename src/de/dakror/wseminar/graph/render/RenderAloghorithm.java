package de.dakror.wseminar.graph.render;

import de.dakror.wseminar.graph.Graph;

/**
 * @author Maximilian Stark | Dakror
 */
public interface RenderAloghorithm<V> {
	public Graph<Vertex<V>> render(Graph<V> sourceGraph);
	
	public Graph<Vertex<V>> render(Graph<V> sourceGraph, int maxCycles);
}
