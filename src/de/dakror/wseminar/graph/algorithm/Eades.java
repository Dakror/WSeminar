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
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.VertexData.Force;
import de.dakror.wseminar.graph.VertexData.Position;
import de.dakror.wseminar.graph.algorithm.common.Layout;
import de.dakror.wseminar.math.Vector2;

/**
 * @author Maximilian Stark | Dakror
 */
public class Eades<V> extends Layout<V> {
	
	float stretch = 0.7f;
	int repulsionRange = 10;
	float forceMultiplier = 1.0f / 3.0f;
	float desiredLength = 3;
	
	public Eades(Graph<V> sourceGraph) {
		super(sourceGraph, Const.defaultCycles);
	}
	
	public Eades(Graph<V> sourceGraph, int maxCycles) {
		super(sourceGraph, maxCycles, (long) (Math.random() * Long.MAX_VALUE));
	}
	
	public Eades(Graph<V> sourceGraph, int maxCycles, long seed) {
		super(sourceGraph, maxCycles, seed);
	}
	
	@Override
	public void init() {
		graph = sourceGraph.getVertexGraph(Position.class, Force.class);
		// -- random init positions -- //
		for (Vertex<V> v : graph.getVertices())
			v.get(Position.class).pos.set(r.nextFloat(), r.nextFloat());
	}
	
	@Override
	public void step() {
		for (Vertex<V> v : graph.getVertices()) {
			Force f = v.get(Force.class);
			f.F.scl(1 / 4.0f);
			f.Fe.zero();
			f.Fr.zero();
		}
		
		relaxEdges();
		calculateRepulsion();
		moveNodes();
	}
	
	void relaxEdges() {
		for (Edge<Vertex<V>> e : graph.getEdges()) {
			Vector2 dif = e.getFrom().get(Position.class).pos.clone().sub(e.getTo().get(Position.class).pos);
			float length = Math.max(dif.length(), EPSILON);
			
			float f = (forceMultiplier * (desiredLength - length) / length) * (float) Math.pow(stretch, graph.getEdges(e.getFrom()).size() + graph.getEdges(e.getTo()).size() - 2);
			
			dif.scl(f);
			
			e.getFrom().get(Force.class).Fe.add(dif);
			e.getTo().get(Force.class).Fe.sub(dif);
		}
	}
	
	void calculateRepulsion() {
		for (Vertex<V> v : graph.getVertices()) {
			Vector2 d = new Vector2();
			for (Vertex<V> u : graph.getVertices()) {
				if (v == u) continue;
				
				Vector2 dif = v.get(Position.class).pos.clone().sub(u.get(Position.class).pos);
				float distance = dif.length();
				
				if (distance == 0) {
					d.add(r.nextFloat(), r.nextFloat());
				} else if (distance < repulsionRange) {
					d.add(dif.scl(1 / (distance * distance)));
				}
			}
			
			float dlen = d.length() / 2;
			if (dlen > 0) {
				v.get(Force.class).Fr.add(d.scl(1 / dlen));
			}
		}
	}
	
	void moveNodes() {
		for (Vertex<V> v : graph.getVertices()) {
			Force f = v.get(Force.class);
			f.F.add(f.Fe).add(f.Fr);
			
			float maxMovement = 5;
			
			v.get(Position.class).pos.add(Math.max(-maxMovement, Math.min(maxMovement, f.F.x)), Math.max(-maxMovement, Math.min(maxMovement, f.F.y)));
		}
	}
}
