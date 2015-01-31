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
}
