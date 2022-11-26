package application.model;

public class PositionIsCheckmateException extends IllegalPositionException{

	public PositionIsCheckmateException() {
		super("Position is in Checkmate");
	}
	
	public PositionIsCheckmateException(String message) {
		super(message);
	}
}
