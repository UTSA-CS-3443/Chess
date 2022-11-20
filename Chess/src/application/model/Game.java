package application.model;

import static application.model.Piece.*;
import static application.model.Color.*;

import java.util.HashMap;

/**
 * The Game class contains all the information relevant to a game of chess.
 * For example, it has a 2d array of pieces, and contains information on
 * whose move it is, castling rights, en passant target squares, and half/full
 * move counters for drawing purposes.
 * 
 * Additionally, it contains the names of the players.
 * @author Blake Herrera xng021
 * @see https://en.wikipedia.org/wiki/Rules_of_chess
 */
public class Game {

	public static final int BOARD_ROWS = 8,
			BOARD_COLS = 8;
	public static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	private Piece[][] board;
	private Color turn;
	private boolean whiteCanCastleKingside,
	whiteCanCastleQueenside,
	blackCanCastleKingside,
	blackCanCastleQueenside;
	private Coordinate enPassantTargetSquare;
	private int halfMoveCounter,
	fullMoveCounter;

	private String whiteName,
	blackName;

	HashMap<String, Integer> previousFENs = new HashMap<>();

	public Game(String whiteName, String blackName) {
		this(Game.STARTING_FEN, whiteName, blackName);
	}

	public Game(String fen, String whiteName, String blackName) {
		this.loadFromFEN(fen);
		this.whiteName = whiteName;
		this.blackName = blackName;
	}

	/**
	 * Tests whether a given coordinate lies within the board.
	 * @param coordinate The coordinate to test.
	 * @return true if the coordinate is within the board.
	 * false otherwise.
	 */
	public boolean isInBounds(Coordinate coordinate) {
		return this.isInBounds(coordinate.getRow(), coordinate.getCol());
	}

	/**
	 * Tests whether a given row and col lie within the board.
	 * @param row The row to test.
	 * @param col The col to test.
	 * @return true if the given row and col lie within
	 * the board. false otherwise.
	 */
	public boolean isInBounds(int row, int col) {
		return 0 <= row && 
				0 <= col && 
				row < this.getBoard().length &&
				col < this.getBoard()[row].length;
	}

	/**
	 * Detects if the game was set up in a legal position.
	 * An example of an illegal position is one where the black king
	 * is in check while it is White's turn.
	 * Another example is there is not exactly 1 white and black king.
	 * @return true, if the current position is legal, otherwise false.
	 */
	public boolean isLegalPosition() {
		// Set up counts for pieces
		HashMap<Piece, Integer> pieceCounts = new HashMap<>();
		for(Piece piece : ALL_PIECES) {
			pieceCounts.put(piece, 0);
		}
		int totalCountWhite = 0,
				totalCountBlack = 0;

		// There should be exactly 1 white and black king
		if(pieceCounts.get(WHITE_KING) != 1 ||
				pieceCounts.get(BLACK_KING) != 1) {
			return false;
		}

		// All else passed, position is legal
		return true;
	}

	/**
	 * Gets the piece at a specific coordinate.
	 * Note that (0, 0) is the top-left corner.
	 * @param coordinate The coordinate to get the piece from.
	 * @return The piece that sits on the given coordinate.
	 * null if there is no piece.
	 */
	public Piece getPieceAt(Coordinate coordinate) {
		return this.getPieceAt(coordinate.getRow(), coordinate.getCol());
	}

