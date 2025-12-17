package com.esiea.pootp.gui;

import com.esiea.pootp.Action;
import com.esiea.pootp.Player;
import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.monsters.Monster;
import com.esiea.pootp.objects.Consumable;

import javax.swing.*;
import java.awt.*;

public class BattleFrame extends JFrame {
    private JTextArea logArea;
    private JPanel actionPanel;
    private ArenaPanel arenaPanel;

    public BattleFrame(Player p1, Player p2) {
        setTitle("POOTP - Battle Arena");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        arenaPanel = new ArenaPanel();
        add(arenaPanel, BorderLayout.CENTER);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(25, 25, 25));
        logArea.setForeground(new Color(220, 220, 220));
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setPreferredSize(new Dimension(300, 0));
        scroll.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.DARK_GRAY), "Journal de Combat", 0, 0, null, Color.LIGHT_GRAY));
        add(scroll, BorderLayout.EAST);

        actionPanel = new JPanel(new BorderLayout());
        actionPanel.setPreferredSize(new Dimension(0, 180));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions Disponibles"));
        add(actionPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
        updateUI(p1, p2);
    }

    public void updateUI(Player p1, Player p2) {
        if (arenaPanel != null) {
            arenaPanel.setMonsters(p1.getActiveMonster(), p2.getActiveMonster());
        }
    }

    public void addLog(String message) {
        logArea.append("> " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void showMainMenu(Player player, Monster opponent) {
        actionPanel.removeAll();

        JLabel turnHeader = new JLabel("C'est au tour de " + player.getName().toUpperCase());
        turnHeader.setHorizontalAlignment(SwingConstants.CENTER);
        turnHeader.setFont(new Font("Arial", Font.ITALIC, 14));
        actionPanel.add(turnHeader, BorderLayout.NORTH);

        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        JButton atk = createStyledButton("ATTAQUER", new Color(192, 57, 43));
        JButton obj = createStyledButton("OBJETS", new Color(41, 128, 185));
        JButton swi = createStyledButton("MONSTRES", new Color(39, 174, 96));

        atk.addActionListener(e -> showAttackMenu(player));
        obj.addActionListener(e -> showItemMenu(player));
        swi.addActionListener(e -> showSwitchMenu(player));

        btnBox.add(atk); btnBox.add(obj); btnBox.add(swi);
        actionPanel.add(btnBox, BorderLayout.CENTER);
        refreshActionPanel();
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(150, 40));
        return b;
    }

    private void showAttackMenu(Player player) {
        actionPanel.removeAll();

        JLabel title = new JLabel("Attaques de " + player.getActiveMonster().getName());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        actionPanel.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new FlowLayout());
        for (Attack atk : player.getActiveMonster().getAttacks()) {
            String label = atk.getName() + " (" + atk.getNbUse() + " PP)";
            JButton btn = new JButton(label);

            Color typeColor = getColorForType(atk.getType().getName());
            btn.setBackground(typeColor);
            btn.setOpaque(true);
            btn.setBorderPainted(true);

            if (atk.getType().getName().equals("Soil")) {
                btn.setForeground(Color.WHITE);
            }

            if (atk.getNbUse() <= 0) {
                btn.setEnabled(false);
                btn.setBackground(Color.GRAY);
            }

            btn.addActionListener(e -> {
                player.notifyDecision(new Action(atk));
                clearActionPanel();
            });
            grid.add(btn);
        }

        actionPanel.add(grid, BorderLayout.CENTER);
        addBackButton(player);
        refreshActionPanel();
    }

    private void showItemMenu(Player player) {
        actionPanel.removeAll();
        if (player.getInventory().isEmpty()) {
            addLog("Sac vide !");
            showMainMenu(player, null);
            return;
        }
        JPanel grid = new JPanel(new FlowLayout());
        for (Consumable c : player.getInventory()) {
            JButton b = new JButton(c.getName());
            b.addActionListener(e -> {
                player.notifyDecision(new Action(c, player.getActiveMonster()));
                clearActionPanel();
            });
            grid.add(b);
        }
        actionPanel.add(grid, BorderLayout.CENTER);
        addBackButton(player);
        refreshActionPanel();
    }

    private void showSwitchMenu(Player player) {
        actionPanel.removeAll();
        JPanel grid = new JPanel(new FlowLayout());
        for (Monster m : player.getTeam()) {
            if (m != player.getActiveMonster() && m.getHp() > 0) {
                JButton b = new JButton(m.getName() + " [" + m.getHp() + " HP]");
                b.addActionListener(e -> {
                    player.notifyDecision(new Action(m));
                    clearActionPanel();
                });
                grid.add(b);
            }
        }
        actionPanel.add(grid, BorderLayout.CENTER);
        addBackButton(player);
        refreshActionPanel();
    }

    private void addBackButton(Player p) {
        JButton back = new JButton("RETOUR");
        back.addActionListener(e -> showMainMenu(p, null));
        actionPanel.add(back, BorderLayout.SOUTH);
    }

    private void clearActionPanel() {
        actionPanel.removeAll();
        JLabel l = new JLabel("Calcul du tour en cours...");
        l.setHorizontalAlignment(SwingConstants.CENTER);
        actionPanel.add(l, BorderLayout.CENTER);
        refreshActionPanel();
    }

    private void refreshActionPanel() {
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private Color getColorForType(String typeName) {
        switch (typeName) {
            case "Fire":    return new Color(255, 100, 100);
            case "Water":   return new Color(100, 150, 255);
            case "Electric": return new Color(255, 255, 100);
            case "Plant":
            case "Nature":  return new Color(120, 200, 80);
            case "Soil":    return new Color(180, 130, 70);
            case "Normal":  return new Color(200, 200, 200);
            default:        return Color.WHITE;
        }
    }
}