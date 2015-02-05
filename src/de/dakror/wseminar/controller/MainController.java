package de.dakror.wseminar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
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
		new_graph_label.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				WSeminar.createDialog("generate_graph_dialog", "Neues Netz generieren", WSeminar.window);
			}
		});
		
		zoom.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (WSeminar.instance.getGraph() != null) {
					Pane pane = (Pane) WSeminar.window.getScene().lookup("#graph");
					
					pane.setScaleX(Math.max(0.1f, Math.min(2, newValue.floatValue() / 100f)));
					pane.setScaleY(Math.max(0.1f, Math.min(2, newValue.floatValue() / 100f)));
				}
			}
		});
		
		// new_graph
		menu_graph.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				WSeminar.createDialog("generate_graph_dialog", "Neues Netz generieren", WSeminar.window);
			}
		});
		
		// relayout_graph, JFX bug!
		menu_graph.getItems().get(1).setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (WSeminar.instance.getSourceGraph() != null)
					WSeminar.instance.transitionTo(new FRLayout<Integer>(WSeminar.instance.getGraphSize()).render(WSeminar.instance.getSourceGraph(),
																																																				Const.defaultCycles * WSeminar.instance.getGraphSize()));
			}
		});
	}
}
