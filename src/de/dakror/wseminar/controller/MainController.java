package de.dakror.wseminar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import de.dakror.wseminar.WSeminar;

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
	private ListView<?> graph_list;
	
	@FXML
	private TreeView<?> paths_tree;
	
	@FXML
	void initialize() {
		new_graph_label.setOnMouseClicked(e -> createGenerateDialog());
		
		zoom.valueProperty().addListener((obs, newVal, oldVal) -> {
			if (WSeminar.instance.getGraph() != null) {
				Pane pane = (Pane) WSeminar.window.getScene().lookup("#graph");
				
				Bounds a = pane.getBoundsInParent();
				
				pane.setScaleX(Math.max(0.1f, Math.min(4, newVal.floatValue() / 100f)));
				pane.setScaleY(Math.max(0.1f, Math.min(4, newVal.floatValue() / 100f)));
				
				Bounds b = pane.getBoundsInParent();
				
				float mouseX = (float) (WSeminar.instance.scrollMouse.x == 0 ? a.getWidth() / 2 : WSeminar.instance.scrollMouse.x);
				float mouseY = (float) (WSeminar.instance.scrollMouse.y == 0 ? a.getHeight() / 2 : WSeminar.instance.scrollMouse.y);
				
				pane.setTranslateX(pane.translateXProperty().add(mouseX - (mouseX / a.getWidth() * b.getWidth())).get());
				pane.setTranslateY(pane.translateYProperty().add(mouseY - (mouseY / a.getHeight() * b.getHeight())).get());
			}
		});
		
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
