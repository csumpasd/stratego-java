import board.Board;
import board.Field;
import helper.InvalidMoveException;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;
import pieces.Piece;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the Board, Field, and Piece classes
 */
class AllTests {
    Board board;

    /**
     * Creates a board to do all the tests on, and sets it to the playing state
     */
    @BeforeClass
    void setUp() {
        board = new Board(null);
        board.setGameStage(0);
    }


    /**
     * Copies a piece, moves it, and compares it to the copy
     */
    @Test
    void move() {
        Piece originalPiece = board.getBoard()[6][0].getPiece();

        try {
            board.getBoard()[6][0].getPiece().move(board.getBoard()[5][0]);
        } catch (InvalidMoveException ignored) {}
        assertEquals(board.getBoard()[5][0].getPiece().getStrength(), originalPiece.getStrength());
    }

    /**
     * Kills a piece and tests if it died
     */
    @Test
    void steppedOn() {
        board.getBoard()[3][1].getPiece().steppedOn(board.getBoard()[6][1].getPiece());
        assertTrue(board.getBoard()[3][1].isEmpty());
    }

    /**
     * Finds the list of valid moves of a piece which can't move, and tests if the list is empty
     */
    @Test
    void findValidMoves() {
        Vector<Field> validMove = board.getBoard()[2][6].getPiece().findValidMoves(board);
        assertTrue(validMove.isEmpty());
    }

    /**
     * Gets an image from resources, and tests if a piece which should have that image actually does
     */
    @Test
    void getImg() {
        BufferedImage expected = null;
        try {
             expected = ImageIO.read(Objects.requireNonNull(getClass().getResource("/drk-5.png")));
        } catch (IOException ignored) {}
        assertEquals(board.getBoard()[2][7].getPiece().getImg(), expected);
    }


    /**
     * Adds a piece to a field which was empty, and tests if it still is
     */
    @Test
    void accept() {
        board.getBoard()[4][4].accept(new Piece(false, 7));
        assertFalse(board.getBoard()[4][4].isEmpty());
    }

    /**
     * Removes a piece from a field, and tests if it's now empty
     */
    @Test
    void remove() {
        board.getBoard()[0][0].remove();
        assertTrue(board.getBoard()[0][0].isEmpty());
    }

    /**
     * Gets the piece on a field, and tests if it's strength is correct
     */
    @Test
    void getPiece() {
        Piece p = board.getBoard()[0][1].getPiece();
        assertEquals(p.getStrength(),0);
    }


    /**
     * Tests the canStepHere() method on a field in one of the ponds
     */
    @Test
    void canStepHere() {
        boolean canStepHere = board.canStepHere(new Point(3,4), false);
        assertFalse(canStepHere);
    }

    /**
     * Makes the light team win, and tests if the game moved on to the win game state.
     */
    @Test
    void win() {
        board.win(board.getBoard()[9][9].getPiece());
        assertEquals(board.getGameStage(), 1);
    }

    /**
     * Gets board, and tests if a known field is correctly empty
     */
    @Test
    void getBoard() {
        Field[][] b = board.getBoard();
        assertFalse(b[9][0].isEmpty());
    }

}