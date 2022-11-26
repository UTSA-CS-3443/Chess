package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import application.model.Game;
import application.model.PositionIsCheckmateException;
import application.model.PositionIsDrawException;
import application.model.PositionIsInsufficientMaterial;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

class GameController{
	public static Game game;
}

public class MainController implements EventHandler<ActionEvent>, Initializable {
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
	private Label fenLabel;
	@FXML
	private TextField fenTextField;
	@FXML
	private Label errorLabel;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		errorLabel.setText("");
	}
	
	@Override
	public void handle(ActionEvent event) {
		String player1Name;
		String player2Name;
		String fenText = fenTextField.getText();
		
		// ensure playerName labels are not empty
		if(player1.getText().replaceAll(" ", "").equals("")) player1Name = "Player1";
		else player1Name = player1.getText();
		if(player2.getText().replaceAll(" ", "").equals("")) player2Name = "Player2";
		else player2Name = player2.getText();
		
		if(fenText.equals("")) {
				GameController.game = new Game(player1Name, player2Name);
				errorLabel.setText("");
				this.loadBoardScene();
		} else {
			// check if fen is valid/legal
			// TODO: isCheckmate only checks current turn
			//			i.e. if black king is checkmate but
			//			fen shows it is white turn, isCheckmate == false
			try {
				GameController.game = new Game(fenText, player1Name, player2Name);
				errorLabel.setText("");
				this.loadBoardScene();
			} catch (PositionIsCheckmateException e) {
				System.out.println("CheckmateException");
				errorLabel.setText("FEN entered is Checkmate");
			} catch (PositionIsInsufficientMaterial e) {
				errorLabel.setText("FEN entered has insufficient pieces");
			} catch (PositionIsDrawException e) {
				System.out.println("DrawException");
				errorLabel.setText("FEN entered has Drawn");
			} catch (Exception e) {
				System.out.println("Exception");
				errorLabel.setText("Invalid FEN");
			}
		}
	}
	
	public void loadBoardScene() {
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