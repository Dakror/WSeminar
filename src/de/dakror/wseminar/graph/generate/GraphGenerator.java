package de.dakror.wseminar.graph.generate;

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
	/**
	 * @param type the desired type of graph
	 * @param size the size of the graph (1 - 3)
	 * @param seed the RNG seed
	 * @param factory to create storage data
	 * @return the generated graph
	 */
	@SuppressWarnings("unchecked")
	public Graph<V> generateGraph(GraphType type, int size, long seed, String[] weights) {
		Graph<V> graph = null;
		
		Random random = new Random(seed);
		
		switch (type) {
			case ABSTRACT_GRAPH: {
				graph = new DefaultGraph<>();
				
				int nodes = (random.nextInt(Const.nodeAmount / 2) + Const.nodeAmount / 2) * size;
				
				for (int i = 0; i < nodes; i++) {
					try {
						graph.addVertex((V) type.supply(i, nodes, random));
					} catch (Exception e) {
						throw new IllegalStateException("Generics not matching graph type!", e);
					}
				}
				
				System.out.println("Added " + graph.getVertices().size() + " nodes to the graph.");
				
				int edgesPlaced = 0;
				
				for (int i = 0; i < nodes; i++) {
					int edges = Math.max(random.nextInt(Const.edges), 1);
					for (int j = 0; j < edges; j++) {
						int index = i;
						do {
							index = random.nextInt(nodes);
						} while (index == i || graph.areConnected(graph.getVertices().get(i), graph.getVertices().get(index)));
						
						Edge<V> edge = new WeightedEdge<V>(graph.getVertices().get(i), graph.getVertices().get(index), random.nextInt(Const.edgesMaxCost));
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
