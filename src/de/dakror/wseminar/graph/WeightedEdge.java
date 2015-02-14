package de.dakror.wseminar.graph;

/**
 * @author Maximilian Stark | Dakror
 */
public class WeightedEdge<V> extends Edge<V> {
	float weight;
	String type;
	
	public WeightedEdge(V from, V to, float weight, String type) {
		super(from, to);
		this.weight = weight;
		this.type = type;
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
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "WE[" + from + " " + (directed ? "" : "<") + " " + weight + " (" + type + ") > " + to + "]";
	}
}
