package application.model;

public abstract class Piece implements Movable {
	
	// attributes
	public enum pColor { pWHITE, pBLACK };
	private pColor color; 
	
	/**
	 * Constructs piece of color
	 * 
	 * @param 
	 */
	public Piece(pColor color) {
		
	}
	
}
