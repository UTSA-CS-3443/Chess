package application.model;

/**
 * The Move class represents a move by a piece
 * and contains from location and to location
 * 
 * @author Duarte Cern qag985
 * @date 11/06/2022
 */
public class Move {
	
	private Coordinate from;
	private Coordinate to;

	public Move(int fromRow, int fromCol, int toRow, int toCol) {
		this(new Coordinate(fromRow, fromCol), new Coordinate(toRow, toCol));
	}
	
	public Move(Coordinate from, Coordinate to) {
		this.from = from;
		this.to = to;
	}
	
	public boolean isLegal() {
		return false;
	}

	/**
	 * @return the from
	 */
	public Coordinate getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Coordinate from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Coordinate getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Coordinate to) {
		this.to = to;
	}
	
}
