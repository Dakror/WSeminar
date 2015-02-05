package de.dakror.wseminar.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Maximilian Stark | Dakror
 */
public class DefaultGraph<V> implements Graph<V> {
	List<Edge<V>> edges = new ArrayList<>();
	List<V> vertices = new ArrayList<>();
	
	@Override
	public List<Edge<V>> getEdges() {
		return edges;
	}
	
	@Override
	public List<Edge<V>> getEdgesFrom(V from) {
		return edges.stream().filter(edge -> edge.getFrom().equals(from)).collect(Collectors.toList()); // Java8 OP
	}
	
	@Override
	public List<Edge<V>> getEdgesTo(V to) {
		return edges.stream().filter(edge -> edge.getTo().equals(to)).collect(Collectors.toList()); // Java8 OP
	}
	
	@Override
	public List<V> getVertices() {
		return vertices;
	}
	
	@Override
	public void addEdge(Edge<V> edge) {
		if (!containsVertex(edge.getFrom())) addVertex(edge.getFrom());
		if (!containsVertex(edge.getTo())) addVertex(edge.getTo());
		edges.add(edge);
	}
	
	@Override
	public boolean removeEdge(V from, V to) {
		return edges.removeIf(e -> e.getFrom().equals(from) && e.getTo().equals(to)); // java8 OP
	}
	
	public boolean containsEdge(Edge<V> edge) {
		return edges.contains(edge);
	}
	
	@Override
	public void addVertex(V v) {
		vertices.add(v);
	}
	
	@Override
	public boolean removeVertex(V v) {
		return edges.removeIf(e -> e.getFrom().equals(v) || e.getTo().equals(v)) && vertices.add(v);
	}
	
	public boolean containsVertex(V v) {
		return vertices.contains(v);
	}
	
	@Override
	public boolean areConnected(V from, V to) {
		return getEdge(from, to) != null;
	}
	
	@Override
	public Edge<V> getEdge(V from, V to) {
		return edges.stream().filter(e -> e.isDirected() ? e.getFrom().equals(from) && e.getTo().equals(to) : (e.getFrom().equals(from) || e.getFrom().equals(to)) && (e.getTo().equals(from) || e.getTo().equals(to))).findFirst().orElse(null);
	}
	
	@Override
	public float getWeight(V from, V to) {
		Edge<V> edge = getEdge(from, to);
		return edge instanceof WeightedEdge ? ((WeightedEdge<V>) edge).getWeight() : 0;
	}
	
	@Override
	public Graph<Vertex<V>> getVertexGraph(Class<?>... vertexData) {
		Graph<Vertex<V>> graph = new DefaultGraph<>();
		for (V v : vertices) {
			Vertex<V> vertex = new Vertex<V>(v);
			vertex.decorate(vertexData);
			graph.addVertex(vertex);
		}
		for (Edge<V> edge : edges) {
			Vertex<V> from = graph.getVertices().get(vertices.indexOf(edge.getFrom()));
			Vertex<V> to = graph.getVertices().get(vertices.indexOf(edge.getTo()));
			
			graph.addEdge(edge instanceof WeightedEdge ? new WeightedEdge<Vertex<V>>(from, to, ((WeightedEdge<V>) edge).getWeight()) : new Edge<Vertex<V>>(from, to));
		}
		return graph;
	}
	
	@SuppressWarnings("hiding")
	@Override
	public <V> boolean isConnected(V v, Edge<V> edge) {
		
		return edge.isDirected() ? edge.getFrom().equals(v) : edge.getFrom().equals(v) || edge.getTo().equals(v);
	}
}
