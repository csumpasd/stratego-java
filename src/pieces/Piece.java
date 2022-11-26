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


    public void move(Board b, Point chosen) throws InvalidMoveException {

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


        // conflict between our piece and an enemy one
        if (b.getBoard()[chosen.x][chosen.y] != null) {
            Piece[][] boardCopy = b.getBoard();
            Piece conflicting = b.getBoard()[chosen.x][chosen.y];
            int solution = conflicting.steppedOn(this);
            switch (solution) {
                // we get removed :<
                case -1:
                    boardCopy[coords.x][coords.y] = null;
                    b.setBoard(boardCopy);
                    break;
                // we don't do anything because the other piece is stronger
                case 0:
                    break;
                // success, we got them!
                case 1:
                    //step there
                    boardCopy[coords.x][coords.y] = null;
                    boardCopy[chosen.x][chosen.y] = this;
                    b.setBoard(boardCopy);
                    break;
                default:
                    break;
            }

        }

    }

    public int steppedOn(Piece by) {

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

    public int getMovedst() {
        return movedst;
    }


    private Vector<Point> findValidMoves(Board b) {
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
