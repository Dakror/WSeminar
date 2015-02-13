package de.dakror.wseminar.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class EditWeightsDialogController {
	@FXML
	private Button add;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private ImageView logo;
	
	@FXML
	private Button okButton;
	
	@FXML
	private HBox okParent;
	
	@FXML
	private ListView<String> list;
	
	@FXML
	private Label messageLabel;
	
	@FXML
	private Button remove;
	
	@FXML
	void initialize() {
		cancelButton.setOnAction(e -> ((Stage) cancelButton.getScene().getWindow()).close());
		
		add.setOnAction(e -> list.getItems().add("Gewicht #" + (list.getItems().size() + 1)));
		remove.setOnAction(e -> list.getItems().remove(list.getSelectionModel().getSelectedIndex()));
		
		list.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> remove.setDisable(newVal == null));
		list.setCellFactory(TextFieldListCell.forListView());
		list.setOnEditCommit(t -> list.getItems().set(t.getIndex(), t.getNewValue()));
	}
}
