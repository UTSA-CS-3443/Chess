package application.model;

public class PositionIsDrawException extends IllegalPositionException {

	public PositionIsDrawException() {
		super("Position is in Draw");
	}
	
	public PositionIsDrawException(String message) {
		super(message);
	}
	
}
