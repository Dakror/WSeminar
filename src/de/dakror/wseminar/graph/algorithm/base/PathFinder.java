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


package de.dakror.wseminar.graph.algorithm.base;

import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Path;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.VertexData.PathCommons;
import de.dakror.wseminar.util.Benchmark;

/**
 * @author Maximilian Stark | Dakror
 */
public abstract class PathFinder<V> {
	protected Benchmark<Vertex<V>> BM;
	
	protected Graph<Vertex<V>> graph;
	
	protected Class<?>[] metaClasses = { PathCommons.class };
	
	protected boolean animate;
	
	public PathFinder(Graph<Vertex<V>> graph, boolean animate) {
		this.graph = graph;
		BM = new Benchmark<>();
		this.animate = animate;
	}
	
	public Graph<Vertex<V>> getGraph() {
		return graph;
	}
	
	public abstract Path<Vertex<V>> findPath(Vertex<V> from, Vertex<V> to);
	
	protected void cleanup() {
		for (Vertex<V> v : graph.getVertices()) {
			for (Class<?> c : metaClasses)
				v.remove(c);
		}
	}
	
	public Benchmark<Vertex<V>> getBenchmark() {
		return BM;
	}
	
	/**
	 * 
	 * @param parent the node which this call origins from
	 * @param node the active node
	 * @param to the final goal
	 * @return wether the step was taken
	 */
	protected abstract boolean takeStep(Vertex<V> parent, Vertex<V> node, Vertex<V> to);
}
