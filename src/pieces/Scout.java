package pieces;

/**
 * Special piece, doesn't have a movement cap
 */
public class Scout extends Piece {

    /**
     * Constructs a scout, makes sure it can move as much as the board is large
     * @param isLightTeam Whether the new scout is on the light team or not
     */
    public Scout(boolean isLightTeam) {

        super(isLightTeam, 2);
        this.maxMovement = 10;

    }


}
