package gui;

import board.Board;
import helper.FileIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * Houses the actual game, handles saving and loading
 */
public class GameWindow implements Serializable {

    /**
     * Makes sure serialization doesn't break
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The game window itself
     */
    private final JFrame gameWindow;

    /**
     * The board inside the game window
     */
    private Board board;

    /**
     * The savefile
     */
    private File file;

    /**
     * Constructs a game window
     * @param load Whether to load from file or not
     */
    public GameWindow(boolean load) {

        // creates the frame
        gameWindow = new JFrame();

        // loads previous game if chosen so
        try_load: if (load) {

            // tries to load game
            load();

            // if can't load, goes on to create new game
            if (board == null || file == null) { break try_load; }

            board.setGameWindow(this);

            // and also sets the correct title
            switch (board.getGameStage()) {

                case -1 -> this.setTitle("Stratego! - " + (board.isLightTurn() ? "Light" : "Dark") + " sets up, click either pond when finished");

                case 0 -> this.setTitle("Stratego! - " + (board.isLightTurn() ? "Light" : "Dark") + "'s turn" + (board.hasStartedTurn() ? "" : ", click either pond to start"));

            }

        }

        // or if didn't want to, or couldn't load, creates new one
        if (!load || board == null || file == null) {

            // creates a new board
            this.board = new Board(this);

            // asks player to select a savefile
            FileDialog dialog = new FileDialog((Frame)null, "Choose file to save to");
            dialog.setMode(FileDialog.SAVE);
            dialog.setVisible(true);
            this.file = new File(dialog.getDirectory(), dialog.getFile());
            dialog.dispose();

            // sets the correct title for a new game
            this.setTitle("Stratego! - Light sets up, click either pond when finished");

        }

        // sets the size of the window and centers it on screen, sets layout
        gameWindow.setSize(new Dimension(600,600));
        gameWindow.setResizable(false);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setLayout(new BorderLayout(0,0));

        // adds the board to the window
        gameWindow.add(board, BorderLayout.CENTER);

        // adds a listener to save the game when closing the window
        gameWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                save();
                super.windowClosing(e);
            }
        });

        // shows the window
        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameWindow.setVisible(true);

    }

    /**
     * Sets the title of the window
     * @param title The title to set
     */
    public void setTitle(String title) {
        gameWindow.setTitle(title);
    }

    /**
     * Loads the board from file
     */
    public void load() {

        // asks the player to select a savefile to load it from
        FileDialog dialog = new FileDialog((Frame)null, "Choose savefile to load from");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        if (dialog.getDirectory() != null && dialog.getFile() != null) {
            this.file = new File(dialog.getDirectory(), dialog.getFile());
        }
        dialog.dispose();

        // if the player chose a file
        if (file != null) {

            try {
                board = FileIO.read(this.file);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

    }

    /**
     * Saves the board to file
     */
    public void save() {

        if (file != null) {
            try {
                FileIO.write(this.file, board);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
