package application.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import application.Main;
import application.model.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;


/**
 * this handles the ChessBoard and its related methods
 * @author Jack Nelson tge389
 *
 */
public class ChessBoardController implements EventHandler<ActionEvent>, Initializable{
	Game game = GameController.game;
	public static MyButton[][] buttons = new MyButton[8][8];
	private MyButton lastSquareClicked;
	static Popup popup = new Popup();
	
	@FXML
	private Button homeButton;
	@FXML
	private Label player1Label;
	@FXML
	public static GridPane stageBase;
	@FXML
	private Label player2Label;
	@FXML
	GridPane ChessBoard;
	@FXML
	private Button showFenBtn;
	@FXML
	private TextField fenLabel;


	/**
	 *initializes the board to its starting point
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		player2Label.setText(game.getBlackName());
		player1Label.setText(game.getWhiteName());
		if(game.getTurn() == PieceColor.WHITE) {
			player1Label.setTextFill(Color.YELLOWGREEN);
			player2Label.setTextFill(Color.BLACK);
		} else {
			player2Label.setTextFill(Color.YELLOWGREEN);
			player1Label.setTextFill(Color.BLACK);
		}
		fenLabel.setVisible(false);

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col ++) {
				MyButton tile = new MyButton(row,col);
				buttons[row][col] = tile;
				tile.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						tile.handle(e);
					}
				}); //CHECK HERE FOR ERROR....Maybe should go into the handle method?
				String color ;
				if ((row + col) % 2 == 0) {
					color = "white";
				} else {
					color = "grey";
				}
				tile.setStyle("-fx-background-color: "+color+";");
				tile.setMaxHeight(52);
				tile.setMaxWidth(52);
				tile.setMinHeight(52);
				tile.setMinWidth(52);
				tile.setPrefHeight(52);
				tile.setPrefWidth(52);
				tile.updateImage();
				ChessBoard.add(tile, col, row);
			}
		}
	}

	/**
	 * handles the event that the home or fen button is pressed
	 */
	@Override
	public void handle(ActionEvent event) {
		Button button = (Button) event.getSource();
		
		if(button.getId().equals("showFenBtn")) {
			if(!fenLabel.isVisible()) {
				fenLabel.setVisible(true);
				fenLabel.setText(game.getFEN());
			} else {
				fenLabel.setVisible(false);
			}
			
			return;
		}
		popup.hide();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("../view/Main.fxml"));
			Scene scene2;
			scene2 = new Scene(loader.load());
			Main.stage.setScene(scene2);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * creates and handles the MyButton objects and their methods
	 * @author Jack Nelson tge389
	 *
	 */
	class MyButton extends Button implements EventHandler<ActionEvent>{

		Game game = GameController.game;
		private final int r;
		private final int c;

		private boolean isHighlighted;
		private boolean hasPiece;

		public MyButton(int r,int c) {
			this.r = r;
			this.c = c;
		}

		/**
		 * updates the image to its new location and removes the graphic from buttons where the piece has moved away from
		 */
		public void updateImage() {
			Piece piece = game.getPieceAt(this.getRow(),this.getCol());
			if(piece != null) {
				this.hasPiece = true;
				ImageView imageView = getImageFromPiece(piece);
				this.setGraphic(imageView);
			}
			else this.setGraphic(null);
		}

