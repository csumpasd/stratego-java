import javax.swing.*;

import gui.MenuWindow;

/**
 * Entry point to the program
 * Runs menu with invokeLater to be able to handle it from other threads
 */
public class Game implements Runnable {
    public void run() {
        SwingUtilities.invokeLater(new MenuWindow());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}