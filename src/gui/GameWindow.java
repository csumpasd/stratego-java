package gui;

import board.Board;
import helper.FileIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class GameWindow implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final JFrame gameWindow;
    private Board board;
    private File file;

    public GameWindow(boolean load) {
        gameWindow = new JFrame();

        // load previous game if chosen so
        if (load) {
            load();
            board.setGameWindow(this);
            switch (board.getGameStage()) {
                case -1 -> this.setTitle("Stratego! - " + (board.isLightTurn() ? "Light" : "Dark") + " sets up, click either pond when finished");
                case 0 -> this.setTitle("Stratego! - " + (board.isLightTurn() ? "Light" : "Dark") + "'s turn" + (board.hasStartedTurn() ? "" : ", click either pond to start"));
            }


        } else {
            this.board = new Board(this);
            // select savefile
            FileDialog dialog = new FileDialog((Frame)null, "Choose savefile");
            dialog.setMode(FileDialog.SAVE);
            dialog.setVisible(true);
            this.file = new File(dialog.getDirectory(), dialog.getFile());
            dialog.dispose();

            this.setTitle("Stratego! - Light sets up, click either pond when finished");
        }
        gameWindow.setPreferredSize(new Dimension(600,600));
        gameWindow.setMinimumSize(gameWindow.getPreferredSize());
        gameWindow.setMaximumSize(gameWindow.getPreferredSize());
        gameWindow.setSize(gameWindow.getPreferredSize());
        gameWindow.setResizable(false);
        gameWindow.setLocationRelativeTo(null);

        gameWindow.setLayout(new BorderLayout(0,0));

        gameWindow.add(board, BorderLayout.CENTER);

        gameWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                save();
                super.windowClosing(e);
            }
        });

        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameWindow.setVisible(true);
    }

    public void setTitle(String title) {
        gameWindow.setTitle(title);
    }

    public void load() {
        FileDialog dialog = new FileDialog((Frame)null, "Load game");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        this.file = new File(dialog.getDirectory(), dialog.getFile());
        dialog.dispose();
        if (file != null) {
            try {
                board = FileIO.read(this.file);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

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
