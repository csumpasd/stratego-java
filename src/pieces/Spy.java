package pieces;

import board.Field;
import helper.InvalidMoveException;

import java.util.Vector;

public class Spy extends Piece {
    public Spy(boolean isLightTeam) {
        super(isLightTeam, 1);
    }

    public void move(Field chosen) throws InvalidMoveException {

        // make a list of all the valid moves for the current piece
        Vector<Field> validMoves = findValidMoves(field.getBoard());

        // this breaks execution if the chosen move was invalid, so after this point we can assume it is
        if (!validMoves.contains(chosen)) { throw new InvalidMoveException(); }

        // conflict between our piece and an enemy one, if the piece we stepped on returns true we die, otherwise we do nothing
        if (chosen.getPiece() != null) {
            this.revealed = true;
            Piece conflicting = chosen.getPiece();
            switch (conflicting.steppedOn(this)) {
                case 0 -> {
                    // in case of stepping on a marshall, we win
                    if (conflicting.strength == 10) {
                        conflicting.getField().remove();
                        chosen.accept(field.remove());
                    }
                    return;
                }
                case 1 -> {
                    chosen.accept(field.remove());
                    return;
                }
                case 2 -> {
                    field.remove();
                    return;
                }
            }
        }
        // in case of no conflict, move to the chosen spot
        chosen.accept(field.remove());
    }
}
