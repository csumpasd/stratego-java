package pieces;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

import board.*;
import helper.*;

/**
 * Implements the basic piece in the game
 * <p>
 *     These are what stand on the fields which make up the board, they are generated at the start of the game and only moved and destroyed afterwards.
 * </p>
 * <p>
 *     Special pieces are extensions of this class, they mostly just overwrite the move() or steppedOn() methods;
 * </p>
 */
public class Piece implements Serializable {
    /**
     * Makes sure serialization doesn't break
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The field the piece is standing on
     */
    protected Field field;

    /**
     * True if the piece is part of the Light team, false if it's part of the Dark team
     */
    protected final boolean isLightTeam;

    /**
     * Goes from -1 to 10, mostly just helps figure out which piece takes which in a conflict
     */
    protected final int strength;

    /**
     * Max move distance of the piece for checking the valid spots to move to
     */
    protected int maxMovement;

    /**
     * Stores if the other team sees what piece it is or not
     */
    protected boolean revealed;

    /**
     * Image to render when the type of the piece is shown
     */
    transient BufferedImage shown;

    /**
     * Image to render when the type of the piece is hidden
     */
    transient BufferedImage hidden;


    /**
     * Constructs a piece: loads required images based on strength and initializes values
     * @param isLightTeam Whether the new piece is on the light team or not
     * @param strength The strength of the new piece
     */
    public Piece(boolean isLightTeam, int strength) {
        this.field = null;
        this.isLightTeam = isLightTeam;
        this.strength = strength;
        this.maxMovement = 1;
        this.revealed = false;

        try {
            this.shown = ImageIO.read(Objects.requireNonNull(getClass().getResource("/" + (isLightTeam ? "lgt-" : "drk-") + strength + ".png")));
            this.hidden = ImageIO.read(Objects.requireNonNull(getClass().getResource("/" + (isLightTeam ? "lgt-" : "drk-") + "unknown.png")));
        } catch (IOException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    /**
     * Tries to move to the chosen field
     * <p>
     *     Does this by finding the list of valid moves for the current piece, testing if the chosen field is in that list, and if so, moving the piece there.
     * </p>
     * <p>
     *     Also tests for conflicts, and if there is one, calls the conflicting piece's steppedOn() method. This might result in the piece not being able to move to the desired spot, or the piece getting removed.
     * </p>
     * @param chosen The chosen field to move to
     * @throws InvalidMoveException If the piece can't move to the chosen spot
     */
    public void move(Field chosen) throws InvalidMoveException {

        // makes a list of all the valid moves for the current piece
        Vector<Field> validMoves = findValidMoves(field.getBoard());

        // breaks execution if the chosen move is invalid, so after this point we can assume it is
        if (!validMoves.contains(chosen)) { throw new InvalidMoveException(); }

        // if there's a conflict between the current piece and an enemy one, then
        if (chosen.getPiece() != null) {

            // then sets the piece as revealed, and then
            this.revealed = true;
            Piece conflicting = chosen.getPiece();
            switch (conflicting.steppedOn(this)) {
                case 0: // either does nothing
                    return;
                case 1: // or moves the piece because it was stronger than the enemy
                    chosen.accept(field.remove());
                    return;
                case 2: // or removes the piece, for example because it was of equal power
                    field.remove();
                    return;
            }

        }

        // in case of no conflict, moves the piece to the chosen spot
        chosen.accept(field.remove());
    }

    /**
     * Handles being stepped on, that is if a piece tries to move to this piece's location
     * @param by The piece that is trying to move here
     * @return A coded signal of what the other piece should do, explained in move()
     */
    public int steppedOn(Piece by) {

        // sets the piece as revealed
        this.revealed = true;

        // if the enemy piece is stronger, removes this piece and tells them to move here
        if (strength < by.getStrength()) {
            field.remove();
            return 1;

        }

        // if the two pieces' strength is equal, removes this one, and tells the other to remove itself too
        if (strength == by.getStrength()) {
            field.remove();
            return 2;
        }

        // and if the other piece was weaker, tells them to not move here
        return 0;

    }

    /**
     * Getter for isLightTeam
     * @return Whether the piece is on the Light team
     */
    public boolean isLightTeam() {
        return isLightTeam;
    }

    /**
     * Getter for strength
     * @return The piece's strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Getter for maxMovement
     * @return The piece's max movement distance;
     */
    public int getMaxMovement() {
        return maxMovement;
    }

    /**
     * Getter for shown
     * @return The piece's image when shown
     */
    public BufferedImage getImg() {
        return shown;
    }

    /**
     * Getter for field
     * @return The field the piece is standing on
     */
    public Field getField() {
        return field;
    }

    /**
     * Setter for field
     * @param field The new field to stand on
     */
    public void setField(Field field) {
        this.field = field;
    }

    /**
     * Setter for revealed
     * @param revealed If the piece should be revealed or not
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    /**
     * Graphics method for drawing the piece
     * @param g The graphics doing the drawing
     */
    public void draw(Graphics g) {

        // gets the pixel position of the field the piece is standing on, this is where the piece will get drawn
        int x = field.getX();
        int y = field.getY();

        // if the turn has started, and the piece should be otherwise shown, then draw it so
        boolean shouldShow = field.getBoard().hasStartedTurn() && ((field.getBoard().isLightTurn() == isLightTeam) || revealed);
        g.drawImage(shouldShow ? this.shown : this.hidden, x, y, null);

    }

    public Vector<Field> findValidMoves(Board b) {
        Vector<Field> validMoves = new Vector<>();

        // uses some ~lambda~ to run the tester function in all directions
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

    private Vector<Field> testForValidMovesInDirection(PointShiftFunction direction, Board b) {
        Vector<Field> validMovesInThisDirection = new Vector<>();
        boolean found = false;
        for (int i = 1; i <= maxMovement; i++) {
            Point tested = new Point(direction.shiftPoint(field.getPos().x,field.getPos().y,i));
            if (b.canStepHere(tested, isLightTeam) && !found) {
                validMovesInThisDirection.add(b.getBoard()[tested.y][tested.x]);
                if (!b.getBoard()[tested.y][tested.x].isEmpty()) { return validMovesInThisDirection; }
            } else { found = true; }
        }
        return validMovesInThisDirection;
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.shown = ImageIO.read(Objects.requireNonNull(getClass().getResource("/" + (isLightTeam ? "lgt-" : "drk-") + strength + ".png")));
        this.hidden = ImageIO.read(Objects.requireNonNull(getClass().getResource("/" + (isLightTeam ? "lgt-" : "drk-") + "unknown.png")));
    }

}
