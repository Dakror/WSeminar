package de.dakror.wseminar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
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
	private MenuItem menu_about;
	
	@FXML
	private Pane graph;
	
	@FXML
	private Label newGraph;
	
	@FXML
	void initialize() {
		assert node != null : "fx:id=\"node\" was not injected: check your FXML file 'main.fxml'.";
		assert node_new != null : "fx:id=\"node_new\" was not injected: check your FXML file 'main.fxml'.";
		assert templates != null : "fx:id=\"templates\" was not injected: check your FXML file 'main.fxml'.";
		assert node_track != null : "fx:id=\"node_track\" was not injected: check your FXML file 'main.fxml'.";
		assert node_disabled != null : "fx:id=\"node_disabled\" was not injected: check your FXML file 'main.fxml'.";
		assert node_finish != null : "fx:id=\"node_finish\" was not injected: check your FXML file 'main.fxml'.";
		assert node_start != null : "fx:id=\"node_start\" was not injected: check your FXML file 'main.fxml'.";
		assert menu_about != null : "fx:id=\"menu_about\" was not injected: check your FXML file 'main.fxml'.";
		assert graph != null : "fx:id=\"graph\" was not injected: check your FXML file 'main.fxml'.";
		assert newGraph != null : "fx:id=\"newGraph\" was not injected: check your FXML file 'main.fxml'.";
		
		// component logic
		
		newGraph.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				WSeminar.createDialog("generate_graph_dialog", "Neues Netz generieren", WSeminar.window);
			}
		});
	}
}
