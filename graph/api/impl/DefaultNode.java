package de.dakror.wseminar.graph.api.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.dakror.wseminar.graph.api.DirectedEdge;
import de.dakror.wseminar.graph.api.Edge;
import de.dakror.wseminar.graph.api.Node;

/**
 * @author Dakror
 */
public class DefaultNode<T> implements Node<T> {
	ArrayList<Edge<T>> edges = new ArrayList<>();
	
	T storage;
	boolean walkable;
	
	public DefaultNode(T storage) {
		this(storage, true);
	}
	
	public DefaultNode(T storage, boolean walkable) {
		this.storage = storage;
		this.walkable = walkable;
	}
	
	@Override
	public boolean isWalkable() {
		return walkable;
	}
	
	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}
	
	@Override
	public List<Edge<T>> getEdges() {
		return edges;
	}
	
	@Override
	public T getStorage() {
		return storage;
	}
	
	@Override
	public void addEdge(Edge<T> edge) {
		if (edge.getV() != this && edge.getU() != this) throw new IllegalArgumentException("Invalid edge for this node!");
		edges.add(edge);
	}
	
	@Override
	public float getCost(Node<T> o) {
		for (Edge<T> c : edges) {
			if ((c.getV().equals(o) && !(c instanceof DirectedEdge)) || c.getU().equals(o)) return c.getCost();
		}
		
		return 0;
	}
	
	@Override
	public String toString() {
		return "N[" + storage + ", " + walkable + ", " + edges + "]";
	}
	
	@Override
	public boolean removeEdge(Node<T> otherNode) {
		for (Iterator<Edge<T>> iter = edges.iterator(); iter.hasNext();) {
			Edge<T> e = iter.next();
			if (e.getV() == otherNode || e.getU() == otherNode) {
				iter.remove();
				return true;
			}
		}
		
		return false;
	}
}
