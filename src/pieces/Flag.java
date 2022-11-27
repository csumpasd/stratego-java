package pieces;

import board.Field;
import helper.InvalidMoveException;

public class Flag extends Piece {
    public Flag(boolean isLightTeam) {
        super(isLightTeam, -1);
        this.maxMovement = 0;
    }

    public void move(Field chosen) throws InvalidMoveException {
        throw new InvalidMoveException();
    }

    public int steppedOn(Piece by) {

        this.field.getBoard().win(by);

        // they win
        field.remove();
        return 1;

    }
}
