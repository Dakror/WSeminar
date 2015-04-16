package de.dakror.wseminar.ui;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.shape.Circle;
import de.dakror.wseminar.Const;
import de.dakror.wseminar.Const.State;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.graph.VertexData.Position;
import de.dakror.wseminar.graph.algorithm.BritishMuseum;

/**
 * @author Maximilian Stark | Dakror
 */
public class VisualVertex<V> extends Circle {
	Vertex<V> vertex;
	
	@SuppressWarnings("unchecked")
	public VisualVertex(String selector, Vertex<V> v) {
		vertex = v;
		setCenterX(Const.cellSize / 2);
		setCenterY(Const.cellSize / 2);
		setRadius(12);
		setTranslateX(v.get(Position.class).pos.x * Const.cellSize);
		setTranslateY(v.get(Position.class).pos.y * Const.cellSize);
		getStyleClass().add("visual-vertex");
		
		setOnMouseClicked(e -> {
			BritishMuseum bm = new BritishMuseum(WSeminar.instance.getGraph());
			
			if (WSeminar.instance.activeVertex != null) {
				WSeminar.instance.activeVertex.setActive(false);
				System.out.println("HELLO " + WSeminar.instance.activeVertex.vertex.data() + " -> " + VisualVertex.this.vertex.data() + ": "
						+ bm.findPath(WSeminar.instance.activeVertex.vertex, VisualVertex.this.vertex));
			}
			
			setActive(true);
			WSeminar.instance.activeVertex = (VisualVertex<Integer>) VisualVertex.this;
			TreeView<String> tv = ((TreeView<String>) getScene().lookup("#graph_tree"));
			for (TreeItem<String> item : tv.getRoot().getChildren()) {
				if (item instanceof GraphTreeItem) {
					GraphTreeItem gti = (GraphTreeItem) item;
					if (gti.node == VisualVertex.this) {
						tv.getSelectionModel().select(gti);
						break;
					}
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public void setActive(boolean active) {
		if (active) {
			getStyleClass().add("active");
			getParent().getChildrenUnmodifiable().stream().filter(n -> (n instanceof VisualEdge) && WSeminar.instance.getGraph().isConnected(vertex, ((VisualEdge<V>) n).edge)).forEach(n -> ((VisualEdge<V>) n).setState(State.ACTIVE));
		} else {
			getStyleClass().remove("active");
			getParent().getChildrenUnmodifiable().stream().filter(n -> (n instanceof VisualEdge) && WSeminar.instance.getGraph().isConnected(vertex, ((VisualEdge<V>) n).edge)).forEach(n -> ((VisualEdge<V>) n).setState(State.DEFAULT));
		}
	}
	
	public Vertex<V> getVertex() {
		return vertex;
	}
}
