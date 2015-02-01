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
public class FRLayout<V> implements Layout<V> {
	float k;
	
	/**
	 * To fake minimal distance between nodes
	 */
	final float EPSILON = 0.00001f;
	
	@Override
	public Graph<Vertex<V>> render(Graph<V> sourceGraph) {
		return render(sourceGraph, Const.defaultCycles);
	}
	
	@Override
	public Graph<Vertex<V>> render(Graph<V> sourceGraph, int maxCycles) {
		// -- init -- //
		Graph<Vertex<V>> graph = sourceGraph.getVertexGraph(Position.class, Disposition.class);
		// -- random init positions -- //
		for (Vertex<V> v : graph.getVertices())
			v.get(Position.class).pos.set((float) Math.random() * Const.gridColumns, (float) Math.random() * Const.gridRows);
		
		calculateK(graph.getVertices().size());
		
		// -- alg -- //
		
		float temperature = 1f / 10 * Const.gridColumns;
		
		float step = temperature / maxCycles;
		
		for (int i = 0; i < maxCycles; i++) {
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
				p.pos.x = Math.min(Const.gridColumns, Math.max(0, p.pos.x));
				p.pos.y = Math.min(Const.gridRows, Math.max(0, p.pos.y));
			}
			temperature -= step;
			
		}
		
		return graph;
	}
	
	/**
	 * k is the optimal distance between vertices
	 */
	void calculateK(int vertexCount) {
		k = (float) (0.75f * Math.sqrt((Const.gridColumns * Const.gridRows) / vertexCount));
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
