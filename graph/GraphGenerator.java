package de.dakror.wseminar.graph;

import java.util.ArrayList;
import java.util.Random;

import de.dakror.wseminar.Const;
import de.dakror.wseminar.graph.api.Graph;
import de.dakror.wseminar.graph.api.GraphType;
import de.dakror.wseminar.graph.api.Node;
import de.dakror.wseminar.graph.api.impl.DefaultEdge;
import de.dakror.wseminar.graph.api.impl.DefaultGraph;
import de.dakror.wseminar.graph.api.impl.DefaultNode;

/**
 * @author Dakror
 */
public class GraphGenerator<T> {
	/**
	 * @param type the desired type of graph
	 * @param size the size of the graph (1 - 3)
	 * @param seed the RNG seed
	 * @param factory to create storage data
	 * @return the generated graph
	 */
	public Graph<T> generateGraph(GraphType type, int size, long seed) {
		Graph<T> graph = null;
		
		Random random = new Random(seed);
		
		switch (type) {
			case ABSTRACT_GRAPH: {
				graph = new DefaultGraph<>();
				
				int nodes = (random.nextInt(Const.nodeAmount / 2) + Const.nodeAmount / 2) * size;
				
				ArrayList<T> storages = new ArrayList<>();
				
				for (int i = 0; i < nodes; i++) {
					try {
						@SuppressWarnings("unchecked")
						T storage = (T) type.supply(i, nodes, random);
						storages.add(storage);
						graph.addNode(new DefaultNode<T>(storage));
					} catch (ClassCastException e) {
						throw new IllegalStateException("Generics not matching graph type!", e);
					}
				}
				
				System.out.println("Added " + graph.getNodes().size() + " nodes to the graph.");
				
				int edgesPlaced = 0;
				
				for (int i = 0; i < nodes; i++) {
					int edges = random.nextInt(Const.edges) + 1;
					for (int j = 0; j < edges; j++) {
						int index = i;
						do {
							index = random.nextInt(nodes);
						} while (index != i);
						
						Node<T> from = graph.getNode(storages.get(i));
						Node<T> to = graph.getNode(storages.get(index));
						from.addEdge(new DefaultEdge<T>(random.nextInt(Const.edgesMaxCost), from, to));
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