	/**
	 * Gets the piece at a specific row and col.
	 * Note that (0, 0) is the top-left corner.
	 * @param row The row that the piece sits on.
	 * @param col The col that the piece sits on.
	 * @return The piece that sits on the specified row and col.
	 * null if there is no piece.
	 */
	public Piece getPieceAt(int row, int col) {
		return this.getBoard()[row][col];
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<Piece,Integer> getPieceCount() {
		HashMap<Piece, Integer> pieceCount = new HashMap<>();
		for(Piece piece : ALL_PIECES) {
			pieceCount.put(piece, 0);
		}
		Piece[][] board = this.getBoard();
		for(int r=0; r<board.length; r++) {
			for(int c=0; c<board[r].length; c++) {
				Piece piece = board[r][c];
				if(piece != null) {
					pieceCount.put(piece, 1 + pieceCount.get(piece));
				}
			}
		}
		return pieceCount;
	}





	/**
	 * For a checkmate to occur, the king must be in check, and
	 * there must be no legal moves for the side to move.
	 * (If the king is not in check, this is a stalemate.)
	 * @return true if the current position is checkmate, otherwise false.
	 * @see Game.isDraw()
	 */
	public boolean isCheckmate() {
		return false;
	}

	/**
	 *
	 * 
	 */
	public void changeTurn() {

	}

	/**
	 * A draw may be achieved in several ways:
	 * 
	 * 1. The players agree to a draw.
	 * 
	 * 2. Stalemate. The side to move has no legal moves,
	 * but the king is not in check.
	 * 
	 * 3. There is insufficient checkmating material.
	 * (e.g. King + Knight/Bishop vs King)
	 * 
	 * 4. The game goes on for 50 moves without any captures
	 * or pawn advances. ("50-move rule").
	 * 
	 * 5. The same position repeats 3 times. The repeats
	 * do not have to be consecutive. ("3-fold repetition").
	 * @return
	 */
	public boolean isDraw() {
		return false; //!(isStalemate() && isThreefoldRepetition() && isFiftyMoveRule() && isInsufficientMaterial());
	}

	/**
	 * 
	 * @return
	 */
	public boolean is50MoveRule() {
		if(getHalfMoveCounter() >= 100) {
			return true;	
		}
		else {
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean is3FoldRepetition(){
		String fen = getFEN();
		String part [] =fen.split(" ");
		String fenFirst4 = part[0]+part[1]+part[2]+part[3];
		if(previousFENs.containsKey(fenFirst4)) {
			int repetitions = previousFENs.get(fenFirst4);
			if(repetitions > 2) {
				return true;
			} else {
				previousFENs.put(fenFirst4, repetitions + 1);
				return false;
			}
		} else {
			previousFENs.put(fen, 1);
			return false;
		}

	}

	/**
	 * 
	 * @return
	 */
	public boolean isInsufficientMaterial(){
		HashMap<Piece, Integer> pieceCounts = getPieceCount();
		if(pieceCounts.get(WHITE_PAWN) == 0 && 	pieceCounts.get(WHITE_ROOK)==0 && pieceCounts.get(WHITE_BISHOP) == 0 
				&& pieceCounts.get(WHITE_KNIGHT)<=1 && pieceCounts.get(BLACK_PAWN) == 0 && 	pieceCounts.get(BLACK_ROOK)==0 && pieceCounts.get(BLACK_BISHOP) == 0 
				&& pieceCounts.get(BLACK_KNIGHT) == 0 || pieceCounts.get(WHITE_PAWN) == 0 && 	pieceCounts.get(WHITE_ROOK)==0 && pieceCounts.get(WHITE_BISHOP) == 0 
				&& pieceCounts.get(WHITE_KNIGHT) ==0 && pieceCounts.get(BLACK_PAWN) == 0 && pieceCounts.get(BLACK_ROOK)==0 && pieceCounts.get(BLACK_BISHOP) == 0 
				&& pieceCounts.get(BLACK_KNIGHT) <= 1)
		{
			return true;
		}

		else if (pieceCounts.get(WHITE_PAWN) == 0 && 	pieceCounts.get(WHITE_ROOK)==0 && pieceCounts.get(WHITE_BISHOP) <= 1 
				&& pieceCounts.get(WHITE_KNIGHT) ==0 && pieceCounts.get(BLACK_PAWN) == 0 && 	pieceCounts.get(BLACK_ROOK)==0 && pieceCounts.get(BLACK_BISHOP) == 0 
				&& pieceCounts.get(BLACK_KNIGHT) == 0 || pieceCounts.get(WHITE_PAWN) == 0 && 	pieceCounts.get(WHITE_ROOK)==0 && pieceCounts.get(WHITE_BISHOP) == 0 
				&& pieceCounts.get(WHITE_KNIGHT) ==0 && pieceCounts.get(BLACK_PAWN) == 0 && pieceCounts.get(BLACK_ROOK)==0 && pieceCounts.get(BLACK_BISHOP) <= 1
				&& pieceCounts.get(BLACK_KNIGHT) == 0) {
			return true;
		} else {


			return false;
		}
	}

	/**
	 * Sets the current game based on the given fen.
	 * @param fen The FEN to load the game based off of.
	 * @see Game.getFEN()
	 */
	//need to create previous fen list and update method
	public void loadFromFEN(String fen) {
		// Split the FEN into parts
		String[] parts = fen.split(" ");

		// Set pieces from part 1 of FEN
		String[] pieces = parts[0].split("/");
		Piece[][] board = new Piece[8][8];
		for(int r=0; r<board.length; r++) {
			for(int c=0; c<board[r].length; c++) {
				char ch = pieces[r].charAt(c);
				if('0' <= ch && ch <= '9') c += ch - '0' - 1;
				else board[r][c] = Piece.CHAR_TO_PIECE.get(ch);
			}
		}
		this.setBoard(board);

		// Get the current turn
		this.setTurn(parts[1].equals("w") ? WHITE : BLACK);

		// Get castling rights
		this.setWhiteCanCastleKingside(parts[2].contains("K"));
		this.setWhiteCanCastleQueenside(parts[2].contains("Q"));
		this.setBlackCanCastleKingside(parts[2].contains("k"));
		this.setBlackCanCastleQueenside(parts[2].contains("q"));

		// Get halfmove counter
		this.setHalfMoveCounter(Integer.parseInt(parts[4]));

		// Get fullmove counter
		this.setFullMoveCounter(Integer.parseInt(parts[5]));
	}

	/**
	 * Returns the Forsyth-Edwards Notation of a game.
	 * 
	 * FEN is a standard notation for describing a particular board
	 * position of a chess game. The purpose of FEN is to provide
	 * all the necessary information to restart a game from a
	 * particular position.
	 * 
	 * A FEN record defines a particular game position, all in one
	 * text line and using only the ASCII character set. A FEN
	 * record contains six fields, each separated by a space.
	 * The fields are as follows:
	 * 
	 * 1. Piece placement data: Each rank is described, starting
	 * with rank 8 and ending with rank 1, with a '/' between each
	 * one; within each rank, the contents of the squares are
	 * described in order from the a-file to the h-file. Each piece
	 * is identified by a single letter taken from the standard
	 * English names in algebraic notation (pawn = 'P', knight = 'N',
	 * bishop = 'B', rook = 'R', queen = 'Q', and king = 'K').
	 * White pieces are designated using upperase letters ("PNBRQK"),
	 * while black pieces use lowercase letters ("pnbrqk"). A set
	 * of one or more consecutive empty squares within a rank is
	 * denoted by a digit from '1' to '8', corresponding to the
	 * number of squares.
	 * 
	 * 2. Active color: 'w' means that White is to move;
	 * 'b' means that Black is to move.
	 * 
	 * 3. Castling availability: If neither side has the ability to
	 * castle, this field uses the character '-'. Otherwise, this
	 * field contains one or more letters: 'K' if White can castle
	 * kingside, 'Q' if White can castle queenside, 'k' if Black can
	 * castle kingside, and 'q' if Black can castle queenside. A
	 * situation that temporarily prevents castling does not prevent
	 * use of this notation.
	 * 
	 * 4. En passant target square: This is a square over which a
	 * pawn has just passed while moving two squares; it is given in
	 * algebraic notation. If there is no en passant target square,
	 * this field uses the character '-'. This is recorded
	 * regardless of whether there is a pawn in position to capture
	 * en passant.
	 * 
	 * 5. Halfmove clock: The number of halfmoves since the last
	 * capture or pawn advance, used for the fifty-move rule.
	 * 
	 * 6. Fullmove number: The number of full moves. It starts
	 * at 1 and is incremented after Black's move.
	 * @return The FEN string for this game.
	 * @see https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
	 */
	public String getFEN() {
		String fen = "";

		// Iterate through the board
		Piece[][] board = this.getBoard();
		int consecutiveBlankSquares = 0;
		for(int r=0; r<board.length; r++) {
			for(int c=0; c<board[r].length; c++) {
				Piece piece = board[r][c];
				if(piece == null) consecutiveBlankSquares++;
				else fen += piece.getCharacter();
			}
			if(consecutiveBlankSquares != 0) fen += consecutiveBlankSquares;
			if(r + 1 != board.length) fen += '/';
		}

		// Determine castling rights
		String castlingRights = 
				(this.whiteCanCastleKingside() ? "K" : "") + 
				(this.whiteCanCastleQueenside() ? "Q" : "") + 
				(this.blackCanCastleKingside() ? "k" : "") +
				(this.blackCanCastleQueenside() ? "q" : "");
		if(castlingRights.equals("")) {
			castlingRights += '-';
		}

		// Add the other fields to the FEN
		fen += ' ' + 
				(this.getTurn() == WHITE ? 'w' : 'b') + 
				' ' +
				castlingRights +
				' ' +
				(this.getEnPassantTargetSquare() == null ? '-'
						: this.getEnPassantTargetSquare().toAlgebraicNotation()) +
				' ' +
				this.getHalfMoveCounter() +
				' ' +
				this.getFullMoveCounter();

		return fen;
	}

	
	public String toString() {
		return this.getFEN();
	}

	public Piece[][] getBoard() {
		return board;
	}

	public void setBoard(Piece[][] board) {
		this.board = board;
	}

	public Color getTurn() {
		return this.turn;
	}

	public void setTurn(Color turn) {
		this.turn = turn;
	}

	public boolean whiteCanCastleKingside() {
		return whiteCanCastleKingside;
	}

	public void setWhiteCanCastleKingside(boolean whiteCanCastleKingside) {
		this.whiteCanCastleKingside = whiteCanCastleKingside;
	}

	public boolean whiteCanCastleQueenside() {
		return whiteCanCastleQueenside;
	}

	public void setWhiteCanCastleQueenside(boolean whiteCanCastleQueenside) {
		this.whiteCanCastleQueenside = whiteCanCastleQueenside;
	}

	public boolean blackCanCastleKingside() {
		return blackCanCastleKingside;
	}

	public void setBlackCanCastleKingside(boolean blackCanCastleKingside) {
		this.blackCanCastleKingside = blackCanCastleKingside;
	}

	public boolean blackCanCastleQueenside() {
		return blackCanCastleQueenside;
	}

	public void setBlackCanCastleQueenside(boolean blackCanCastleQueenside) {
		this.blackCanCastleQueenside = blackCanCastleQueenside;
	}

	public int getHalfMoveCounter() {
		return halfMoveCounter;
	}

	public void setHalfMoveCounter(int halfMoveCounter) {
		this.halfMoveCounter = halfMoveCounter;
	}

	public int getFullMoveCounter() {
		return fullMoveCounter;
	}

	public void setFullMoveCounter(int fullMoveCounter) {
		this.fullMoveCounter = fullMoveCounter;
	}

	public Coordinate getEnPassantTargetSquare() {
		return enPassantTargetSquare;
	}

	public void setEnPassantTargetSquare(Coordinate enPassantTargetSquare) {
		this.enPassantTargetSquare = enPassantTargetSquare;
	}


	public String getWhiteName() {
		return whiteName;
	}


	public void setWhiteName(String whiteName) {
		this.whiteName = whiteName;
	}

	public String getBlackName() {
		return blackName;
	}

	public void setBlackName(String blackName) {
		this.blackName = blackName;
	}
}
