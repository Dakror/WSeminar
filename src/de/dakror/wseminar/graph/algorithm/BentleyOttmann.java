package de.dakror.wseminar.graph.algorithm;

import java.util.TreeSet;

import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.vertexdata.Position;
import de.dakror.wseminar.math.Vector2;

/**
 * @author Maximilian Stark | Dakror
 */
public class BentleyOttmann<V extends Vertex<V>> {
	/**
	 * @author Maximilian Stark | Dakror
	 */
	public static enum EventType {
		START,
		END,
		INTERSECT;
	}
	
	/**
	 * @author Maximilian Stark | Dakror
	 */
	public class Event {
		EventType type;
		boolean sweeping;
		final Vector2 pos = new Vector2();
		Edge<Vertex<V>> edge;
		
		public Event(Edge<Vertex<V>> edge) {
			this(edge, EventType.INTERSECT);
		}
		
		public Event(Edge<Vertex<V>> edge, EventType type) {
			this(edge, type, null);
		}
		
		public Event(Edge<Vertex<V>> edge, EventType type, Vector2 pos) {
			this.edge = edge;
			this.type = type;
			this.pos.set(pos);
		}
		
		public Edge<Vertex<V>> getEdge() {
			return edge;
		}
		
		public EventType getType() {
			return type;
		}
		
		public Vector2 getPos() {
			return pos;
		}
		
		public boolean isSweeping() {
			return sweeping;
		}
	}
	
	TreeSet<Event> events;
	TreeSet<Event> sweepLine;
	
	public BentleyOttmann(Graph<Vertex<V>> graph) {
		events = new TreeSet<Event>((a, b) -> a.pos.compareTo(b.pos));
		
		for (Edge<Vertex<V>> e : graph.getEdges()) {
			events.add(new Event(e, EventType.START, e.getFrom().get(Position.class).pos));
			events.add(new Event(e, EventType.END, e.getTo().get(Position.class).pos));
		}
	}
	
	public int getIntersections() {
		int inters = 0;
		
		while (!events.isEmpty()) {
			Event event = events.pollFirst();
			
		}
		
		return inters;
	}
}
