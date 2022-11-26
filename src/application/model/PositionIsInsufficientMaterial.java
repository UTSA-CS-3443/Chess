package application.model;

public class PositionIsInsufficientMaterial extends IllegalPositionException {
	
	public PositionIsInsufficientMaterial() {
		super("Position is in Checkmate");
	}
	
	public PositionIsInsufficientMaterial(String message) {
		super(message);
	}

}
