package board;

import pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Implements the field that make up the board
 * <p>
 *     These are the places where pieces can stand, they are fix and are colored in a chessboard pattern
 * </p>
 */
public class Field extends JComponent implements Serializable {

    /**
     * Makes sure serialization doesn't break
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The board the field is on
     */
    private final Board board;

    /**
     * The position of the field on the board (0..9)
     */
    private final Point pos;

    /**
     * The color of the field, true if light
     */
    private final boolean color;

    /**
     * The piece standing on the field, if any
     */
    private Piece piece;

    /**
     * Whether to display the piece on the field, used when dragging pieces
     */
    private boolean display;

    /**
     * Constructs a field
     * @param b The board on which the field is
     * @param p The coordinates of the new field
     * @param c The color of the new field
     */
    public Field(Board b, Point p, boolean c) {

        this.board = b;
        this.pos = p;
        this.color = c;
        this.display = true;

        this.setBorder(BorderFactory.createEmptyBorder());

    }

    /**
     * Getter for piece
     * @return The piece on the field, if any
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Getter for pos
     * @return The position of the field on the board
     */
    public Point getPos() {
        return pos;
    }

    /**
     * Getter for board
     * @return The board the field is on
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets whether piece is null
     * @return Whether there's a piece on the field
     */
    public boolean isEmpty() {
        return (this.piece == null);
    }

    /**
     * Setter for display
     * @param display Whether to display the piece
     */
    public void setDisplay(boolean display) {
        this.display = display;
    }

    /**
     * Puts a piece on this field
     * @param p The piece to put here
     */
    public void accept(Piece p) {
        this.piece = p;
        p.setField(this);
    }

    /**
     * Removes the piece from this field
     * @return The removed piece
     */
    public Piece remove() {
        Piece p = this.piece;
        this.piece = null;
        return p;
    }

    /**
     * Graphics method, draws the field and the piece on it
     * @param g The graphics object doing the drawing
     * @param highlight Whether to highlight this field or not
     */
    public void paintComponent(Graphics g, boolean highlight) {

        super.paintComponent(g);

        // sets the appropriate color to draw the field with
        boolean inPonds = (pos.x >= 2 && pos.x <= 3 && pos.y >= 4 && pos.y <= 5) || (pos.x >= 6 && pos.x <= 7 && pos.y >= 4 && pos.y <= 5);
        if (color) {

            g.setColor(new Color(120, 120, 120));
            if (!inPonds)  { g.setColor(new Color(240, 240, 240)); }
            if (highlight) { g.setColor(new Color(120, 240, 120)); }
            if (!isEmpty() && highlight) { g.setColor(new Color(240, 120, 120)); }

        } else {

            g.setColor(new Color(100, 100, 100));
            if (!inPonds)  { g.setColor(new Color(200, 200, 200)); }
            if (highlight) { g.setColor(new Color(100, 200, 100)); }
            if (!isEmpty() && highlight) { g.setColor(new Color(200, 100, 100)); }

        }

        // draws the field
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        // draws the piece if there's one here, and is displayed
        if (!isEmpty() && display) {
            piece.draw(g);
        }

    }

    /**
     * Changes the default equals method to only care about position
     * @param o The other field
     * @return Whether it is the same field
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return pos.equals(field.pos);

    }

    /**
     * Changes the default hash method to only care about position, as that is unique in itself
     * @return The hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }


}
