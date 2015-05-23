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
