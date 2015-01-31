package de.dakror.wseminar.graph.api.impl;

import java.util.ArrayList;
import java.util.List;

import de.dakror.wseminar.graph.api.DirectedEdge;
import de.dakror.wseminar.graph.api.Edge;
import de.dakror.wseminar.graph.api.Graph;
import de.dakror.wseminar.graph.api.GraphType;
import de.dakror.wseminar.graph.api.Node;

/**
 * Implementation of {@link GraphType#ABSTRACT_GRAPH}
 * 
 * @author Dakror
 */
public class DefaultGraph<T> implements Graph<T> {
	ArrayList<Node<T>> nodes = new ArrayList<>();
	ArrayList<Edge<T>> edges = new ArrayList<>();
	ArrayList<T> storages = new ArrayList<>();
	
	public DefaultGraph() {}
	
	@SafeVarargs
	public DefaultGraph(Node<T>... nodes) {
		for (Node<T> n : nodes)
			addNode(n);
	}
	
	@Override
	public List<Node<T>> getNodes() {
		return nodes;
	}
	
	@Override
	public boolean areConnected(Node<T> n1, Node<T> n2) {
		return n1.getCost(n2) > 0 || n2.getCost(n1) > 0;
	}
	
	@Override
	public List<Node<T>> getNeighbors(Node<T> n) {
		ArrayList<Node<T>> list = new ArrayList<>();
		
		for (Edge<T> c : n.getEdges()) {
			list.add(c.getV() == n ? c.getU() : c.getV());
		}
		
		return list;
	}
	
	@Override
	public void addNode(Node<T> n) {
		nodes.add(n);
		storages.add(n.getStorage());
	}
	
	@Override
	public boolean removeNode(Node<T> n) {
		return nodes.remove(n) && storages.remove(n.getStorage());
	}
	
	@Override
	public Node<T> getNode(T l) {
		int index = storages.indexOf(l);
		if (index < 0) return null;
		
		return nodes.get(index);
	}
	
	@Override
	public void addEdge(Edge<T> edge) {
		edge.getV().addEdge(edge);
		if (!(edge instanceof DirectedEdge)) edge.getU().addEdge(edge);
		edges.add(edge);
	}
	
	public boolean removeEdge(Edge<T> e) {
		return (e.getV().removeEdge(e.getU()) || e.getU().removeEdge(e.getV())) && edges.remove(e);
	}
	
	@Override
	public List<Edge<T>> getEdges() {
		return edges;
	}
}
