package de.dakror.wseminar.graph.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Path;

/**
 * @author Maximilian Stark | Dakror
 */
public class BritishMuseum<V> {
	Graph<V> graph;
	ArrayList<V> nodes;
	
	ArrayList<V> pathStub;
	
	
	
	boolean visualMode;
	
	public BritishMuseum(Graph<V> graph) {
		this.graph = graph;
		nodes = new ArrayList<>();
		pathStub = new ArrayList<>();
	}
	
	public Path<V> findPath(V from, V to) {
		if (from.equals(to)) return new Path<>();
		
		nodes.clear();
		pathStub.clear();
		
		takeStep(from, to);
		
		return new Path<>(nodes);
	}
	
	boolean takeStep(V node, V to) {
		if (pathStub.contains(node)) return false;
		
		nodes.add(node);
		if (node.equals(to)) return true;
		pathStub.add(node);
		
		List<Edge<V>> edges = graph.getEdges(node);
		Collections.sort(edges);
		
		for (Edge<V> e : edges) {
			if (takeStep(e.getOtherEnd(node), to)) {
				pathStub.remove(node);
				return true;
			}
		}
		
		pathStub.remove(node);
		nodes.remove(node);
		return false;
	}
	
	public void setVisualMode(boolean visualMode) {
		this.visualMode = visualMode;
	}
	
	public boolean isVisualMode() {
		return visualMode;
	}
	
	public Graph<V> getGraph() {
		return graph;
	}
}
