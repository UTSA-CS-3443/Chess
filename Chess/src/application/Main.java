package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;



public class Main extends Application {
	public static Stage stage;
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		try {
			GridPane root = new GridPane();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/Main.fxml"));
			root = (GridPane) loader.load();
			
			Scene scene = new Scene(root,600,600);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}