package de.dakror.wseminar.graph;

import java.util.Random;

import de.dakror.wseminar.graph.api.Graph;
import de.dakror.wseminar.graph.api.GraphType;
import de.dakror.wseminar.graph.api.def.DefaultGraph;

/**
 * @author Dakror
 */
public class GraphGenerator<T> {
	final int NODE_AMOUNT_FACTOR = 8;
	final int ABSTRACT_GRAPH_CELL_SIZE = 24;
	
	/**
	 * @param type the desired type of graph
	 * @param size the size of the graph (1 - 3)
	 * @param seed the RNG seed
	 * @return the generated graph
	 */
	public Graph<T> generateGraph(GraphType type, int size, long seed) {
		Graph<T> graph = null;
		
		Random random = new Random(seed);
		
		switch (type) {
			case ABSTRACT_GRAPH: {
				graph = new DefaultGraph<>();
				
				int nodes = (random.nextInt(NODE_AMOUNT_FACTOR / 2) + NODE_AMOUNT_FACTOR / 2) * size;
				
				
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
