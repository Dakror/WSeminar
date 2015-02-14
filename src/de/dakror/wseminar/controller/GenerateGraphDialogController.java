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
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.GraphType;
import de.dakror.wseminar.graph.generate.GraphGenerator;
import de.dakror.wseminar.graph.generate.GraphGenerator.Params;
import de.dakror.wseminar.graph.layout.FRLayout;

/**
 * @author Dakror
 */
public class GenerateGraphDialogController {
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private URL location;
	
	@FXML
	private ChoiceBox<GraphType> graph_type;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private TitledPane advanced;
	
	@FXML
	private Slider graph_size;
	
	@FXML
	private TextField graph_seed;
	
	@FXML
	private ImageView logo;
	
	@FXML
	private Slider edge_count;
	
	@FXML
	private Button okButton;
	
	@FXML
	private Slider node_count;
	
	@FXML
	private HBox okParent;
	
	@FXML
	private Button edit_weights;
	
	@FXML
	private Label messageLabel;
	
	public static final int speed = 400;
	
	String[] weights = { "Standardgewicht" };
	
	@SuppressWarnings("unchecked")
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
		
		graph_type.getItems().addAll(GraphType.values());
		graph_type.setValue(GraphType.ABSTRACT_GRAPH);
		
		EventHandler<ActionEvent> close = e -> ((Stage) cancelButton.getScene().getWindow()).close();
		
		cancelButton.setOnAction(close);
		
		edit_weights.setOnAction(e -> {
			Stage stage = WSeminar.createDialog("edit_weights_dialog", "Kantengewichte bearbeiten", WSeminar.window);
			stage.setAlwaysOnTop(true);
			((ListView<String>) stage.getScene().lookup("#list")).getItems().addAll(weights);
			
			((Button) stage.getScene().lookup("#okButton")).setOnAction(ev -> {
				weights = ((ListView<?>) stage.getScene().lookup("#list")).getItems().toArray(new String[] {});
				((Stage) ((Button) stage.getScene().lookup("#okButton")).getScene().getWindow()).close();
			});
		});
		
		okButton.setOnAction(e -> {
			long seed = 0;
			
			try {
				if (graph_seed.getText().length() == 0) {
					seed = (long) (Math.random() * Long.MAX_VALUE);
				} else {
					seed = Long.decode(graph_seed.getText());
				}
			} catch (Exception e1) {
				seed = graph_seed.getText().hashCode();
			}
			
			Params params = new Params().put("type", graph_type.getValue()).put("size", (int) graph_size.getValue()).put("seed", seed).put("weights", weights);
			if (node_count.getValue() != Const.nodeAmount) params.put("nodes", (int) node_count.getValue());
			if (edge_count.getValue() != Const.edgeAmount) params.put("edges", (int) edge_count.getValue());
			
			Graph<Integer> graph = new GraphGenerator<Integer>().generateGraph(params);
			
			WSeminar.instance.setSourceGraph(graph);
			WSeminar.instance.setSeed(seed);
			WSeminar.instance.setGraphSize((int) graph_size.getValue());
			
			WSeminar.instance.setGraph(new FRLayout<Integer>((int) graph_size.getValue()).render(graph, (int) (Const.defaultCycles * graph_size.getValue()), seed), true);
			
			close.handle(null);
		});
	}
}
