package de.dakror.wseminar.ui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.vertexdata.Position;

/**
 * @author Maximilian Stark | Dakror
 */
public class VisualVertex<V> extends Circle {
	Vertex<V> vertex;
	
	public VisualVertex(String selector, Vertex<V> v) {
		vertex = v;
		setCenterX(Const.cellSize / 2);
		setCenterY(Const.cellSize / 2);
		setRadius(12);
		setTranslateX(v.get(Position.class).pos.x * Const.cellSize);
		setTranslateY(v.get(Position.class).pos.y * Const.cellSize);
		getStyleClass().add("visual-vertex");
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(MouseEvent e) {
				if (WSeminar.instance.activeVertex != null) WSeminar.instance.activeVertex.setActive(false);
				
				setActive(true);
				WSeminar.instance.activeVertex = (VisualVertex<Integer>) VisualVertex.this;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public void setActive(boolean active) {
		if (active) {
			getStyleClass().add("active");
			getParent().getChildrenUnmodifiable().stream().filter(n -> (n instanceof VisualEdge) && WSeminar.instance.getGraph().isConnected(vertex, ((VisualEdge<V>) n).edge)).forEach(n -> ((VisualEdge<V>) n).setColor(Color.valueOf("#519cf8")));
		} else {
			getStyleClass().remove("active");
			getParent().getChildrenUnmodifiable().stream().filter(n -> (n instanceof VisualEdge) && WSeminar.instance.getGraph().isConnected(vertex, ((VisualEdge<V>) n).edge)).forEach(n -> ((VisualEdge<V>) n).setColor(Color.DARKGRAY));
		}
	}
}
