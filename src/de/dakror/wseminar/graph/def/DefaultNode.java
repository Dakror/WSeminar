package de.dakror.wseminar.graph.def;

import java.util.ArrayList;
import java.util.List;

import de.dakror.wseminar.graph.Connection;
import de.dakror.wseminar.graph.Node;

/**
 * @author Dakror
 */
public class DefaultNode<T> implements Node<T> {
	ArrayList<Connection<T>> connections = new ArrayList<>();
	
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
	
	@Override
	public List<Connection<T>> getConnections() {
		return connections;
	}
	
	@Override
	public T getStorage() {
		return storage;
	}
	
	@Override
	public void addConnection(Connection<T> connection) {
		connections.add(connection);
	}
	
	@Override
	public float getCost(Node<T> o) {
		for (Connection<T> c : connections) {
			if (c.getFrom() == o || c.getTo() == o) return c.getCost();
		}
		
		return 0;
	}
}
