package de.dakror.wseminar.graph.api;

/**
 * @author Maximilian Stark | Dakror
 */
public interface DirectedEdge<T> extends Edge<T> {
	/**
	 * The Node this edge is comming from
	 */
	@Override
	public Node<T> getV();
	
	/**
	 * The Node this edge leads to
	 */
	@Override
	public Node<T> getU();
}
