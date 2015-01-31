package de.dakror.wseminar.graph.render;

import de.dakror.wseminar.Const;
import de.dakror.wseminar.graph.DefaultGraph;
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.WeightedEdge;
import de.dakror.wseminar.math.Vector2;

/**
 * @author Maximilian Stark | Dakror
 */
public class FruchtermanReingoldAlgorithm<V> implements RenderAloghorithm<V> {
	public static class FRVertex<V> implements Vertex<V> {
		Vector2 pos;
		Vector2 disp;
		V storage;
		
		public FRVertex(V storage) {
			pos = new Vector2();
			disp = new Vector2();
			this.storage = storage;
		}
		
		public Vector2 getDisp() {
			return disp;
		}
		
		@Override
		public Vector2 getPos() {
			return pos;
		}
		
		@Override
		public V getStorage() {
			return storage;
		}
	}
	
	float k;
	
	@Override
	public Graph<Vertex<V>> render(Graph<V> sourceGraph) {
		return render(sourceGraph, Const.defaultCycles);
	}
	
	@Override
	public Graph<Vertex<V>> render(Graph<V> sourceGraph, int maxCycles) {
		// -- init -- //
		Graph<Vertex<V>> graph = new DefaultGraph<>();
		for (Edge<V> edge : sourceGraph.getEdges())
			graph.addEdge(edge instanceof WeightedEdge ? new WeightedEdge<Vertex<V>>(new FRVertex<V>(edge.getFrom()), new FRVertex<V>(edge.getTo()), ((WeightedEdge<V>) edge).getWeight()) : new Edge<Vertex<V>>(new FRVertex<V>(edge.getFrom()), new FRVertex<V>(edge.getTo())));
		
		calculateK(graph.getVertices().size());
		
		// -- alg -- //
		
		float temperature = 1f / 10 * Const.gridColumns;
		float step = 1f / maxCycles;
		
		for (int i = 0; i < maxCycles; i++) {
			// calc Fr
			for (Vertex<V> v : graph.getVertices()) {
				((FRVertex<V>) v).disp.zero();
				for (Vertex<V> u : graph.getVertices()) {
					if (u != v) {
						Vector2 delta = ((FRVertex<V>) v).pos.clone().sub(((FRVertex<V>) u).pos);
						((FRVertex<V>) v).disp.add(delta.normalize().scl(getFr(delta.length())));
					}
				}
			}
			
			// calc Fa
			for (Edge<Vertex<V>> e : graph.getEdges()) {
				Vector2 delta = ((FRVertex<V>) e.getFrom()).pos.clone().sub(((FRVertex<V>) e.getTo()).pos);
				((FRVertex<V>) e.getFrom()).disp.sub(delta.normalize().scl(getFa(delta.length())));
				((FRVertex<V>) e.getTo()).disp.add(delta.normalize().scl(getFa(delta.length())));
			}
			
			// adjustments
			for (Vertex<V> v : graph.getVertices()) {
				((FRVertex<V>) v).pos.add(((FRVertex<V>) v).disp.normalize().scl(Math.min(((FRVertex<V>) v).disp.length(), temperature)));
				((FRVertex<V>) v).pos.x = Math.min(Const.gridColumns / 2, Math.max(-Const.gridColumns / 2, ((FRVertex<V>) v).pos.x));
				((FRVertex<V>) v).pos.y = Math.min(Const.gridRows / 2, Math.max(-Const.gridRows / 2, ((FRVertex<V>) v).pos.y));
			}
			temperature -= step;
		}
		
		return graph;
	}
	
	/**
	 * k is the optimal distance between vertices
	 */
	void calculateK(int vertexCount) {
		k = (float) Math.sqrt((Const.gridColumns * Const.gridRows) / vertexCount);
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
		return -(k * k) / d;
	}
}
