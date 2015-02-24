package de.dakror.wseminar.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import de.dakror.wseminar.WSeminar;

/**
 * @author Maximilian Stark | Dakror
 */
public class GraphTreeCell extends TreeCell<String> {
	final HBox graphic = new HBox();
	final Label name = new Label();
	final TextField value = new TextField();
	
	@SuppressWarnings("unchecked")
	public GraphTreeCell() {
		getStyleClass().add("hierarchy-tree-cell");
		name.setMinWidth((-1.0D / 0.0D));
		
		value.getStyleClass().addAll("hierarchy-readwrite-label", "label");
		value.setDisable(true); // TODO: temporary
		HBox.setHgrow(value, Priority.ALWAYS);
		graphic.getStyleClass().add("tree-cell-graphic");
		graphic.getChildren().addAll(new Node[] { name, value });
		selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (getTreeItem() != null && isSelected()) {
				Node node = ((GraphTreeItem) getTreeItem()).getNode();
				if (node instanceof VisualVertex) {
					if (WSeminar.instance.activeVertex != null) WSeminar.instance.activeVertex.setActive(false);
					WSeminar.instance.activeVertex = (VisualVertex<Integer>) node;
					((VisualVertex<?>) node).setActive(true);
				}
			}
		});
	}
	
	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (getIndex() == 0 && !getStyleClass().contains("hierarchy-first-cell")) getStyleClass().add("hierarchy-first-cell");
			
			name.setText(item);
			Node node = ((GraphTreeItem) getTreeItem()).getNode();
			if (node instanceof VisualVertex) {
				value.setText(((VisualVertex<?>) ((GraphTreeItem) getTreeItem()).getNode()).getVertex().data() + "");
			}
			
			value.setVisible(value.getText().length() > 0);
			setGraphic(graphic);
		}
	}
}
