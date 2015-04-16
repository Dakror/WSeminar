package de.dakror.wseminar.graph.algorithm;

import java.util.EnumMap;

import de.dakror.wseminar.Const;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Path;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.generate.Params;

/**
 * @author Maximilian Stark | Dakror
 */
public abstract class PathFinder<V> {
	public enum Modes {
		optimize_weight,
		optimize_node_count,
		optimize_turns,
	}
	
	public static class PathFinderParams extends Params<Modes> {
		public PathFinderParams() {
			map = new EnumMap<Modes, Object>(Modes.class);
		}
	}
	
	Graph<Vertex<V>> graph;
	boolean visualMode;
	
	
	public void setVisualMode(boolean visualMode) {
		this.visualMode = visualMode;
	}
	
	public boolean isVisualMode() {
		return visualMode;
	}
	
	public Graph<Vertex<V>> getGraph() {
		return graph;
	}
	
	public abstract Path<Vertex<V>> findPath(Vertex<V> from, Vertex<V> to);
	
	protected boolean takeStep(Vertex<V> node, Vertex<V> to) {
		if (visualMode) {
			try {
				Thread.sleep(Const.visualModeTimeout);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		return false;
	}
}
