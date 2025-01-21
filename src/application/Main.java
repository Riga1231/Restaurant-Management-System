package application;
	


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {
	 double xOffset, yOffset;
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			root.setOnMousePressed(event -> {
	            xOffset = event.getSceneX();
	            yOffset = event.getSceneY();
	        });
			
	        root.setOnMouseDragged(event -> {
	            primaryStage.setX(event.getScreenX() - xOffset);
	            primaryStage.setY(event.getScreenY() - yOffset);
	        });
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
