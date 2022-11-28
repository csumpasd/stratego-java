package pieces;

import board.Field;

import java.util.Vector;

/**
 * Special piece, doesn't get removed from stepping on bombs
 */
public class Miner extends Piece {

    /**
     * Constructs a miner, makes sure it has the correct strength
     * @param isLightTeam Whether the new miner is on the light team or not
     */
    public Miner(boolean isLightTeam) {
        super(isLightTeam, 3);
    }

    /**
     * Normal movement, but in case of stepping on a bomb, the piece doesn't get removed
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
                    return;

                case 1: // or moves the piece because it was stronger than the enemy
                    chosen.accept(field.remove());
                    return;

                case 2: // or removes the piece, except if the conflicting one was a bomb
                    if (conflicting.strength == 0) {
                        chosen.accept(field.remove());
                    } else {
                        field.remove();
                    }
                    return;

            }

        }

        // in case of no conflict, moves the piece to the chosen spot
        chosen.accept(field.remove());

    }


}
