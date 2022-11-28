package pieces;

import board.Field;

/**
 * Special piece, if stepped on, the game is won
 */
public class Flag extends Piece {

    /**
     * Constructs a flag, makes sure it can't move and has the correct strength
     * @param isLightTeam Whether the new flag is on the light team or not
     */
    public Flag(boolean isLightTeam) {

        super(isLightTeam, -1);
        this.maxMovement = 0;

    }

    /**
     * The flag doesn't move
     * @param chosen The chosen field to move to, ignored
     */
    @Override
    public void move(Field chosen) {}

    /**
     * Makes the team stepping on this flag win the game
     * @param by The piece that is trying to move here
     * @return 1, as to tell the other piece to move here
     */
    @Override
    public int steppedOn(Piece by) {

        // makes the team stepping on this flag win the game
        this.field.getBoard().win(by);

        // tells the other piece to move here
        field.remove();
        return 1;

    }


}
