package pieces;

import board.Field;

import java.util.Vector;

/**
 * Special piece, is weaker than everyone else but can remove the marshal
 */
public class Spy extends Piece {

    /**
     * Constructs a spy, makes sure it can move as much as the board is large
     * @param isLightTeam Whether the new spy is on the light team or not
     */
    public Spy(boolean isLightTeam) {
        super(isLightTeam, 1);
    }

    /**
     * Normal movement, but in case of stepping on a marshal, removes it
     * @param chosen The chosen field to move to
     */
    @Override
    public void move(Field chosen) {

        // makes a list of all the valid moves for the current piece
        Vector<Field> validMoves = findValidMoves(field.getBoard());

        // breaks execution if the chosen move is invalid, so after this point we can assume it is
        if (!validMoves.contains(chosen)) { return; }

        // if there's a conflict between the current piece and an enemy one then
        if (chosen.getPiece() != null) {

            // sets the piece as revealed and then
            this.revealed = true;
            Piece conflicting = chosen.getPiece();
            switch (conflicting.steppedOn(this)) {

                case 0: // either does nothing
                    // or in case of stepping on a marshal, removes it and moves there
                    if (conflicting.strength == 10) {

                        conflicting.getField().remove();
                        chosen.accept(field.remove());

                    }
                    return;

                case 1: // or moves the piece because it was stronger than the enemy
                    chosen.accept(field.remove());
                    return;

                case 2: // or removes the piece, for example because it was of equal power
                    field.remove();
                    return;

            }

        }

        // in case of no conflict, move to the chosen spot
        chosen.accept(field.remove());

    }


}
