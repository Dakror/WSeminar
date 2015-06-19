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
public class WeightedEdge<V> extends Edge<V> {
	float weight;
	
	public WeightedEdge(V from, V to, float weight) {
		super(from, to);
		this.weight = weight;
	}
	
	public WeightedEdge(V from, V to, boolean directed, float weight) {
		super(from, to, directed);
		this.weight = weight;
	}
	
	public float getWeight() {
		return weight;
	}
	
	public Edge<V> setWeight(float weight) {
		this.weight = weight;
		return this;
	}
	
	@Override
	public String toString() {
		return "WE[" + from + " " + (directed ? "" : "<") + " " + weight + " > " + to + "]";
	}
	
	@Override
	public int compareTo(Edge<V> o2) {
		return Float.compare(weight, o2 instanceof WeightedEdge ? ((WeightedEdge<V>) o2).getWeight() : 0);
	}
}
