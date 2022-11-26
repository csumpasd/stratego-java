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


    public void move(Point chosen, Board b) throws InvalidMoveException {

        // make a list of all the valid moves for the current piece
        Vector<Point> validMoves = findValidMoves(b);

        // if the chosen move is in the list, mark it as so
        boolean chosenValidMove = false;
        for (Point tested : validMoves) {
            if (tested == chosen) {
                chosenValidMove = true;
                break;
            }
        }

        // this breaks execution if the chosen move was invalid, so after this point we can assume it is
        if (!chosenValidMove) { throw new InvalidMoveException(); }

        Piece[][] boardCopy = b.getBoard();

        // conflict between our piece and an enemy one, if the piece we stepped on returns true we die, otherwise we do nothing
        if (b.getBoard()[chosen.x][chosen.y] != null) {

            Piece conflicting = b.getBoard()[chosen.x][chosen.y];
            if (conflicting.steppedOn(this, b)) {
                boardCopy[coords.x][coords.y] = null;
                b.setBoard(boardCopy);
            }

            // todo set piece to be shown to enemy in recap/now?
            return;
        }

        // if there was no conflict, move to the chosen spot
        boardCopy[coords.x][coords.y] = null;
        boardCopy[chosen.x][chosen.y] = this;
        b.setBoard(boardCopy);

    }

    public boolean steppedOn(Piece by, Board b) {

        // todo set piece to be shown to enemy in recap/now?
        Piece[][] boardCopy = b.getBoard();

        // only we die, return false
        if (strength < by.getStrength()) {
            boardCopy[coords.x][coords.y] = null;
            b.setBoard(boardCopy);
            return false;

        }
        // we both die, return true
        if (strength == by.getStrength()) {
            boardCopy[coords.x][coords.y] = null;
            b.setBoard(boardCopy);
            return true;
        }

        // we were stronger, nobody dies
        return false;

    }

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

    private Vector<Point> findValidMoves(Board b) {
        Vector<Point> validMoves = new Vector<>();

        PointShiftFunction xPlus = (x, y, i) -> new Point(x+i,y);
        PointShiftFunction xMinus = (x, y, i) -> new Point(x-i,y);
        PointShiftFunction yPlus = (x, y, i) -> new Point(x,y+i);
        PointShiftFunction yMinus = (x, y, i) -> new Point(x,y-i);

        validMoves.addAll(testForValidMovesInDirection(xPlus, b));
        validMoves.addAll(testForValidMovesInDirection(xMinus, b));
        validMoves.addAll(testForValidMovesInDirection(yPlus, b));
        validMoves.addAll(testForValidMovesInDirection(yMinus, b));

        return validMoves;
    }

    private Vector<Point> testForValidMovesInDirection(PointShiftFunction direction, Board b) {
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
