package de.dakror.wseminar.graph.generate;

import java.util.HashMap;
import java.util.Random;

import de.dakror.wseminar.Const;
import de.dakror.wseminar.graph.DefaultGraph;
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.GraphType;
import de.dakror.wseminar.graph.WeightedEdge;

/**
 * @author Dakror
 */
public class GraphGenerator<V> {
	public static class Params {
		HashMap<String, Object> map = new HashMap<>();
		
		public Params put(String k, Object v) {
			map.put(k, v);
			return this;
		}
		
		public <T> T get(String k) {
			return orElse(k, null);
		}
		
		@SuppressWarnings("unchecked")
		public <T> T orElse(String k, T o) {
			if (map.containsKey(k)) return (T) map.get(k);
			else return o;
		}
	}
	
	/**
	 * @param type the desired type of graph
	 * @param size the size of the graph (1 - 3)
	 * @param seed the RNG seed
	 * @param factory to create storage data
	 * @return the generated graph
	 */
	@SuppressWarnings("unchecked")
	public Graph<V> generateGraph(Params params) {
		Graph<V> graph = null;
		
		long seed = params.get("seed");
		
		Random random = new Random(seed);
		
		switch ((GraphType) params.get("type")) {
			case ABSTRACT_GRAPH: {
				graph = new DefaultGraph<>();
				
				int nodeAmount = params.orElse("nodes", Const.nodeAmount);
				
				int nodes = (random.nextInt(nodeAmount / 2) + nodeAmount / 2) * (int) params.get("size");
				
				for (int i = 0; i < nodes; i++) {
					try {
						graph.addVertex((V) ((GraphType) params.get("type")).supply(i, nodes, random));
					} catch (Exception e) {
						throw new IllegalStateException("Generics not matching graph type!", e);
					}
				}
				
				System.out.println("Added " + graph.getVertices().size() + " nodes to the graph.");
				
				int edgesPlaced = 0;
				
				String[] weights = params.get("weights");
				
				for (int i = 0; i < nodes; i++) {
					int edges = Math.max(random.nextInt(params.orElse("edges", Const.edgeAmount)) * weights.length, 1);
					
					for (int j = 0; j < edges; j++) {
						int index = i;
						do {
							index = random.nextInt(nodes);
						} while (index == i || graph.areConnected(graph.getVertices().get(i), graph.getVertices().get(index)));
						
						Edge<V> edge = new WeightedEdge<V>(graph.getVertices().get(i), graph.getVertices().get(index), random.nextInt(Const.edgesMaxCost),
																								weights[random.nextInt(weights.length)]);
						
						// TODO: not 2 same types to the same node
						graph.addEdge(edge);
					}
					
					edgesPlaced += edges;
				}
				
				System.out.println("Made " + edgesPlaced + " connections.");
				
				break;
			}
			case GRID:
			case LABYRINTH: {
				throw new UnsupportedOperationException("Graph type not supported yet!");
			}
			default:
				break;
		}
		
		return graph;
	}
}
