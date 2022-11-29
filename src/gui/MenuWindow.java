package gui;

import java.awt.*;
import javax.swing.*;

import static java.lang.System.exit;

public class MenuWindow implements Runnable {

    /**
     * Constructs and shows the menu
     */
    @Override
    public void run() {

        // creates the frame and moves it to the center
        JFrame menuWindow = new JFrame("Stratego!");
        menuWindow.setSize(200,130);
        menuWindow.setLocationRelativeTo(null);

        // creates the main panel and sets it's layout to a boxlayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // creates a new game button
        JButton start = new JButton("New Game");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.addActionListener(e -> {
            new GameWindow(false);
            menuWindow.dispose();
        });

        // creates a load game button
        JButton load = new JButton("Load Game");
        load.setAlignmentX(Component.CENTER_ALIGNMENT);
        load.addActionListener(e -> {
            new GameWindow(true);
            menuWindow.dispose();
        });

        // creates a quit button
        JButton quit = new JButton("Quit");
        quit.setAlignmentX(Component.CENTER_ALIGNMENT);
        quit.addActionListener(e -> {
            menuWindow.dispose();
            exit(0);
        });

        // puts everything to where it should be
        panel.add(Box.createVerticalStrut(10));
        panel.add(start);
        panel.add(load);
        panel.add(quit);
        menuWindow.add(panel);

        // shows the menu
        menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuWindow.setVisible(true);

    }


}
