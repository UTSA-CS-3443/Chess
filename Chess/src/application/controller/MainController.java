package application.controller;

import java.io.IOException;
import application.Main;
import application.model.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

class GameController{
	public static Game game;
}

public class MainController implements EventHandler<ActionEvent> {
	
	 @FXML
	    private Label player1Label;

	    @FXML
	    private TextField player1;

	    @FXML
	    private TextField player2;

	    @FXML
	    private Button startButton;

	    @FXML
	    private Label gameTitle;

	    @FXML
	    private Label player2Label;

	    @FXML
	    public void handle(ActionEvent event) {
	    	GameController.game = new Game(player1.getText(), player2.getText());
	    	//create method to hold playerNames here 
	    	try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../view/ChessBoard.fxml"));
				Scene scene2;
				scene2 = new Scene(loader.load());
				Main.stage.setScene(scene2);
				Main.stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
}
