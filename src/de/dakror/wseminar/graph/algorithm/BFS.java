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

import static de.dakror.wseminar.util.Benchmark.Type.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import de.dakror.wseminar.Const.State;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Path;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.VertexData.PathCommons;
import de.dakror.wseminar.graph.WeightedEdge;
import de.dakror.wseminar.graph.algorithm.base.PathFinder;
import de.dakror.wseminar.util.Visualizer;

/**
 * @author Maximilian Stark | Dakror
 */
public class BFS<V> extends PathFinder<V> {
	HashMap<Vertex<V>, PathCommons<V>> meta;
	LinkedList<Vertex<V>> queue;
	
	public BFS(Graph<Vertex<V>> graph, boolean animate) {
		super(graph, animate);
		meta = new HashMap<>();
		queue = new LinkedList<>();
	}
	
	@Override
	public Path<Vertex<V>> findPath(Vertex<V> from, Vertex<V> to) {
		BM.time();
		Visualizer.resetAll(graph, true, false);
		
		queue.add(from);
		
		while (!queue.isEmpty()) {
			if (takeStep(null, queue.poll(), to)) break;
			
		}
		
		Path<Vertex<V>> p = new Path<Vertex<V>>();
		p.setUserData("BFS" + (animate ? " anim" : ""));
		Vertex<V> v = to;
		
		while (meta.get(v).parent != null) {
			p.add(0, v);
			v = meta.get(v).parent;
			
			BM.inc(PATH_CREATION);
		}
		
		p.add(0, from);
		BM.inc(PATH_CREATION);
		p.calculateCost(graph);
		
		p.setBenchmark(BM);
		
		cleanup();
		Visualizer.resetAll(graph, false, false);
		
		BM.time();
		
		System.out.println("bye");
		return p;
	}
	
	@Override
	protected boolean takeStep(Vertex<V> parent, Vertex<V> node, Vertex<V> to) {
		if (meta.containsKey(node)) parent = meta.get(node).parent;
		
		
		Visualizer.setVertexState(node, State.OPENLIST, false);
		
		
		BM.inc(OPEN_LIST_SIZE);
		
		if (node.equals(to)) return true;
		
		List<Vertex<V>> nexts = graph.getEdges(node).stream().filter(e -> {
			Vertex<V> v = e.getOtherEnd(node);
			
			BM.inc(v);
			
			boolean free = meta.get(v) == null;
			Visualizer.setEdgeActive(e, free, false);
			
			return free;
		}).sorted((a, b) -> Float.compare(a instanceof WeightedEdge ? ((WeightedEdge<Vertex<V>>) a).getWeight() : 0,
																			b instanceof WeightedEdge ? ((WeightedEdge<Vertex<V>>) b).getWeight() : 0)).map(e -> {
																				Vertex<V> v = e.getOtherEnd(node);
																				BM.inc(v);
																				return v;
																			}).collect(Collectors.toList());
		BM.inc(SORTS);
		
		if (parent != null) {
			System.out.println(graph.getEdge(parent, node) + ", " + parent + ", " + node);
			Visualizer.setEdgePath(graph.getEdge(parent, node), true);
		}
		
		for (Vertex<V> v : nexts) {
			PathCommons<V> pc = new PathCommons<>();
			pc.parent = node;
			meta.put(v, pc);
			
			if (v.equals(to)) {
				return takeStep(node, v, to);
			}
		}
		
		
		queue.addAll(nexts);
		BM.dec(OPEN_LIST_SIZE);
		BM.inc(CLOSED_LIST_SIZE);
		Visualizer.setVertexState(node, State.CLOSEDLIST);
		
		return false;
	}
}




















