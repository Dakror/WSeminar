package de.dakror.wseminar.ui;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 * @author Maximilian Stark | Dakror
 */
public class GraphTreeItem extends TreeItem<String> {
	Node node;
	
	public GraphTreeItem() {}
	
	public GraphTreeItem(Node node, String name) {
		super(name);
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
}
