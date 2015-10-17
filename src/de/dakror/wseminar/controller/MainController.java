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

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.collections.ObservableListWrapper;

import de.dakror.wseminar.Const.State;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Path;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.algorithm.base.Layout;
import de.dakror.wseminar.graph.algorithm.base.PathFinder;
import de.dakror.wseminar.math.Vector2;
import de.dakror.wseminar.ui.PathLineChart;
import de.dakror.wseminar.ui.PathTreeItem;
import de.dakror.wseminar.util.Benchmark.Timestamp;
import de.dakror.wseminar.util.Benchmark.Type;
import de.dakror.wseminar.util.Visualizer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * @author Dakror
 */
public class MainController {
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private URL location;
	
	@FXML
	private TreeView<String> path_tree;
	
	@FXML
	private TreeView<String> path_tree_benchmark;
	
	@FXML
	private MenuItem relayout_graph;
	
	@FXML
	private Slider zoom;
	
	@FXML
	private CheckBox path_benchmark;
	
	@FXML
	private Button path_find;
	
	@FXML
	private PathLineChart<Long, Integer> chart_timeline;
	
	@FXML
	private Pane graph;
	
	@FXML
	private Label new_graph_label;
	
	@FXML
	private MenuItem new_graph;
	
	@FXML
	private Button path_start;
	
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
	private Button path_x;
	
	@FXML
	private CheckBox path_animate;
	
	@FXML
	private BarChart<String, Long> chart_alltime;
	
	@FXML
	private Tab tab_benchmark;
	
	@FXML
	private SplitPane benchmark;
	
	@FXML
	private TableView<Path<Vertex<Integer>>> chart_table;
	
	@FXML
	private Label seed_label;
	
	float lastX = -1, lastY = -1;
	
	float dragStartX, dragStartY;
	
	long last;
	
	boolean animatingPathFinding;
	
	boolean batch = false;
	
