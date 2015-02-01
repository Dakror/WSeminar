package de.dakror.wseminar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
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
	private Circle node;
	
	@FXML
	private Circle node_new;
	
	@FXML
	private Pane templates;
	
	@FXML
	private Circle node_track;
	
	@FXML
	private Circle node_disabled;
	
	@FXML
	private Circle node_finish;
	
	@FXML
	private Circle node_start;
	
	@FXML
	private Pane graph;
	
	@FXML
	private Label new_graph;
	
	@FXML
	private Slider zoom;
	
	@FXML
	private MenuItem relayout_graph;
	
	@FXML
	private Menu menu_graph;
	
	@FXML
	void initialize() {
		assert node != null : "fx:id=\"node\" was not injected: check your FXML file 'main.fxml'.";
		assert node_new != null : "fx:id=\"node_new\" was not injected: check your FXML file 'main.fxml'.";
		assert templates != null : "fx:id=\"templates\" was not injected: check your FXML file 'main.fxml'.";
		assert node_track != null : "fx:id=\"node_track\" was not injected: check your FXML file 'main.fxml'.";
		assert node_disabled != null : "fx:id=\"node_disabled\" was not injected: check your FXML file 'main.fxml'.";
		assert node_finish != null : "fx:id=\"node_finish\" was not injected: check your FXML file 'main.fxml'.";
		assert node_start != null : "fx:id=\"node_start\" was not injected: check your FXML file 'main.fxml'.";
		assert graph != null : "fx:id=\"graph\" was not injected: check your FXML file 'main.fxml'.";
		assert new_graph != null : "fx:id=\"new_graph\" was not injected: check your FXML file 'main.fxml'.";
		assert zoom != null : "fx:id=\"zoom\" was not injected: check your FXML file 'main.fxml'.";
		assert relayout_graph != null : "fx:id=\"relayout_graph\" was not injected: check your FXML file 'main.fxml'.";
		assert menu_graph != null : "fx:id=\"menu_graph\" was not injected: check your FXML file 'main.fxml'.";
		
		// component logic
		
		new_graph.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
		
		// relayout_graph, JFX bug!
		menu_graph.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (WSeminar.instance.getSourceGraph() != null)
					WSeminar.instance.transitionTo(new FRLayout<Integer>(WSeminar.instance.getGraphSize()).render(WSeminar.instance.getSourceGraph(),
																																																				Const.defaultCycles * WSeminar.instance.getGraphSize()));
			}
		});
	}
}
