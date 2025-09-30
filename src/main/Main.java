package main;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class Main extends Application {
	public Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
	    try {
	        Parent root = FXMLLoader.load(getClass().getResource("/fxml/trangChu.fxml"));
	        StackPane container = new StackPane(root);
	        Scene scene = new Scene(container); 
	        primaryStage.setScene(scene);
	        primaryStage.setMaximized(true);
	        primaryStage.setResizable(true);
	        primaryStage.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}



}
