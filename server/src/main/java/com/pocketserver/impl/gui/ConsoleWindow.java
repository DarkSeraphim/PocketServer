package com.pocketserver.impl.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.pocketserver.command.CommandManager;
import com.pocketserver.command.ConsoleCommandExecutor;
import com.pocketserver.player.Player;

public class ConsoleWindow {

    private final JFrame frame;
    private final JScrollPane pane;
    private final JTextArea console;
    private final JTextField command;
    private final JButton sendButton;
    
    public ConsoleWindow(List<? extends Player> players) {

        JMenu settingsMenu = new JMenu("Settings");
        JMenu playersMenu = new JMenu("Players");

        players.forEach(p -> playersMenu.add(new JMenuItem(p.getName())));

        JMenuBar bar = new JMenuBar();
        bar.add(settingsMenu);
        bar.add(playersMenu);

        JPanel consolePanel = new JPanel();
        JPanel commandPanel = new JPanel();
        
        consolePanel.setBorder(BorderFactory.createTitledBorder("Console"));
        commandPanel.setBorder(BorderFactory.createTitledBorder("Command"));
        
        GroupLayout consoleLayout = new GroupLayout(consolePanel);
        GroupLayout commandLayout = new GroupLayout(commandPanel);
        
        consolePanel.setLayout(consoleLayout);
        consoleLayout.setAutoCreateContainerGaps(true);
        consoleLayout.setAutoCreateGaps(true);
        
        consoleLayout.setHorizontalGroup(consoleLayout.createSequentialGroup()
            .addComponent(pane = new JScrollPane(console = new JTextArea("", 20, 46), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)));
        consoleLayout.setVerticalGroup(consoleLayout.createParallelGroup()
            .addComponent(pane));
        
        commandPanel.setLayout(commandLayout);
        commandLayout.setAutoCreateContainerGaps(true);
        commandLayout.setAutoCreateGaps(true);
        
        commandLayout.setHorizontalGroup(commandLayout.createSequentialGroup()
            .addComponent(command = new JTextField(39))
            .addComponent(sendButton = new JButton("Send")));
        commandLayout.setVerticalGroup(commandLayout.createParallelGroup()
            .addComponent(command)
            .addComponent(sendButton));
        
        command.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    command(command.getText());
                    command.setText("");
                    e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });
        
        sendButton.addActionListener(a -> {
            command(command.getText());
            command.setText("");
        });
        
        frame = new JFrame("PocketServer GUI");
        
        console.setEditable(false);
        
        pane.setSize(100, 100);
        frame.setSize(600, 490);
        console.setSize(100, 200);
        
        frame.setJMenuBar(bar);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panels = new JPanel();
        
        panels.add(consolePanel);
        panels.add(commandPanel);
        
        frame.add(panels);
        frame.setVisible(true);
    }
    
    public void command(String string) {
        if (string.isEmpty())
            return;
        write(false, "Sent command: " + string + "\n");
        CommandManager.INSTANCE.executeCommand(new ConsoleCommandExecutor(), string);
    }
    
    public void write(boolean isErr, char c) {
        console.setText(console.getText() + c);
    }
    
    public void write(boolean isErr, String str) {
        console.setText(console.getText() + str);
    }
    
}
