package application.model;

public abstract class IllegalPositionException extends IllegalArgumentException {
	
	public IllegalPositionException() {
		super("Illegal position");
	}
	
	public IllegalPositionException(String message) {
		super(message);
	}

}
