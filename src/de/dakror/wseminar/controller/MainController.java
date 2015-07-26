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


package de.dakror.wseminar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.algorithm.common.Layout;
import de.dakror.wseminar.math.Vector2;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Dakror
 */
public class MainController {
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private URL location;
	
	@FXML
	private ChoiceBox<String> path_strategy;
	
	@FXML
	private MenuItem relayout_graph;
	
	@FXML
	private CheckBox path_faststack;
	
	@FXML
	private Slider zoom;
	
	@FXML
	private CheckBox path_benchmark;
	
	@FXML
	private CheckBox path_goalbounding;
	
	@FXML
	private Pane graph;
	
	@FXML
	private Label new_graph_label;
	
	@FXML
	private MenuItem new_graph;
	
	@FXML
	private Button path_start;
	
	@FXML
	private Button path_find;
	
	@FXML
	private Button path_delete;
	
	@FXML
	private Menu menu_graph;
	
	@FXML
	private ChoiceBox<String> path_algorithm;
	
	@FXML
	private TreeView<String> graph_tree;
	
	@FXML
	private Button path_goal;
	
	@FXML
	private CheckBox path_animate;
	
	float lastX = -1, lastY = -1;
	
	long last;
	
	@FXML
			void initialize() {
		Vector2 scrollMouse = new Vector2();
		
		new_graph_label.setOnMouseClicked(e -> createGenerateDialog());
		
		zoom.valueProperty().addListener((obs, newVal, oldVal) -> {
			if (WSeminar.instance.getGraph() != null) {
				Pane pane = (Pane) WSeminar.window.getScene().lookup("#graph");
				
				Bounds a = pane.getBoundsInParent();
				
				pane.setScaleX(Math.max(0.1f, Math.min(4, newVal.floatValue() / 100f)));
				pane.setScaleY(Math.max(0.1f, Math.min(4, newVal.floatValue() / 100f)));
				
				Bounds b = pane.getBoundsInParent();
				
				float mouseX = (float) (scrollMouse.x == 0 ? a.getWidth() / 2 : scrollMouse.x);
				float mouseY = (float) (scrollMouse.y == 0 ? a.getHeight() / 2 : scrollMouse.y);
				
				pane.setTranslateX(pane.translateXProperty().add(mouseX - (mouseX / a.getWidth() * b.getWidth())).get());
				pane.setTranslateY(pane.translateYProperty().add(mouseY - (mouseY / a.getHeight() * b.getHeight())).get());
			}
		});
		
		graph.getParent().setOnScroll(e -> {
			if (graph != null) {
				Bounds bounds = graph.getBoundsInParent();
				
				float x = (float) (e.getX() - graph.getBoundsInParent().getMinX());
				float y = (float) (e.getY() - graph.getBoundsInParent().getMinY());
				if (x < 0 || x > bounds.getWidth()) x = (float) (bounds.getWidth() / 2);
				if (y < 0 || y > bounds.getHeight()) x = (float) (bounds.getHeight() / 2);
				
				scrollMouse.set(x, y);
				Slider zoom = ((Slider) WSeminar.window.getScene().lookup("#zoom"));
				zoom.setValue(zoom.getValue() + e.getDeltaY() * 0.25f);
			}
		});
		
		EventHandler<MouseEvent> eh = e -> {
			scrollMouse.set((float) e.getX(), (float) e.getY());
			
			if (e.isSecondaryButtonDown() && graph != null) {
				graph.getScene().setCursor(Cursor.MOVE);
				if (lastX != -1) {
					float deltaX = (float) (e.getX() - lastX);
					float deltaY = (float) (e.getY() - lastY);
					graph.setTranslateX(graph.getTranslateX() + deltaX);
					graph.setTranslateY(graph.getTranslateY() + deltaY);
				}
				
				lastX = (float) e.getX();
				lastY = (float) e.getY();
			} else {
				if (e.isPrimaryButtonDown() && WSeminar.instance.activeVertex != null) {
					if (!WSeminar.instance.activeVertex.contains(e.getX(), e.getY())) {
						WSeminar.instance.activeVertex.setActive(false);
						WSeminar.instance.activeVertex = null;
					}
				}
				lastX = -1;
				lastY = -1;
				if (graph.getScene().getCursor() == Cursor.MOVE) graph.getScene().setCursor(Cursor.DEFAULT);
			}
		};
		
		graph.getParent().setOnMouseReleased(eh);
		graph.getParent().setOnMouseDragged(eh);
		graph.getParent().setOnMouseExited(e -> scrollMouse.zero());
		
		// new_graph
		menu_graph.getItems().get(0).setOnAction(e -> createGenerateDialog());
		
		// relayout_graph, JFX bug!
		menu_graph.getItems().get(1).setOnAction(e -> {
			if (WSeminar.instance.getSourceGraph() != null && (System.currentTimeMillis() - last) > 200) {
				MainController.doLayoutWithProgress(WSeminar.instance.getLayout(), null, true, true);
				last = System.currentTimeMillis();
			}
		});
		
		// -- path section -- //
		path_start.setOnAction(e -> {
			if (WSeminar.instance.getSourceGraph() == null) return;
			WSeminar.instance.selectGoalVertex = false;
			WSeminar.instance.selectStartVertex = true;
			path_start.getScene().setCursor(Cursor.HAND);
		});
		
		path_goal.setOnAction(e -> {
			if (WSeminar.instance.getSourceGraph() == null) return;
			WSeminar.instance.selectStartVertex = false;
			WSeminar.instance.selectGoalVertex = true;
			path_goal.getScene().setCursor(Cursor.HAND);
		});
		
		path_algorithm.getItems().addAll(/*"BFS",*/ "DFS"/*, "Dijkstra", "A*"*/);
		path_algorithm.setValue(path_algorithm.getItems().get(0));
		
		path_algorithm.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
			path_faststack.setDisable(newVal.intValue() != 3);
			path_goalbounding.setDisable(newVal.intValue() != 3);
		});
		
		path_find.setOnAction(e -> {
			if (WSeminar.instance.startVertex == null || WSeminar.instance.goalVertex == null || WSeminar.instance.startVertex == WSeminar.instance.goalVertex) return;
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					graph.fireEvent(new ActionEvent());
				}
			});
			/*new Thread() {
				@Override
				public void run() {
					Path<Vertex<Integer>> p = new DFS<Integer>(WSeminar.instance.getGraph()).findPath(WSeminar.instance.startVertex.getVertex(), WSeminar.instance.goalVertex.getVertex());
				}
			}.start();*/
		});
		
	}
	
	void createGenerateDialog() {
		WSeminar.createDialog("generate_graph_dialog", "Neues Netz generieren", WSeminar.window);
	}
	
	public static void doLayoutWithProgress(Layout<Integer> layout, String message, boolean transition, boolean setGraphAnimate) {
		if (layout.getSourceGraph().getVertices().size() < 100) {
			Graph<Vertex<Integer>> render = layout.render();
			if (transition) WSeminar.instance.transitionTo(render);
			else WSeminar.instance.setGraph(render, setGraphAnimate);
			
			return;
		}
		
		Stage progress = WSeminar.createDialog("progress", "Fortschritt", WSeminar.window, StageStyle.TRANSPARENT, Modality.NONE);
		
		if (message == null) {
			progress.getScene().setFill(null);
			progress.getScene().getRoot().setStyle("-fx-background-color: transparent");
		}
		
		Label msgLabel = (Label) progress.getScene().lookup("#message");
		msgLabel.setVisible(message == null);
		msgLabel.setText(message);
		
		progress.setAlwaysOnTop(true);
		
		ProgressIndicator pi = ((ProgressIndicator) progress.getScene().lookup("#progress"));
		pi.setProgress(0);
		
		ChangeListener<Number> cl = (obs, newVal, oldVal) -> Platform.runLater(() -> {
			pi.setProgress(Math.round(newVal.doubleValue() * 100) / 100.0);
		});
		
		new Thread() {
			@Override
			public void run() {
				layout.progress.addListener(cl);
				Graph<Vertex<Integer>> render = layout.render();
				layout.progress.removeListener(cl);
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						progress.close();
						if (transition) WSeminar.instance.transitionTo(render);
						else WSeminar.instance.setGraph(render, setGraphAnimate);
					}
				});
			}
		}.start();
	}
}
