package de.dakror.wseminar;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import de.dakror.wseminar.controller.GenerateGraphDialogController;
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.WeightedEdge;
import de.dakror.wseminar.graph.vertexdata.Delay;
import de.dakror.wseminar.graph.vertexdata.Position;

/**
 * @author Dakror
 */
public class WSeminar extends Application {
	public static WSeminar instance;
	public static Window window;
	
	static HashMap<String, Image> imgCache = new HashMap<>();
	
	Graph<Integer> sourceGraph;
	int graphSize;
	long seed;
	Graph<Vertex<Integer>> graph;
	
	int duration = 400;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		window = primaryStage;
		primaryStage.setMaximized(true);
		
		primaryStage.setScene(createScene("main"));
		primaryStage.setTitle("WSeminar Extrema: Wegfindung - Maximilian Stark");
		
		primaryStage.getIcons().addAll(getImage("mind_map-24.png"), getImage("mind_map-32.png"));
		
		primaryStage.show();
		
		Pane pane = (Pane) window.getScene().lookup("#graph");
		pane.getParent().addEventHandler(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				if (graph != null) {
					pane.setScaleX(Math.max(0.1f, Math.min(2, pane.getScaleX() + event.getDeltaY() * 0.001f)));
					pane.setScaleY(Math.max(0.1f, Math.min(2, pane.getScaleY() + event.getDeltaY() * 0.001f)));
					
					((Slider) WSeminar.window.getScene().lookup("#zoom")).setValue(100 * pane.getScaleX());
					
					event.consume();
				}
			}
		});
		pane.getParent().addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
			float lastX = -1, lastY = -1;
			
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && graph != null) {
					window.getScene().setCursor(Cursor.MOVE);
					if (lastX != -1) {
						float deltaX = (float) (event.getX() - lastX);
						float deltaY = (float) (event.getY() - lastY);
						pane.setTranslateX(pane.getTranslateX() + deltaX);
						pane.setTranslateY(pane.getTranslateY() + deltaY);
						
						event.consume();
					}
					
					lastX = (float) event.getX();
					lastY = (float) event.getY();
				} else {
					lastX = -1;
					lastY = -1;
					window.getScene().setCursor(Cursor.DEFAULT);
				}
			}
		});
	}
	
	public void setSeed(long seed) {
		this.seed = seed;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public void setSourceGraph(Graph<Integer> sourceGraph) {
		getMenuItem("#menu_graph", "relayout_graph").setDisable(sourceGraph == null);
		this.sourceGraph = sourceGraph;
	}
	
	public Graph<Integer> getSourceGraph() {
		return sourceGraph;
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
			Circle newCircle = createGraphVertex("#node", v);
			
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
			Text text = (Text) pane.lookup("#ET" + i);
			
			Circle newFrom = createGraphVertex("#node", e.getFrom());
			Circle newTo = createGraphVertex("#node", e.getTo());
			//@off
			Timeline tl = new Timeline(new KeyFrame(new Duration(GenerateGraphDialogController.speed), 
			                                        new KeyValue(node.startXProperty(), newFrom.getTranslateX() + Const.cellSize / 2),
																							new KeyValue(node.startYProperty(), newFrom.getTranslateY() + Const.cellSize / 2), 
																							new KeyValue(node.endXProperty(),     newTo.getTranslateX() + Const.cellSize / 2),
																							new KeyValue(node.endYProperty(),     newTo.getTranslateY() + Const.cellSize / 2)));			
			//@on
			TranslateTransition tt = new TranslateTransition(new Duration(GenerateGraphDialogController.speed), text);
			tt.setToX(0.5f * (newFrom.getTranslateX() + Const.cellSize / 2 + newTo.getTranslateX() + Const.cellSize / 2));
			tt.setToY(0.5f * (newFrom.getTranslateY() + Const.cellSize / 2 + newTo.getTranslateY() + Const.cellSize / 2));
			
			pt.getChildren().addAll(tl, tt);
		}
		
		pt.play();
		this.graph = graph;
	}
	
	public void setGraph(Graph<Vertex<Integer>> graph, boolean animate) {
		this.graph = graph;
		Node n = WSeminar.window.getScene().lookup("#newGraph");
		if (n != null) n.setVisible(graph == null);
		Pane pane = (Pane) WSeminar.window.getScene().lookup("#graph");
		pane.getChildren().clear();
		
		for (Vertex<Integer> v : graph.getVertices()) {
			Circle circle = createGraphVertex("#node", v);
			circle.setId("V" + v.data());
			
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
			
			ParallelTransition pt = new ParallelTransition(circle, ft, ft2, st);
			pane.getChildren().add(circle);
			
			pane.getChildren().add(l);
			float delay = (float) (Math.random() * 400);
			
			Delay d = new Delay();
			d.delay = delay;
			v.add(d);
			
			pt.setDelay(Duration.millis(delay));
			if (animate) pt.play();
		}
		
		addEdges(pane, graph, animate);
	}
	
	void addEdges(Pane pane, Graph<Vertex<Integer>> graph, boolean animate) {
		for (int i = 0; i < graph.getEdges().size(); i++) {
			Edge<Vertex<Integer>> e = graph.getEdges().get(i);
			Line edge = createEdge(e);
			
			edge.setId("E" + i);
			// if (animate) edge.setOpacity(0);
			
			pane.getChildren().add(0, edge);
			
			FadeTransition ft = null;
			if (e instanceof WeightedEdge) {
				Text text = new Text(((WeightedEdge<Vertex<Integer>>) e).getWeight() + "");
				// if (animate) text.setOpacity(0);
				text.setId("ET" + i);
				text.setTranslateX(0.5f * (edge.getStartX() + edge.getEndX()));
				text.setTranslateY(0.5f * (edge.getStartY() + edge.getEndY()));
				pane.getChildren().add(text);
				
				ft = new FadeTransition(Duration.millis(duration), text);
				ft.setFromValue(0);
				ft.setToValue(1);
				ft.setInterpolator(Interpolator.EASE_OUT);
			}
			
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
			if (ft != null) pt.getChildren().add(ft);
			
			pt.setDelay(Duration.millis(Math.max(e.getFrom().get(Delay.class).delay, e.getTo().get(Delay.class).delay)));
			
			if (animate) pt.play();
		}
	}
	
	public static <V> Line createEdge(Edge<Vertex<V>> e) {
		Line line = new Line(e.getFrom().get(Position.class).pos.x * Const.cellSize + Const.cellSize / 2, e.getFrom().get(Position.class).pos.y * Const.cellSize + Const.cellSize / 2,
													e.getTo().get(Position.class).pos.x * Const.cellSize + Const.cellSize / 2, e.getTo().get(Position.class).pos.y * Const.cellSize + Const.cellSize / 2);
		return line;
	}
	
	public static <V> Circle createGraphVertex(String selector, Vertex<V> v) {
		Circle template = (Circle) WSeminar.window.getScene().lookup(selector);
		
		Circle circle = new Circle(Const.cellSize / 2, Const.cellSize / 2, template.getRadius());
		circle.setTranslateZ(2);
		circle.setTranslateX(v.get(Position.class).pos.x * Const.cellSize);
		circle.setTranslateY(v.get(Position.class).pos.y * Const.cellSize);
		circle.setFill(template.getFill());
		circle.setStroke(template.getStroke());
		circle.setStrokeType(template.getStrokeType());
		circle.setEffect(template.getEffect());
		
		return circle;
	}
	
	public static Scene createScene(String resource) {
		try {
			FXMLLoader l = new FXMLLoader(WSeminar.class.getResource("/assets/fxml/" + resource + ".fxml"));
			Pane pane = (Pane) l.load();
			Scene scene = new Scene(pane);
			scene.getStylesheets().addAll("assets/css/Theme.css", "assets/css/style.css");
			
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Stage createDialog(String sceneResource, String title, Window owner) {
		Stage stage = new Stage();
		stage.setResizable(false);
		stage.getIcons().addAll(WSeminar.getImage("mind_map-24.png"), WSeminar.getImage("mind_map-32.png"));
		
		stage.setScene(WSeminar.createScene(sceneResource));
		stage.setTitle(title);
		stage.initModality(Modality.APPLICATION_MODAL);
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
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				e.printStackTrace();
				
				Stage stage = createDialog("alert", "Fehler!", window);
				((Label) stage.getScene().lookup("#message")).setText(e.getClass().getSimpleName());
				((Label) stage.getScene().lookup("#details")).setText(e.getLocalizedMessage());
			}
		});
		
		launch(args);
	}
}
