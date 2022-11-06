package application.model;

import static application.model.Color.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * The Piece enum contains the different types of chess pieces
 * (e.g. white king, black pawn, etc.)
 * Each piece has a functional interface that can generate pseudo-legal
 * and legal moves for the piece. The Move class is responsible for
 * determining if the move is legal or not.
 * @author Blake Herrera xng021
 * @date 11/04/2022
 */
public enum Piece implements Movable {
	
	WHITE_KING (WHITE, 'K', Piece.KING_MOVE_GENERATOR),
	WHITE_QUEEN (WHITE, 'Q', Piece.QUEEN_MOVE_GENERATOR),
	WHITE_ROOK (WHITE, 'R', Piece.ROOK_MOVE_GENERATOR),
	WHITE_BISHOP (WHITE, 'B', Piece.BISHOP_MOVE_GENERATOR),
	WHITE_KNIGHT (WHITE, 'N', Piece.KNIGHT_MOVE_GENERATOR),
	WHITE_PAWN (WHITE, 'P', Piece.PAWN_MOVE_GENERATOR),
	BLACK_KING (BLACK, 'k', Piece.KING_MOVE_GENERATOR),
	BLACK_QUEEN (BLACK, 'q', Piece.QUEEN_MOVE_GENERATOR),
	BLACK_ROOK (BLACK, 'r', Piece.ROOK_MOVE_GENERATOR),
	BLACK_BISHOP (BLACK, 'b', Piece.BISHOP_MOVE_GENERATOR),
	BLACK_KNIGHT (BLACK, 'n', Piece.KNIGHT_MOVE_GENERATOR),
	BLACK_PAWN (BLACK, 'p', Piece.PAWN_MOVE_GENERATOR);
	
	static {
		for(Piece piece : Piece.ALL_PIECES) {
			Piece.CHAR_TO_PIECE.put(piece.getCharacter(), piece);
		}
	};
	
	public static final Piece[] ALL_PIECES = {WHITE_KING, WHITE_QUEEN, WHITE_ROOK, WHITE_BISHOP, WHITE_KNIGHT,
			WHITE_PAWN, BLACK_KING, BLACK_QUEEN, BLACK_ROOK, BLACK_BISHOP, BLACK_KNIGHT, BLACK_PAWN};
	public static final Map<Character, Piece> CHAR_TO_PIECE = new HashMap<>();
	
	public static final BiFunction<Game, Coordinate, List<Move>>
	ROOK_MOVE_GENERATOR = new Rider(new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}),
	BISHOP_MOVE_GENERATOR = new Rider(new int[][] {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}),
	QUEEN_MOVE_GENERATOR = new Rider(new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}}),
	KNIGHT_MOVE_GENERATOR = new Hopper(new int[][] {{2, 1}, {1, 2}, {2, -1}, {-1, 2}, {-2, 1}, {1, -2}, {-2, -1}, {-1, -2}}),
	KING_MOVE_GENERATOR = new Hopper(new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}}) {
		@Override
		public List<Move> apply(Game game, Coordinate coordinate) {
			List<Move> moves = super.apply(game, coordinate);
			
			// add castling to list of "Hopper" moves.
			moves.add(new Move(coordinate, coordinate.offset(0, 2)));
			moves.add(new Move(coordinate, coordinate.offset(0, -2)));
			
			return moves;
		}
	},
	PAWN_MOVE_GENERATOR = (game, coordinate) -> {
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
	};
	
	private final Color color;
	private final char character;
	private final BiFunction<Game, Coordinate, List<Move>> moveGenerator;

	private Piece(Color color, char character, BiFunction<Game, Coordinate, List<Move>> moveGenerator) {
	    this.color = color;
	    this.character = character;
	    this.moveGenerator = moveGenerator;
	}
	
	/**
	 * Gets the generator function for this piece's moves.
	 * @return The generator function for this piece's moves.
	 */
	public BiFunction<Game, Coordinate, List<Move>> getMoveGenerator() {
		return moveGenerator;
	}
	
	/**
	 * Gets the Color of this piece.
	 * @return The Color of this piece. 
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Gets the char representation of this piece.
	 * @return The char representation of this piece.
	 */
	public char getCharacter() {
		return this.character;
	}
	
	@Override
	public List<Move> getPseudoLegalMoves(Game game, Coordinate coordinate) {
		return this.getMoveGenerator().apply(game, coordinate);
	}
	
	@Override
	public List<Move> getLegalMoves(Game game, Coordinate coordinate) {
		List<Move> moves = this.getPseudoLegalMoves(game, coordinate);
		for(Move move : moves) {
			if(!move.isLegal()) moves.remove(move);
		}
		return moves;
	}
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
 * @date 11/04/2022
 */
class Rider implements BiFunction<Game, Coordinate, List<Move>> {
	
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
 * @date 11/04/2022
 */
class Hopper implements BiFunction<Game, Coordinate, List<Move>> {
	
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
			int newCol = row + offset[1];
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


