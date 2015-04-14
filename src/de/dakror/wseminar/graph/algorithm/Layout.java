package de.dakror.wseminar.graph.algorithm;

import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.VertexData.Position;
import de.dakror.wseminar.math.Vector2;

/**
 * @author Maximilian Stark | Dakror
 */
public abstract class Layout<V> {
	/**
	 * To fake minimal distance between nodes
	 */
	protected final float EPSILON = 0.00001f;
	
	protected Graph<V> sourceGraph;
	protected Graph<Vertex<V>> graph;
	protected int maxCycles;
	protected long seed;
	protected boolean isInit = false;
	protected Random r;
	
	protected float minX = Integer.MAX_VALUE;
	protected float minY = Integer.MAX_VALUE;
	protected float maxX = 0;
	protected float maxY = 0;
	
	protected Bounds bounds;
	
	public DoubleProperty progress = new SimpleDoubleProperty(0);
	
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
		return render(true);
	}
	
	public Graph<Vertex<V>> render(boolean init) {
		return render(maxCycles, true);
	}
	
	public Graph<Vertex<V>> render(int steps, boolean init) {
		if (init || !isInit) init();
		isInit = true;
		progress.set(0);
		
		for (int i = 0; i < steps; i++) {
			step();
			progress.set(progress.get() + 1.0 / steps);
		}
		
		finish();
		progress.set(1);
		
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
