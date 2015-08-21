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

import com.sun.javafx.collections.ObservableListWrapper;

import de.dakror.wseminar.graph.Edge;
import javafx.collections.ObservableList;

/**
 * @author Maximilian Stark | Dakror
 *
 */
public class Benchmark<V> {
	public static class Timestamp implements Comparable<Timestamp> {
		public final long time;
		public final float stamp;
		
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
		VERTICES("Knoten"),
		VERTICES_UNIQUE("Verschiedene Knoten"),
		SORTS("Sortierungen"),
		OPEN_LIST_SIZE("OpenList-Größe"),
		CLOSED_LIST_SIZE("ClosedList-Größe"),
		BACK_TRACKS("Rückschritte"),
		PATH_CREATION("Wegerstellung"),
		OVERRIDES("Überschreibungen");
		
		public final String desc;
		
		private Type(String desc) {
			this.desc = desc;
		}
		
		public static Type getByDesc(String desc) {
			for (Type t : values()) {
				if (t.desc.equals(desc)) return t;
			}
			
			return null;
		}
	}
	
	long time, time1;
	
	long firstTime;
	
	ObservableList<Timestamp>[] values;
	
	List<V> vertices;
	List<Edge<V>> edges;
	
	@SuppressWarnings("unchecked")
	public Benchmark() {
		values = new ObservableList[Type.values().length];
		for (int i = 0; i < values.length; i++)
			values[i] = new ObservableListWrapper<>(new ArrayList<>());
			
		vertices = new ArrayList<>();
		edges = new ArrayList<>();
	}
	
	public void add(Type t, float f) {
		long time = System.nanoTime();
		
		if (firstTime == 0) firstTime = time;
		
		float last = values[t.ordinal()].size() > 0 ? values[t.ordinal()].get(values[t.ordinal()].size() - 1).stamp : 0;
		
		values[t.ordinal()].add(new Timestamp(time - firstTime, last + f));
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
	public void inc(V v) {
		inc(Type.VERTICES);
		if (!vertices.contains(v)) {
			vertices.add(v);
			inc(Type.VERTICES_UNIQUE);
		}
	}
	
	public ObservableList<Timestamp> get(Type t) {
		return values[t.ordinal()];
	}
	
	public ObservableList<Timestamp>[] getAll() {
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
