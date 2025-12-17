package com.esiea.pootp.gui;

import com.esiea.pootp.Player;
import com.esiea.pootp.monsters.Monster;
import javax.swing.*;
import java.awt.*;

public class GameGUI extends JFrame {
    private JTextArea logArea;
    private JProgressBar player1HpBar, player2HpBar;
    private JLabel p1NameLabel, p2NameLabel;
    private JPanel buttonPanel;

    public GameGUI(Player p1, Player p2) {
        setTitle("POOTP - Battle Arena");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel statusPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        player1HpBar = createHpBar(Color.GREEN);
        player2HpBar = createHpBar(Color.GREEN);
        p1NameLabel = new JLabel(p1.getActiveMonster().getName());
        p2NameLabel = new JLabel(p2.getActiveMonster().getName());

        statusPanel.add(createMonsterStatus(p1NameLabel, player1HpBar));
        statusPanel.add(createMonsterStatus(p2NameLabel, player2HpBar));
        add(statusPanel, BorderLayout.NORTH);


        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.WHITE);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        add(new JScrollPane(logArea), BorderLayout.CENTER);


        buttonPanel = new JPanel(new FlowLayout());
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createMonsterStatus(JLabel name, JProgressBar bar) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(name, BorderLayout.NORTH);
        p.add(bar, BorderLayout.CENTER);
        return p;
    }

    private JProgressBar createHpBar(Color color) {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(100);
        bar.setStringPainted(true);
        bar.setForeground(color);
        return bar;
    }


    public void addLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void updateStatus(Monster m1, Monster m2) {
        p1NameLabel.setText(m1.getName() + " (Statut: " + m1.getState() + ")");
        p2NameLabel.setText(m2.getName() + " (Statut: " + m2.getState() + ")");

        player1HpBar.setMaximum(m1.getMaxHp());
        player1HpBar.setValue(m1.getHp());

        player2HpBar.setMaximum(m2.getMaxHp());
        player2HpBar.setValue(m2.getHp());
    }
}