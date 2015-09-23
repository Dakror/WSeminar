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


package de.dakror.wseminar.util;

import de.dakror.wseminar.Const.State;
import de.dakror.wseminar.WSeminar;
import de.dakror.wseminar.graph.Edge;
import de.dakror.wseminar.graph.Graph;
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.ui.VisualEdge;
import de.dakror.wseminar.ui.VisualVertex;
import javafx.application.Platform;
import javafx.scene.Node;

/**
 * @author Maximilian Stark | Dakror
 */
public class Visualizer {
	static boolean enabled = true;
	
	public static void setEnabled(boolean enable) {
		enabled = enable;
	}
	
	public static boolean isEnabled() {
		return enabled;
	}
	
	public static <V> void setVertexState(Vertex<V> v, State state) {
		setVertexState(v, state, true);
	}
	
	@SuppressWarnings("unchecked")
	public static <V> void setVertexState(Vertex<V> v, State state, boolean tick) {
		if (enabled) Platform.runLater(() -> {
			((VisualVertex<V>) lookup("#V" + v.data())).setState(state);
		});
		
		if (tick) tick();
	}
	
	public static <V> void setEdgePath(Edge<Vertex<V>> e, boolean path) {
		setEdgePath(e, path, true);
	}
	
	@SuppressWarnings("unchecked")
	public static <V> void setEdgePath(Edge<Vertex<V>> e, boolean path, boolean tick) {
		if (enabled) Platform.runLater(() -> {
			((VisualEdge<V>) lookup("#E" + e.hashCode())).setPath(path);
		});
		
		if (tick) tick();
	}
	
	@SuppressWarnings("unchecked")
	public static <V> void setEdgePath(Edge<Vertex<V>> e, boolean path, boolean soft, boolean tick) {
		if (enabled) Platform.runLater(() -> {
			((VisualEdge<V>) lookup("#E" + e.hashCode())).setPath(path, soft);
		});
		
		if (tick) tick();
	}
	
	public static <V> void setEdgeActive(Edge<Vertex<V>> e, boolean active) {
		setEdgeActive(e, active, true);
	}
	
	@SuppressWarnings("unchecked")
	public static <V> void setEdgeActive(Edge<Vertex<V>> e, boolean active, boolean tick) {
		if (enabled) Platform.runLater(() -> {
			((VisualEdge<V>) lookup("#E" + e.hashCode())).setActive(active);
		});
		
		if (tick) tick();
	}
	
	@SuppressWarnings("unchecked")
	public static <V> void resetAll(Graph<Vertex<V>> graph, boolean full, boolean start) {
		if (enabled) Platform.runLater(() -> {
			for (Edge<Vertex<V>> e : graph.getEdges()) {
				((VisualEdge<V>) lookup("#E" + e.hashCode())).setActive(false);
				if (full) ((VisualEdge<V>) lookup("#E" + e.hashCode())).setPath(false);
				VisualVertex<V> f = ((VisualVertex<V>) lookup("#V" + e.getFrom().data()));
				VisualVertex<V> t = ((VisualVertex<V>) lookup("#V" + e.getTo().data()));
				
				if (full) {
					f.resetState(start);
					t.resetState(start);
				}
				
				f.setActive(false);
				t.setActive(false);
			}
		});
	}
	
	public static void tick() {
		try {
			if (enabled) Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	static Node lookup(String selector) {
		return WSeminar.window.getScene().lookup(selector);
	}
}
