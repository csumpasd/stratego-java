package pieces;

import java.awt.Point;
import java.util.*;

import board.*;
import helper.*;

public class Piece {
    private String name;
    private Point coords;
    private int team;

    private int strength;
    private int movedst;

    public Vector<Point> selected(Board b) {
        b.setSelected(this);

        Vector<Point> validMoves = new Vector<>();

        PointShiftFunction xPlus = (x, y, i) -> new Point(x+i,y);
        PointShiftFunction xMinus = (x, y, i) -> new Point(x-i,y);
        PointShiftFunction yPlus = (x, y, i) -> new Point(x,y+i);
        PointShiftFunction yMinus = (x, y, i) -> new Point(x,y-i);

        validMoves.addAll(testForValidMovesInDirection(b, xPlus));
        validMoves.addAll(testForValidMovesInDirection(b, xMinus));
        validMoves.addAll(testForValidMovesInDirection(b, yPlus));
        validMoves.addAll(testForValidMovesInDirection(b, yMinus));

        return validMoves;
    }

    public void move(Vector<Point> validMoves, Point chosen) throws InvalidMoveException {
        boolean chosenValidMove = false;
        for (Point tested : validMoves) {
            if (tested == chosen) {
                chosenValidMove = true;
                break;
            }
        }

        if (!chosenValidMove) { throw new InvalidMoveException(); }

        //if steps on guy
        //else just move there
    }
    public void steppedOn() {}



    public String getName() {
        return name;
    }

    public Point getCoords() {
        return coords;
    }

    public int getTeam() {
        return team;
    }

    public int getStrength() {
        return strength;
    }

    public int getMovedst() {
        return movedst;
    }



    private Vector<Point> testForValidMovesInDirection(Board b, PointShiftFunction direction) {
        Vector<Point> validMovesInThisDirection = new Vector<>();
        for (int i = 0; i <= movedst; i++) {
            Point tested = new Point(direction.shiftPoint(coords.x,coords.y,i));
            if (b.canStepHere(tested, team)) {
                validMovesInThisDirection.add(tested);
                if (b.getBoard()[tested.x][tested.y] != null) { return validMovesInThisDirection; }
            }
        }
        return validMovesInThisDirection;
    }


}
