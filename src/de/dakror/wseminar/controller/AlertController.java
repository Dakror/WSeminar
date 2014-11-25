package de.dakror.wseminar.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author Dakror
 */
public class AlertController {
	
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private URL location;
	
	@FXML
	private ImageView logo;
	
	@FXML
	private Label details;
	
	@FXML
	private Button okButton;
	
	@FXML
	private Label message;
	
	@FXML
	void initialize() {
		assert logo != null : "fx:id=\"logo\" was not injected: check your FXML file 'alert.fxml'.";
		assert details != null : "fx:id=\"details\" was not injected: check your FXML file 'alert.fxml'.";
		assert okButton != null : "fx:id=\"okButton\" was not injected: check your FXML file 'alert.fxml'.";
		assert message != null : "fx:id=\"message\" was not injected: check your FXML file 'alert.fxml'.";
		
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Stage) okButton.getScene().getWindow()).close();
			}
		});
	}
}
