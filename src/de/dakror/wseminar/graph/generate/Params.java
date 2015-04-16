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
