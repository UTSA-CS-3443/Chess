package application.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import application.model.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class ChessBoardController implements EventHandler<MouseEvent>, Initializable{
	Game game = GameController.game;
	public ArrayList<Rectangle> tiles = new ArrayList<>();

	 @FXML
	    private ImageView whiteRookLeft,whiteKnightRight,whitePawn6,whitePawn5,whiteKnightLeft,whitePawn8,blackRookRight,whitePawn7,whitePawn2,whitePawn1
	    ,whitePawn4,whitePawn3,blackKing,blackKnightRight,whiteBishopLeft,blackBishopLeft,whiteQueen,whiteBishopRight,blackPawn1,blackPawn2
	    ,blackPawn5,whiteRookRight,blackPawn6,blackPawn3,blackPawn4,blackBishopRight,blackQueen,blackPawn7,blackPawn8,blackKnightLeft,blackRookLeft
	    ,whiteKing;

	    @FXML
	    private Label player1Label;

	    @FXML
	    private GridPane stageBase;

	    @FXML
	    private Label player2Label;

	    @FXML
	    private GridPane ChessBoard;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		player1Label.setText(game.getWhiteName());
		player2Label.setText(game.getBlackName());
		
		/*for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                Rectangle tile = new Rectangle(i,j);
                tile.setHeight(52);
                tile.setWidth(52);
                tiles.add(tile);
                ChessBoard.add(tile, i, j, 1, 1);
                if((i+j)%2==0){
                 tile.setFill(Color.BLACK);
                }
                else {
                tile.setFill(Color.WHITE);
                }*/
     		//}
		//}
	
		for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col ++) {
                StackPane tile = new StackPane();
                String color ;
                if ((row + col) % 2 == 0) {
                    color = "white";
                } else {
                    color = "grey";
                }
                tile.setStyle("-fx-background-color: "+color+";");
                	setupTile(tile,row,col);
                ChessBoard.add(tile, col, row);
            }
        }
		setupImages();
                	
   
	}

	@Override
	public void handle(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void handleWhiteRook(MouseEvent event) {
		System.out.println("success");
		if(event.getButton().equals(MouseButton.PRIMARY)) {
			if(event.getClickCount() == 2) {
				System.out.println("yes");
			}
		}
	}
	
	public void setupTile(StackPane tile,int row,int col) {
		if(row == 0 && col ==0) {
        	tile.getChildren().add(whiteRookLeft);
        }
        else if(row == 0 && col == 1) {
        	tile.getChildren().add(whiteKnightLeft);
        }
        else if(row == 0 && col ==2) {
        	tile.getChildren().add(whiteBishopLeft);
        }
        else if(row == 0 && col ==3) {
        	tile.getChildren().add(whiteQueen);
        }
        else if(row == 0 && col ==4 ) {
        	tile.getChildren().add(whiteKing);
        }
        else if(row == 0 && col ==5) {
        	tile.getChildren().add(whiteBishopRight);
        }
        else if(row == 0 && col == 6) {
        	tile.getChildren().add(whiteKnightRight);
        }
        else if(row ==0 && col == 7) {
        	tile.getChildren().add(whiteRookRight);
        }
        else if(row == 1 && col ==0) {
        	tile.getChildren().add(whitePawn1);
        }
        else if(row == 1 && col ==1) {
        	tile.getChildren().add(whitePawn2);
        }
        else if(row == 1 && col ==2) {
        	tile.getChildren().add(whitePawn3);
        }
        else if(row == 1 && col ==3) {
        	tile.getChildren().add(whitePawn4);
        }
        else if(row == 1 && col ==4) {
        	tile.getChildren().add(whitePawn5);
        }
        else if(row == 1 && col ==5) {
        	tile.getChildren().add(whitePawn6);
        }
        else if(row == 1 && col ==6) {
        	tile.getChildren().add(whitePawn7);
        }
        else if(row == 1 && col ==7) {
        	tile.getChildren().add(whitePawn8);
        }
        else if(row == 6 && col ==0) {
        	tile.getChildren().add(blackPawn1);
        }
        else if(row == 6 && col ==1) {
        	tile.getChildren().add(blackPawn2);
        }
        else if(row == 6 && col ==2) {
        	tile.getChildren().add(blackPawn3);
        }
        else if(row == 6 && col ==3) {
        	tile.getChildren().add(blackPawn4);
        }
        else if(row == 6 && col ==4) {
        	tile.getChildren().add(blackPawn5);
        }
        else if(row == 6 && col ==5) {
        	tile.getChildren().add(blackPawn6);
        }
        else if(row == 6 && col ==6) {
        	tile.getChildren().add(blackPawn7);
        }
        else if(row == 6 && col ==7) {
        	tile.getChildren().add(blackPawn8);
        }
        else if(row == 7 && col ==0) {
        	tile.getChildren().add(blackRookLeft);
        }
        else if(row == 7 && col ==1) {
        	tile.getChildren().add(blackKnightLeft);
        }
        else if(row == 7 && col ==2) {
        	tile.getChildren().add(blackBishopLeft);
        }
        else if(row == 7 && col ==3) {
        	tile.getChildren().add(blackQueen);
        }
        else if(row == 7 && col ==4) {
        	tile.getChildren().add(blackKing);
        }
        else if(row == 7 && col ==5) {
        	tile.getChildren().add(blackBishopRight);
        }
        else if(row == 7 && col ==6) {
        	tile.getChildren().add(blackKnightRight);
        }
        else if(row == 7 && col ==7) {
        	tile.getChildren().add(blackRookRight);
        }
	}
	
	public void setupImages() {
		try {
			InputStream stream = new FileInputStream("Chess/src/Images/WhitePawn.png");
			Image image = new Image(stream);
			whitePawn1.setImage(image);
			whitePawn2.setImage(image);
			whitePawn3.setImage(image);
			whitePawn4.setImage(image);
			whitePawn5.setImage(image);
			whitePawn6.setImage(image);
			whitePawn7.setImage(image);
			whitePawn8.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/WhiteKing.png");
			Image image = new Image(stream);
			whiteKing.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/WhiteQueen.png");
			Image image = new Image(stream);
			whiteQueen.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/WhiteRook.png");
			Image image = new Image(stream);
			whiteRookLeft.setImage(image);
			whiteRookRight.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/WhiteBishop.png");
			Image image = new Image(stream);
			whiteBishopLeft.setImage(image);
			whiteBishopRight.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/WhiteKnight.png");
			Image image = new Image(stream);
			whiteKnightLeft.setImage(image);
			whiteKnightRight.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/BlackPawn.png");
			Image image = new Image(stream);
			blackPawn1.setImage(image);
			blackPawn2.setImage(image);
			blackPawn3.setImage(image);
			blackPawn4.setImage(image);
			blackPawn5.setImage(image);
			blackPawn6.setImage(image);
			blackPawn7.setImage(image);
			blackPawn8.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/BlackKing.png");
			Image image = new Image(stream);
			blackKing.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/BlackQueen.png");
			Image image = new Image(stream);
			blackQueen.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/BlackRook.png");
			Image image = new Image(stream);
			blackRookLeft.setImage(image);
			blackRookRight.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/BlackKnight.png");
			Image image = new Image(stream);
			blackKnightLeft.setImage(image);
			blackKnightRight.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    	try {
			InputStream stream = new FileInputStream("Chess/src/Images/BlackBishop.png");
			Image image = new Image(stream);
			blackBishopLeft.setImage(image);
			blackBishopRight.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
    
}
