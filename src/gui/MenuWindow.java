package gui;

import java.awt.*;
import javax.swing.*;

import static java.lang.System.exit;

public class MenuWindow implements Runnable {
    @Override
    public void run() {
        JFrame menuWindow = new JFrame("Stratego!");
        menuWindow.setSize(200,130);
        menuWindow.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        menuWindow.add(panel);

        JButton start = new JButton("New Game");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.addActionListener(e -> {
            new GameWindow(false);
            menuWindow.dispose();
        });

        JButton load = new JButton("Load Game");
        load.setAlignmentX(Component.CENTER_ALIGNMENT);
        load.addActionListener(e -> {
            new GameWindow(true);
            menuWindow.dispose();
        });

        JButton quit = new JButton("Quit");
        quit.setAlignmentX(Component.CENTER_ALIGNMENT);
        quit.addActionListener(e -> {
            menuWindow.dispose();
            exit(0);
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(start);
        panel.add(load);
        panel.add(quit);

        menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuWindow.setVisible(true);
    }
}
