package application.model;

import static application.model.Color.WHITE;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * The MoveGenerator class mostly provides static constants for
 * how each piece moves.
 * @author Blake Herrera xng021
 * @see Movable
 * @see Rider
 * @see Hopper
 */
public abstract class MoveGenerator 
implements BiFunction<Game, Coordinate, List<Move>> {

	public static final MoveGenerator
	ROOK_MOVE_GENERATOR = new Rider(new int[][] 
			{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}),
	BISHOP_MOVE_GENERATOR = new Rider(new int[][] 
			{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}),
	QUEEN_MOVE_GENERATOR = new Rider(new int[][] 
			{{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}}),
	KNIGHT_MOVE_GENERATOR = new Hopper(new int[][] 
			{{2, 1}, {1, 2}, {2, -1}, {-1, 2}, {-2, 1}, {1, -2}, {-2, -1}, {-1, -2}}),
	KING_MOVE_GENERATOR = new Hopper(new int[][] 
			{{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}}) {
		@Override
		public List<Move> apply(Game game, Coordinate coordinate) {
			List<Move> moves = super.apply(game, coordinate);
			// add castling to list of "Hopper" moves.
			moves.add(new Move(coordinate, coordinate.offset(0, 2)));
			moves.add(new Move(coordinate, coordinate.offset(0, -2)));
			return moves;
		}
	},
	PAWN_MOVE_GENERATOR = new MoveGenerator() {
		@Override
		public List<Move> apply(Game game, Coordinate coordinate) {
			// Pawns do not fit within the dynamic of Riders and Hoppers.
			List<Move> moves = new ArrayList<>();

			// Determine direction the pawn is moving
			int direction = game.getPieceAt(coordinate).getColor() == WHITE ? -1 : 1;

			// Add pawn jumps 1 or 2 spaces to list of moves
			moves.add(new Move(coordinate, coordinate.offset(direction, 0)));
			moves.add(new Move(coordinate, coordinate.offset(direction * 2, 0)));

			// Add pawn captures to list of moves
			moves.add(new Move(coordinate, coordinate.offset(direction, 1)));
			moves.add(new Move(coordinate, coordinate.offset(direction, -1)));
			return moves;
		}
	};
}

/**
 * The Rider class provides reusable code for Rooks, Bishops, and Queens.
 * These pieces are said to be "Riders" as they may "ride" in one direction
 * until they hit another piece, in which case it can be captured if
 * it is an enemy piece.
 * Rooks ride along straight paths,
 * Bishops ride along diagonals,
 * Queens ride along both straight paths and diagonals.
 * @author Blake Herrera xng021
 * @see Movable
 */
class Rider extends MoveGenerator {

	private final int[][] directions;

	public Rider(int[][] directions) {
		this.directions = directions;
	}

	@Override
	public List<Move> apply(Game game, Coordinate coordinate) {
		List<Move> moves = new ArrayList<Move>();
		int row = coordinate.getRow();
		int col = coordinate.getCol();
		for(int[] direction : this.getDirections()) {
			for(int newRow = row + direction[0], newCol = col + direction[1];
					game.isInBounds(newRow, newCol);
					newRow += direction[0], newCol += direction[1]) {
				moves.add(new Move(coordinate, new Coordinate(newRow, newCol)));
				if(game.getPieceAt(new Coordinate(newRow, newCol)) != null) break;
			}
		}
		return moves;
	}

	/**
	 * Gets an Nx2 dimensional array this piece may "ride".
	 * Column 0 represents the y/row direction,
	 * Column 1 represents the x/col direction.
	 * @return The directions this piece may ride.
	 */
	public int[][] getDirections() {
		return directions;
	}
}

/**
 * The hopper class contains information for pieces that have a
 * set enumeration of moves, such as the knight. The knight moves in
 * a (2, 1) pattern regardless of other pieces - it "hops" over them.
 * The king may be considered a derivative of a Hopper, with some
 * extra rules added in - it "hops" a single space.
 * @author Blake Herrera xng021
 * @see Movable
 */
class Hopper extends MoveGenerator {

	private final int[][] offsets;

	public Hopper(int[][] offsets) {
		this.offsets = offsets;
	}

	@Override
	public List<Move> apply(Game game, Coordinate coordinate) {
		List<Move> moves = new ArrayList<Move>();
		int row = coordinate.getRow();
		int col = coordinate.getCol();
		for(int[] offset : this.getOffsets()) {
			int newRow = row + offset[0];
			int newCol = col + offset[1];
			if(game.isInBounds(newRow, newCol)) {
				moves.add(new Move(coordinate, new Coordinate(newRow, newCol)));
			}
		}
		return moves;
	}

	/**
	 * Gets an Nx2 dimensional array this piece may "hop".
	 * Column 0 represents the y/row direction,
	 * Column 1 represents the x/col direction.
	 * @return The directions this piece may hop.
	 */
	public int[][] getOffsets() {
		return offsets;
	}
}
