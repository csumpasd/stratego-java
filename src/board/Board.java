package board;

import gui.GameWindow;
import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serial;
import java.io.Serializable;
import java.util.Vector;

/**
 * Implements the board as an extension to a JPanel
 * <p>
 *     Houses the fields on which the pieces stand
 * </p>
 * <p>
 *     Also handles mouse events and all that :)
 * </p>
 */
public class Board extends JPanel implements Serializable, MouseListener, MouseMotionListener {

    /**
     * Makes sure serialization doesn't break
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The fields on the board
     */
    private final Field[][] board;

    /**
     * The window in which the board is
     */
    private GameWindow gameWindow;

    /**
     * Whether it is Light's turn
     */
    private boolean lightTurn;

    /**
     * The piece that's "picked up" by the cursor
     */
    private Piece currPiece;

    /**
     * The position of the cursor, offset to draw the "picked up" piece centered under the cursor
     */
    private Point currPos;

    /**
     * The current game stage, -1 if players are setting up, 0 if playing, and 1 if somebody has won
     */
    private int gameStage;

    /**
     * Whether the current player has started their turn by clicking on either pond
     */
    private boolean startedTurn;

    /**
     * Constructs the board, places the fields and pieces, and sets some values
     * @param g The window in which the board will be
     */
    public Board(GameWindow g) {

        this.gameWindow = g;
        this.board = new Field[10][10];
        this.gameStage = -1;
        this.startedTurn = true;

        setLayout(new GridLayout(10, 10, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // creates all the fields and adds them to the right location
        for (int y = 0; y <= 9; y++) {
            for (int x = 0; x <= 9; x++) {
                boolean color = ((x + y) % 2) == 0;
                board[y][x] = new Field(this, new Point(x,y), color);
                this.add(board[y][x]);
            }
        }

        this.setPreferredSize(new Dimension(600,600));
        this.setMinimumSize(this.getPreferredSize());
        this.setMaximumSize(this.getPreferredSize());
        this.setSize(this.getPreferredSize());

        lightTurn = true;

        placePieces();

        // after loading, sets everything correctly
        if (gameStage == 0) {
            startedTurn = false;
            if (gameWindow != null) gameWindow.setTitle("Stratego! - " + (lightTurn ? "Light" : "Dark") + "'s turn, click either pond to start");
            repaint();
        }

    }

    /**
     * Places all the pieces on their default fields as part of the board initialization
     */
    private void placePieces() {

        // -1 flags x1
        board[0][0].accept(new Flag(false));
        board[9][9].accept(new Flag(true));

        // 0 bombs x6
        for (int i = 1; i <= 6; i++) { board[0][i].accept(new Bomb(false)); }
        for (int i = 8; i >= 3; i--) { board[9][i].accept(new Bomb(true)); }

        // 1 spies x1
        board[0][7].accept(new Spy(false));
        board[9][2].accept(new Spy(true));

        // 2 scouts x8
        board[0][8].accept(new Scout(false));
        board[0][9].accept(new Scout(false));
        board[9][1].accept(new Scout(true));
        board[9][0].accept(new Scout(true));
        for (int i = 0; i <= 5; i++) { board[1][i].accept(new Scout(false)); }
        for (int i = 9; i >= 4; i--) { board[8][i].accept(new Scout(true)); }

        // 3 miners x5
        for (int i = 6; i <= 9; i++) { board[1][i].accept(new Miner(false)); }
        for (int i = 3; i >= 0; i--) { board[8][i].accept(new Miner(true)); }
        board[2][0].accept(new Miner(false));
        board[7][9].accept(new Miner(true));

        // 4 sergeants x4
        for (int i = 1; i <= 4; i++) { board[2][i].accept(new Piece(false, 4)); }
        for (int i = 8; i >= 5; i--) { board[7][i].accept(new Piece(true, 4)); }

        // 5 lieutenants x4
        for (int i = 5; i <= 8; i++) { board[2][i].accept(new Piece(false, 5)); }
        for (int i = 4; i >= 1; i--) { board[7][i].accept(new Piece(true, 5)); }

        // 6 captains x4
        board[2][9].accept(new Piece(false, 6));
        board[7][0].accept(new Piece(true, 6));
        for (int i = 0; i <= 2; i++) { board[3][i].accept(new Piece(false, 6)); }
        for (int i = 9; i >= 7; i--) { board[6][i].accept(new Piece(true, 6)); }

        // 7 majors x3
        for (int i = 3; i <= 5; i++) { board[3][i].accept(new Piece(false, 7)); }
        for (int i = 6; i >= 4; i--) { board[6][i].accept(new Piece(true, 7)); }

        // 8 colonels x2
        for (int i = 6; i <= 7; i++) { board[3][i].accept(new Piece(false, 8)); }
        for (int i = 3; i >= 2; i--) { board[6][i].accept(new Piece(true, 8)); }

        // 9 generals x1
        board[3][8].accept(new Piece(false, 9));
        board[6][1].accept(new Piece(true, 9));

        // 10 marshals x1
        board[3][9].accept(new Piece(false, 10));
        board[6][0].accept(new Piece(true, 10));

    }

    /**
     * Checks whether a piece could move to a given point.
     * <p>
     *     First checks if the point is on the board, then makes sure it's not in either pond, then finally checks if there's a teammate there.
     * </p>
     * @param p Point with x,y coordinates
     * @return true if valid, false otherwise
     */
    public boolean canStepHere(Point p, boolean team) {

        int x = p.x;
        int y = p.y;

        boolean onMap = (x >= 0 && x <= 9 && y >= 0 && y <= 9);
        boolean notInPonds = !(x >= 2 && x <= 3 && y >= 4 && y <= 5) && !(x >= 6 && x <= 7 && y >= 4 && y <= 5);

        boolean noTeammateHere = true;
        if (onMap && !(board[y][x].isEmpty())) { noTeammateHere = (team != board[y][x].getPiece().isLightTeam()); }

        return onMap && notInPonds && noTeammateHere;

    }

    /**
     * Reveals everything, and sets the game to stage 1, which effectively freezes everything
     * @param by The winning piece that stepped on the enemy flag
     */
    public void win(Piece by) {

        for (int y = 0; y <= 9; y++) {
            for (int x = 0; x <= 9; x++) {
                if (board[y][x].getPiece() != null) {
                    board[y][x].getPiece().setRevealed(true);
                }
            }
        }

        this.gameStage = 1;
        if (gameWindow != null) gameWindow.setTitle("Stratego! - " + (by.isLightTeam() ? "Light" : "Dark") + " Wins!");

    }

    /**
     * Getter for board
     * @return The fields on the board
     */
    public Field[][] getBoard() {
        return board;
    }

    /**
     * Getter for lightTurn
     * @return Whether it is the Light player's turn
     */
    public boolean isLightTurn() {
        return lightTurn;
    }

    /**
     * Getter for startedTurn
     * @return Whether the player has started their current turn
     */
    public boolean hasStartedTurn() {
        return startedTurn;
    }

    /**
     * Getter for gameStage
     * @return The current game stage
     */
    public int getGameStage() {
        return gameStage;
    }

    /**
     * Setter for gameStage
     * @param gameStage The new game stage
     */
    public void setGameStage(int gameStage) {
        this.gameStage = gameStage;
    }

    /**
     * Setter for gameWindow
     * @param gameWindow The new game window
     */
    public void setGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    /**
     * Draws the board and everything on it
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {

        // for every field on the board
        for (int y = 0; y <= 9; y++) {
            for (int x = 0; x <= 9; x++) {

                Field f = board[y][x];

                // if there's a piece "picked up" by the cursor then
                if (gameStage == 0 && currPiece != null) {

                    // finds the valid moves of that piece
                    Vector<Field> validMoves = currPiece.findValidMoves(this);
                    // and draws every valid field as highlighted and others normally
                    f.paintComponent(g, validMoves.contains(f));

                // or otherwise
                } else {

                    // draws every field normally
                    f.paintComponent(g, false);

                }

            }
        }

        // if the cursor "picks up" a piece then
        if (currPiece != null && (currPiece.getMaxMovement() != 0 || gameStage == -1)) {

            // draw that
            final Image img = currPiece.getImg();
            g.drawImage(img, currPos.x, currPos.y, null);

        }

    }

    /**
     * Figures out what to do at the moment when the mouse is pressed down
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

        // clears the current piece to ensure a clean slate
        currPiece = null;

        // sets the draw position to be under the cursor
        this.currPos = new Point(e.getX() - 30 ,e.getY() - 30);

        // gets the field under the cursor
        Field f = (Field) this.getComponentAt(new Point(e.getX(), e.getY()));

        // if the game hasn't ended, the turn has started, and there is a piece under the cursor then
        if (this.gameStage < 1 && startedTurn && !f.isEmpty()) {

            // makes the cursor "pick up" that piece
            currPiece = f.getPiece();

            // if the piece isn't the current player's piece, puts it back down
            if ((currPiece.isLightTeam() != lightTurn) ) { currPiece = null; return; }

            // and if the piece can move, or if the game is in the setup phase then
            if (currPiece.getMaxMovement() != 0 || this.gameStage == -1) {

                f.setDisplay(false);

            }

        }

        // if the field under the cursor is in either pond then
        int x = f.getPos().x;
        int y = f.getPos().y;
        boolean inPonds = (x >= 2 && x <= 3 && y >= 4 && y <= 5) ||
                (x >= 6 && x <= 7 && y >= 4 && y <= 5);
        if (inPonds) {

            // and if the game is in the setup phase, then
            if (gameStage == -1) {

                // if clicked the first time then
                if (lightTurn) {

                    // transitions to Dark's setup turn
                    if (gameWindow != null) gameWindow.setTitle("Stratego! - Dark sets up, click either pond when finished");
                    lightTurn = false;

                // and if clicked the second time then
                } else {

                    // transitions to Light's first normal turn
                    if (gameWindow != null) gameWindow.setTitle("Stratego! - Light's turn, click either pond to start");
                    lightTurn = true;
                    startedTurn = false;
                    gameStage = 0;

                }

            // or if the game is already in the playing phase, then
            } else if (gameStage == 0) {

                // start the current turn
                if (gameWindow != null) gameWindow.setTitle("Stratego! - " + (lightTurn ? "Light" : "Dark") + "'s turn");
                startedTurn = true;

            }

        }

        repaint();

    }

    /**
     * Figures out what to do when the mouse is released
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {

        // if there's a piece picked up then
        if (currPiece != null) {

            // finds the chosen field, aka where the mouse was released
            Field chosen = (Field) this.getComponentAt(new Point(e.getX(), e.getY()));

            // if in setup phase, handles swapping
            if (this.gameStage == -1) {

                // if the player's trying to swap pieces in the correct half
                boolean inLightHalf = chosen.getPos().y >= 6 && chosen.getPos().y <= 9;
                boolean inDarkHalf = chosen.getPos().y >= 0 && chosen.getPos().y <= 3;
                if ( (inLightHalf && chosen.getPiece().isLightTeam()) || (inDarkHalf && !chosen.getPiece().isLightTeam()) ) {

                    Field f = currPiece.getField();
                    Piece temp = currPiece;
                    f.accept(chosen.remove());
                    chosen.accept(temp);

                    f.setDisplay(true);

                }

            }

            // if in playing phase, handles movement
            if (this.gameStage == 0) {

                // shows all the valid spots to move to
                Vector<Field> validMoves = currPiece.findValidMoves(this);

                // if player chooses valid spot then
                if (validMoves.contains(chosen)) {

                    // move there
                    currPiece.move(chosen);

                    // and transition to the other player's turn
                    lightTurn = !lightTurn;
                    if (gameWindow != null && gameStage != 1) gameWindow.setTitle("Stratego! - " + (isLightTurn() ? "Light" : "Dark") + "'s turn, click either pond to start");
                    startedTurn = false;

                }

            }

            currPiece.getField().setDisplay(true);
            chosen.setDisplay(true);

        }

        currPiece = null;
        repaint();
        gameWindow.save();

    }

    /**
     * Updates the drawing position when dragging the mouse
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        // sets the draw position to be under the mouse
        this.currPos = new Point(e.getX() - 30 ,e.getY() - 30);

        repaint();

    }

    // Irrelevant methods
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}


}
