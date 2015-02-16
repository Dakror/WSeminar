package de.dakror.wseminar.graph.layout;

import java.util.Random;

import de.dakror.wseminar.Const;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;

/**
 * @author Maximilian Stark | Dakror
 */
public abstract class Layout<V> {
	/**
	 * To fake minimal distance between nodes
	 */
	final float EPSILON = 0.00001f;
	
	Graph<V> sourceGraph;
	Graph<Vertex<V>> graph;
	int maxCycles;
	long seed;
	boolean isInit = false;
	Random r;
	
	public Layout(Graph<V> sourceGraph) {
		this(sourceGraph, Const.defaultCycles);
	}
	
	public Layout(Graph<V> sourceGraph, int maxCycles) {
		this(sourceGraph, maxCycles, (long) (Math.random() * Long.MAX_VALUE));
	}
	
	public Layout(Graph<V> sourceGraph, int maxCycles, long seed) {
		this.sourceGraph = sourceGraph;
		this.maxCycles = maxCycles;
		if (seed != WSeminar.seed) {
			this.seed = seed;
			this.r = new Random(seed);
		} else r = WSeminar.r;
	}
	
	public Graph<Vertex<V>> render() {
		init();
		isInit = true;
		
		for (int i = 0; i < maxCycles; i++)
			step();
		
		finish();
		
		return graph;
	}
	
	public Graph<Vertex<V>> render(int customSteps) {
		if (!isInit) init();
		isInit = true;
		
		for (int i = 0; i < customSteps; i++)
			step();
		
		finish();
		
		return graph;
	}
	
	public abstract void init();
	
	public abstract void step();
	
	public void finish() {}
	
	public Graph<Vertex<V>> getGraph() {
		return graph;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public Graph<V> getSourceGraph() {
		return sourceGraph;
	}
	
	public int getMaxCycles() {
		return maxCycles;
	}
}
