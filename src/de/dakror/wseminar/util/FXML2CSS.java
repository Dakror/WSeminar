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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javafx.application.Application;
import javafx.css.CssMetaData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Maximilian Stark | Dakror
 */
public class FXML2CSS extends Application {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) throw new IllegalArgumentException("No file specified");
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		File fxml = new File(getParameters().getRaw().get(0));
		
		try {
			FXMLLoader l = new FXMLLoader(fxml.toURI().toURL());
			Scene scene = new Scene(l.load());
			Stage stage = new Stage();
			stage.setOpacity(0);
			stage.setScene(scene);
			stage.show();
			printRec(stage.getScene().getRoot());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void printRec(Parent parent) {
		for (Node c : parent.getChildrenUnmodifiable()) {
			if (c instanceof Parent) {
				printRec((Parent) c);
			} else if (c.getId() != null) {
				System.out.println();
				
				System.out.println("#" + c.getId() + "{");
				for (CssMetaData css : c.getCssMetaData()) {
					Object value = css.getStyleableProperty(c).getValue();
					if (value != null && !value.equals(css.getInitialValue(c))) {
						String str = value.toString();
						if (value instanceof Double[]) {
							if (((Double[]) value).length == 0) continue;
							str = Arrays.toString((Double[]) value);
						}
						
						System.out.println("    " + css.getProperty() + ": " + str + ";");
					}
				}
				System.out.println("}");
			}
		}
	}
}
