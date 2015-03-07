package de.dakror.wseminar.ui;

import java.text.NumberFormat;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.Const.State;
import de.dakror.wseminar.WSeminar;
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
	public Text text;
	Polygon p;
	Edge<Vertex<V>> edge;
	State state;
	
	boolean left;
	
	public VisualEdge(Edge<Vertex<V>> edge, int id, Pane pane) {
		super(edge.getFrom().get(Position.class).pos.x * Const.cellSize + Const.cellSize / 2, edge.getFrom().get(Position.class).pos.y * Const.cellSize + Const.cellSize / 2,
					edge.getTo().get(Position.class).pos.x * Const.cellSize + Const.cellSize / 2, edge.getTo().get(Position.class).pos.y * Const.cellSize + Const.cellSize / 2);
		this.edge = edge;
		setId("E" + id);
		
		state = State.DEFAULT;
		
		left = WSeminar.r.nextBoolean();
		
		text = new Text();
		if (edge instanceof WeightedEdge) {
			
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMaximumFractionDigits(2);
			
			text.setText(nf.format(((WeightedEdge<Vertex<V>>) edge).getWeight()));
			text.setOpacity(0);
			pane.getChildren().add(text);
		}
		
		p = new Polygon(-5, 4, 5, 4, 0, -6);
		if (edge.isDirected()) {
			p.setOpacity(0);
			pane.getChildren().add(p);
		}
		
		ChangeListener<Number> cl = (obs, newVal, oldVal) -> {
			setColor(color);
			text.setTranslateX(0.5f * (getStartX() + getEndX()) - (left ? text.getBoundsInLocal().getWidth() : 0));
			text.setTranslateY(0.5f * (getStartY() + getEndY()));
			
			Vector2 dir = new Vector2((float) getEndX() - (float) getStartX(), (float) getEndY() - (float) getStartY()).nor();
			Vector2 back = dir.clone().scl(18);
			
			p.setTranslateX(getEndX() - back.x);
			p.setTranslateY(getEndY() - back.y);
			
			p.setRotate(Math.toDegrees(Math.atan2(dir.y, dir.x)) + 90);
		};
		
		opacityProperty().addListener((obs, newVal, oldVal) -> {
			if (newVal.doubleValue() == 1 && oldVal.doubleValue() == 0) return;
			text.setOpacity(newVal.doubleValue());
			p.setOpacity(newVal.doubleValue());
		});
		
		setColor(Color.DARKGRAY);
		
		cl.changed(null, 0, 0); // initial placement for sub elements
		
		EventHandler<MouseEvent> en = e -> setColor(Color.valueOf("#5f5f5f"));
		EventHandler<MouseEvent> ex = e -> setColor(state.getLineColor() != null ? state.getLineColor() : Color.DARKGRAY);
		
		setOnMouseEntered(en);
		setOnMouseExited(ex);
		
		p.setOnMouseEntered(en);
		p.setOnMouseExited(ex);
		
		startXProperty().addListener(cl);
		startYProperty().addListener(cl);
		endXProperty().addListener(cl);
		endYProperty().addListener(cl);
		
		setStrokeWidth(2);
		setStrokeLineCap(StrokeLineCap.ROUND);
		p.setStrokeWidth(1);
	}
	
	void setColor(Color color) {
		this.color = color;
		setStroke(color);
		p.setStroke(color.darker());
		p.setFill(color);
	}
	
	public void setState(State state) {
		this.state = state;
		if (state.getLineColor() != null) setColor(state.getLineColor());
		else setColor(Color.DARKGRAY);
		if (state.getTextColor() != null) text.setFill(state.getTextColor());
		else text.setFill(Color.BLACK);
	}
	
	public State getState() {
		return state;
	}
}
