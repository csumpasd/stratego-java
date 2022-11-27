package pieces;

public class Scout extends Piece {
    public Scout(boolean isLightTeam) {
        super(isLightTeam, 2);
        this.maxMovement = 10;
    }
}
