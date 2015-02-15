package de.dakror.wseminar.graph.layout;

import de.dakror.wseminar.Const;
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.vertexdata.Disposition;
import de.dakror.wseminar.graph.vertexdata.Position;
import de.dakror.wseminar.math.Vector2;

/**
 * @author Maximilian Stark | Dakror
 */
public class FRLayout<V> extends Layout<V> {
	float k;
	
	int width, height;
	
	public FRLayout(Graph<V> sourceGraph) {
		this(sourceGraph, Const.defaultCycles);
	}
	
	public FRLayout(Graph<V> sourceGraph, int maxCycles) {
		this(sourceGraph, maxCycles, (long) (Math.random() * Long.MAX_VALUE));
	}
	
	public FRLayout(Graph<V> sourceGraph, int maxCycles, long seed) {
		this(sourceGraph, maxCycles, seed, Const.gridColumns, Const.gridRows);
	}
	
	public FRLayout(Graph<V> sourceGraph, int maxCycles, long seed, int width, int height) {
		super(sourceGraph, maxCycles, seed);
		this.width = width;
		this.height = height;
	}
	
	public FRLayout(Graph<V> sourceGraph, int maxCycles, long seed, int size) {
		this(sourceGraph, maxCycles, seed, Const.gridColumns * size, Const.gridRows * size);
	}
	
	@Override
	public void init() {
		graph = sourceGraph.getVertexGraph(Position.class, Disposition.class);
		// -- random init positions -- //
		for (Vertex<V> v : graph.getVertices())
			v.get(Position.class).pos.set(r.nextFloat() * width, r.nextFloat() * height);
		
		calculateK(graph.getVertices().size());
	}
	
	@Override
	public void step() {
		float temperature = 1f / 10 * width;
		
		float step = temperature / maxCycles;
		
		// calc Fr
		for (Vertex<V> v : graph.getVertices()) {
			Disposition d = v.get(Disposition.class);
			d.disp.zero();
			for (Vertex<V> u : graph.getVertices()) {
				if (u != v) {
					Vector2 delta = v.get(Position.class).pos.clone().sub(u.get(Position.class).pos);
					float length = Math.max(EPSILON, delta.length());
					float fr = getFr(length);
					d.disp.add(delta.x / length * fr, delta.y / length * fr);
				}
			}
		}
		
		// calc Fa
		for (Edge<Vertex<V>> e : graph.getEdges()) {
			Vector2 delta = e.getFrom().get(Position.class).pos.clone().sub(e.getTo().get(Position.class).pos);
			float length = Math.max(EPSILON, delta.length());
			float fa = getFa(length);
			
			float dx = delta.x / length * fa;
			float dy = delta.y / length * fa;
			
			e.getFrom().get(Disposition.class).disp.sub(dx, dy);
			e.getTo().get(Disposition.class).disp.add(dx, dy);
		}
		
		// adjustments
		for (Vertex<V> v : graph.getVertices()) {
			Position p = v.get(Position.class);
			Disposition d = v.get(Disposition.class);
			
			float length = Math.max(EPSILON, d.disp.length());
			float min = Math.min(length, temperature);
			
			p.pos.add(d.disp.x / length * min, d.disp.y / length * min);
			p.pos.x = Math.min(width, Math.max(0, p.pos.x));
			p.pos.y = Math.min(height, Math.max(0, p.pos.y));
		}
		temperature -= step;
	}
	
	/**
	 * k is the optimal distance between vertices
	 */
	void calculateK(int vertexCount) {
		k = (float) (0.75f * Math.sqrt((width * height) / vertexCount));
	}
	
	/**
	 * Attractive force
	 * 
	 * @param d
	 * @return
	 */
	float getFa(float d) {
		return (d * d) / k;
	}
	
	/**
	 * Repulsive force
	 * 
	 * @param d
	 * @return
	 */
	float getFr(float d) {
		return (k * k) / d;
	}
}
