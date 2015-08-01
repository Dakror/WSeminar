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

import de.dakror.wseminar.Const;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.VertexData.Disposition;
import de.dakror.wseminar.graph.VertexData.Position;
import de.dakror.wseminar.graph.algorithm.common.Layout;
import de.dakror.wseminar.math.Vector2;

/**
 * @author Maximilian Stark | Dakror
 */
public class FruchtermanReingold<V> extends Layout<V> {
	float k;
	
	float width, height;
	float size = 2;
	
	public FruchtermanReingold(Graph<V> sourceGraph) {
		this(sourceGraph, Const.defaultCycles);
	}
	
	public FruchtermanReingold(Graph<V> sourceGraph, int maxCycles) {
		this(sourceGraph, maxCycles, WSeminar.seed);
	}
	
	public FruchtermanReingold(Graph<V> sourceGraph, int maxCycles, long seed) {
		this(sourceGraph, maxCycles, seed, 1);
	}
	
	public FruchtermanReingold(Graph<V> sourceGraph, int maxCycles, long seed, float width, float height) {
		super(sourceGraph, maxCycles, seed);
		this.width = width;
		this.height = height;
	}
	
	public FruchtermanReingold(Graph<V> sourceGraph, int maxCycles, long seed, float size) {
		this(sourceGraph, maxCycles, seed, sourceGraph.getVertices().size() * size, sourceGraph.getVertices().size() * size);
		this.size = size;
	}
	
	@Override
	public void init() {
		graph = sourceGraph.getVertexGraph(Position.class, Disposition.class);
		// -- random init positions -- //
		for (Vertex<V> v : graph.getVertices())
			v.get(Position.class).pos.set(r.nextFloat() * width, r.nextFloat() * height);
			
		calculateK(graph.getVertices().size());
	}
	
	@Override
	public void step() {
		float temperature = 1f / 10 * width;
		
		float step = temperature / maxCycles;
		
		// calc Fr
		for (Vertex<V> v : graph.getVertices()) {
			Disposition d = v.get(Disposition.class);
			d.disp.zero();
			for (Vertex<V> u : graph.getVertices()) {
				if (u != v) {
					Vector2 delta = v.get(Position.class).pos.clone().sub(u.get(Position.class).pos);
					float length = Math.max(EPSILON, delta.length());
					float fr = getFr(length);
					d.disp.add(delta.x / length * fr, delta.y / length * fr);
				}
			}
		}
		
		// calc Fa
		for (Edge<Vertex<V>> e : graph.getEdges()) {
			Vector2 delta = e.getFrom().get(Position.class).pos.clone().sub(e.getTo().get(Position.class).pos);
			float length = Math.max(EPSILON, delta.length());
			float fa = getFa(length);
			
			float dx = delta.x / length * fa;
			float dy = delta.y / length * fa;
			
			e.getFrom().get(Disposition.class).disp.sub(dx, dy);
			e.getTo().get(Disposition.class).disp.add(dx, dy);
		}
		
		// adjustments
		for (Vertex<V> v : graph.getVertices()) {
			Position p = v.get(Position.class);
			Disposition d = v.get(Disposition.class);
			
			float length = Math.max(EPSILON, d.disp.length());
			float min = Math.min(length, temperature);
			
			p.pos.add(d.disp.x / length * min, d.disp.y / length * min);
			p.pos.x = Math.min(width - r.nextFloat(), Math.max(r.nextFloat(), p.pos.x));
			p.pos.y = Math.min(height - r.nextFloat(), Math.max(r.nextFloat(), p.pos.y));
		}
		temperature -= step;
	}
	
	/**
	 * k is the optimal distance between vertices
	 */
			void calculateK(int vertexCount) {
		k = (float) (Math.sqrt((width * height / (size / 2)) / vertexCount));
	}
	
	/**
	 * Attractive force
	 * 
	 * @param d
	 * @return
	 */
			float getFa(float d) {
		return (d * d) / k;
	}
	
	/**
	 * Repulsive force
	 * 
	 * @param d
	 * @return
	 */
			float getFr(float d) {
		return (k * k) / d;
	}
}
