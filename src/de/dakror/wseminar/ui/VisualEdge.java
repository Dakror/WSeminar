package de.dakror.wseminar.ui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.WeightedEdge;
import de.dakror.wseminar.graph.vertexdata.Position;
import de.dakror.wseminar.math.Vector2;

/**
 * @author Maximilian Stark | Dakror
 */
public class VisualEdge<V> extends Line {
	Color color;
	Text text;
	
	public VisualEdge(Edge<Vertex<V>> edge, int id, Pane pane) {
		super(edge.getFrom().get(Position.class).pos.x * Const.cellSize + Const.cellSize / 2, edge.getFrom().get(Position.class).pos.y * Const.cellSize + Const.cellSize / 2,
					edge.getTo().get(Position.class).pos.x * Const.cellSize + Const.cellSize / 2, edge.getTo().get(Position.class).pos.y * Const.cellSize + Const.cellSize / 2);
		
		setId("E" + id);
		
		setColor(Color.DARKGRAY);
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setColor(Color.valueOf("#519cf8"));
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setColor(Color.DARKGRAY);
			}
		});
		
		if (edge instanceof WeightedEdge) {
			text = new Text(((WeightedEdge<Vertex<V>>) edge).getWeight() + "");
			text.setId("ET" + id);
			text.setTranslateX(0.5f * (getStartX() + getEndX()));
			text.setTranslateY(0.5f * (getStartY() + getEndY()));
			pane.getChildren().add(text);
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
		Vector2 middle = new Vector2((float) (getEndX() + getStartX()), (float) (getEndY() + getStartY())).scl(0.5f);
		Vector2 vec = new Vector2((float) (getEndX() - getStartX()), (float) (getEndY() - getStartY()));
		
		// rotate 90° cw
		float tmp = vec.x;
		vec.x = vec.y;
		vec.y = -tmp;
		
		float size = 5;
		
		vec.nor().scl(size);
		Vector2 a = middle.clone().sub(vec.clone().scl(0.5f));
		Vector2 b = middle.clone().add(vec.clone().scl(0.5f));
		
		setStrokeWidth(size);
		setStrokeLineCap(StrokeLineCap.ROUND);
		setStroke(new LinearGradient(a.x, a.y, b.x, b.y, false, CycleMethod.NO_CYCLE, new Stop(0, Color.TRANSPARENT), new Stop(0.5, color), new Stop(1, Color.TRANSPARENT)));
	}
	
	public Text getText() {
		return text;
	}
	
	public Color getColor() {
		return color;
	}
}
