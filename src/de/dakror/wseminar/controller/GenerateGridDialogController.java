package de.dakror.wseminar.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import de.dakror.wseminar.GraphType;
import de.dakror.wseminar.WSeminar;

/**
 * @author Dakror
 */
public class GenerateGridDialogController {
	
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private URL location;
	
	@FXML
	private ChoiceBox<GraphType> graph_type;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private ImageView logo;
	
	@FXML
	private Slider graph_size;
	
	@FXML
	private TextArea graph_seed;
	
	@FXML
	private Button okButton;
	
	@FXML
	private HBox okParent;
	
	@FXML
	private Label messageLabel;
	
	@FXML
	void initialize() {
		assert graph_type != null : "fx:id=\"graph_type\" was not injected: check your FXML file 'generate_grid_dialog.fxml'.";
		assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'generate_grid_dialog.fxml'.";
		assert graph_size != null : "fx:id=\"graph_size\" was not injected: check your FXML file 'generate_grid_dialog.fxml'.";
		assert graph_seed != null : "fx:id=\"graph_seed\" was not injected: check your FXML file 'generate_grid_dialog.fxml'.";
		assert okButton != null : "fx:id=\"okButton\" was not injected: check your FXML file 'generate_grid_dialog.fxml'.";
		assert okParent != null : "fx:id=\"okParent\" was not injected: check your FXML file 'generate_grid_dialog.fxml'.";
		assert messageLabel != null : "fx:id=\"messageLabel\" was not injected: check your FXML file 'generate_grid_dialog.fxml'.";
		assert logo != null : "fx:id=\"logo\" was not injected: check your FXML file 'generate_grid_dialog.fxml'.";
		
		// component logic
		
		graph_size.setLabelFormatter(new StringConverter<Double>() {
			
			String[] sizes = { "Klein", "Mittel", "Groß" };
			
			@Override
			public String toString(Double object) {
				return sizes[object.intValue() - 1];
			}
			
			@Override
			public Double fromString(String string) {
				return Arrays.asList(sizes).indexOf(string) + 1d;
			}
		});
		
		logo.setImage(WSeminar.getImage("new_graph-50.png"));
		
		graph_type.getItems().addAll(GraphType.values());
		graph_type.setValue(GraphType.ABSTRACT_GRAPH);
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				((Stage) cancelButton.getScene().getWindow()).close();
			}
		});
	}
}