	@FXML
	public void initialize() {
		Vector2 scrollMouse = new Vector2();
		
		new_graph_label.setOnMouseClicked(e -> createGenerateDialog());
		
		graph.visibleProperty().addListener((obs, newVal, oldVal) -> zoom.setDisable(newVal));
		
		zoom.valueProperty().addListener((obs, newVal, oldVal) -> {
			if (WSeminar.instance.getGraph() != null && graph.isVisible()) {
				Pane pane = (Pane) WSeminar.window.getScene().lookup("#graph");
				
				Bounds a = pane.getBoundsInParent();
				
				pane.setScaleX(Math.max(0.1f, Math.min(2, newVal.floatValue() / 200f)));
				pane.setScaleY(Math.max(0.1f, Math.min(2, newVal.floatValue() / 200f)));
				
				Bounds b = pane.getBoundsInParent();
				
				float mouseX = (float) (scrollMouse.x == 0 ? a.getWidth() / 2 : scrollMouse.x);
				float mouseY = (float) (scrollMouse.y == 0 ? a.getHeight() / 2 : scrollMouse.y);
				
				pane.setTranslateX(pane.translateXProperty().add(mouseX - (mouseX / a.getWidth() * b.getWidth())).get());
				pane.setTranslateY(pane.translateYProperty().add(mouseY - (mouseY / a.getHeight() * b.getHeight())).get());
			}
		});
		
		Tooltip tooltip = new Tooltip("Klicken, um zu kopieren.");
		hackTooltipStartTiming(tooltip);
		seed_label.setOnMouseClicked(e -> {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			content.putString("" + WSeminar.seed);
			clipboard.setContent(content);
		});
		Tooltip.install(seed_label, tooltip);
		
		graph.getParent().setOnScroll(e -> {
			if (graph != null && graph.isVisible()) {
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
			
			if (e.isSecondaryButtonDown() && graph != null && graph.isVisible()) {
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
		menu_graph.getItems().get(0).setOnAction(e -> {
			if (!animatingPathFinding) createGenerateDialog();
		});
		
		// relayout_graph, JFX bug!
		menu_graph.getItems().get(1).setOnAction(e -> {
			if (WSeminar.instance.getSourceGraph() != null && (System.currentTimeMillis() - last) > 200 && !animatingPathFinding) {
				MainController.doLayoutWithProgress(WSeminar.instance.getLayout(), null, true, true);
				last = System.currentTimeMillis();
			}
		});
		tab_benchmark.selectedProperty().addListener((obs, oldVal, newVal) -> {
			graph.setVisible(!newVal);
			benchmark.setVisible(newVal);
			benchmark.autosize();
			graph.autosize();
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
		
		path_x.setOnAction(e -> {
			if (WSeminar.instance.getSourceGraph() == null) return;
			if (WSeminar.instance.goalVertex != null) WSeminar.instance.goalVertex.setState(null);
			WSeminar.instance.selectGoalVertex = false;
			WSeminar.instance.goalVertex = null;
		});
		
		path_algorithm.getItems().addAll("DFS", "Dijkstra", "AStar");
		path_algorithm.setValue(path_algorithm.getItems().get(0));
		
		path_tree.setRoot(new PathTreeItem<Integer>("Pfade"));
		path_tree_benchmark.setRoot(new PathTreeItem<Integer>("Pfade"));
		
		path_delete.setOnAction(e -> {
			PathTreeItem<Integer> ti = (PathTreeItem<Integer>) path_tree.getSelectionModel().getSelectedItem();
			if (ti.isSpec()) WSeminar.instance.paths.remove(ti.getPathId());
			else for (TreeItem<String> ti2 : ti.getChildren())
				WSeminar.instance.paths.remove(((PathTreeItem<Integer>) ti2).getPathId());
				
			ti.getParent().getChildren().remove(ti);
		});
		
		path_tree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newV) -> {
			Graph<Vertex<Integer>> g = WSeminar.instance.getGraph();
			
			if (newV == null) return;
			
			Path<Vertex<Integer>> newVal = WSeminar.instance.paths.get(((PathTreeItem<Integer>) newV).getPathId());
			path_delete.setDisable(path_tree.getRoot().equals(newV));
			
			Visualizer.resetAll(g, true, true);
			if (newVal == null) return;
			for (int i = 0; i < newVal.size() - 1; i++) {
				Visualizer.setVertexState(newVal.get(i), State.CLOSEDLIST, false);
				Visualizer.setVertexState(newVal.get(i + 1), State.CLOSEDLIST, false);
				Visualizer.setEdgePath(g.getEdge(newVal.get(i), newVal.get(i + 1)), true, false);
			}
			Visualizer.setVertexState(newVal.get(0), State.START, false);
			Visualizer.setVertexState(newVal.get(newVal.size() - 1), State.GOAL, false);
		});
		
		path_tree.getRoot().addEventHandler(TreeItem.childrenModificationEvent(), e -> path_tree_benchmark.setRoot(path_tree.getRoot()));
		
		path_find.setOnAction(e -> {
			if (WSeminar.instance.startVertex == null || WSeminar.instance.startVertex == WSeminar.instance.goalVertex) return;
			
			if (WSeminar.instance.goalVertex == null) {
				if (!batch) {
					Stage stage = WSeminar.createDialog("prompt", "Massen-Wegsuche", WSeminar.window);
					((Label) stage.getScene().lookup("#message")).setText("Sicher?");
					((Label) stage.getScene().lookup("#details")).setText("Sie haben nur einen Start- aber keinen Endknoten gewählt. Somit wird eine Wegsuche für jeden anderen Knoten im Graph gestartet. Dies kann sehr zeitaufwendig werden. \nSind Sie sicher, dass sie dies starten möchten?");
					((Button) stage.getScene().lookup("#okButton")).setOnAction(i -> {
						((Stage) stage.getScene().getWindow()).close();
						batch = true;
						path_find.fire();
					});
					return;
				}
			} else batch = false;
			
			new Thread() {
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					try {
						animatingPathFinding = true;
						menu_graph.getItems().get(0).setDisable(true);
						menu_graph.getItems().get(1).setDisable(true);
						
						Class<?> c = Class.forName("de.dakror.wseminar.graph.algorithm." + path_algorithm.getValue());
						Constructor<?> con = c.getConstructor(Graph.class, boolean.class);
						
						Visualizer.setEnabled(path_animate.isSelected() && !batch);
						
						if (batch) {
							for (Vertex<Integer> v : WSeminar.instance.getGraph().getVertices()) {
								if (!v.equals(WSeminar.instance.startVertex.getVertex())) {
									PathFinder<Integer> pf = (PathFinder<Integer>) con.newInstance(WSeminar.instance.getGraph(), false);
									Path<Vertex<Integer>> p = pf.findPath(WSeminar.instance.startVertex.getVertex(), v);
									
									if (p == null) {
										System.out.println("no path to " + v);
										continue;
									}
									Platform.runLater(() -> {
										PathTreeItem<Integer> pti = null;
										if ((pti = ((PathTreeItem<Integer>) path_tree.getRoot()).insert(p, true)) != null) {
											WSeminar.instance.paths.put(p.hashCode(), p);
											path_tree.getSelectionModel().select(pti);
										}
									});
								}
							}
							batch = false;
						} else {
							PathFinder<Integer> pf = (PathFinder<Integer>) con.newInstance(WSeminar.instance.getGraph(), path_animate.isSelected());
							Path<Vertex<Integer>> p = pf.findPath(WSeminar.instance.startVertex.getVertex(), WSeminar.instance.goalVertex.getVertex());
							Visualizer.setEnabled(true);
							
							animatingPathFinding = false;
							menu_graph.getItems().get(0).setDisable(false);
							menu_graph.getItems().get(1).setDisable(false);
							
							Platform.runLater(() -> {
								PathTreeItem<Integer> pti = null;
								
								if (p == null) {
									Stage stage = WSeminar.createDialog("alert", "Wegfindung", WSeminar.window);
									((Label) stage.getScene().lookup("#message")).setText("Wegfindung fehlgeschlagen");
									((Label) stage.getScene().lookup("#details")).setText("Womöglich konnte der Weg aufgrund eines nicht vollständig zusammenhängenden oder gerichteten Graphen gefunden werden. Bitte wählen Sie andere Endknoten zur Wegfindung.");
								} else if ((pti = ((PathTreeItem<Integer>) path_tree.getRoot()).insert(p, false)) != null) {
									WSeminar.instance.paths.put(p.hashCode(), p);
									path_tree.getSelectionModel().select(pti);
								}
							});
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		});
		
		// -- benchmark section -- //
		chart_timeline.setAnimated(false);
		chart_timeline.setCreateSymbols(true);
		
		chart_alltime.setAnimated(false);
		
		//		path_tree_benchmark.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		path_tree_benchmark.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newV) -> {
			
			//						path_tree_benchmark.getSelectionModel().getSelectedItems().forEach(new
			
			if (newV == null) return;
			Path<Vertex<Integer>> newVal = WSeminar.instance.paths.get(((PathTreeItem<Integer>) newV).getPathId());
			chart_timeline.getData().clear();
			chart_alltime.getData().clear();
			chart_table.getItems().clear();
			
			if (newV.getParent() == null) return;
			
			TimeLineDataFiller tldf = new TimeLineDataFiller();
			if (newV.getParent().equals(path_tree_benchmark.getRoot()) && !newV.isLeaf()) {
				
				XYChart.Series<String, Long> sc = new XYChart.Series<>();
				sc.setName("Gesamtzeit");
				chart_alltime.getData().add(sc);
				
				for (TreeItem<String> ti : newV.getChildren()) {
					Path<Vertex<Integer>> path = WSeminar.instance.paths.get(((PathTreeItem<Integer>) ti).getPathId());
					
					XYChart.Data<String, Long> d = new XYChart.Data<>(path.getUserData().toString(), path.getBenchmark().getTime() / 1000);
					sc.getData().add(d);
					Tooltip tt = new Tooltip(path.getUserData().toString() + ": " + (path.getBenchmark().getTime() / 1000) + "ms");
					hackTooltipStartTiming(tt);
					Tooltip.install(d.getNode(), tt);
				}
				
				tldf.generateColors(newV.getChildren().size() * 2);
				for (TreeItem<String> ti : newV.getChildren()) {
					Path<Vertex<Integer>> path = WSeminar.instance.paths.get(((PathTreeItem<Integer>) ti).getPathId());
					tldf.fill(path);
					chart_table.getItems().add(path);
				}
			} else {
				XYChart.Series<String, Long> sc = new XYChart.Series<>();
				sc.setName("Gesamtzeit");
				chart_alltime.getData().add(sc);
				XYChart.Data<String, Long> d = new XYChart.Data<>(newVal.getUserData().toString(), newVal.getBenchmark().getTime() / 1000);
				sc.getData().add(d);
				
				tldf.generateColors(2);
				tldf.fill(newVal);
				
				chart_table.getItems().add(newVal);
			}
			
			for (int i = 0; i < chart_timeline.getData().size(); i++) {
				XYChart.Series<Long, Integer> s = chart_timeline.getData().get(i);
				
				Path<Vertex<Integer>> path = newV.getParent().equals(path_tree_benchmark.getRoot()) && !newV.isLeaf()
						? WSeminar.instance.paths.get(((PathTreeItem<Integer>) newV.getChildren().get(i / Type.values().length)).getPathId()) : newVal;
						
				Color c = tldf.palette[(i % Type.values().length) * (tldf.palette.length / Type.values().length) + i / Type.values().length];
				s.getNode().setStyle(String.format("-fx-stroke: #%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()));
				
				for (XYChart.Data<Long, Integer> d : s.getData()) {
					Tooltip tt = new Tooltip(path.getUserData().toString() + "(" + d.getXValue() + (path.getUserData().toString().contains("anim") ? "m" : "µ") + "s): " + d.getYValue() + " "
							+ s.getName());
					hackTooltipStartTiming(tt);
					((StackPane) d.getNode()).setPrefSize(8, 8);
					d.getNode().setStyle(String.format("-fx-background-color: #%02x%02x%02x, white", c.getRed(), c.getGreen(), c.getBlue()));
					Tooltip.install(d.getNode(), tt);
				}
			}
			
			Legend l = (Legend) chart_timeline.getChartLegend();
			l.setItems(new ObservableListWrapper<>(l.getItems().subList(0, Type.values().length)));
			
			for (Node n : l.lookupAll(".chart-legend-item")) {
				n.setOnMouseClicked(e -> {
					if (!n.getStyleClass().contains("disabled")) n.getStyleClass().add("disabled");
					else n.getStyleClass().remove("disabled");
					
					boolean ds = n.getStyleClass().contains("disabled");
					
					for (int i = Type.getByDesc(((Label) n).getText()).ordinal(); i < chart_timeline.getData().size(); i += Type.values().length) {
						XYChart.Series<Long, Integer> s = chart_timeline.getData().get(i);
						s.getNode().setVisible(!ds);
						s.getData().forEach(d -> d.getNode().setVisible(!ds));
					}
				});
			}
			
			for (int i = 0; i < l.getItems().size(); i++) {
				Color c = tldf.palette[(i % Type.values().length) * (tldf.palette.length / Type.values().length)];
				l.getItems().get(i).getSymbol().setStyle(String.format("-fx-background-color: #%02x%02x%02x, white;", c.getRed(), c.getGreen(), c.getBlue()));
			}
		});
		
		TableColumn<Path<Vertex<Integer>>, String> tc = new TableColumn<>("Pfad");
		tc.setCellValueFactory(p -> new ReadOnlyObjectWrapper<String>(p.getValue().getUserData().toString()));
		chart_table.getColumns().add(tc);
		
		for (Type t : Type.values()) {
			if (t.name().endsWith("SIZE")) {
				TableColumn<Path<Vertex<Integer>>, Integer> tc2 = new TableColumn<>("min. " + t.desc);
				tc2.setCellValueFactory(p -> new ReadOnlyObjectWrapper<Integer>((int) p.getValue().getBenchmark().getMin(t)));
				chart_table.getColumns().add(tc2);
				tc2 = new TableColumn<>("max. " + t.desc);
				tc2.setCellValueFactory(p -> new ReadOnlyObjectWrapper<Integer>((int) p.getValue().getBenchmark().getSum(t)));
				chart_table.getColumns().add(tc2);
			} else {
				TableColumn<Path<Vertex<Integer>>, Integer> tc2 = new TableColumn<>(t.desc);
				tc2.setCellValueFactory(p -> new ReadOnlyObjectWrapper<Integer>((int) p.getValue().getBenchmark().getSum(t)));
				chart_table.getColumns().add(tc2);
			}
		}
		
	}
	
	public static void createGenerateDialog() {
		WSeminar.createDialog("generate_graph_dialog", "Neues Netz generieren", WSeminar.window);
	}
	
	/** 
	 * Hack to work around the currently unmodifiable tooltip timeout.<br>
	* Found at <a href=http://stackoverflow.com/a/27739605/4882174>http://stackoverflow.com/a/27739605/4882174</a>
	*/
	public static void hackTooltipStartTiming(Tooltip tooltip) {
		try {
			Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
			fieldBehavior.setAccessible(true);
			Object objBehavior = fieldBehavior.get(tooltip);
			
			Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
			fieldTimer.setAccessible(true);
			Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);
			
			objTimer.getKeyFrames().clear();
			objTimer.getKeyFrames().add(new KeyFrame(new Duration(50)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void doLayoutWithProgress(Layout<Integer> layout, String message, boolean transition, boolean setGraphAnimate) {
		if (layout.getSourceGraph().getVertices().size() < 100) {
			Graph<Vertex<Integer>> render = layout.render();
			if (transition) WSeminar.instance.transitionTo(render);
			else WSeminar.instance.setGraph(render, setGraphAnimate);
			
			return;
		}
		
		Stage progress = showProgressDialog(layout.progress, message);
		
		new Thread() {
			@Override
			public void run() {
				Graph<Vertex<Integer>> render = layout.render();
				
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
	
	public static Stage showProgressDialog(ObservableValue<? extends Number> ov, String message) {
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
		
		ov.addListener(cl);
		progress.setOnHiding(e -> ov.removeListener(cl));
		
		return progress;
	}
	
	class TimeLineDataFiller {
		Color palette[];
		int highestX = 0;
		int highestY = 0;
		
		void generateColors(int n) {
			n *= Type.values().length;
			Color[] cols = new Color[n];
			for (int i = 0; i < n; i++) {
				cols[i] = Color.getHSBColor(i / (float) (n - 1), 0.9f, i % 2 == 0 ? 0.85f : 1.0f);
			}
			palette = cols;
		}
		
		void fill(Path<Vertex<Integer>> path) {
			for (Type t : Type.values()) {
				XYChart.Series<Long, Integer> series = new XYChart.Series<>();
				series.setName(t.desc);
				for (Timestamp ts : path.getBenchmark().get(t)) {
					if (ts.time > highestX) highestX = (int) ts.time;
					if (ts.stamp > highestY) highestY = (int) ts.stamp;
					
					XYChart.Data<Long, Integer> d = new XYChart.Data<>((long) (ts.time / (path.getUserData().toString().contains("anim") ? 1000f : 1)), (int) ts.stamp);
					series.getData().add(d);
				}
				chart_timeline.getData().add(series);
			}
		}
	}
}
