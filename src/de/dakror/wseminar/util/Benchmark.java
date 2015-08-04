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


package de.dakror.wseminar.util;

import java.util.ArrayList;
import java.util.List;

import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Vertex;

/**
 * @author Maximilian Stark | Dakror
 *
 */
public class Benchmark<V> {
	public static class Timestamp implements Comparable<Timestamp> {
		long time;
		float stamp;
		
		public Timestamp(long time, float stamp) {
			this.time = time;
			this.stamp = stamp;
		}
		
		@Override
		public int compareTo(Timestamp o) {
			return Long.compare(time, o.time);
		}
	}
	
	public static enum Type {
		VERTICES,
		VERTICES_UNIQUE,
		EDGES,
		EDGES_UNIQUE,
		SORTS,
		OPEN_LIST_SIZE,
		CLOSED_LIST_SIZE,
		BACK_TRACKS,
		PATH_CREATION,
		OVERRIDES
	}
	
	long time, time1;
	
	List<Timestamp>[] values;
	
	List<Vertex<V>> vertices;
	List<Edge<Vertex<V>>> edges;
	
	@SuppressWarnings("unchecked")
	public Benchmark() {
		values = new ArrayList[Type.values().length];
		for (int i = 0; i < values.length; i++)
			values[i] = new ArrayList<>();
			
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
	}
	
	public void add(Type t, float f) {
		values[t.ordinal()].add(new Timestamp(System.nanoTime(), f));
	}
	
	public void inc(Type t) {
		add(t, 1);
	}
	
	public void dec(Type t) {
		add(t, -1);
	}
	
	/**
	 * Use for {@link Type#VERTICES_UNIQUE} or {@link Type#VERTICES}
	 * @param v
	 */
	public void inc(Vertex<V> v) {
		inc(Type.VERTICES);
		if (!vertices.contains(v)) {
			vertices.add(v);
			inc(Type.VERTICES_UNIQUE);
		}
	}
	
	/**
	 * Use for {@link Type#EDGES_UNIQUE} or {@link Type#EDGES}
	 * @param v
	 */
	public void inc(Edge<Vertex<V>> v) {
		inc(Type.EDGES);
		if (!edges.contains(v)) {
			edges.add(v);
			inc(Type.EDGES_UNIQUE);
		}
	}
	
	public List<Timestamp> get(Type t) {
		return values[t.ordinal()];
	}
	
	public List<Timestamp>[] getAll() {
		return values;
	}
	
	public void time() {
		if (time1 == 0) time1 = System.nanoTime();
		else {
			time = System.nanoTime() - time1;
			time1 = 0;
		}
	}
	
	public long getTime() {
		return time;
	}
	
	public float getSum(Type t) {
		float m = 0;
		for (Timestamp ts : get(t))
			if (ts.stamp > m) m = ts.stamp;
			
		return m;
	}
}
