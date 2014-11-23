package de.dakror.wseminar.graph.def;

import java.util.ArrayList;
import java.util.List;

import de.dakror.wseminar.graph.Connection;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.GraphType;
import de.dakror.wseminar.graph.Node;

/**
 * Implementation of {@link GraphType#ABSTRACT_GRAPH}
 * 
 * @author Dakror
 */
public class DefaultGraph<T extends Node<T>> implements Graph<T> {
	
	ArrayList<Node<T>> nodes = new ArrayList<>();
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
		return n1.getCost(n2) > 0;
	}
	
	@Override
	public List<Node<T>> getNeighbors(Node<T> n) {
		ArrayList<Node<T>> list = new ArrayList<>();
		
		for (Connection<T> c : n.getConnections()) {
			list.add(c.getFrom() == n ? c.getTo() : c.getFrom());
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
		return nodes.get(storages.indexOf(l));
	}
}
