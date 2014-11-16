package de.dakror.wseminar;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author Dakror
 */
public class WSeminar extends Application {
	static HashMap<String, Image> imgCache = new HashMap<>();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(createScene("main"));
		primaryStage.setTitle("WSeminar Extrema: Wegfindung - Maximilian Stark");
		
		primaryStage.getIcons().addAll(getImage("mind_map-24.png"), getImage("mind_map-32.png"));
		
		primaryStage.show();
	}
	
	public static Scene createScene(String resource) {
		try {
			FXMLLoader l = new FXMLLoader(WSeminar.class.getResource("/assets/jfx/" + resource + ".fxml"));
			Pane pane = (Pane) l.load();
			Scene scene = new Scene(pane);
			scene.getStylesheets().add("assets/jfx/Theme.css");
			
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image getImage(String resource) {
		if (imgCache.containsKey(resource)) return imgCache.get(resource);
		else {
			Image img = new Image(WSeminar.class.getResourceAsStream("/assets/img/" + resource));
			
			imgCache.put(resource, img);
			return img;
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
