package application.model;

/**
 * The PieceColor enum refers to both the colors of the pieces and
 * the player who is controlling said pieces.
 * @author Blake Herrera xng021
 */
public enum PieceColor {

	WHITE, BLACK;

	/**
	 * The invert function swaps the color, similar
	 * to a boolean not function.
	 * @return BLACK, if the color was WHITE.
	 * WHITE, if the color was BLACK.
	 */
	public PieceColor invert() {
		return this == WHITE ? BLACK : WHITE;
	}
}
