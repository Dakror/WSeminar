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

import de.dakror.wseminar.Const.State;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.WeightedEdge;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * @author Maximilian Stark | Dakror
 */
public class GraphTreeCell extends TreeCell<String> {
	final HBox graphic = new HBox();
	final Label name = new Label();
	final TextField value = new TextField();
	
	@SuppressWarnings("unchecked")
	public GraphTreeCell() {
		getStyleClass().add("hierarchy-tree-cell");
		name.setMinWidth((-1.0D / 0.0D));
		
		value.setDisable(true);
		value.getStyleClass().addAll("hierarchy-readwrite-label", "label");
		value.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) graphic.requestFocus();
		});
		value.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal) {
				String val = value.getText();
				
				Node node = ((GraphTreeItem) getTreeItem()).getNode();
				
				if (node instanceof VisualEdge) {
					float num = 0;
					try {
						num = (float) Double.parseDouble(val);
					} catch (NumberFormatException e) {
						value.setText(((VisualEdge<Integer>) node).text.getText());
						return;
					}
					
					((VisualEdge<Integer>) node).text.setText((int) num + "");
					((WeightedEdge<Vertex<Integer>>) ((VisualEdge<Integer>) node).edge).setWeight(num);
					for (TreeItem<String> vti : getTreeView().getRoot().getChildren()) {
						for (TreeItem<String> eti : vti.getChildren()) {
							if (((GraphTreeItem) eti).getParam().equals(((GraphTreeItem) getTreeItem()).getParam())) {
								String value = eti.getValue();
								eti.setValue(null);
								eti.setValue(value);
							}
						}
					}
				}
			}
		});
		
		HBox.setHgrow(value, Priority.ALWAYS);
		graphic.getStyleClass().add("tree-cell-graphic");
		graphic.getChildren().addAll(new Node[] { name, value });
		selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (getTreeItem() != null && isSelected()) {
				Node node = ((GraphTreeItem) getTreeItem()).getNode();
				
				if (WSeminar.instance.activeVertex != null) WSeminar.instance.activeVertex.setState(State.DEFAULT);
				if (WSeminar.instance.activeEdge != null) WSeminar.instance.activeEdge.setActive(false);
				
				if (node instanceof VisualVertex) {
					WSeminar.instance.activeVertex = (VisualVertex<Integer>) node;
					((VisualVertex<?>) node).setState(State.ACTIVE);
				}
				if (node instanceof VisualEdge) {
					WSeminar.instance.activeEdge = (VisualEdge<Integer>) node;
					((VisualEdge<?>) node).setActive(true);
				}
			}
		});
	}
	
	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		
		value.setDisable(true);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (getIndex() == 0 && !getStyleClass().contains("hierarchy-first-cell")) getStyleClass().add("hierarchy-first-cell");
			
			name.setText(item);
			Node node = ((GraphTreeItem) getTreeItem()).getNode();
			if (node instanceof VisualVertex) {
				value.setText(((VisualVertex<?>) ((GraphTreeItem) getTreeItem()).getNode()).getVertex().data() + "");
			} else if (node instanceof VisualEdge) {
				value.setText(((VisualEdge<?>) ((GraphTreeItem) getTreeItem()).getNode()).text.getText());
				value.setDisable(!(((VisualEdge<?>) node).edge instanceof WeightedEdge));
			}
			
			value.setMaxWidth(50);
			value.setVisible(value.getText().length() > 0);
			setGraphic(graphic);
		}
	}
}
