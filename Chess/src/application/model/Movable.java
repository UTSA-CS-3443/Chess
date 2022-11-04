package application.model;

public interface Movable {
	Move getPseudoLegalMoves();
	Move getLegalMoves();
}
