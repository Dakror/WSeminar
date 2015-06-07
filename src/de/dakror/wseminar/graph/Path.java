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
import java.util.stream.Collectors;

/**
 * @author Maximilian Stark | Dakror
 */
public class Path<V> implements Iterable<V> {
	ArrayList<V> nodes;
	
	public Path() {
		nodes = new ArrayList<>();
	}
	
	public Path(V[] nodes) {
		this();
		for (V v : nodes)
			add(v);
	}
	
	public Path(Collection<? extends V> nodes) {
		this();
		addAll(nodes);
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
	
	@Override
	public String toString() {
		return nodes.stream().map(v -> v.toString()).collect(Collectors.joining(" -> ")).toString();
	}
}
