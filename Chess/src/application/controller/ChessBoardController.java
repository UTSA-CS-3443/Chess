package application.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import application.model.*;
import javafx.scene.layout.GridPane;


/**
 * this handles the ChessBoard and its related methods
 * @author Jack Nelson tge389
 *
 */
public class ChessBoardController implements EventHandler<ActionEvent>, Initializable{
	Game game = GameController.game;
	public static MyButton[][] buttons = new MyButton[8][8];

	    @FXML
	    private Label player1Label;

	    @FXML
	    private GridPane stageBase;

	    @FXML
	    private Label player2Label;

	    @FXML
	    private GridPane ChessBoard;

	/**
	 *initializes the board to its starting point
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		player1Label.setText(game.getWhiteName());
		player2Label.setText(game.getBlackName());
		
		for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col ++) {
                MyButton tile = new MyButton(row,col);
                buttons[row][col] = tile;
                tile.setOnAction(event -> tile.handle(event));//CHECK HERE FOR ERROR....Maybe should go into the handle method?
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

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		
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
	
	boolean isHighlighted;
	private static MyButton lastSquareClicked;
	
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
		ImageView imageView = getImageFromPiece(piece);
		this.setGraphic(imageView);
		}
		else
			this.setGraphic(null);
	}
		
	/**
	 * returns the proper ImageView depending on the piece selected
	 * @param piece the piece on the board(Piece)
	 * @return ImageView(image) the image of the selected piece
	 */
	public ImageView getImageFromPiece(Piece piece){
		switch(piece) {
		case WHITE_KING:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case WHITE_QUEEN:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case WHITE_ROOK:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case WHITE_BISHOP:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case WHITE_KNIGHT:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case WHITE_PAWN:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case BLACK_KING:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case BLACK_QUEEN:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case BLACK_ROOK:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case BLACK_BISHOP:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case BLACK_KNIGHT:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		case BLACK_PAWN:
			try {
				InputStream stream = new FileInputStream(piece.getImageURL());
				Image image = new Image(stream);
				return new ImageView(image);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		default:
			System.out.println("ERROR IN UPDATE IMAGE");
			return null;
		}
	}
	
	/**
	 * this method handles the actions performed on the chess board
	 *@param event the event that a piece is selected(ActionEvent)
	 */
	public void handle(ActionEvent event){
		int row = this.getRow();
		int col = this.getCol();
		
		if(this.isHighLighted()) {
			game.pushMove(new Move(lastSquareClicked.getRow(),
								   lastSquareClicked.getCol(),
								   row,
								   col));
			lastSquareClicked = null;
			
			//update graphics
			for(int r = 0; r < 8; r++) {
				for(int c = 0; c < 8; c++) {
					ChessBoardController.buttons[r][c].updateImage();
				}
			}
			
			if(game.isCheckmate()) {
				//change to CheckMate PopUp
				//Maybe make the PopUp a label that appears in the center of the screen
				//put home/reset button on the chess board 
			}
			
			else if(game.isDraw()) {
				//change to draw PopUp
			}
		}
		
		else {
		resetHighLightedSquares();
		List<Move> legalMoves = game.getLegalMoves();
		for(Move move: legalMoves){
			int r = move.getToRow();
			int c = move.getToCol();
			
			this.highLightSquare(r,c);
		}
		lastSquareClicked = this;
		}
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
				ChessBoardController.buttons[r][c].setEffect(null);
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
		Effect shadow = new DropShadow();
		ChessBoardController.buttons[r][c].setEffect(shadow);
		//System.out.println("Row: " + r + "Col: " + c +"is Highlighted");
		return this.isHighlighted = true;
	}
}
