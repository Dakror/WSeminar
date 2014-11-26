package de.dakror.wseminar.graph;

import java.util.ArrayList;
import java.util.Random;

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
	 * @param factory to create and manipulate storage data
	 * @return the generated graph
	 */
	public Graph<T> generateGraph(GraphType type, int size, long seed, Factory<T> factory) {
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
						storage = factory.create(x, y);
					} while (storages.contains(storage));
					
					Node<T> n = new DefaultNode<T>(storage);
					
					int connections = random.nextInt(CONNECTIONS_FACTOR);
					
					final T tmp = factory.create(0, 0);
					
					System.out.println(x + ", " + y);
					
					int radius = 1;
					while (n.getConnections().size() < connections) {
						if (radius > 10) break;
						
						for (int j = -radius; j <= radius; j++) {
							for (int k = -radius; k <= radius; k++) {
								if ((j == 0 && k == 0) || x + j < 0 || y + k < 0) continue;
								
								factory.set(tmp, x + j, y + k);
								
								Node<T> node = graph.getNode(tmp);
								if (node != null && !graph.areConnected(n, node)) {
									n.addConnection(new DefaultConnection<T>(random.nextInt(CONNECTION_COST_MAX - 1) + 1, n, node));
								}
							}
						}
						
						radius++;
					}
					
					// TODO: kinda not working LOL
					
					System.out.println("n has connections: " + n.getConnections().size());
					graph.addNode(n);
				}
				
				break;
			}
			case GRID: {
				throw new UnsupportedOperationException("Graph type not supported yet!");
			}
			case LABYRINTH: {
				throw new UnsupportedOperationException("Graph type not supported yet!");
			}
			default:
				break;
		}
		
		return graph;
	}
}
