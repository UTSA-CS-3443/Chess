package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import application.model.*;
import javafx.scene.layout.GridPane;

public class ChessBoardController implements EventHandler<ActionEvent>, Initializable{

    @FXML
    private Label player1Label;

    @FXML
    private GridPane ChessBoard;

    @FXML
    private GridPane stageBase;

    @FXML
    private Label player2Label;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Game game = GameController.game;
		player1Label.setText(game.getWhiteName());
		player2Label.setText(game.getBlackName());
		
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}
    
}
