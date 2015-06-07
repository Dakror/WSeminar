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


package de.dakror.wseminar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import de.dakror.wseminar.controller.GenerateGraphDialogController;
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.VertexData.Delay;
import de.dakror.wseminar.graph.algorithm.Layout;
import de.dakror.wseminar.ui.GraphTreeCell;
import de.dakror.wseminar.ui.GraphTreeItem;
import de.dakror.wseminar.ui.VisualEdge;
import de.dakror.wseminar.ui.VisualVertex;

/**
 * @author Dakror
 */
public class WSeminar extends Application {
	public static WSeminar instance;
	public static Window window;
	
	public static Random r;
	public static long seed;
	
	static HashMap<String, Image> imgCache = new HashMap<>();
	
	Graph<Integer> sourceGraph;
	Layout<Integer> layout;
	Graph<Vertex<Integer>> graph;
	
	int graphSize;
	
	public VisualVertex<Integer> activeVertex;
	public VisualEdge<Integer> activeEdge;
	
	int duration = 200;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		window = primaryStage;
		primaryStage.setMaximized(true);
		primaryStage.setScene(createScene("main"));
		
		primaryStage.setTitle("WSeminar Extrema: Wegfindung - Maximilian Stark");
		
		primaryStage.getIcons().addAll(getImage("mind_map-24.png"), getImage("mind_map-32.png"));
		
