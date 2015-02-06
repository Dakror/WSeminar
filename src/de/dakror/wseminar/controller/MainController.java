package de.dakror.wseminar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.layout.FRLayout;

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
				
				pane.setScaleX(Math.max(0.1f, Math.min(2, newVal.floatValue() / 100f)));
				pane.setScaleY(Math.max(0.1f, Math.min(2, newVal.floatValue() / 100f)));
			}
		});
		
		// new_graph
		menu_graph.getItems().get(0).setOnAction(e -> createGenerateDialog());
		
		// relayout_graph, JFX bug!
		menu_graph.getItems().get(1).setOnAction(	e -> {
																								if (WSeminar.instance.getSourceGraph() != null)
																									WSeminar.instance.transitionTo(new FRLayout<Integer>(WSeminar.instance.getGraphSize()).render(WSeminar.instance.getSourceGraph(),
																																																																								Const.defaultCycles
																																																																										* WSeminar.instance.getGraphSize()));
																							});
	}
	
	void createGenerateDialog() {
		WSeminar.createDialog("generate_graph_dialog", "Neues Netz generieren", WSeminar.window);
	}
}
