package de.dakror.wseminar;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import de.dakror.wseminar.graph.GraphGenerator;
import de.dakror.wseminar.graph.api.Graph;
import de.dakror.wseminar.graph.api.Node;
import de.dakror.wseminar.math.Vector2;

/**
 * @author Dakror
 */
public class WSeminar extends Application {
	
	public static WSeminar instance;
	public static Window window;
	
	static HashMap<String, Image> imgCache = new HashMap<>();
	
	Graph<Vector2> graph;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		window = primaryStage;
		
		primaryStage.setScene(createScene("main"));
		primaryStage.setTitle("WSeminar Extrema: Wegfindung - Maximilian Stark");
		
		primaryStage.getIcons().addAll(getImage("mind_map-24.png"), getImage("mind_map-32.png"));
		
		primaryStage.show();
	}
	
	public void setGraph(Graph<Vector2> graph) {
		this.graph = graph;
		WSeminar.window.getScene().lookup("#newGraph").setVisible(graph == null);
		
		Pane pane = (Pane) WSeminar.window.getScene().lookup("#graph");
		
		for (Node<Vector2> node : graph.getNodes()) {
			pane.getChildren().add(createAbstractGraphNode("#node", node));
		}
	}
	
	public static Line createEdge(Node<Vector2> from, Node<Vector2> to) {
		Line line = new Line(//
		from.getStorage().x * GraphGenerator.CELL_SIZE + GraphGenerator.CELL_SIZE / 2, //
		from.getStorage().y * GraphGenerator.CELL_SIZE + GraphGenerator.CELL_SIZE, //
		to.getStorage().x * GraphGenerator.CELL_SIZE + GraphGenerator.CELL_SIZE, //
		to.getStorage().y * GraphGenerator.CELL_SIZE + GraphGenerator.CELL_SIZE //
		);
		
		return line;
	}
	
	public static Circle createAbstractGraphNode(String selector, Node<Vector2> node) {
		Circle template = (Circle) WSeminar.window.getScene().lookup(selector);
		
		Circle circle = new Circle(node.getStorage().x * GraphGenerator.CELL_SIZE + GraphGenerator.CELL_SIZE / 2, node.getStorage().y * GraphGenerator.CELL_SIZE + GraphGenerator.CELL_SIZE / 2, template.getRadius());
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
