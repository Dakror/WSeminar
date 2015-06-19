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
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.algorithm.FruchtermanReingold;
import de.dakror.wseminar.graph.algorithm.Layout;
import de.dakror.wseminar.graph.generate.GraphGenerator;
import de.dakror.wseminar.graph.generate.Params;
import de.dakror.wseminar.graph.generate.Params.SParams;

/**
 * @author Dakror
 */
public class GenerateGraphDialogController {
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private URL location;
	
	@FXML
	private GridPane adv_abstract;
	
	@FXML
	private TitledPane advanced;
	
	@FXML
	private Slider graph_size;
	
	@FXML
	private Slider edge_count;
	
	@FXML
	private Button okButton;
	
	@FXML
	private HBox okParent;
	
	@FXML
	private Label messageLabel;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private TextField graph_seed;
	
	@FXML
	private ImageView logo;
	
	@FXML
	private Slider node_count;
	
	@FXML
	private ChoiceBox<String> edge_type;
	
	public static final int speed = 400;
	
	public static final String[] edgeTypes = { "Ungerichtet", "Gerichtet", "Gemischt" };
	
	long seed = 0;
	
	@FXML
	void initialize() {
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
		
		edge_type.getItems().addAll(edgeTypes);
		edge_type.setValue(edge_type.getItems().get(0));
		
		EventHandler<ActionEvent> close = e -> ((Stage) cancelButton.getScene().getWindow()).close();
		
		cancelButton.setOnAction(close);
		
		okButton.setOnAction(e -> {
			try {
				if (graph_seed.getText().length() == 0) {
					seed = (long) (Math.random() * Long.MAX_VALUE);
				} else {
					seed = Long.decode(graph_seed.getText());
				}
			} catch (Exception e1) {
				seed = graph_seed.getText().hashCode();
			}
			
			Params<String> params = new SParams().put("size", (int) graph_size.getValue()).put("seed", seed);
			params.put("edge_type", edge_type.getItems().indexOf(edge_type.getValue()));
			
			if (node_count.getValue() != Const.nodeAmount) params.put("nodes", (int) Math.max(8, node_count.getValue()));
			if (edge_count.getValue() != Const.edgeAmount) params.put("edges", (int) Math.max(2, edge_count.getValue()));
			
			Graph<Integer> graph = new GraphGenerator<Integer>().generateGraph(params);
			WSeminar.instance.setSourceGraph(graph);
			WSeminar.instance.setGraphSize((int) graph_size.getValue());
			
			Layout<Integer> layout = new FruchtermanReingold<Integer>(graph, Const.defaultCycles * (int) graph_size.getValue(), seed,
																																(float) Math.sqrt(Math.sqrt(graph.getVertices().size() / graph_size.getValue())));
			WSeminar.instance.setLayout(layout);
			
			MainController.doLayoutWithProgress(layout, null, false, true);
			
			// new Thread() {
			// @Override
			// public void run() {
			// for (int i = 0; i < Const.defaultCycles; i += 20) {
			// Platform.runLater(new Runnable() {
			// @Override
			// public void run() {
			// layout.step();
			// layout.finish();
			// WSeminar.instance.transitionTo(layout.getGraph());
			// }
			// });
			// if (!WSeminar.window.isShowing()) return;
			//
			// try {
			// Thread.sleep(speed);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }
			// }
			// }.start();
			
			close.handle(null);
		});
	}
}
