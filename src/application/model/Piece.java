package application.model;

import static application.model.Color.*;
import static application.model.MoveGenerator.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Piece enum contains the different types of chess pieces
 * (e.g. white king, black pawn, etc.) Each piece has a character
 * representation (upper-case for White and lower-case for Black).
 * KQRBNP denotes King, Queen, Rook, Bishop, Knight, and Pawn,
 * respectively.
 * 
 * Each piece has a functional interface that can generate pseudo-legal
 * and legal moves for the piece. The Move class is responsible for
 * determining if the move is legal or not.
 * @author Blake Herrera xng021
 * @date 11/04/2022
 */
public enum Piece implements Movable {
	
	WHITE_KING (WHITE, 'K', KING_MOVE_GENERATOR,"Chess/src/Images/WhiteKing.png"),
	WHITE_QUEEN (WHITE, 'Q', QUEEN_MOVE_GENERATOR,"Chess/src/Images/WhiteQueen.png"),
	WHITE_ROOK (WHITE, 'R', ROOK_MOVE_GENERATOR,"Chess/src/Images/WhiteRook.png"),
	WHITE_BISHOP (WHITE, 'B', BISHOP_MOVE_GENERATOR,"Chess/src/Images/WhiteBishop.png"),
	WHITE_KNIGHT (WHITE, 'N', KNIGHT_MOVE_GENERATOR,"Chess/src/Images/WhiteKnight.png"),
	WHITE_PAWN (WHITE, 'P', PAWN_MOVE_GENERATOR,"Chess/src/Images/WhitePawn.png"),
	BLACK_KING (BLACK, 'k', KING_MOVE_GENERATOR,"Chess/src/Images/BlackKing.png"),
	BLACK_QUEEN (BLACK, 'q', QUEEN_MOVE_GENERATOR,"Chess/src/Images/BlackQueen.png"),
	BLACK_ROOK (BLACK, 'r', ROOK_MOVE_GENERATOR,"Chess/src/Images/BlackRook.png"),
	BLACK_BISHOP (BLACK, 'b', BISHOP_MOVE_GENERATOR,"Chess/src/Images/BlackBishop.png"),
	BLACK_KNIGHT (BLACK, 'n', KNIGHT_MOVE_GENERATOR,"Chess/src/Images/BlackKnight.png"),
	BLACK_PAWN (BLACK, 'p', PAWN_MOVE_GENERATOR,"Chess/src/Images/BlackPawn.png");
	
	public static final Piece[] ALL_PIECES = {WHITE_KING, WHITE_QUEEN, WHITE_ROOK, WHITE_BISHOP, WHITE_KNIGHT,
			WHITE_PAWN, BLACK_KING, BLACK_QUEEN, BLACK_ROOK, BLACK_BISHOP, BLACK_KNIGHT, BLACK_PAWN};
	public static final Map<Character, Piece> CHAR_TO_PIECE = new HashMap<>();
	static {
		// Populate values in CHAR_TO_PIECE
		for(Piece piece : Piece.ALL_PIECES) {
			Piece.CHAR_TO_PIECE.put(piece.getCharacter(), piece);
		}
	};
	
	private final Color color;
	private final char character;
	private final MoveGenerator moveGenerator;
	private final String imageURL;

	private Piece(Color color, char character, MoveGenerator moveGenerator,String imageURL) {
	    this.color = color;
	    this.character = character;
	    this.moveGenerator = moveGenerator;
	    this.imageURL = imageURL;
	}
	
	/**
	 * Returns whether this piece is the same color as the other.
	 * @param other The other piece.
	 * @return true if the two pieces are the same color.
	 * false otherwise.
	 */
	public boolean isAlliedWith(Piece other) {
		return this.getColor() == other.getColor();
	}
	
	/**
	 * Gets the generator function for this piece's moves.
	 * @return The generator function for this piece's moves.
	 */
	public MoveGenerator getMoveGenerator() {
		return this.moveGenerator;
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
	
	/**
	 * Gets the images URL for the piece
	 * @return the image URL for the piece
	 */
	public String getImageURL() {
		return this.imageURL;
	}
	
	@Override
	public List<Move> getPseudoLegalMoves(Game game, Coordinate coordinate) {
		return this.getMoveGenerator().apply(game, coordinate);
	}
	
	@Override
	public List<Move> getLegalMoves(Game game, Coordinate coordinate) {
		List<Move> moves = this.getPseudoLegalMoves(game, coordinate);
		Iterator<Move> iterator = moves.iterator();
		while(iterator.hasNext()) {
			if(!game.isLegalMove(iterator.next())) {
				iterator.remove();
			}
		}
		return moves;
	}
}
