package com.pocketserver.impl.gui;

import com.pocketserver.player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ConsoleWindow extends JFrame {

    public ConsoleWindow(List<? extends Player> players) {
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JMenu settingsMenu = new JMenu("Settings");
        JMenu playersMenu = new JMenu("Players");

        players.forEach(p -> playersMenu.add(new JMenu(p.getName())));

        JMenuBar bar = new JMenuBar();
        bar.add(settingsMenu);
        bar.add(playersMenu);
        this.setJMenuBar(bar);

        JPanel panel = new JPanel();
        panel.add(new TextArea());
        this.add(panel);

        this.add(new TextField());
        this.add(new JButton("Send"));
        this.setVisible(true);
    }
}
