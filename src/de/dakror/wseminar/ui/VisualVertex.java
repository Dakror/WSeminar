package de.dakror.wseminar.ui;

import javafx.scene.shape.Circle;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.Const.State;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.vertexdata.Position;

/**
 * @author Maximilian Stark | Dakror
 */
public class VisualVertex<V> extends Circle {
	Vertex<V> vertex;
	
	@SuppressWarnings("unchecked")
	public VisualVertex(String selector, Vertex<V> v) {
		vertex = v;
		setCenterX(Const.cellSize / 2);
		setCenterY(Const.cellSize / 2);
		setRadius(12);
		setTranslateX(v.get(Position.class).pos.x * Const.cellSize);
		setTranslateY(v.get(Position.class).pos.y * Const.cellSize);
		getStyleClass().add("visual-vertex");
		
		setOnMouseClicked(e -> {
			if (WSeminar.instance.activeVertex != null) WSeminar.instance.activeVertex.setActive(false);
			
			setActive(true);
			WSeminar.instance.activeVertex = (VisualVertex<Integer>) VisualVertex.this;
		});
	}
	
	@SuppressWarnings("unchecked")
	public void setActive(boolean active) {
		if (active) {
			getStyleClass().add("active");
			getParent().getChildrenUnmodifiable().stream().filter(n -> (n instanceof VisualEdge) && WSeminar.instance.getGraph().isConnected(vertex, ((VisualEdge<V>) n).edge)).forEach(n -> ((VisualEdge<V>) n).setState(State.ACTIVE));
		} else {
			getStyleClass().remove("active");
			//@off
			getParent().getChildrenUnmodifiable().
			stream().
			filter(n -> 
			(n instanceof VisualEdge)
			&& WSeminar.instance.
			getGraph()
			.isConnected(
			             vertex,
			             ((VisualEdge<V>) n).edge))
			             .forEach(n -> (
			            		 (VisualEdge<V>) n)
			            		 .setState(State.DEFAULT));
		}
	}
	
	public Vertex<V> getVertex() {
		return vertex;
	}
}
