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

import de.dakror.wseminar.math.Vector2;

/**
 * @author Maximilian Stark | Dakror
 */
public class VertexData {
	public static class Delay {
		public float delay;
	}
	
	public static class Disposition {
		public Vector2 disp = new Vector2();
	}
	
	public static class Force {
		public Vector2 Fe = new Vector2(), Fr = new Vector2(), F = new Vector2();
	}
	
	public static class Position {
		public Vector2 pos = new Vector2();
	}
	
	public static class PathCommons<V> {
		public Vertex<V> parent;
		/**
		 * Used for manual override only, otherwise gets determined by wether this Data Element exists on the Vertex
		 */
		public boolean visited = true;
	}
	
	public static class Heuristics<V> extends PathCommons<V> {
		public float G, H;
		
		public float F() {
			return G + H;
		}
	}
	
	public static class InfPath<V> extends PathCommons<V> {
		public float d = Float.MAX_VALUE;
	}
}
