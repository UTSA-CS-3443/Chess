package application.model;

public class Game {

	private String whiteName;
	private String blackName;
	private int fiftyMoveTurns; // turns since last capture or pawn moved
	private boolean canInvoke3FRep;
	
	/**
	 * 
	 */
	public Game() {
		
	}
	
	/**
	 * 
	 */
	public void changeTurn() {
		
	}
	
	/**
	 * 
	 */
	public void isCheckmate() {
		
	}
	
	/**
	 * 
	 */
	public void isDraw() {
		
	}
	
	/* Threefold Repetition
	 * 
	 * A player may claim a draw if the same position occurs three times during the game, https://en.wikipedia.org/wiki/Threefold_repetition
	 * 
	 * The game is not automatically drawn if a position occurs for the third time – one 
	 * of the players, on their turn, must claim the draw with the arbiter. The claim
	 * must be made either before making the move which will produce the third 
	 * repetition, or after the opponent has made a move producing a third repetition.
	 * 
	 * Variables that define "equal" positions:
	 * - Piece type (not specific individual piece)
	 * - Board square occupied
	 * - Player
	 * - Current:
	 * 		- Castling rights
	 * 		- Possibility to capture en passant
	 * 
	 * Implementation:
	 * - Store every unique (according to variables above) move
	 * - For non-unique moves increment a counter for the stored move it is equal to
	 * - For every move made:
	 * 		1. Check 'uniqueness'
	 * 		2. If unique
	 * 				- Add to list/array/etc
	 * 			else
	 * 				- If this move == 2
	 * 					- canInvoke3FRep = true
	 * 				  else
	 * 					- Increment move
	 * 		3. For every turn after this players can invoke 3fold repetition
	 * 
	 * Considerations:
	 * - Automatic?
	 * - If not, how to implement a player invoking rule *before* making qualifying move?
	 * 		- Player could spam it every turn for every move until a move qualifies. Does this even matter?
	 * 			- Time limit per turn would make this a waste of time, but what about late in the game with 
	 * 			  small # pieces (when someone is more likely to invoke to avoid loss)?
	 */
}
