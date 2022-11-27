package board;

import pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Field extends JComponent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Board board;
    private final Point pos;

    private final int color;

    private Piece piece;
    private boolean display;

    public Field(Board b, Point p, int c) {
        this.board = b;
        this.pos = p;
        this.color = c;
        this.display = true;

        this.setBorder(BorderFactory.createEmptyBorder());
    }

    public Piece getPiece() {
        return piece;
    }

    public Point getPos() {
        return pos;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isEmpty() {
        return (this.piece == null);
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public void accept(Piece p) {
        this.piece = p;
        p.setField(this);
    }

    public Piece remove() {
        Piece p = this.piece;
        this.piece = null;
        return p;
    }

    public void paintComponent(Graphics g, boolean highlight) {
        super.paintComponent(g);

        if (!isEmpty() && display) {
            piece.draw(g);
        }

        if (highlight) {
            if (isEmpty()) {
                if (color == 0) {
                    g.setColor(new Color(120, 240, 120));
                } else {
                    g.setColor(new Color(100, 200, 100));
                }
            } else {
                if (color == 0) {
                    g.setColor(new Color(240, 120, 120));
                } else {
                    g.setColor(new Color(200, 100, 100));
                }
            }
        } else {
            boolean inPonds = (pos.x >= 2 && pos.x <= 3 && pos.y >= 4 && pos.y <= 5) || (pos.x >= 6 && pos.x <= 7 && pos.y >= 4 && pos.y <= 5);
            if (inPonds) {
                if (color == 0) {
                    g.setColor(new Color(120, 120, 120));
                } else {
                    g.setColor(new Color(100, 100, 100));
                }
            } else {
                if (color == 0) {
                    g.setColor(new Color(240, 240, 240));
                } else {
                    g.setColor(new Color(200, 200, 200));
                }
            }

        }

        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        if(!isEmpty() && display) {
            piece.draw(g);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return pos.equals(field.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos);
    }
}
