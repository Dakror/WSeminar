package de.dakror.wseminar.graph;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiFunction;

import de.dakror.wseminar.graph.api.Connection;
import de.dakror.wseminar.graph.api.Graph;
import de.dakror.wseminar.graph.api.GraphType;
import de.dakror.wseminar.graph.api.Node;
import de.dakror.wseminar.graph.api.def.DefaultConnection;
import de.dakror.wseminar.graph.api.def.DefaultGraph;
import de.dakror.wseminar.graph.api.def.DefaultNode;

/**
 * @author Dakror
 */
public class GraphGenerator<T> {
	public static int GRID_COLUMNS = 20;
	public static int GRID_ROWS = 12;
	public static int ABSTRACT_GRAPH_CELL_SIZE = 24;
	
	public static int NODE_AMOUNT_FACTOR = 8;
	public static int CONNECTIONS_FACTOR = 4;
	public static int CONNECTION_COST_MAX = 30;
	
	
	/**
	 * @param type the desired type of graph
	 * @param size the size of the graph (1 - 3)
	 * @param seed the RNG seed
	 * @param factory to create storage data
	 * @return the generated graph
	 */
	public Graph<T> generateGraph(GraphType type, int size, long seed, BiFunction<Integer, Integer, T> factory) {
		Graph<T> graph = null;
		
		Random random = new Random(seed);
		
		switch (type) {
			case ABSTRACT_GRAPH: {
				graph = new DefaultGraph<>();
				
				int nodes = (random.nextInt(NODE_AMOUNT_FACTOR / 2) + NODE_AMOUNT_FACTOR / 2) * size;
				
				ArrayList<T> storages = new ArrayList<>();
				
				int x = 0, y = 0;
				
				for (int i = 0; i < nodes; i++) {
					T storage = null;
					do {
						x = random.nextInt(GRID_COLUMNS);
						y = random.nextInt(GRID_ROWS);
						storage = factory.apply(x, y);
					} while (storages.contains(storage));
					
					graph.addNode(new DefaultNode<T>(storage));
				}
				
				System.out.println("Added " + graph.getNodes().size() + " nodes to the graph.");
				
				int con = 0;
				
				for (Node<T> n : graph.getNodes()) {
					int connections = random.nextInt(CONNECTIONS_FACTOR);
					
					int radius = 1;
					while (n.getConnections().size() < connections) {
						if (radius > 10) break;
						
						for (int j = -radius; j <= radius; j++) {
							for (int k = -radius; k <= radius; k++) {
								if ((j == 0 && k == 0) || x + j < 0 || y + k < 0 || x + j > GRID_COLUMNS || y + k > GRID_ROWS) continue;
								
								T tmp = factory.apply(x + j, y + k);
								
								Node<T> node = graph.getNode(tmp);
								if (node != null && !graph.areConnected(n, node)) {
									Connection<T> c = new DefaultConnection<T>(random.nextInt(CONNECTION_COST_MAX - 1) + 1, n, node);
									n.addConnection(c);
									node.addConnection(c);
									
									con++;
								}
							}
						}
						
						radius++;
					}
				}
				
				System.out.println("Made " + con + " connections.");
				
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
