package de.dakror.wseminar.graph.impl.abstr;

import java.util.ArrayList;
import java.util.List;

import de.dakror.wseminar.graph.Connection;
import de.dakror.wseminar.graph.Node;

/**
 * @author Dakror
 */
public class AbstractNode<T> implements Node<T> {
	ArrayList<Connection<T>> connections = new ArrayList<>();
	
	T storage;
	float weight;
	
	public AbstractNode(T storage) {
		this.storage = storage;
	}
	
	public AbstractNode(T storage, float weight) {
		this.storage = storage;
		this.weight = weight;
	}
	
	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	@Override
	public float getWeight() {
		return weight;
	}
	
	@Override
	public boolean isWalkable() {
		return weight > 0;
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
