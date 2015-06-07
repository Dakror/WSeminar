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


package de.dakror.wseminar.graph;

import java.util.List;

/**
 * @author Maximilian Stark | Dakror
 */
public interface Graph<V> {
	public static enum GraphType {
		ABSTRACT_GRAPH("Abstrakter Graph") {
			@Override
			public Object supply(int nodeIndex, int totalNodes) {
				return nodeIndex;
			}
		},
		GRID("Raster-Netz"),
		LABYRINTH("Raster-Labyrinth"),
		
		;
		
		String title;
		
		private GraphType(String title) {
			this.title = title;
		}
		
		public Object supply(int nodeIndex, int totalNodes) {
			throw new UnsupportedOperationException("Graph type not supported yet!");
		}
		
		@Override
		public String toString() {
			return title;
		}
	}
	
	public List<Edge<V>> getEdges();
	
	public List<Edge<V>> getEdgesFrom(V from);
	
	public List<Edge<V>> getEdgesTo(V to);
	
	public List<Edge<V>> getEdges(V v);
	
	public List<V> getVertices();
	
	public void addEdge(Edge<V> edge);
	
	public boolean removeEdge(V from, V to);
	
	public void addVertex(V v);
	
	public boolean removeVertex(V v);
	
	public boolean areConnected(V from, V to);
	
	@SuppressWarnings("hiding")
	public <V> boolean isConnected(V v, Edge<V> edge);
	
	public Edge<V> getEdge(V from, V to);
	
	public float getWeight(V from, V to);
	
	public Graph<Vertex<V>> getVertexGraph(Class<?>... vertexData);
}
