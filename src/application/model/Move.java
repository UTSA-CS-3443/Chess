package application.model;

/**
 * The Move class is a simple wrapper around two coordinates
 * or thus a wrapper around four ints representing the row and
 * columns of the source and destination.
 * It also provides some useful common methods for manipulating
 * and retrieving data from these coordinate pairs.
 * 
 * As Move is designed to be a wrapper around two Coordinates
 * which are immutable, a Move is also an immutable type.
 * @author Blake Herrera xng021
 */
public class Move {

	private final Coordinate fromCoordinate,
					   		 toCoordinate;
	
	public Move(int fromRow, int fromCol, int toRow, int toCol) {
		this(new Coordinate(fromRow, fromCol),
				new Coordinate(toRow, toCol));
	}

	public Move(Coordinate fromCoordinate, Coordinate toCoordinate) {
		this.fromCoordinate = fromCoordinate;
		this.toCoordinate = toCoordinate;
	}
	
	/**
	 * Returns true if both the source and destination coordinate
	 * are in bounds.
	 * @return true if both the source and destination coordinate
	 * are in bounds. false otherwise.
	 */
	public boolean isInBounds() {
		return this.getFromCoordinate().isInBounds() && this.getToCoordinate().isInBounds();
	}

	/**
	 * Compares two moves and returns true if the coordinates are equal.
	 * @param other The other Move to compare to.
	 * @return true if the moves have the same source and destination
	 * coordinates. false otherwise.
	 */
	public boolean equals(Move other) {
		if(other == null) {
			return false;
		}
		return this.getFromCoordinate().equals(other.getFromCoordinate()) &&
				this.getToCoordinate().equals(other.getToCoordinate());
	}

	/**
	 * Returns the toString() method of the source coordinate
	 * followed by the toString() method of the destination
	 * coordinate.
	 */
	public String toString() {
		return this.getFromCoordinate().toString() + this.getToCoordinate().toString();
	}

	/**
	 * Returns the difference in rows between the destination
	 * and source coordinates.
	 * @return The difference in rows between the destination
	 * and source coordinates.
	 */
	public int getRowDifference() {
		return this.getToRow() - this.getFromRow();
	}

	/**
	 * Returns the difference in columns between the destination
	 * and source coordinates.
	 * @return The difference in columns between the destination
	 * and source coordinates.
	 */
	public int getColDifference() {
		return this.getToCol() - this.getFromCol();
	}

	/**
	 * Returns the source coordinate.
	 * @return The source coordinate.
	 */
	public Coordinate getFromCoordinate() {
		return this.fromCoordinate;
	}

	/**
	 * Returns the destination coordinate.
	 * @return The destination coordinate.
	 */
	public Coordinate getToCoordinate() {
		return this.toCoordinate;
	}

	/**
	 * Returns the source row.
	 * @return The source row.
	 */
	public int getFromRow() {
		return this.getFromCoordinate().getRow();
	}

	/**
	 * Returns the source column.
	 * @return The source column.
	 */
	public int getFromCol() {
		return this.getFromCoordinate().getCol();
	}

	/**
	 * Returns the destination row.
	 * @return The destination row.
	 */
	public int getToRow() {
		return this.getToCoordinate().getRow();
	}

	/**
	 * Returns the destination column.
	 * @return The destination column.
	 */
	public int getToCol() {
		return this.getToCoordinate().getCol();
	}

}
