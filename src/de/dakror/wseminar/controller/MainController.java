package de.dakror.wseminar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import de.dakror.wseminar.WSeminar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
		
		// component logic
		
		menu_about.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = new Stage();
				stage.setResizable(false);
				stage.getIcons().addAll(WSeminar.getImage("mind_map-24.png"), WSeminar.getImage("mind_map-32.png"));
				
				stage.setScene(WSeminar.createScene("generate_grid_dialog"));
				stage.setTitle("Neues Netz generieren");
				stage.initModality(Modality.WINDOW_MODAL);
				stage.initOwner(graph.getScene().getWindow());
				stage.show();
			}
		});
	}
}
