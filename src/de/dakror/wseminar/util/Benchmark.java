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

/**
 * @author Maximilian Stark | Dakror
 *
 */
public class Benchmark {
	public static enum Type {
		ALL_OPS,
		VERTICES,
		VERTICES_UNIQUE,
		EDGES,
		EDGES_UNIQUE,
		SORTS,
		OPEN_LIST_SIZE,
		CLOSED_LIST_SIZE,
		BACK_TRACKS,
		OVERRIDES,
		TIME
	}
	
	float[] values;
	
	public Benchmark() {
		values = new float[Type.values().length];
	}
	
	public void add(Type t, float f) {
		values[t.ordinal()] += f;
	}
	
	public void inc(Type t) {
		add(t, 1);
	}
	
	public float get(Type t) {
		return values[t.ordinal()];
	}
	
	public float[] getAll() {
		return values;
	}
}
