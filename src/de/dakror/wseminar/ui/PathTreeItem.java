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


package de.dakror.wseminar.ui;

import de.dakror.wseminar.graph.Path;
import de.dakror.wseminar.graph.Vertex;
import javafx.scene.control.TreeItem;

/**
 * @author Maximilian Stark | Dakror
 */
public class PathTreeItem<V> extends TreeItem<String> {
	int pathId = -1;
	
	public PathTreeItem(String text) {
		super(text);
	}
	
	public PathTreeItem(Path<Vertex<V>> p, boolean spec) {
		super(p.toString(spec));
		if (spec) pathId = p.hashCode();
	}
	
	public int getPathId() {
		return pathId;
	}
	
	public boolean isSpec() {
		return pathId != -1;
	}
	
	/**
	 * Expected to be called for the root node. Don't call this method on a subnode
	 * @param p
	 * @return
	 */
	public PathTreeItem<V> insert(Path<Vertex<V>> p, boolean batch) {
		if (p == null) return null;
		
		for (TreeItem<String> ti : getChildren()) {
			if (ti.getValue().equals(p.toGrouperString()) || (batch && isSameBatch(ti.getValue(), p.toGrouperString()))) {
				for (TreeItem<String> ti2 : ti.getChildren())
					if (ti2.getValue().equals(p.toSpecString())) return null;
					
				PathTreeItem<V> pti = new PathTreeItem<V>(p, true);
				
				if (batch && !ti.getValue().endsWith("( * )")) {
					ti.setValue(ti.getValue().substring(0, ti.getValue().indexOf("->") + 2) + " ( * )");
				}
				
				ti.getChildren().add(pti);
				return pti;
			}
		}
		
		String gr = p.toGrouperString();
		
		PathTreeItem<V> pti = new PathTreeItem<V>(batch ? (gr.substring(0, gr.lastIndexOf("(")) + "( * )") : gr);
		PathTreeItem<V> pti2 = new PathTreeItem<V>(p, true);
		
		pti.getChildren().add(pti2);
		
		getChildren().add(pti);
		return pti2;
	}
	
	boolean isSameBatch(String s1, String s2) {
		return s1.substring(0, s1.indexOf("->")).equals(s2.substring(0, s2.indexOf("->")));
	}
}
