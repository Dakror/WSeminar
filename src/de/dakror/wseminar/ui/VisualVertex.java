package de.dakror.wseminar.ui;

import javafx.scene.shape.Circle;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.vertexdata.Position;

/**
 * @author Maximilian Stark | Dakror
 */
public class VisualVertex<V> extends Circle {
	public VisualVertex(String selector, Vertex<V> v) {
		setCenterX(Const.cellSize / 2);
		setCenterY(Const.cellSize / 2);
		setRadius(12);
		setTranslateX(v.get(Position.class).pos.x * Const.cellSize);
		setTranslateY(v.get(Position.class).pos.y * Const.cellSize);
		getStyleClass().add("visual-vertex");
	}
}
