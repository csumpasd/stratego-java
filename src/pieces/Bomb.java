package pieces;

import board.Field;
import helper.InvalidMoveException;

public class Bomb extends Piece {
    public Bomb(boolean isLightTeam) {
        super(isLightTeam, 0);
        this.maxMovement = 0;
    }

    public void move(Field chosen) throws InvalidMoveException {
        throw new InvalidMoveException();
    }

    public int steppedOn(Piece by) {

        this.revealed = true;

        // we both die
        field.remove();
        return 2;

    }
}