		primaryStage.show();
	}
	
	public void setSourceGraph(Graph<Integer> sourceGraph) {
		getMenuItem("#menu_graph", "relayout_graph").setDisable(sourceGraph == null);
		this.sourceGraph = sourceGraph;
	}
	
	public Graph<Integer> getSourceGraph() {
		return sourceGraph;
	}
	
	public void setLayout(Layout<Integer> layout) {
		this.layout = layout;
	}
	
	public Layout<Integer> getLayout() {
		return layout;
	}
	
	public void setGraphSize(int graphSize) {
		this.graphSize = graphSize;
	}
	
	public int getGraphSize() {
		return graphSize;
	}
	
	public Graph<Vertex<Integer>> getGraph() {
		return graph;
	}
	
	public void transitionTo(Graph<Vertex<Integer>> graph) {
		if (this.graph == null) {
			setGraph(graph, true);
			return;
		}
		
		Pane pane = (Pane) WSeminar.window.getScene().lookup("#graph");
		
		ParallelTransition pt = new ParallelTransition();
		
		for (int i = 0; i < graph.getVertices().size(); i++) {
			Vertex<Integer> v = graph.getVertices().get(i);
			Node node = pane.lookup("#V" + v.data());
			Node text = pane.lookup("#VT" + v.data());
			TranslateTransition tt = new TranslateTransition(Duration.millis(GenerateGraphDialogController.speed), node);
			VisualVertex<Integer> newCircle = new VisualVertex<Integer>("#node", v);
			
			tt.setToX(newCircle.getTranslateX());
			tt.setToY(newCircle.getTranslateY());
			
			TranslateTransition tt2 = new TranslateTransition(Duration.millis(GenerateGraphDialogController.speed), text);
			tt2.setToX(newCircle.getTranslateX());
			tt2.setToY(newCircle.getTranslateY());
			
			pt.getChildren().addAll(tt, tt2);
		}
		
		for (int i = 0; i < graph.getEdges().size(); i++) {
			Edge<Vertex<Integer>> e = graph.getEdges().get(i);
			Line node = (Line) pane.lookup("#E" + i);
			
			VisualVertex<Integer> newFrom = new VisualVertex<Integer>("#node", e.getFrom());
			VisualVertex<Integer> newTo = new VisualVertex<Integer>("#node", e.getTo());
			//@off
			Timeline tl = new Timeline(new KeyFrame(new Duration(GenerateGraphDialogController.speed), 
			                                        new KeyValue(node.startXProperty(), newFrom.getTranslateX() + Const.cellSize / 2),
																							new KeyValue(node.startYProperty(), newFrom.getTranslateY() + Const.cellSize / 2), 
																							new KeyValue(node.endXProperty(),     newTo.getTranslateX() + Const.cellSize / 2),
																							new KeyValue(node.endYProperty(),     newTo.getTranslateY() + Const.cellSize / 2)));			
			//@on
			
			pt.getChildren().add(tl);
		}
		
		pt.play();
		this.graph = graph;
	}
	
	@SuppressWarnings("unchecked")
	public void setGraph(Graph<Vertex<Integer>> graph, boolean animate) {
		activeVertex = null;
		this.graph = graph;
		
		Node n = window.getScene().lookup("#newGraph");
		if (n != null) n.setVisible(graph == null);
		Pane pane = (Pane) window.getScene().lookup("#graph");
		pane.getChildren().clear();
		
		((Slider) window.getScene().lookup("#zoom")).setValue(100);
		pane.setScaleX(1);
		pane.setScaleY(1);
		
		TreeView<String> tv = ((TreeView<String>) window.getScene().lookup("#graph_tree"));
		
		tv.setCellFactory(tree -> new GraphTreeCell());
		
		GraphTreeItem root = new GraphTreeItem(null, "Graph");
		tv.setRoot(root);
		
		ArrayList<Vertex<Integer>> addedVertices = new ArrayList<>();
		
		for (int i = 0; i < graph.getEdges().size(); i++) {
			Edge<Vertex<Integer>> e = graph.getEdges().get(i);
			VisualEdge<Integer> edge = new VisualEdge<>(e, i, pane);
			
			for (Vertex<Integer> v : new Vertex[] { e.getFrom(), e.getTo() }) {
				int index = addedVertices.indexOf(v);
				
				VisualVertex<Integer> circle = new VisualVertex<Integer>("#node", v);
				circle.setId("V" + v.data());
				
				GraphTreeItem gti = new GraphTreeItem(circle, "Vertex", v.data());
				
				if (v.equals(e.getFrom()) || !e.isDirected()) {
					TreeItem<String> item = index == -1 ? gti : root.getChildren().get(index);
					item.getChildren().add(new GraphTreeItem(edge, "Kante " + (e.isDirected() ? "" : "<") + "-> " + e.getOtherEnd(v).data(), 14));
				}
				if (index > -1) continue;
				
				root.getChildren().add(gti);
				
				Label l = new Label(v.data() + "");
				l.setId("VT" + v.data());
				l.setTextFill(Color.BLACK);
				l.setFont(Font.font(null, FontWeight.NORMAL, 15));
				l.setMinSize(Const.cellSize, Const.cellSize);
				l.setAlignment(Pos.CENTER);
				l.setTranslateX(circle.getTranslateX());
				l.setTranslateY(circle.getTranslateY());
				
				FadeTransition ft = new FadeTransition(Duration.millis(duration), l);
				ft.setFromValue(0);
				ft.setToValue(1);
				ft.setInterpolator(Interpolator.EASE_OUT);
				
				FadeTransition ft2 = new FadeTransition(Duration.millis(duration), circle);
				ft2.setFromValue(0);
				ft2.setToValue(1);
				ft2.setInterpolator(Interpolator.EASE_OUT);
				
				ScaleTransition st = new ScaleTransition(Duration.millis(duration), circle);
				st.setFromX(0);
				st.setFromY(0);
				st.setToX(1);
				st.setToY(1);
				st.setInterpolator(Const.overlyEaseIn);
				
				l.setDisable(true);
				
				ParallelTransition pt = new ParallelTransition(circle, ft, ft2, st);
				
				pane.getChildren().add(circle);
				pane.getChildren().add(l);
				float delay = (float) (Math.random() * 400);
				
				Delay d = new Delay();
				d.delay = delay;
				v.add(d);
				
				pt.setDelay(Duration.millis(delay));
				if (animate) pt.play();
				
				addedVertices.add(v);
			}
			
			pane.getChildren().add(0, edge);
			
			FadeTransition ft2 = new FadeTransition(Duration.millis(duration), edge);
			ft2.setFromValue(0);
			ft2.setToValue(1);
			ft2.setInterpolator(Interpolator.EASE_OUT);
			
			ScaleTransition st = new ScaleTransition(Duration.millis(duration), edge);
			st.setFromX(0);
			st.setFromY(0);
			st.setToX(1);
			st.setToY(1);
			
			ParallelTransition pt = new ParallelTransition(edge, ft2, st);
			
			pt.setDelay(Duration.millis(Math.max(e.getFrom().get(Delay.class).delay, e.getTo().get(Delay.class).delay)));
			
			if (animate) pt.play();
		}
		
		root.getChildren().sort((a, b) -> Integer.compare(((GraphTreeItem) a).getParam(), ((GraphTreeItem) b).getParam()));
		
		Bounds pB = pane.getParent().getParent().getBoundsInParent();
		
		pane.setTranslateX((pB.getWidth() - layout.getBounds().getWidth() * Const.cellSize) / 2);
		pane.setTranslateY((pB.getHeight() - layout.getBounds().getHeight() * Const.cellSize) / 2);
	}
	
	// -- statics -- //
	
	public static void setSeed(long seed2) {
		if (r != null) System.out.println("Overriding main Seed!");
		r = new Random(seed2);
		seed = seed2;
	}
	
	public static Scene createScene(String resource) {
		try {
			FXMLLoader l = new FXMLLoader(WSeminar.class.getResource("/assets/fxml/" + resource + ".fxml"));
			Parent pane = (Parent) l.load();
			Scene scene = new Scene(pane);
			scene.getStylesheets().addAll("assets/css/Theme.css", "assets/css/style.css");
			
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Stage createDialog(String resource, String title, Window owner) {
		return createDialog(resource, title, owner, StageStyle.DECORATED, Modality.APPLICATION_MODAL);
	}
	
	public static Stage createDialog(String resource, String title, Window owner, StageStyle style, Modality modality) {
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.initStyle(style);
		stage.getIcons().addAll(WSeminar.getImage("mind_map-24.png"), WSeminar.getImage("mind_map-32.png"));
		stage.setScene(WSeminar.createScene(resource));
		stage.setTitle(title);
		stage.initModality(modality);
		stage.initOwner(owner);
		stage.show();
		
		return stage;
	}
	
	public static MenuItem getMenuItem(String menuSelector, String id) {
		MenuButton menu = (MenuButton) window.getScene().lookup(menuSelector);
		for (MenuItem mi : menu.getItems())
			if (mi.getId().equals(id)) return mi;
		
		System.out.println("NOP");
		return null;
	}
	
	public static Image getImage(String resource) {
		if (imgCache.containsKey(resource)) return imgCache.get(resource);
		else {
			Image img = new Image(WSeminar.class.getResourceAsStream("/assets/img/" + resource));
			
			imgCache.put(resource, img);
			return img;
		}
	}
	
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			e.printStackTrace();
			
			Stage stage = createDialog("alert", "Fehler!", window);
			((Label) stage.getScene().lookup("#message")).setText(e.getClass().getSimpleName());
			((Label) stage.getScene().lookup("#details")).setText(e.getLocalizedMessage());
		});
		
		launch(args);
	}
}
