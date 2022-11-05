package application.model;

import java.util.List;

/**
 * The Movable interface provides functionality to board objects which
 * can be moved (i.e., the pieces). Pieces have pseudo-legal moves which
 * move the pieces in the correct direction, but may result in capturing
 * a friendly piece or putting their own king in check.
 * @author Blake Herrera xng021
 * @date 11/04/2022
 */
public interface Movable {
	
	/**
	 * Gets a List of Moves that the piece may possibly make.
	 * These moves may not be legal to play; for example, a piece may
	 * capture one of its allies, the move may violate castling rights, or
	 * may put the player's king in check.
	 * @param game - The chess game to generate moves from.
	 * @param coordinate - The coordinate the piece sits on.
	 * @return A List of Moves; some (or all) of which may not be legal (playable).
	 */
	public List<Move> getPseudoLegalMoves(Game game, Coordinate coordinate);
	
	/**
	 * Gets a List of Moves that the piece is definitely allowed to make.
	 * If the length of this list is 0, then the game has ended by
	 * either checkmate or stalemate.
	 * The Move class is responsible for determining the legality of a Move.
	 * @param game - The game to generate moves from.
	 * @param coordinate - The coordinate the piece sits on.
	 * @return A List of Moves; all of which are legal (playable).
	 */
	public List<Move> getLegalMoves(Game game, Coordinate coordinate);
	
}
