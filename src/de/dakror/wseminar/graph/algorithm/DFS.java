/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.wseminar.graph.algorithm;

import de.dakror.wseminar.Const.State;
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Path;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.VertexData;
import de.dakror.wseminar.graph.VertexData.PathCommons;
import de.dakror.wseminar.graph.algorithm.common.PathFinder;
import de.dakror.wseminar.util.Visualizer;

/**
 * @author Maximilian Stark | Dakror
 */
public class DFS<V> extends PathFinder<V> {
	public DFS(Graph<Vertex<V>> graph) {
		super(graph);
	}
	
	Vertex<V> from;
	
	@SuppressWarnings("unchecked")
	@Override
	public Path<Vertex<V>> findPath(Vertex<V> from, Vertex<V> to) {
		Visualizer.resetAll(graph, true);
		
		this.from = from;
		if (!takeStep(from, to)) System.out.println("say what?");
		
		Path<Vertex<V>> p = new Path<Vertex<V>>();
		Vertex<V> v = to;
		
		while (v.get(PathCommons.class).parent != null) {
			p.add(0, v);
			v = v.get(PathCommons.class).parent;
		}
		p.add(0, from);
		
		cleanup();
		Visualizer.resetAll(graph, false);
		
		return p;
	}
	
	@Override
	protected boolean takeStep(Vertex<V> node, Vertex<V> to) {
		node.add(new PathCommons<V>());
		Visualizer.setVertexState(node, State.OPENLIST, false);
		
		if (node.equals(to)) return true;
		
		// select next edges
		for (Edge<Vertex<V>> e : graph.getEdges(node)) {
			Vertex<V> oe = e.getOtherEnd(node);
			if (oe.get(PathCommons.class) == null) {
				if (oe.equals(from)) {
					System.out.println(oe.get(PathCommons.class));
					System.err.println("gawgijurhe");
				}
				Visualizer.setEdgeActive(e, true);
			}
		}
		
		// is target reachable?
		for (Edge<Vertex<V>> e : graph.getEdges(node)) {
			Vertex<V> oe = e.getOtherEnd(node);
			if (oe.get(PathCommons.class) == null) {
				if (oe.equals(to)) {
					Visualizer.setVertexState(node, State.CLOSEDLIST);
					Visualizer.setEdgePath(e, true);
					return takeStep(oe, to);
				}
			}
		}
		
		// take next step
		for (Edge<Vertex<V>> e : graph.getEdges(node)) {
			Vertex<V> oe = e.getOtherEnd(node);
			if (oe.get(PathCommons.class) == null) {
				Visualizer.tick();
				
				Visualizer.setVertexState(node, State.CLOSEDLIST);
				Visualizer.setEdgePath(e, true);
				
				for (Edge<Vertex<V>> e1 : graph.getEdges(node)) {
					if (e1 == e) continue;
					Vertex<V> oe1 = e1.getOtherEnd(node);
					if (oe1.get(PathCommons.class) == null) {
						Visualizer.setEdgeActive(e1, false, false);
					}
				}
				
				
				if (takeStep(oe, to)) return true;
				
				for (Edge<Vertex<V>> e1 : graph.getEdges(node)) {
					if (e1 == e) continue;
					Vertex<V> oe1 = e1.getOtherEnd(node);
					if (oe1.get(PathCommons.class) == null) {
						Visualizer.setEdgeActive(e1, true, false);
					}
				}
				
				Visualizer.setEdgePath(e, false);
			}
		}
		Visualizer.setVertexState(node, State.BACKTRACK, false);
		return false;
		
		/*Visualizer.setVertexState(node, State.OPENLIST, false);
		
		if (node.equals(to)) return true;
		List<Edge<Vertex<V>>> edges = graph.getEdges(node).stream().filter(e -> {
			boolean free = isNotVisited(e.getOtherEnd(node));
			Visualizer.setEdgeActive(e, free);
			return free;
		}).sorted((a, b) -> Float.compare(a instanceof WeightedEdge ? ((WeightedEdge<Vertex<V>>) a).getWeight() : 0,
																			b instanceof WeightedEdge ? ((WeightedEdge<Vertex<V>>) b).getWeight() : 0)).collect(Collectors.toList());
																			
																			
		// goto target, if it is available
		for (Edge<Vertex<V>> e : edges) {
			Vertex<V> oe = e.getOtherEnd(node);
			if (oe.equals(to)) {
				for (Edge<Vertex<V>> e1 : edges) {
					if (e1 != e) Visualizer.setEdgeActive(e1, false, false);
				}
				
				Visualizer.setVertexState(node, State.CLOSEDLIST, false);
				Visualizer.setEdgePath(e, true, true);
				PathCommons<V> pc = new VertexData.PathCommons<V>();
				pc.parent = node;
				oe.add(pc);
				return takeStep(oe, to);
			}
		}
		
		// else just take the best next step
		for (Edge<Vertex<V>> e : edges) {
			for (Edge<Vertex<V>> e1 : edges) {
				if (e1 != e) Visualizer.setEdgeActive(e1, false, false);
			}
			Visualizer.setVertexState(node, State.CLOSEDLIST, false);
			Visualizer.setEdgePath(e, true, true);
			Vertex<V> oe = e.getOtherEnd(node);
			PathCommons<V> pc = new VertexData.PathCommons<V>();
			pc.parent = node;
			oe.add(pc);
			
			if (takeStep(oe, to)) return true;
			Visualizer.setEdgePath(e, false);
		}
		
		Visualizer.setVertexState(node, null, false);
		return false;*/
	}
	
	@SuppressWarnings("unchecked")
	public boolean isNotVisited(Vertex<V> node) {
		VertexData.PathCommons<V> data;
		return (data = node.get(PathCommons.class)) == null || !data.visited;
	}
}
