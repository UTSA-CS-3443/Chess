package application.model;

public class Coordinate {
	
	private int row;
	private int col;

	public Coordinate(int row, int col) {
		this.setRow(row);
		this.setCol(col);
	}
	
	public Coordinate offset(int dRow, int dCol) {
		return new Coordinate(this.getRow() + dRow, this.getCol() + dCol);
	}
	
	/**
	 * Returns Coordinate in algebraic notation
	 * i.e., (3,4) == c4
	 * 
	 * 'a' == 97, ... , 'h' == 104
	 * 
	 * @return Algebraic notation
	 */
	public String toAlgebraicNotation() {
		return (String) ( (char) (this.col + 97) + ((Integer) this.row).toString());
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
}
