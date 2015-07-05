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
import de.dakror.wseminar.graph.Vertex;
import de.dakror.wseminar.ui.VisualEdge;
import de.dakror.wseminar.ui.VisualVertex;
import javafx.application.Platform;
import javafx.scene.Node;

/**
 * @author Maximilian Stark | Dakror
 */
public class Visualizer {
	public static <V> void setVertexState(Vertex<V> v, State state) {
		setVertexState(v, state, true);
	}
	
	public static <V> void setVertexState(Vertex<V> v, State state, boolean tick) {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				((VisualVertex<V>) lookup("#V" + v.data())).setState(state);
			}
		});
		
		if (tick) tick();
	}
	
	public static <V> void setEdgePath(Edge<Vertex<V>> e, boolean path) {
		setEdgePath(e, path, true);
	}
	
	public static <V> void setEdgePath(Edge<Vertex<V>> e, boolean path, boolean tick) {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				((VisualEdge<V>) lookup("#E" + e.hashCode())).setPath(path);
			}
		});
		
		if (tick) tick();
	}
	
	
	public static <V> void setEdgeActive(Edge<Vertex<V>> e, boolean active) {
		setEdgeActive(e, active, true);
	}
	
	public static <V> void setEdgeActive(Edge<Vertex<V>> e, boolean active, boolean tick) {
		Platform.runLater(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				((VisualEdge<V>) lookup("#E" + e.hashCode())).setActive(active);
			}
		});
		
		if (tick) tick();
	}
	
	static void tick() {
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	static Node lookup(String selector) {
		return WSeminar.window.getScene().lookup(selector);
	}
}
