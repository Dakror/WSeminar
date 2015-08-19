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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.dakror.wseminar.util.Benchmark;

/**
 * @author Maximilian Stark | Dakror
 */
public class Path<V> implements Iterable<V> {
	ArrayList<V> nodes;
	Object userData;
	Benchmark<V> benchmark;
	float cost;
	
	public Path() {
		nodes = new ArrayList<>();
	}
	
	public Benchmark<V> getBenchmark() {
		return benchmark;
	}
	
	public void setBenchmark(Benchmark<V> benchmark) {
		this.benchmark = benchmark;
	}
	
	@Override
	public Iterator<V> iterator() {
		return nodes.iterator();
	}
	
	public int size() {
		return nodes.size();
	}
	
	public boolean isEmpty() {
		return nodes.isEmpty();
	}
	
	public boolean contains(Object o) {
		return nodes.contains(o);
	}
	
	public V get(int index) {
		return nodes.get(index);
	}
	
	public boolean add(V e) {
		return nodes.add(e);
	}
	
	public void add(int index, V element) {
		nodes.add(index, element);
	}
	
	public V remove(int index) {
		return nodes.remove(index);
	}
	
	public boolean remove(Object o) {
		return nodes.remove(o);
	}
	
	public void clear() {
		nodes.clear();
	}
	
	public boolean addAll(Collection<? extends V> c) {
		return nodes.addAll(c);
	}
	
	public void calculateCost(Graph<V> graph) {
		float cost = 0;
		for (int i = 0; i < size() - 1; i++) {
			cost += graph.getWeight(nodes.get(i), nodes.get(i + 1));
		}
		
		this.cost = cost;
	}
	
	public float getCost() {
		return cost;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	public void setUserData(Object userData) {
		this.userData = userData;
	}
	
	public Object getUserData() {
		return userData;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return obj.hashCode() == hashCode();
	}
	
	public String toGrouperString() {
		return nodes.get(0) + " -> " + nodes.get(nodes.size() - 1);
	}
	
	public String toSpecString() {
		return userData + " { " + (nodes.size() - 1) + " } = " + cost;
	}
	
	public String toString(boolean spec) {
		return spec ? toSpecString() : toGrouperString();
	}
	
	@Override
	public String toString() {
		return (userData != null ? userData + " " : "") + nodes.get(0) + " -> { " + (nodes.size() - 2) + " } -> " + nodes.get(nodes.size() - 1) + " = " + cost;
	}
}
