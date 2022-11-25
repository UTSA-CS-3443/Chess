package application.model;

/**
 * The Coordinate class is a simple wrapper around a pair
 * representing the row and column of a square.
 * It also provides some useful methods for displaying
 * a coordinate and checking if the coordinate is in bounds.
 * 
 * A Coordinate is considered an immutable type.
 * 
 * Note that (row=0, col=0) is the top-left corner and is
 * represented by the algebraic notation "a8". The white king
 * starts on (row=7, col=4) with the algebraic notation "e1".
 * @author Blake Herrera xng021
 */
public class Coordinate {

	private final int row;
	private final int col;

	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}

	/**
	 * Returns whether this coodinate lies within the bounds
	 * of the chess board.
	 * @return true if this coordinate lies within the bounds
	 * of the chess board. false otherwise.
	 */
	public boolean isInBounds() {
		return 0 <= this.getRow() &&
				this.getRow() < Game.BOARD_ROWS &&
				0 <= this.getCol() &&
				this.getCol() < Game.BOARD_COLS;
	}

	/**
	 * Returns the algebraic notation representation of this
	 * coordinate.
	 */
	public String toString() {
		return this.getAlgebraicNotation();
	}

	/**
	 * Returns the algebraic notation for this coordinate
	 * as a String.
	 * TODO throw exception if coordinate is out of bounds.
	 * @return The algebraic notation of this coordinate,
	 * as a String.
	 */
	public String getAlgebraicNotation() {
		return (char) (this.getCol() + 'a') + "" + (8 - this.getRow());
	}

	/**
	 * Returns a new coordinate based on a row and column offset
	 * from the current coordinate.
	 * @param dRow The change in row from the current coordinate.
	 * @param dCol The change in column from the current coordinate.
	 * @return A new coordinate based on the given row and column
	 * offset from the current coordinate.
	 */
	public Coordinate offset(int dRow, int dCol) {
		return new Coordinate(this.getRow() + dRow, this.getCol() + dCol);
	}

	/**
	 * Returns the row of the coordinate.
	 * @return The row of the coordinate.
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * Returns the column of the coordinate.
	 * @return The column of the coordinate.
	 */
	public int getCol() {
		return this.col;
	}

	/**
	 * Returns true if the two coordinates have the same row and column.
	 * @param other The other coordinate to compare to.
	 * @return true if the two coordinates have the same row and column.
	 * false otherwise.
	 */
	public boolean equals(Coordinate other) {
		if(other == null) {
			return false;
		}
		return this.getRow() == other.getRow() && this.getCol() == other.getCol();
	}
}
