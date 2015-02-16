package de.dakror.wseminar.graph.generate;

import java.util.HashMap;

public class Params {
	HashMap<String, Object> map = new HashMap<>();
	
	public Params put(String k, Object v) {
		map.put(k, v);
		return this;
	}
	
	public <T> T get(String k) {
		return orElse(k, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T orElse(String k, T o) {
		if (map.containsKey(k)) return (T) map.get(k);
		else return o;
	}
}
