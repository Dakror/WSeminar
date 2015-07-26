/*******************************************************************************

 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


package de.dakror.wseminar.ui;

import de.dakror.wseminar.Const;
import de.dakror.wseminar.Const.State;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.VertexData.Position;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;

/**
 * @author Maximilian Stark | Dakror
 */
public class VisualVertex<V> extends Circle {
	Vertex<V> vertex;
	State prevState, state;
	boolean active;
	
	@SuppressWarnings("unchecked")
	public VisualVertex(Vertex<V> v) {
		vertex = v;
		setCenterX(Const.cellSize / 2);
		setCenterY(Const.cellSize / 2);
		setRadius(12);
		setTranslateX(v.get(Position.class).pos.x * Const.cellSize);
		setTranslateY(v.get(Position.class).pos.y * Const.cellSize);
		getStyleClass().add("visual-vertex");
		
		setOnMouseClicked(e -> {
			if (e.getButton() != MouseButton.PRIMARY) return;
			
			if (WSeminar.instance.selectStartVertex || WSeminar.instance.selectGoalVertex) {
				if (WSeminar.instance.selectStartVertex) {
					if (WSeminar.instance.startVertex != null) WSeminar.instance.startVertex.setState(null);
					setState(State.START);
					WSeminar.instance.startVertex = (VisualVertex<Integer>) VisualVertex.this;
				}
				
				if (WSeminar.instance.selectGoalVertex) {
					if (WSeminar.instance.goalVertex != null) WSeminar.instance.goalVertex.setState(null);
					setState(State.GOAL);
					WSeminar.instance.goalVertex = (VisualVertex<Integer>) VisualVertex.this;
				}
				
				WSeminar.instance.selectStartVertex = false;
				WSeminar.instance.selectGoalVertex = false;
				getScene().setCursor(Cursor.DEFAULT);
				return;
			}
			
			if (WSeminar.instance.activeVertex == VisualVertex.this) return;
			
			if (WSeminar.instance.activeVertex != null) WSeminar.instance.activeVertex.setActive(false);
			
			setActive(true);
			WSeminar.instance.activeVertex = (VisualVertex<Integer>) VisualVertex.this;
			TreeView<String> tv = ((TreeView<String>) getScene().lookup("#graph_tree"));
			for (TreeItem<String> item : tv.getRoot().getChildren()) {
				if (item instanceof GraphTreeItem) {
					GraphTreeItem gti = (GraphTreeItem) item;
					if (gti.node == VisualVertex.this) {
						tv.getSelectionModel().select(gti);
						break;
					}
				}
			}
		});
		
		addEventHandler(ActionEvent.ANY, e -> System.out.println("fgwi"));
		addEventHandler(Const.RESET, e -> System.out.println("ohayou"));
	}
	
	@SuppressWarnings("unchecked")
	public void setActive(boolean active) {
		if (getStyleClass().contains("active")) getStyleClass().remove("active");
		else getStyleClass().add("active");
		
		getParent().getChildrenUnmodifiable().stream().filter(n -> (n instanceof VisualEdge)
				&& WSeminar.instance.getGraph().isConnected(vertex, ((VisualEdge<V>) n).edge)).forEach(n -> ((VisualEdge<V>) n).setActive(active));
				
		this.active = active;
	}
	
	public void setState(State state) {
		if (state == State.REVERT) {
			revertState();
			return;
		}
		
		if (this.state == state) return;
		
		if (this.state != null) getStyleClass().remove(this.state.name().toLowerCase());
		if (state != null) getStyleClass().add(state.name().toLowerCase());
		prevState = this.state;
		this.state = state;
	}
	
	public void revertState() {
		state = prevState;
		prevState = null;
	}
	
	public Vertex<V> getVertex() {
		return vertex;
	}
}
