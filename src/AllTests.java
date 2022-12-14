import board.Board;
import board.Field;
import pieces.Piece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for the Board, Field, and Piece classes
 */
class AllTests {

    /**
     * Board to do all the tests on
     */
    Board board;

    /**
     * Creates the board, and sets it to the playing state
     */
    @BeforeEach
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

        board.getBoard()[6][0].getPiece().move(board.getBoard()[5][0]);

        assertEquals(originalPiece.getStrength(), board.getBoard()[5][0].getPiece().getStrength());

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
     * Tests if the image is properly given back
     */
    @Test
    void getImg() {

        assertNotNull(board.getBoard()[2][7].getPiece().getImg());

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
        assertEquals(1, board.getGameStage());

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