package de.dakror.wseminar.ui;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 * @author Maximilian Stark | Dakror
 */
public class GraphTreeItem extends TreeItem<String> {
	Node node;
	Object param;
	
	public GraphTreeItem() {}
	
	public GraphTreeItem(Node node, String name) {
		super(name);
		this.node = node;
	}
	
	public <T> GraphTreeItem(Node node, String name, T param) {
		super(name);
		this.node = node;
		this.param = param;
	}
	
	public <T> void setParam(T param) {
		this.param = param;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getParam() {
		return (T) param;
	}
	
	public Node getNode() {
		return node;
	}
}
