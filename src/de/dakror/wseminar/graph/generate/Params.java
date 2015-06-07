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


package de.dakror.wseminar.graph.generate;

import java.util.HashMap;
import java.util.Map;

public abstract class Params<K> {
	public static class SParams extends Params<String> {}
	
	protected Map<K, Object> map;
	
	public Params() {
		map = new HashMap<>();
	}
	
	public Params<K> put(K k, Object v) {
		map.put(k, v);
		return this;
	}
	
	public <T> T get(K k) {
		return orElse(k, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T orElse(K k, T o) {
		if (map.containsKey(k)) return (T) map.get(k);
		else return o;
	}
}
