# Chess
A simple, lightweight, and easy-to-use chess desktop application. Allows support for a two-player game of chess with loading from a FEN string.

## Special Rules Implemented
- En Passant Capture
- Castling
- Pawn Promotion
- Fifty-Move Rule
- Threefold Repetition Draw
- Stalemate Draw

## UML
UML is located within the GITHUB File system

## Documentation
#### Controllers
ChessBoardController: Controls ChessBoard.fxml view

MainController: Controls Main.fxml view

#### Model
##### Game
The main driver of program logic.

Significant Methods:
- isLegalPosition(): Detects if the game was set up in a legal position
- getPseudoLegalMoves(PieceColor color): Return the list of pseudo legal moves for a player
- getLegalMoves(): Return the lsit of legal moves for this position
- isLegalMove(Move move): Returns whether or not a move is legal
- pushMove(Move move): Puts a legal move onto the board
- popMove(): Undoes most recent move
- setPieceAt(Coordinate coordinate, Piece piece): Changes piece at a coordinate
- getPieceAt(Coordinate coordinate): Gets the piece at a coordinate
- isCheckmate(): Returns if current position is checkmate
- isDraw(): Returns if game is a draw (stalemate, insufficient material, 50 move rule, 3fold repetition)
- loadFromFEN(String fen): Sets this game's FEN to FEN passed
- getFEN(): Returns this game's current FEN

##### Move
A wrapper around two coordinates.

Significant Methods:
- equals(Move other): Returns if *other* is equal to this Move
- getRowDifference(): Returns the difference between source and destination rows
- getColDifference(): Returns the difference between source and destination columns
- getFromCoordinate(): Returns the source coordinate
- getToCoordinate(): Returns the destination coordinate

##### MoveGenerator
An abstract class that defines static constants for each piece type moves.
**Classes that extend MoveGenerator:**
- Rider: Pieces that "ride" in one direction until they hit another piece or a bound
- Hopper: Pieces that may move regardless of other piece in their path

##### Movable
An interface that provides functionality to objects on a board that can be moved (i.e., pieces).

Method to implement:
- getPseudoLegalMoves(Game game, Coordinate coordinate): Returns a List of Moves that the piece may possibly make (legal and illegal)
- getLegalMoves(Game game, Coordinate coordinate): Returns a List of Moves that the piece is *actually* allowed to make

##### Piece
An enumeration that implements the Movable interface.

Significant Methods:
- isAlliedWith(Piece other): Returns whether this peice is the same color as *other*
- getPseudoLegalMoves(Game game, Coordinate coordinate): Returns a List of pseudo legal moves
- getLegalMoves(Game game, Coordinate coordinate): Returns a List of legal moves

##### Coordinate
A wrapper a around a pair (row/column) of a square.

Significant Methods:
- isInBounds(): Returns whether this Coordinate is within bounds of chess board
- getAlgebraicNotation(): Returns the algebraic notation of this Coordinate as a string
- offset(int dRow, int dCol): Returns a new Coordinate based on a row and column offset from this Coordinate

##### Other Enumeration Classes
- PieceColor: The color (side) of a piece
- Side: The side of the board, i.e., (king/queen)side

##### Exception Classes
Exception classes are used for communicating if a FEN entered through the GUI is invalid.
- abstract IllegalPositionException
	- PositionIsCheckmateException
	- PositionIsDrawException
	- PositionIsInsufficientMaterialException