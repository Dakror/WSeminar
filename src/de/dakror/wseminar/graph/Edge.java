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

/**
 * @author Maximilian Stark | Dakror
 */
public class Edge<V> implements Comparable<Edge<V>> {
	V from, to;
	boolean directed;
	
	public Edge(V from, V to) {
		this.from = from;
		this.to = to;
	}
	
	public Edge(V from, V to, boolean directed) {
		this.from = from;
		this.to = to;
		this.directed = directed;
	}
	
	public V getFrom() {
		return from;
	}
	
	public Edge<V> setFrom(V from) {
		this.from = from;
		return this;
	}
	
	public V getTo() {
		return to;
	}
	
	public Edge<V> setTo(V to) {
		this.to = to;
		return this;
	}
	
	public Edge<V> setDirected(boolean directed) {
		this.directed = directed;
		return this;
	}
	
	public V getOtherEnd(V end) {
		if (end.equals(from)) return to;
		else return from;
	}
	
	public boolean isDirected() {
		return directed;
	}
	
	@Override
	public String toString() {
		return "E[" + from + " " + (directed ? "" : "<") + "-> " + to + "]";
	}
	
	@Override
	public int hashCode() {
		return from.hashCode() + toString().hashCode() * (directed ? -1 : 1);
	}
	
	@Override
	public int compareTo(Edge<V> o) {
		return 0;
	}
}
