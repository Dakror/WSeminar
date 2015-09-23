/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.wseminar.ui;

import java.text.NumberFormat;

import de.dakror.wseminar.Const;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.VertexData.Position;
import de.dakror.wseminar.graph.WeightedEdge;
import de.dakror.wseminar.math.Vector2;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;

/**
 * @author Maximilian Stark | Dakror
 */
public class VisualEdge<V> extends Line {
	Color color;
	public Text text;
	Polygon p;
	Edge<Vertex<V>> edge;
	boolean active;
	boolean path;
	
	boolean left;
	
	public VisualEdge(Edge<Vertex<V>> edge, Pane pane) {
		super(edge.getFrom().get(Position.class).pos.x * Const.cellSize + Const.cellSize / 2, edge.getFrom().get(Position.class).pos.y * Const.cellSize + Const.cellSize / 2,
					edge.getTo().get(Position.class).pos.x * Const.cellSize + Const.cellSize / 2, edge.getTo().get(Position.class).pos.y * Const.cellSize + Const.cellSize / 2);
		this.edge = edge;
		setId("E" + edge.hashCode());
		getStyleClass().add("visual-edge");
		
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
		
		EventHandler<MouseEvent> en = e -> {
			if (!path) setColor(Color.valueOf("#5f5f5f"));
		};
		EventHandler<MouseEvent> ex = e -> {
			if (!path) setColor(active ? Color.valueOf("#2279e5") : Color.DARKGRAY);
		};
		
		pane.getParent().addEventHandler(ScrollEvent.ANY, e -> {
			if (getScene() == null) return;
			Slider zoom = ((Slider) getScene().lookup("#zoom"));
			setStrokeWidth(2 + Math.max(0, 15 * (100 - zoom.getValue()) / 100));
		});
		
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
	
	public void setActive(boolean active) {
		if (path && !active) return;
		
		if (active) setColor(Color.valueOf("#2279e5"));
		else setColor(Color.DARKGRAY);
		if (active) text.setFill(Color.valueOf("#2279e5").darker());
		else text.setFill(Color.BLACK);
		
		this.active = active;
	}
	
	public void setPath(boolean path) {
		setPath(path, false);
	}
	
	public void setPath(boolean path, boolean soft) {
		if (path) setColor(Color.valueOf("#3c009f"));
		else {
			if (soft) setActive(active);
			else setColor(Color.DARKGRAY);
		}
		if (path) text.setFill(Color.valueOf("#3c009f").darker());
		else {
			if (soft) setActive(active);
			else text.setFill(Color.BLACK);
		}
		
		this.path = path;
	}
	
	public boolean isPath() {
		return path;
	}
	
	public boolean isActive() {
		return active;
	}
}
