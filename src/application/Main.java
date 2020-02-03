package application;
	
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.io.IOException;

import com.sun.glass.events.WindowEvent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@FXML
    private Button exit;
	@FXML
    private ComboBox<String> NameOfMachine ;
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
		primaryStage.setTitle("Consumption Management");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		primaryStage.setOnCloseRequest(e ->{
			System.out.println("Closing primary Stage!");
			try {
				new Controller().clearSaver();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
	    
	}
	@FXML
    
	public static void main(String[] args) {
		launch(args);
	}
}
