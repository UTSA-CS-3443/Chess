package application.model;

public class Pieces {
	
	
	public abstract class Piece {

	    private boolean killed = false;
	    

	  
	    public void setKilled(boolean killed) {
	        this.killed = killed;
	    }   // setter killed

	    /**
	     * @param metodo astratto per vedere se il pezzo si può muovere
	     * @param board
	     * @param start
	     * @param end
	     * @return
	     */
	    public abstract boolean canMove(Board board, Spot start, Spot end);  // metodo astratto, ritorna se muovibile o no

	}
	public enum Chessmen {

	    WHITE_KING (Group.WHITE),
	    WHITE_QUEEN (Group.WHITE),
	    WHITE_ROOK (Group.WHITE),
	    WHITE_BISHOP (Group.WHITE),
	    WHITE_KNIGHT (Group.WHITE),
	    WHITE_PAWN (Group.WHITE),
	    BLACK_KING (Group.BLACK),
	    BLACK_QUEEN (Group.BLACK),
	    BLACK_ROOK (Group.BLACK),
	    BLACK_BISHOP (Group.BLACK),
	    BLACK_KNIGHT (Group.BLACK),
	    BLACK_PAWN (Group.BLACK),
	    EMPTY(null);

	    private Group group;

	    Chessmen(Group group){
	        this.group = group;
	    }


	    //I needed a getGroup() method here
	    public Group getGroup() {
	        return group;
	    }



	    public enum Group {
	        WHITE,
	        BLACK;
	    }
	}
}
