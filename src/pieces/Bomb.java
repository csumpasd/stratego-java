package pieces;

import board.Field;

/**
 * Special piece, removes whoever steps on it, and then gets removed itself
 */
public class Bomb extends Piece {

    /**
     * Constructs a bomb, makes sure it can't move and has the correct strength
     * @param isLightTeam Whether the new bomb is on the light team or not
     */
    public Bomb(boolean isLightTeam) {

        super(isLightTeam, 0);
        this.maxMovement = 0;

    }

    /**
     * The bomb doesn't move
     * @param chosen The chosen bomb to move to, ignored
     */
    @Override
    public void move(Field chosen) {}

    /**
     * Removes itself and the piece stepping on it
     * @param by The piece that is trying to move here
     * @return 2, as to tell the other piece to remove itself
     */
    @Override
    public int steppedOn(Piece by) {

        // tells the other piece to move here
        field.remove();
        return 2;

    }


}
