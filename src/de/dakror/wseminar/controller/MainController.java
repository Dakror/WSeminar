package de.dakror.wseminar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.math.Vector2;

/**
 * @author Dakror
 */
public class MainController {
	
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private URL location;
	
	@FXML
	private Menu menu_graph;
	
	@FXML
	private MenuItem relayout_graph;
	
	@FXML
	private Slider zoom;
	
	@FXML
	private Pane graph;
	
	@FXML
	private Label new_graph_label;
	
	@FXML
	private MenuItem new_graph;
	
	@FXML
	private TreeView<String> graph_tree;
	
	@FXML
	private TreeView<?> paths_tree;
	
	float lastX = -1, lastY = -1;
	
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
				scrollMouse.set((float) (e.getX() - graph.getBoundsInParent().getMinX()), (float) (e.getY() - graph.getBoundsInParent().getMinY()));
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
				graph.getScene().setCursor(Cursor.DEFAULT);
			}
		};
		
		graph.getParent().setOnMouseReleased(eh);
		graph.getParent().setOnMouseDragged(eh);
		graph.getParent().setOnMouseExited(e -> scrollMouse.zero());
		
		// new_graph
		menu_graph.getItems().get(0).setOnAction(e -> createGenerateDialog());
		
		// relayout_graph, JFX bug!
		menu_graph.getItems().get(1).setOnAction(e -> {
			if (WSeminar.instance.getSourceGraph() != null) WSeminar.instance.transitionTo(WSeminar.instance.getLayout().render());
		});
		
	}
	
	void createGenerateDialog() {
		WSeminar.createDialog("generate_graph_dialog", "Neues Netz generieren", WSeminar.window);
	}
}
