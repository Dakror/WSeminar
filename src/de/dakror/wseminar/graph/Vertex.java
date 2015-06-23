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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maximilian Stark | Dakror
 */
public class Vertex<T> {
	T data;
	Map<Class<?>, Object> vertexData = new HashMap<>();
	
	public Vertex(T data) {
		this.data = data;
	}
	
	public T data() {
		return data;
	}
	
	public void set(T data) {
		this.data = data;
	}
	
	public void add(Object... vd) {
		for (Object v : vd)
			vertexData.put(v.getClass(), v);
	}
	
	public void decorate(Class<?>... vd) {
		for (Class<?> c : vd) {
			try {
				add(c.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <V> V get(Class<V> cl) {
		return (V) vertexData.get(cl);
	}
	
	public Object[] getAll() {
		return vertexData.values().toArray(new Object[] {});
	}
	
	public boolean remove(Class<?> cl) {
		return vertexData.remove(cl) != null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vertex) return data.equals(((Vertex<T>) obj).data);
		return false;
	}
	
	@Override
	public String toString() {
		return "[V (" + data + ")]";
	}
}
