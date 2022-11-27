package board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serial;
import java.io.Serializable;
import java.util.Vector;

import gui.GameWindow;
import helper.InvalidMoveException;
import pieces.*;

public class Board extends JPanel implements Serializable, MouseListener, MouseMotionListener {
    // add images to draw
    @Serial
    private static final long serialVersionUID = 1L;
    private final Field[][] board;
    private GameWindow gameWindow;

    private boolean lightTurn;

    private Piece currPiece;
    private Point currPos;

    private int gameStage;
    private boolean startedTurn;

    public Board(GameWindow g) {

        this.gameWindow = g;
        this.board = new Field[10][10];
        this.gameStage = -1;
        this.startedTurn = true;

        setLayout(new GridLayout(10, 10, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        for (int y = 0; y <= 9; y++) {
            for (int x = 0; x <= 9; x++) {
                int color = (x + y) % 2;
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

        if (gameStage == 0) {
            startedTurn = false;
            g.setTitle("Stratego! - " + (lightTurn ? "Light" : "Dark") + "'s turn, click either pond to start");
            repaint();
        }

    }

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
     *     First checks if the point is on the board,
     *     then makes sure it's not in either pond,
     *     then finally checks if there's a teammate there.
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

    public void win(Piece by) {

        for (int y = 0; y <= 9; y++) {
            for (int x = 0; x <= 9; x++) {
                if (board[y][x].getPiece() != null) {
                    board[y][x].getPiece().setRevealed(true);
                }
            }
        }

        this.gameStage = 1;
        gameWindow.setTitle("Stratego! - " + (by.isLightTeam() ? "Light" : "Dark") + " Wins!");

    }

    public Field[][] getBoard() {
        return board;
    }

    public boolean isLightTurn() {
        return lightTurn;
    }

    public boolean hasStartedTurn() {
        return startedTurn;
    }

    public int getGameStage() {
        return gameStage;
    }

    public void setGameStage(int gameStage) {
        this.gameStage = gameStage;
    }

    public void setGameWindow(GameWindow g) {
        this.gameWindow = g;
    }

    @Override
    public void paintComponent(Graphics g) {

        for (int y = 0; y <= 9; y++) {
            for (int x = 0; x <= 9; x++) {
                Field f = board[y][x];
                if (currPiece != null && gameStage == 0) {
                    Vector<Field> validMoves = currPiece.findValidMoves(this);
                    f.paintComponent(g, validMoves.contains(f));
                } else {
                    f.paintComponent(g, false);
                }

            }
        }

        if (currPiece != null) {
            if (currPiece.getMaxMovement() != 0 || gameStage == -1) {
                final Image img = currPiece.getImg();
                g.drawImage(img, currPos.x, currPos.y, null);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.currPos = new Point(e.getX() - 30 ,e.getY() - 30);

        Field f = (Field) this.getComponentAt(new Point(e.getX(), e.getY()));

        // if there's a piece there and the game hasn't ended, pick up the piece
        if (!f.isEmpty() && this.gameStage < 1 && startedTurn) {
            currPiece = f.getPiece();
            if (currPiece.getMaxMovement() != 0 || this.gameStage == -1) {
                if (currPiece.isLightTeam() && !lightTurn) { return; }
                if (!currPiece.isLightTeam() && lightTurn) { return; }
                f.setDisplay(false);
                repaint();
            }
        }

        // use the ponds as buttons
        int x = f.getPos().x;
        int y = f.getPos().y;
        boolean inPonds = (x >= 2 && x <= 3 && y >= 4 && y <= 5) ||
                (x >= 6 && x <= 7 && y >= 4 && y <= 5);
        if (inPonds) {
            // while in init phase
            if (gameStage == -1) {
                // first time: transition to the other player
                if (lightTurn) {
                    gameWindow.setTitle("Stratego! - Dark sets up, click either pond when finished");
                    lightTurn = false;
                    repaint();
                // second time: start game with light
                } else {
                    gameWindow.setTitle("Stratego! - Light's turn, click either pond to start");
                    lightTurn = true;
                    startedTurn = false;
                    gameStage = 0;
                    repaint();
                }
            } else if (gameStage == 0) {
                startedTurn = true;
                gameWindow.setTitle("Stratego! - " + (lightTurn ? "Light" : "Dark") + "'s turn");
                currPiece = null;
                repaint();
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Field chosen = (Field) this.getComponentAt(new Point(e.getX(), e.getY()));

        // if there's a piece picked up
        if (currPiece != null) {
            if (currPiece.isLightTeam() && !lightTurn) { return; }
            if (!currPiece.isLightTeam() && lightTurn) { return; }

            // and the game is in the playing phase
            if (this.gameStage == 0) {

                // show all the valid spots to move to
                Vector<Field> validMoves = currPiece.findValidMoves(this);
                if (validMoves.contains(chosen)) {

                    // and if player chooses a valid spot, move there
                    chosen.setDisplay(true);
                    try {
                        currPiece.move(chosen);
                    } catch (InvalidMoveException ignored) {
                    }

                    // and transition to the other player's turn
                    currPiece.getField().setDisplay(true);
                    currPiece = null;
                    lightTurn = !lightTurn;
                    if (this.gameStage < 1) {
                        gameWindow.setTitle("Stratego! - " + (isLightTurn() ? "Light" : "Dark") + "'s turn, click either pond to start");
                        startedTurn = false;
                    }

                } else {
                    currPiece.getField().setDisplay(true);
                    currPiece = null;
                }

            // or if the game is in the setup phase
            } else if (this.gameStage == -1){

                // if the player's trying to swap pieces in the correct half, then do that
                boolean inLightHalf = chosen.getPos().x >= 0 && chosen.getPos().x <= 9 && chosen.getPos().y >= 6 && chosen.getPos().y <= 9;
                boolean inDarkHalf = chosen.getPos().x >= 0 && chosen.getPos().x <= 9 && chosen.getPos().y >= 0 && chosen.getPos().y <= 3;

                if ( (inLightHalf && chosen.getPiece().isLightTeam()) || (inDarkHalf && !chosen.getPiece().isLightTeam()) ) {
                    Field f = currPiece.getField();
                    Piece temp = currPiece;
                    f.accept(chosen.remove());
                    chosen.accept(temp);
                    chosen.setDisplay(true);
                    f.setDisplay(true);
                }
                currPiece.getField().setDisplay(true);
                chosen.setDisplay(true);
                currPiece = null;

            }
            repaint();
            gameWindow.save();
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.currPos = new Point(e.getX() - 30 ,e.getY() - 30);
        if (currPiece != null) {
            if (currPiece.isLightTeam() && !lightTurn) {
                return;
            }
            if (!currPiece.isLightTeam() && lightTurn) {
                return;
            }
            repaint();
        }

    }

    // Irrelevant methods, do nothing for these mouse behaviors
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
