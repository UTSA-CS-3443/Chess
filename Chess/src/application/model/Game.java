package application.model;

import static application.model.Piece.*;
import static application.model.Side.*;
import static application.model.Color.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * The Game class contains all the information relevant to a game of chess.
 * For example, it has a 2d array of pieces, and contains information on
 * whose move it is, castling rights, en passant target square, a half-move
 * counter for the purposes of determining a draw, as well as a full-move
 * counter. Additionally, previous FENs are stored to implement functionality
 * for undoing a move and checking for threefold repetition.
 * 
 * Additionally, it contains attributes relevant to the players, such
 * as their names.
 * @author Blake Herrera xng021
 * @see https://en.wikipedia.org/wiki/Rules_of_chess
 */
public class Game {
	
	public static final int BOARD_ROWS = 8,
							BOARD_COLS = 8;
	public static final String STARTING_FEN = 
			"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	private Piece[][] board;
	private Color turn;
	private boolean whiteCanCastleKingside,
					whiteCanCastleQueenside,
					blackCanCastleKingside,
					blackCanCastleQueenside;
	private Coordinate enPassantTargetSquare;
	private int halfMoveCounter,
				fullMoveCounter;
	private Map<String, Integer> previousFensFor3FoldRepetition;
	private Stack<String> previousFenStack;
	
	private String whiteName,
				   blackName;
	
	public Game() {
		this("White", "Black");
	}
	
	public Game(String whiteName, String blackName) {
		this(Game.STARTING_FEN, whiteName, blackName);
	}
	
	// TODO check if illegal starting position
	public Game(String fen, String whiteName, String blackName) {
		this.loadFromFEN(fen);
		this.whiteName = whiteName;
		this.blackName = blackName;
		this.previousFenStack = new Stack<>();
		this.previousFenStack.push(fen);
		this.previousFensFor3FoldRepetition = new HashMap<>();
		this.previousFensFor3FoldRepetition.put(this.getFenFor3FoldRepetition(), 1);
	}
	
	/**
	 * Tests whether a given row and col lie within the board.
	 * TODO move this somewhere else?
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
		 Map<Piece, Integer> pieceCounts = this.getPieceCount();
		
		// There should be exactly 1 white and black king
		if(pieceCounts.get(WHITE_KING) != 1 ||
		   pieceCounts.get(BLACK_KING) != 1) {
			return false;
		}
		
		// TODO no pawns on 1st or last row?
		
		// All else passed, position is legal
		return true;
	}
	
	/**
	 * Returns the list of pseudo-legal moves for a player
	 * in the current position.
	 * @param color The player whose moves should be generated.
	 * @return The list of pseudo-legal moves for the player
	 * in the current position.
	 */
	public List<Move> getPseudoLegalMoves(Color color) {
		List<Move> moves = new LinkedList<>();
		for(int r=0; r<Game.BOARD_ROWS; r++) {
			for(int c=0; c<Game.BOARD_COLS; c++) {
				Piece piece = this.getPieceAt(r, c);
				if(piece == null || piece.getColor() != color) {
					continue;
				}
				moves.addAll(this.getPieceAt(r, c).getPseudoLegalMoves(this, new Coordinate(r, c)));
			}
		}
		return moves;
	}
	
	/**
	 * Returns the list of legal moves for this position.
	 * @return The list of legal moves for this position.
	 */
	public List<Move> getLegalMoves() {
		List<Move> moves = new LinkedList<>();
		for(Coordinate coordinate : Game.coordinateIterator()) {
			Piece piece = this.getPieceAt(coordinate);
			if(piece == null || piece.getColor() != this.getTurn()) {
				continue;
			}
			moves.addAll(piece.getLegalMoves(this, coordinate));
		}
		return moves;
	}
	
	/**
	 * Returns an Iterable which iterates over each of the 64 squares,
	 * in order from left to right, then top to bottom.
	 * @return An Iterable for each Coordinate on the board.
	 */
	public static Iterable<Coordinate> coordinateIterator() {
		return new Iterable<Coordinate>() {
			
			@Override
			public Iterator<Coordinate> iterator() {
				return new Iterator<Coordinate>() {
					
					private int r = 0;
					private int c = 0;

					@Override
					public boolean hasNext() {
						return r < Game.BOARD_ROWS;
					}

					@Override
					public Coordinate next() {
						Coordinate coordinate = new Coordinate(r, c++);
						if(c >= Game.BOARD_COLS) {
							c -= Game.BOARD_COLS;
							r++;
						}
						return coordinate;
					}
					
				};
			}
		};
	}
	
