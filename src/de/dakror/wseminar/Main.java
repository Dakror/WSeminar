package de.dakror.wseminar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Dakror
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane pane = (Pane) FXMLLoader.load(getClass().getResource("/jfx/main.fxml"));
		Scene scene = new Scene(pane, 720, 405);
		scene.getStylesheets().add("jfx/Theme.css");
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
