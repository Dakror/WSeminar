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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Path;
import de.dakror.wseminar.graph.Vertex;

/**
 * @author Maximilian Stark | Dakror
 */
public class BritishMuseum<V> extends PathFinder<V> {
	ArrayList<Vertex<V>> nodes;
	ArrayList<Vertex<V>> pathStub;
	
	public BritishMuseum(Graph<Vertex<V>> graph) {
		nodes = new ArrayList<>();
		pathStub = new ArrayList<>();
	}
	
	@Override
	public Path<Vertex<V>> findPath(Vertex<V> from, Vertex<V> to) {
		if (from.equals(to)) return new Path<>();
		
		nodes.clear();
		pathStub.clear();
		
		takeStep(from, to);
		
		return new Path<>(nodes);
	}
	
	@Override
	protected boolean takeStep(Vertex<V> node, Vertex<V> to) {
		super.takeStep(node, to);
		
		if (pathStub.contains(node)) return false;
		
		nodes.add(node);
		if (node.equals(to)) return true;
		pathStub.add(node);
		
		List<Edge<Vertex<V>>> edges = graph.getEdges(node);
		Collections.sort(edges);
		
		for (Edge<Vertex<V>> e : edges) {
			if (takeStep(e.getOtherEnd(node), to)) {
				pathStub.remove(node);
				return true;
			}
		}
		
		pathStub.remove(node);
		nodes.remove(node);
		
		return false;
	}
}