	/**
	 * Returns an Iterable which iterates over each piece on
	 * the board, from left to right, then top to bottom.
	 * This does not return null pieces.
	 * @return An Iterable for each Piece on the board.
	 * Does not return null pieces.
	 */
	public Iterable<Piece> pieceIterator() {
		return new Iterable<Piece>() {

			@Override
			public Iterator<Piece> iterator() {
				return new Iterator<Piece>() {
					
					private int r = 0;
					private int c = 0;
					
					{
						while(r < Game.BOARD_ROWS &&
								Game.this.getPieceAt(r, c) == null) {
							if(++c >= Game.BOARD_COLS) {
								c -= Game.BOARD_COLS;
								r++;
							}
						}
					}

					@Override
					public boolean hasNext() {
						return r < Game.BOARD_ROWS;
					}

					@Override
					public Piece next() {
						Piece piece = Game.this.getPieceAt(r, c++);
						while(this.hasNext() && 
								Game.this.getPieceAt(r, c) == null) {
							if(++c >= Game.BOARD_COLS) {
								c -= Game.BOARD_COLS;
								r++;
							}
						}
						return piece;
					}
				};
			}
		};
	}
	
	/**
	 * This method returns true if the king of the given color
	 * is in check.
	 * @param color The color king to check.
	 * @return true if the king is in check, false otherwise.
	 */
	public boolean isInCheck(Color color) {
		Coordinate kingLocation = this.getKingLocation(color);
		for(Move move : this.getPseudoLegalMoves(color.invert())) {
			if(move.getToCoordinate().equals(kingLocation)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method returns a Coordinate for the square the king
	 * of the given color sits on.
	 * @param color The color king to find.
	 * @return The Coordinate where the king sits.
	 */
	public Coordinate getKingLocation(Color color) {
		for(Coordinate coordinate : Game.coordinateIterator()) {
			if(this.getPieceAt(coordinate) == (color == WHITE ? WHITE_KING : BLACK_KING)) {
				return coordinate;
			}
		}
		//TODO throw error
		return null;
	}

	/**
	 *  Returns whether or not the given move is legal for this game.
	 *  There are several things which may make a move illegal.
	 *  1. A piece is moved the wrong way. This method
	 *  assumes that the piece moves the right way.
	 *  2. The coordinates are off the board.
	 *  3. There is no piece on the from square.
	 *  4. There is an allied piece on the to square.
	 *  5. The move puts the king in check.
	 *  6. The move violates castling rules/rights.
	 *  7. A pawn moves forward with something in the way.
	 *  8. A pawn tries to capture with no target
	 *  (the target for en-passant is not on the square the
	 *  pawn moves to.)
	 */
	public boolean isLegalMove(Move move) {
		// Check coordinates are in bounds
		if(!move.getFromCoordinate().isInBounds() ||
				!move.getToCoordinate().isInBounds()) {
			return false;
		}
		
		Piece movedPiece = this.getPieceAt(move.getFromCoordinate());
		Piece capturedPiece = this.getPieceAt(move.getToCoordinate());
		int rowDifference = move.getRowDifference();
		int colDifference = move.getColDifference();
		
		// Check moved piece is not null
		if(movedPiece == null) {
			return false;
		}
		
		// Check for friendly captures
		if(capturedPiece != null && capturedPiece.isAlliedWith(movedPiece)) {
			return false;
		}
		
		// Check pawn moves
		if(movedPiece == WHITE_PAWN || movedPiece == BLACK_PAWN) {
			if(colDifference == 0) {
				// Check nothing is in the way of pawn moving forward
				if(capturedPiece != null || Math.abs(rowDifference) == 2 &&
						this.getPieceAt(move.getFromCoordinate().offset(rowDifference / 2, 0)) != null) {
					return false;
				}
			} else if((this.getEnPassantTargetSquare() == null || 
					!move.getToCoordinate().equals(this.getEnPassantTargetSquare()) &&
					this.getPieceAt(move.getToCoordinate()) == null)) {
				return false;
			}
		}
		
		// Check castling
		Coordinate kingLocation = this.getKingLocation(movedPiece.getColor());
		if((movedPiece == WHITE_KING || movedPiece == BLACK_KING) && Math.abs(rowDifference) == 2) {
			// You cannot castle if you are in check.
			if(this.isInCheck(movedPiece.getColor())) {
				return false;
			}
			//squares f1/f8/c1/c8 must be clear and not attacked
			Coordinate castlingTargetSquare = kingLocation.offset(rowDifference / 2, 0);
			//squares g1/g8/c1/c8 must be clear
			Coordinate castlingTargetSquare2 = kingLocation.offset(rowDifference, 0);
			//squares b1/b8 must be clear
			Coordinate castlingTargetSquare3 = rowDifference != -2 ? null : kingLocation.offset(-3, 0);
			if(this.getPieceAt(castlingTargetSquare) != null ||
					this.getPieceAt(castlingTargetSquare2) != null ||
					castlingTargetSquare3 != null && this.getPieceAt(castlingTargetSquare3) != null) {
				return false;
			}
			// You cannot castle "through" check
			for(Move opponentMove : this.getPseudoLegalMoves(movedPiece.getColor().invert())) {
				if(opponentMove.getToCoordinate().equals(castlingTargetSquare)) {
					return false;
				}
			}
			// Castling "into" check is covered later
		}
		// Check castling rights / legality
		if(movedPiece == WHITE_KING) {
			if(rowDifference == 2 && !this.whiteCanCastleKingside()) return false;
			if(rowDifference == -2 && !this.whiteCanCastleQueenside()) return false;
		} else if(movedPiece == BLACK_KING) {
			if(rowDifference == 2 && !this.blackCanCastleKingside()) return false;
			if(rowDifference == -2 && !this.blackCanCastleQueenside()) return false;
		}
		
		/*
		 * Check if the king would be in check by putting the move on the board
		 * and checking the pseudo-legal moves for his opponent. If there is a
		 * pseudo-legal move that attacks a square the king sits on, then the
		 * king would be in check and thus the move is not legal. In either case,
		 * undo the move (we are only trying to test the moves, not play them).
		 */
		this.pushMove(move);
		if(this.isInCheck(this.getTurn().invert())) {
			this.popMove();
			return false;
		}
		this.popMove();
		
		// Everything passed, move is legal.
		return true;
	}
	
	/**
	 * This method puts a legal move onto the board and handles all
	 * special moves such as en passant and castling.
	 * If a non-legal move is passed into this method, unexpected
	 * results may occur.
	 * @param move The legal move to push onto the board.
	 */
	public void pushMove(Move move) {
		// Update the board
		Coordinate source = move.getFromCoordinate();
		Coordinate destination = move.getToCoordinate();
		Piece movedPiece = this.getPieceAt(source);
		Piece capturedPiece = this.getPieceAt(destination);
		this.setPieceAt(source, movedPiece);
		this.setPieceAt(destination, null);
		
		// Check pawn moves
		if(movedPiece == WHITE_PAWN || movedPiece == BLACK_PAWN) {
			int dCol = move.getColDifference();
			
			// Check captures en passant
			if(this.getEnPassantTargetSquare() != null &&
					destination.equals(this.getEnPassantTargetSquare())) {
				Coordinate capturedCoordinate = destination.offset(0, movedPiece == WHITE_PAWN ? 1 : -1);
				capturedPiece = this.getPieceAt(capturedCoordinate);
				this.setPieceAt(capturedCoordinate, null);
			}
			
			// Check if pawn moved 2 squares for en passant
			if(Math.abs(dCol) == 2) {
				this.setEnPassantTargetSquare(destination.offset(0, dCol / 2));
			} else {
				this.setEnPassantTargetSquare(null);
			}
			
			/*
			 * Check pawn promotions
			 * TODO player may select which piece to promote to
			 * instead of queen.
			 */
			if(movedPiece == BLACK_PAWN && move.getToRow() == Game.BOARD_ROWS - 1) {
				this.setPieceAt(destination, BLACK_QUEEN);
			} else if(movedPiece == WHITE_PAWN && move.getToRow() == 0) {
				this.setPieceAt(destination, WHITE_QUEEN);
			}
		}
		
		Coordinate a1 = new Coordinate(7, 0);
		Coordinate a8 = new Coordinate(0, 0);
		Coordinate h1 = new Coordinate(7, 7);
		Coordinate h8 = new Coordinate(0, 7);
		
		// Check castling
		int rowDifference = move.getRowDifference();
		if(movedPiece == WHITE_KING && rowDifference == -2) {
			// White castles queenside
			this.setPieceAt(a1, null);
			this.setPieceAt(7, 3, WHITE_ROOK);
		} else if(movedPiece == WHITE_KING && rowDifference == 2) {
			// White castles kingside
			this.setPieceAt(h1, null);
			this.setPieceAt(7,  5, WHITE_ROOK);
		} else if(movedPiece == BLACK_KING && rowDifference == -2) {
			// Black castles queenside
			this.setPieceAt(a8, null);
			this.setPieceAt(0, 3, BLACK_ROOK);
		} else if(movedPiece == BLACK_KING && rowDifference == 2) {
			// Black castles kingside
			this.setPieceAt(h8, null);
			this.setPieceAt(0, 5, BLACK_ROOK);
		}
		
		// update castling rights
		if(movedPiece == WHITE_KING || source.equals(a1) || destination.equals(a1)) {
			this.setWhiteCanCastleQueenside(false);
		} else if(movedPiece == WHITE_KING || source.equals(h1) || destination.equals(h1)) {
			this.setWhiteCanCastleKingside(false);
		} else if(movedPiece == BLACK_KING || source.equals(a8) || destination.equals(a8)) {
			this.setBlackCanCastleQueenside(false);
		} else if(movedPiece == BLACK_KING || source.equals(h8) || source.equals(h8)) {
			this.setBlackCanCastleKingside(false);
		}
		
		// Update full-move clock
		this.setFullMoveCounter(this.getFullMoveCounter() + (this.getTurn() == BLACK ? 1 : 0));
		
		// Update half-move clock
		this.setHalfMoveCounter(capturedPiece != null || movedPiece == WHITE_PAWN || movedPiece == BLACK_PAWN ?
				0 : this.getHalfMoveCounter() + 1);
		
		// Update whose turn it is
		this.setTurn(this.getTurn().invert());
		
		// Update FEN lists / 3fold repetition counters
		Map<String, Integer> fen3FoldMap = this.getPreviousFensFor3FoldRepetition();
		String fen3Fold = this.getFenFor3FoldRepetition();
		fen3FoldMap.put(fen3Fold, fen3FoldMap.containsKey(fen3Fold) ? fen3FoldMap.get(fen3Fold) + 1 : 1);
		this.getPreviousFenStack().push(this.getFEN());
	}
	
	/**
	 * This move undoes the most recent move played on the board.
	 * It does this by checking the previous FEN and loading the position
	 * from there. It also updates the previous FEN lists for the
	 * previous moves and for checking threefold repetition.
	 */
	public void popMove() {
		// Update 3fold repetition counters
		String fen3Fold = this.getFenFor3FoldRepetition();
		Map<String, Integer> fen3FoldMap = this.getPreviousFensFor3FoldRepetition();
		int repetitions = fen3FoldMap.get(fen3Fold);
		if(repetitions == 1) {
			fen3FoldMap.remove(fen3Fold);
		} else {
			fen3FoldMap.put(fen3Fold, repetitions - 1);
		}
		
		// Update the previous fen lists
		Stack<String> fenStack = this.getPreviousFenStack();
		fenStack.pop();
		this.loadFromFEN(fenStack.peek());
	}
	
	/**
	 * This method returns the relevant parts of the FEN that
	 * are to be used when checking for threefold repetition.
	 * (That is, the first 4 parts of the fen; the half-move counter
	 * and full-move counter update with every move and thus are
	 * never repeated.)
	 * @return The part of the FEN relevant for checking
	 * threefold repetition.
	 */
	public String getFenFor3FoldRepetition() {
		String[] fenParts = this.getFEN().split(" ");
		return fenParts[0] + ' ' + fenParts[1] + ' ' + fenParts[2] + ' ' + fenParts[3];
	}
	
	/**
	 * This method changes the piece at a given coordinate.
	 * TODO make this private?
	 * @param coordinate The coordinate where the updated piece should go.
	 * @param piece The piece to be changed. null if there is no piece.
	 * (In other words, null to remove a piece).
	 * @see Game.setPieceAt(int r, int c, Piece piece)
	 */
	public void setPieceAt(Coordinate coordinate, Piece piece) {
		this.setPieceAt(coordinate.getRow(), coordinate.getCol(), piece);
	}
	
	/**
	 * This method changes the piece at a given row and col.
	 * Note that (0, 0) is the top-left corner.
	 * TODO make this private?
	 * @param r The row where the updated piece should go.
	 * 0 is the top row.
	 * @param c The col where the updated piece should go.
	 * 0 is the leftmost col.
	 * @param piece The piece to be changed. null if there is no piece.
	 * (In other words, null to remove a piece).
	 */
	public void setPieceAt(int r, int c, Piece piece) {
		this.getBoard()[r][c] = piece;
	}
	
	/**
	 * Gets the piece at a specific coordinate.
	 * @param coordinate The coordinate to get the piece from.
	 * @return The piece that sits on the given coordinate.
	 * null if there is no piece (the square is empty).
	 */
	public Piece getPieceAt(Coordinate coordinate) {
		return this.getPieceAt(coordinate.getRow(), coordinate.getCol());
	}
	
	/**
	 * Gets the piece at a specific row and col.
	 * Note that (0, 0) is the top-left corner.
	 * @param row The row that the piece sits on.
	 * 0 is the top row.
	 * @param col The col that the piece sits on.
	 * 0 is the leftmost col.
	 * @return The piece that sits on the specified row and col.
	 * null if there is no piece.
	 */
	public Piece getPieceAt(int row, int col) {
		return this.getBoard()[row][col];
	}
	
	/**
	 * Gets a count of all the pieces on the chess board.
	 * @return A HashMap of the count of each chess piece.
	 */
	public Map<Piece, Integer> getPieceCount() {
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
		return this.getLegalMoves().size() == 0 &&
				this.isInCheck(this.getTurn());
	}
	
	/**
	 * A draw may be achieved in several ways:
	 * 
	 * 1. The players agree to a draw.
	 * (This method does not handle this case.)
	 * 
	 * 2. Stalemate. The side to move has no legal moves,
	 * but the king is not in check.
	 * 
	 * 3. There is insufficient checkmating material.
	 * King + Knight/Bishop vs King is insufficient material.
	 * King + Bishop vs King + bishop of same color is as well.
	 * Everything else may be considered sufficient material for
	 * the purposes of constructing a checkmate through a series
	 * of legal moves, though the game may be trivially drawn by
	 * the 50-move rule (below).
	 * 
	 * 4. The game goes on for 50 moves without any captures
	 * or pawn advances. ("50-move rule").
	 * 
	 * 5. The same position repeats 3 times. The repeats
	 * do not have to be consecutive. ("3-fold repetition").
	 * 
	 * 6. One side runs out of time, but the other side has
	 * only a king left. (There are no clocks in this app.)
	 * @return
	 */
	public boolean isDraw() {
		return this.isStalemate() ||
				this.isInsufficientMaterial() ||
				this.is50MoveRule() ||
				this.is3FoldRepetition();
	}
	
	/**
	 * Returns whether the game is a stalemate.
	 * A stalemate occurs when there are no legal moves
	 * for the side to move, but the king is not in check.
	 * This is counted as a draw.
	 * @return true if the game is stalemate. false otherwise.
	 */
	public boolean isStalemate() {
		return this.getLegalMoves().size() == 0 &&
				!this.isInCheck(this.getTurn());
	}
	
	/**
	 * Returns true if the game is a draw by the 50-move rule:
	 * A game is declared a draw if the game has continued for
	 * 50 full moves with neither side making any captures nor
	 * advancing any pawns.
	 * 
	 * Although there are some theoretical positions where a
	 * checkmate can be forced in over 50 moves with no pawn
	 * moves or piece captures, these are very rare and this
	 * rule is in the official rules for both the United States
	 * Chess Federation (USCF) and the International Chess
	 * Federation (FIDE).
	 * @return true if the game is a draw by the 50-move rule.
	 * false otherwise.
	 */
	public boolean is50MoveRule() {
		return this.getHalfMoveCounter() >= 100; 
	}
	
	/**
	 * Returns whether or not the game is a draw by threefold repetition.
	 * The threefold repetition only applies if it is the same position;
	 * the repetitions do not have to be consecutive.
	 * It is considered the same position if
	 * 1) The pieces are all on the same squares.
	 * 2) The same side is to move.
	 * 3) Castling rights have not changed.
	 * 4) The en-passant target square has not changed.
	 * Thus only the first 4 parts of a FEN need to be considered.
	 * @return true, if the game is a draw by threefold repetition.
	 * false otherwise.
	 * @see Game.getPreviousFensFor3FoldRepetition()
	 * @see Game.getFenFor3FoldRepetition()
	 */
	public boolean is3FoldRepetition(){
		return this.getPreviousFensFor3FoldRepetition().get(this.getFenFor3FoldRepetition()) >= 3;
	}
	
	/**
	 * A game is declared a draw by insufficient material if there
	 * is no way to construct a checkmate by a sequence of legal
	 * moves from the current position.
	 * A King + Knight or Bishop vs a King is considered to be
	 * insufficient material. There is also the special case of
	 * a King + Bishop vs King + Bishop of the same color. This
	 * case is not considered.
	 * Some games may seem like trivial draws, but the 50-move rule
	 * is designed to handle these cases.
	 * @return true if the game is a draw by insufficient material.
	 * false otherwise.
	 */
	public boolean isInsufficientMaterial(){
		 Map<Piece, Integer> pieceCounts = this.getPieceCount();
		 Piece[] maxZero = {WHITE_PAWN, WHITE_ROOK, WHITE_QUEEN, BLACK_PAWN, BLACK_ROOK, BLACK_QUEEN};
		 Piece[] maxOne = {WHITE_KNIGHT, WHITE_BISHOP, BLACK_KNIGHT, BLACK_BISHOP};
		 
		 for(Piece piece : maxZero) {
			 if(pieceCounts.get(piece) != 0) {
				 return false;
			 }
		 }
		 
		 int total = 0;
		 for(Piece piece : maxOne) {
			 total += pieceCounts.get(piece);
		 }
		 return total <= 1;
	}
	
	/**
	 * Sets Game's object attributes based on the String describing
	 * the Forsyth-Edwards Notation of the position.
	 * Note that this method does not change the previous FENs
	 * for the previous positions / checking for threefold repetition.
	 * https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
	 * TODO throw exception if bad FEN / Illegal Position is given.
	 * @param fen The Forsyth-Edwards Notation to load from.
	 * @see Game.getFEN()
	 */
	public void loadFromFEN(String fen) {
		// Split the FEN into parts
		String[] parts = fen.split(" ");
		
		// Set pieces from part 1 of FEN
		String[] pieces = parts[0].split("/");
		Piece[][] board = new Piece[8][8];
		for(int r=0; r<board.length; r++) {
			for(int c=0, i=0; c<board[r].length; c++, i++) {
				char ch = pieces[r].charAt(i);
				if('0' < ch && ch < '9') {
					c += ch - '0' - 1;
				} else {
					board[r][c] = Piece.CHAR_TO_PIECE.get(ch);
				}
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
	 * White pieces are designated using upper-case letters ("PNBRQK"),
	 * while black pieces use lower-case letters ("pnbrqk"). A set
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
	 * 5. Half-move clock: The number of half-moves since the last
	 * capture or pawn advance, used for the fifty-move rule.
	 * 
	 * 6. Full-move number: The number of full moves. It starts
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
				if(piece == null) {
					consecutiveBlankSquares++;
				} else if(consecutiveBlankSquares != 0) {
					fen += consecutiveBlankSquares;
					fen += piece.getCharacter();
					consecutiveBlankSquares = 0;
				} else {
					fen += piece.getCharacter();
				}
			}
			if(consecutiveBlankSquares != 0) {
				fen += consecutiveBlankSquares;
				consecutiveBlankSquares = 0;
			}
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
		fen += " " + 
			   (this.getTurn() == WHITE ? 'w' : 'b') + 
			   ' ' +
			   castlingRights +
			   ' ' +
			   (this.getEnPassantTargetSquare() == null ? '-'
					   : this.getEnPassantTargetSquare().getAlgebraicNotation()) +
			   ' ' +
			   this.getHalfMoveCounter() +
			   ' ' +
			   this.getFullMoveCounter();
		
		return fen;
	}
	
	/**
	 * Returns a String representation of the game, which is
	 * an ASCII representation of the board, followed by
	 * the FEN string on a new line. Pieces use their associated
	 * character for the board, and empty squares are shown
	 * by a period for light squares, or a comma for dark squares.
	 */
	public String toString() {
		String s = "";
		for(int r=0; r<8; r++) {
			for(int c=0; c<8; c++) {
				Piece piece = this.getPieceAt(r, c);
				s += piece == null ? ((r + c) % 2 == 0 ? '.' : ',') : piece.getCharacter();
			}
			s += '\n';
		}
		s += this.getFEN();
		return s;
	}
	
	/**
	 * Gets the matrix of pieces representing the board.
	 * @return The matrix of pieces representing the board.
	 */
	public Piece[][] getBoard() {
		return board;
	}
	
	/**
	 * Sets the matrix of pieces representing the board.
	 * @param board The matrix of pieces representing the board.
	 */
	public void setBoard(Piece[][] board) {
		this.board = board;
	}

	/**
	 * Gets the color of the current side to move.
	 * @return The color of the current side to move.
	 */
	public Color getTurn() {
		return this.turn;
	}
	
	/**
	 * Sets the color of the current side to move.
	 * @param turn The color of the current side to move to set.
	 */
	public void setTurn(Color turn) {
		this.turn = turn;
	}
	
	/**
	 * Returns whether White has kingside castling rights.
	 * @return true if White has kingside castling rights.
	 * false otherwise.
	 * @see Game.hasCastlingRights(Color color, Side side)
	 */
	public boolean whiteCanCastleKingside() {
		return whiteCanCastleKingside;
	}
	
	/**
	 * Sets the kingside castling rights for White.
	 * @param whiteCanCastleKingside The kingside castling
	 * rights to set for White.
	 * @see Game.setCastlingRights(Color color, Side side, boolean canCastle)
	 */
	public void setWhiteCanCastleKingside(boolean whiteCanCastleKingside) {
		this.whiteCanCastleKingside = whiteCanCastleKingside;
	}
	
	/**
	 * Returns whether White has queenside castling rights.
	 * @return true if White has queenside castling rights.
	 * false otherwise.
	 * @see Game.hasCastlingRights(Color color, Side side)
	 */
	public boolean whiteCanCastleQueenside() {
		return whiteCanCastleQueenside;
	}
	
	/**
	 * Sets the queenside castling rights for White.
	 * @param whiteCanCastleQueenside The queenside castling
	 * rights to set for White.
	 * @see Game.setCastlingRights(Color color, Side side, boolean canCastle)
	 */
	public void setWhiteCanCastleQueenside(boolean whiteCanCastleQueenside) {
		this.whiteCanCastleQueenside = whiteCanCastleQueenside;
	}
	
	/**
	 * Returns whether Black has kingside castling rights.
	 * @return true if Black has kingside castling rights.
	 * false otherwise.
	 * @see Game.hasCastlingRights(Color color, Side side)
	 */
	public boolean blackCanCastleKingside() {
		return blackCanCastleKingside;
	}
	
	/**
	 * Sets the kingside castling rights for Black.
	 * @param blackCanCastleKingside The kingside castling
	 * rights to set for Black.
	 * @see Game.setCastlingRights(Color color, Side side, boolean canCastle)
	 */
	public void setBlackCanCastleKingside(boolean blackCanCastleKingside) {
		this.blackCanCastleKingside = blackCanCastleKingside;
	}
	
	/**
	 * Returns whether Black has queenside castling rights.
	 * @return true if Black has queenside castling rights.
	 * false otherwise.
	 * @see Game.hasCastlingRights(Color color, Side side)
	 */
	public boolean blackCanCastleQueenside() {
		return blackCanCastleQueenside;
	}
	
	/**
	 * Sets the queenside castling rights for Black.
	 * @param blackCanCastleQueenside The queenside castling
	 * rights to set for Black.
	 * @see Game.setCastlingRights(Color color, Side side, boolean canCastle)
	 */
	public void setBlackCanCastleQueenside(boolean blackCanCastleQueenside) {
		this.blackCanCastleQueenside = blackCanCastleQueenside;
	}
	
	/**
	 * This method is a generalization of the four methods
	 * that return whether or not a side has castling rights
	 * for a side of the board.
	 * 
	 * A king's castling rights are revoked if:
	 * 1. The king is moved (even if it is moved back to
	 * its original place).
	 * 2. The rook from the side of the board to castle is
	 * moved (even if it is moved back to its original place)
	 * or the rook is captured.
	 * @param color The color whose castling rights are
	 * being checked.
	 * @param side The side of the board the king wishes to
	 * castle on.
	 * @return true if the king of the given color has
	 * castling rights for the given side.
	 */
	public boolean hasCastlingRights(Color color, Side side) {
		if(color == WHITE) {
			if(side == KINGSIDE) {
				return this.whiteCanCastleKingside();
			} else if(side == QUEENSIDE) {
				return this.whiteCanCastleQueenside();
			}
		} else if(color == BLACK) {
			if(side == KINGSIDE) {
				return this.blackCanCastleKingside();
			} else if(side == QUEENSIDE) {
				return this.blackCanCastleQueenside();
			}
		}
		// TODO throw exception
		return false;
	}
	
	/**
	 * This method is a generalization of the four methods that
	 * set castling rights.
	 * 
	 * A king's castling rights are revoked if:
	 * 1. The king is moved (even if it is moved back to
	 * its original place).
	 * 2. The rook from the side of the board to castle is
	 * moved (even if it is moved back to its original place)
	 * or the rook is captured.
	 * @param color The color whose castling rights are being set.
	 * @param side The side in which castling rights are being set.
	 * @param canCastle Whether or not the given color has
	 * the right to castle on the given side.
	 */
	public void setCastlingRights(Color color, Side side, boolean canCastle) {
		if(color == WHITE && side == KINGSIDE) {
			this.setWhiteCanCastleKingside(canCastle);
		} else if(color == WHITE && side == QUEENSIDE) {
			this.setWhiteCanCastleQueenside(canCastle);
		} else if(color == BLACK && side == KINGSIDE) {
			this.setBlackCanCastleKingside(canCastle);
		} else if(color == BLACK && side == QUEENSIDE) {
			this.setBlackCanCastleQueenside(canCastle);
		} else {
			// TODO throw exception
		}
	}
	
	/**
	 * Gets the half-move counter for the game.
	 * 
	 * The half-move counter represents the number of half-moves
	 * (a whole move is one from both sides) since the last pawn
	 * advance or piece capture.
	 * @return The half-move counter for the game.
	 */
	public int getHalfMoveCounter() {
		return halfMoveCounter;
	}
	
	/**
	 * Sets the half-move counter for the game.
	 * 
	 * The half-move counter represents the number of half-moves
	 * (a whole move is one from both sides) since the last pawn
	 * advance or piece capture.
	 * @param halfMoveCounter The half-move counter to set.
	 */
	public void setHalfMoveCounter(int halfMoveCounter) {
		this.halfMoveCounter = halfMoveCounter;
	}
	
	/**
	 * Gets the full-move counter for the game.
	 * 
	 * The full-move counter is simply the number of full-moves
	 * (one for white, followed by one for black)
	 * that have been played over the course of the game.
	 * @return The full-move counter for the game.
	 */
	public int getFullMoveCounter() {
		return fullMoveCounter;
	}
	
	/**
	 * Sets the full-move counter for the game.
	 * 
	 * The full-move counter is simply the number of full-moves
	 * (one for white, followed by one for black)
	 * that have been played over the course of the game.
	 * @param fullMoveCounter The full-move counter to set.
	 */
	public void setFullMoveCounter(int fullMoveCounter) {
		this.fullMoveCounter = fullMoveCounter;
	}
	
	/**
	 * Gets the Coordinate of the en passant target square.
	 * Returns null if there is no en passant target square.
	 * @return The Coordinate of the en passant target square.
	 * null if there is no en passant target square.
	 */
	public Coordinate getEnPassantTargetSquare() {
		return enPassantTargetSquare;
	}
	
	/**
	 * Sets the en passant target square.
	 * @param enPassantTargetSquare The en passant target square
	 * to set. null if there is no en passant target square.
	 */
	public void setEnPassantTargetSquare(Coordinate enPassantTargetSquare) {
		this.enPassantTargetSquare = enPassantTargetSquare;
	}
	
	/**
	 * Gets the name of the player with the white pieces.
	 * @return The name of the player with the white pieces.
	 */
	public String getWhiteName() {
		return whiteName;
	}
	
	/**
	 * Sets the name of the player with the white pieces.
	 * @param whiteName The name of the player with the
	 * white pieces to set.
	 */
	public void setWhiteName(String whiteName) {
		this.whiteName = whiteName;
	}
	
	/**
	 * Gets the name of the player with the black pieces.
	 * @return The name of the player with the black pieces.
	 */
	public String getBlackName() {
		return blackName;
	}
	
	/**
	 * Sets the name of the player with the black pieces.
	 * @param blackName The name of the player with the
	 * black pieces to set.
	 */
	public void setBlackName(String blackName) {
		this.blackName = blackName;
	}

	/**
	 * Gets the stack of previous FENs which represent
	 * the previous positions of the game.
	 * @return The stack of previous FENs which represent
	 * the previous positions of the game.
	 * @see Game.getFEN()
	 */
	public Stack<String> getPreviousFenStack() {
		return previousFenStack;
	}
	
	/**
	 * Sets the stack of previous FENs which represent
	 * the previous positions of the game.
	 * @param previousFenStack The stack of previous FENs which
	 * represent the previous positions of the game to set.
	 * @see Game.getFEN()
	 */
	public void setPreviousFenStack(Stack<String> previousFenStack) {
		this.previousFenStack = previousFenStack;
	}
	
	/**
	 * Gets the map of partial FEN strings (which are relevant
	 * to the threefold repetition rule) to the number of
	 * occurrences found within the game.
	 * @return The map of partial FEN strings (which are relevant
	 * to the threefold repetition rule) to the number of
	 * occurrences found within the game.
	 * @see Game.getFenFor3FoldRepetition()
	 */
	public Map<String, Integer> getPreviousFensFor3FoldRepetition() {
		return previousFensFor3FoldRepetition;
	}
	
	/**
	 * Sets the map of partial FEN strings (which are relevant
	 * to the threefold repetition rule) to the number of
	 * occurrences found within the game.
	 * @param previousFensFor3FoldRepetition The map of partial
	 * FEN strings (which are relevant to the threefold repetition
	 * rule) to the number of occurrences found within the game.
	 * @see Game.getFenFor3FoldRepetition()
	 */
	public void setPreviousFensFor3FoldRepetition(Map<String, Integer> previousFensFor3FoldRepetition) {
		this.previousFensFor3FoldRepetition = previousFensFor3FoldRepetition;
	}
}

