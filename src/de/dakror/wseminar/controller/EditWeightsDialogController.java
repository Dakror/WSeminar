/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


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
