package application.model;

import application.model.Pieces.Chessmen;

public class Board {
	    Tile[][] boxes;
	  
	    public Board()
	    {
	        this.resetBoard();
	    }
	  
	    public Tile getBox(int x, int y) throws Exception
	    {
	  
	        if (x < 0 || x > 7 || y < 0 || y > 7) {
	            throw new Exception("Index out of bound");
	        }
	  
	        return boxes[x][y];
	    }
	  
	    public void resetBoard()
	    {
	    	
	        // initialize white pieces
	        boxes[0][0] = new Tile(0, 0, Chessmen.WHITE_ROOK);
	        boxes[0][1] = new Tile(0, 1, Chessmen.WHITE_KNIGHT);
	        boxes[0][2] = new Tile(0, 2, Chessmen.WHITE_BISHOP);
	        boxes[0][3] = new Tile(0, 3, Chessmen.WHITE_QUEEN);
	        boxes[0][4] = new Tile(0, 4, Chessmen.WHITE_KING);
	        boxes[0][5] = new Tile(0, 5, Chessmen.WHITE_BISHOP);
	        boxes[0][6] = new Tile(0, 6, Chessmen.WHITE_KNIGHT);
	        boxes[0][7] = new Tile(0, 7, Chessmen.WHITE_ROOK);
	        boxes[1][0] = new Tile(1, 0, Chessmen.WHITE_PAWN);
	        boxes[1][1] = new Tile(1, 1, Chessmen.WHITE_PAWN);
	        boxes[1][2] = new Tile(1, 2, Chessmen.WHITE_PAWN);
	        boxes[1][3] = new Tile(1, 3, Chessmen.WHITE_PAWN);
	        boxes[1][4] = new Tile(1, 4, Chessmen.WHITE_PAWN);
	        boxes[1][5] = new Tile(1, 5, Chessmen.WHITE_PAWN);
	        boxes[1][6] = new Tile(1, 6, Chessmen.WHITE_PAWN);
	        boxes[1][7] = new Tile(1, 7, Chessmen.WHITE_PAWN);
	        
	        
	 
	  
	        // initialize black pieces
	        boxes[7][0] = new Tile(7, 0, Chessmen.BLACK_ROOK);
	        boxes[7][1] = new Tile(7, 1, Chessmen.BLACK_KNIGHT);
	        boxes[7][2] = new Tile(7, 2, Chessmen.BLACK_BISHOP);
	        boxes[7][3] = new Tile(7, 3, Chessmen.BLACK_QUEEN);
	        boxes[7][4] = new Tile(7, 4, Chessmen.BLACK_KING);
	        boxes[7][5] = new Tile(7, 5, Chessmen.BLACK_BISHOP);
	        boxes[7][6] = new Tile(7, 6, Chessmen.BLACK_KNIGHT);
	        boxes[7][7] = new Tile(7, 7, Chessmen.BLACK_ROOK);
	        boxes[6][0] = new Tile(6, 0, Chessmen.BLACK_PAWN);
	        boxes[6][1] = new Tile(6, 1, Chessmen.BLACK_PAWN);
	        boxes[6][2] = new Tile(6, 2, Chessmen.BLACK_PAWN);
	        boxes[6][3] = new Tile(6, 3, Chessmen.BLACK_PAWN);
	        boxes[6][4] = new Tile(6, 4, Chessmen.BLACK_PAWN);
	        boxes[6][5] = new Tile(6, 5, Chessmen.BLACK_PAWN);
	        boxes[6][6] = new Tile(6, 6, Chessmen.BLACK_PAWN);
	        boxes[6][7] = new Tile(6, 7, Chessmen.BLACK_PAWN);
	        // initialize remaining boxes without any piece
	        for (int i = 2; i < 6; i++) {
	            for (int j = 0; j < 8; j++) {
	                boxes[i][j] = new Tile(i, j, null);
	            }
	        }
	    }
	}

	