		/**
		 * returns the proper ImageView depending on the piece selected
		 * @param piece the piece on the board(Piece)
		 * @return ImageView(image) the image of the selected piece
		 */
		public ImageView getImageFromPiece(Piece piece) {
			if(piece != null) {
				try {
					return new ImageView(new Image(new FileInputStream(piece.getImageURL())));
				} catch(FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		/**
		 * this method handles the actions performed on the chess board
		 * @param event the event that a piece is selected(ActionEvent)
		 */
		public void handle(ActionEvent event) {
			int row = this.getRow();
			int col = this.getCol();
			System.out.println(row + "" + col + " is highlighted: " + this.isHighlighted);
			if(this.isHighLighted()) {
				Move move = new Move(lastSquareClicked.getRow(),
										lastSquareClicked.getCol(),
										this.getRow(),
										this.getCol());
				
				// update player labels
				if(game.getTurn() == PieceColor.WHITE) {
					player2Label.setTextFill(Color.YELLOWGREEN);
					player1Label.setTextFill(Color.BLACK);
				} else {
					player1Label.setTextFill(Color.YELLOWGREEN);
					player2Label.setTextFill(Color.BLACK);
				}

				game.pushMove(move);
				System.out.println(game);
				lastSquareClicked = null;
				
				
				//update graphics
				for(int r = 0; r < 8; r++) {
					for(int c = 0; c < 8; c++) {
						ChessBoardController.buttons[r][c].updateImage();
						if(ChessBoardController.buttons[r][c].getGraphic() != null) {
						}
					}
				}

				if(game.isCheckmate()) {
					Label label = new Label("CheckMate");
					label.setPrefWidth(400);
					label.setPrefHeight(200);
					label.setAlignment(Pos.BOTTOM_CENTER);
					label.setFont(new Font(50));
					label.setStyle("-fx-text-fill: black");
					ChessBoardController.popup.getContent().add(label);
					ChessBoardController.popup.show(Main.stage);
				}

				else if(game.isDraw()) {
					Label label = new Label("Draw");
					label.setPrefWidth(400);
					label.setPrefHeight(200);
					label.setAlignment(Pos.BOTTOM_CENTER);
					label.setFont(new Font(50));
					label.setStyle("-fx-text-fill: black");
					ChessBoardController.popup.getContent().add(label);
					ChessBoardController.popup.show(Main.stage);
				}
				resetHighLightedSquares();
			} else {
				resetHighLightedSquares();
				System.out.println();
				System.out.println(this);
				if(this.hasPiece) {
					Piece piece = game.getPieceAt(row, col);
					Coordinate cord = new Coordinate(row,col);
					List<Move> legalMoves = piece.getLegalMoves(game, cord);
					for(Move move: legalMoves){
						int r = move.getToRow();
						int c = move.getToCol();

						highLightSquare(r,c);
					}
				}
				lastSquareClicked = this;
			}
			
			// update FEN label if showing FEN
			if(fenLabel.isVisible())
				fenLabel.setText(game.getFEN());
		}

		public int getRow() {
			return this.r;
		}

		public int getCol() {
			return this.c;
		}

		public boolean isHighLighted() {
			//System.out.println("Checking highlighted status: " + this.isHighlighted);
			return this.isHighlighted;
		}

		/**
		 * iterates through entire board resetting all values effect values on buttons to default value(null)
		 * @return boolean the new isHighlighted value for each button
		 */
		public boolean resetHighLightedSquares() {
			//System.out.println("highlight is reset");
			for(int r = 0; r< 8; r++) {
				for(int c = 0; c < 8; c++) {
					MyButton button = ChessBoardController.buttons[r][c];
					button.setStyle("-fx-background-color: " + ((r + c) % 2 == 0 ? "white;" : "grey"));
					button.isHighlighted = false;
				}
			}
			
			return this.isHighlighted = false;
		}

		/**
		 * highlights spaces of legal moves based on piece selected
		 * @param r the row of the square to be highlighted (int)
		 * @param c the column of the square to be highlighted(int)
		 * @return boolean  the isHighlighted boolean value now set to true
		 */
		public boolean highLightSquare(int r, int c) {
			MyButton button = ChessBoardController.buttons[r][c];
			button.setStyle("-fx-background-color: " + ((r + c) % 2 == 0 ? "lemonchiffon;" : "khaki;"));
			return button.isHighlighted = true;
		}

		@Override
		public String toString() {
			return "MyButton [game=" + game + ", r=" + r + ", c=" + c + ", isHighlighted=" + isHighlighted + ", hasPiece="
					+ hasPiece + "]";
		}
	}
}



