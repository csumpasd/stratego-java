import javax.swing.*;

import gui.MenuWindow;

/**
 * Entry point to the program
 * <t>
 *     Runs menu with invokeLater to be able to properly handle it from other threads
 * </t>
 */
public class Game implements Runnable {

    // launches a menu if this class is invoked as a thread
    public void run() {
        SwingUtilities.invokeLater(new MenuWindow());
    }

    // invokes this class as a thread
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }


}