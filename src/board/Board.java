package board;

import java.awt.Point;

import pieces.*;

public class Board {
    private Piece[][] board;
    private Piece selected;

    public Board() {
        this.board = new Piece[10][10];
    }

    /**
     * Checks whether a piece could move to a given point.
     * <p>
     *     First checks if the point is on the board,
     *     then makes sure it's not in either pond,
     *     then finally checks if there's a teammate there.
     * </p>
     * @param p Point with x,y coordinates
     * @return true if valid, false otherwise
     */
    public boolean canStepHere(Point p, int team) {
        int x = p.x;
        int y = p.y;

        boolean onMap = (x >= 0 && x <= 9 && y >= 0 && y <= 9);
        boolean notInPonds = !(x >= 2 && x <= 3 && y >= 4 && y <= 5) && !(x >= 6 && x <= 7 && y >= 4 && y <= 5);

        boolean noTeammateHere = true;
        if (onMap && board[x][y] != null) { noTeammateHere = (team != board[x][y].getTeam()); }

        return onMap && notInPonds && noTeammateHere;
    }

    public Piece getSelected() {
        return selected;
    }

    public void setSelected(Piece selected) {
        this.selected = selected;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setBoard(Piece[][] board) {
        this.board = board;
    }
}
