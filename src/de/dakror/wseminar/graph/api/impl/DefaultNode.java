package de.dakror.wseminar.graph.api.impl;

import java.util.ArrayList;
import java.util.List;

import de.dakror.wseminar.graph.api.Edge;
import de.dakror.wseminar.graph.api.Node;

/**
 * @author Dakror
 */
public class DefaultNode<T> implements Node<T> {
	ArrayList<Edge<T>> connections = new ArrayList<>();
	
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
	public List<Edge<T>> getConnections() {
		return connections;
	}
	
	@Override
	public T getStorage() {
		return storage;
	}
	
	@Override
	public void addConnection(Edge<T> connection) {
		connections.add(connection);
	}
	
	@Override
	public float getCost(Node<T> o) {
		for (Edge<T> c : connections) {
			if (c.getFrom().equals(o) || c.getTo().equals(o)) return c.getCost();
		}
		
		return 0;
	}
	
	@Override
	public String toString() {
		return "N[" + storage + ", " + walkable + ", " + connections + "]";
	}
}
