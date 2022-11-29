import javax.swing.*;

import gui.MenuWindow;

/**
 * Entry point to the program
 * <p>
 *     Runs menu with invokeLater to be able to properly handle it from other threads
 * </p>
 */
public class Game implements Runnable {

    // launches a menu if this class is invoked as a thread
    public void run() {
        SwingUtilities.invokeLater(new MenuWindow());
    }

    /**
     * Invokes this class as a thread
     * @param args The passed args, empty here
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }


}