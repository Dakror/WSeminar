package de.dakror.wseminar.graph.layout;

import java.util.Random;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.vertexdata.Position;
import de.dakror.wseminar.math.Vector2;

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
	
	float minX = Integer.MAX_VALUE;
	float minY = Integer.MAX_VALUE;
	float maxX = 0;
	float maxY = 0;
	
	Bounds bounds;
	
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
	
	public void finish() {
		reposition();
	}
	
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
	
	protected void reposition() {
		graph.getVertices().forEach(v -> {
			Vector2 p = v.get(Position.class).pos;
			if (p.x < minX) minX = p.x;
			if (p.y < minY) minY = p.y;
			if (p.x > maxX) maxX = p.x;
			if (p.y > maxY) maxY = p.y;
		});
		
		bounds = new BoundingBox(minX, minY, maxX - minX, maxY - minY);
		
		graph.getVertices().forEach(v -> v.get(Position.class).pos.sub(minX, minY));
	}
	
	public Bounds getBounds() {
		return bounds;
	}
}
