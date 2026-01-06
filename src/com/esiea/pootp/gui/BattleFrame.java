package com.esiea.pootp.gui;

import com.esiea.pootp.Action;
import com.esiea.pootp.Player;
import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.monsters.Monster;
import com.esiea.pootp.objects.Consumable;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class BattleFrame extends JFrame {
    private JPanel actionPanel;
    private ArenaPanel arenaPanel;
    private JLabel messageLabel;

    private final Color PKM_BORDER_BLUE = new Color(53, 100, 173);

    public BattleFrame(Player p1, Player p2, String arenaUrl) {
        setTitle("COMBAT MONSTRE DE POCHE");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        arenaPanel = new ArenaPanel(arenaUrl);
        add(arenaPanel, BorderLayout.CENTER);

        actionPanel = new JPanel(new BorderLayout());
        actionPanel.setPreferredSize(new Dimension(0, 200));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(PKM_BORDER_BLUE, 6),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        messageLabel = new JLabel("Le combat commence !");
        messageLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        actionPanel.add(messageLabel, BorderLayout.WEST);

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
        messageLabel.setText("<html>" + message.replace("\n", "<br>") + "</html>");
    }

    public void showMainMenu(Player player, Monster opponent) {
        actionPanel.removeAll();
        messageLabel.setText("Que doit faire " + player.getActiveMonster().getName().toUpperCase() + " ?");
        actionPanel.add(messageLabel, BorderLayout.WEST);

        JPanel menuGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        menuGrid.setOpaque(false);
        menuGrid.setPreferredSize(new Dimension(450, 0));

        menuGrid.add(createFullColorButton("ATTAQUE", new Color(192, 57, 43), e -> showAttackMenu(player)));
        menuGrid.add(createFullColorButton("SAC", new Color(41, 128, 185), e -> showItemMenu(player)));
        menuGrid.add(createFullColorButton("MONSTRES", new Color(39, 174, 96), e -> showSwitchMenu(player)));
        menuGrid.add(createFullColorButton("FUITE", new Color(44, 62, 80), e -> System.exit(0)));

        actionPanel.add(menuGrid, BorderLayout.EAST);
        refreshActionPanel();
    }

    private void showAttackMenu(Player player) {
        actionPanel.removeAll();

        JPanel grid = new JPanel(new GridLayout(2, 2, 8, 8));
        grid.setOpaque(false);

        for (Attack atk : player.getActiveMonster().getAttacks()) {
            String label = "<html><center>" + atk.getName().toUpperCase() + "<br><small>PP: " + atk.getNbUse() + "</small></center></html>";
            JButton btn = createFullColorButton(label, getColorForType(atk.getType().getName()), e -> {
                player.notifyDecision(new Action(atk));
                showWaitingMessage();
            });
            if (atk.getNbUse() <= 0) btn.setEnabled(false);
            grid.add(btn);
        }

        actionPanel.add(grid, BorderLayout.CENTER);

        JPanel backContainer = new JPanel(new BorderLayout());
        backContainer.setOpaque(false);
        backContainer.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JButton back = createFullColorButton("RETOUR", Color.GRAY, e -> showMainMenu(player, null));
        back.setPreferredSize(new Dimension(140, 0));

        backContainer.add(back, BorderLayout.CENTER);
        actionPanel.add(backContainer, BorderLayout.EAST);

        refreshActionPanel();
    }

    public void showItemMenu(Player player) {
        actionPanel.removeAll();

        if (player.getInventory() == null || player.getInventory().isEmpty()) {
            messageLabel.setText("Le sac est vide !");
            actionPanel.add(messageLabel, BorderLayout.WEST);
            addStyledBackButton(player);
        } else {
            JPanel grid = new JPanel(new GridLayout(2, 3, 8, 8));
            grid.setOpaque(false);

            for (Consumable c : player.getInventory()) {
                grid.add(createFullColorButton(c.getName(), new Color(41, 128, 185), e -> {
                    player.notifyDecision(new Action(c, player.getActiveMonster()));
                    showWaitingMessage();
                }));
            }
            actionPanel.add(grid, BorderLayout.CENTER);
            addStyledBackButton(player);
        }
        refreshActionPanel();
    }

    public void showSwitchMenu(Player player) {
        actionPanel.removeAll();

        JPanel grid = new JPanel(new GridLayout(0, 2, 8, 8));
        grid.setOpaque(false);

        for (Monster m : player.getTeam()) {
            if (m != player.getActiveMonster() && m.getHp() > 0) {
                String label = "<html><center>" + m.getName().toUpperCase() + "<br><small>HP: " + m.getHp() + "/" + m.getMaxHp() + "</small></center></html>";
                grid.add(createFullColorButton(label, new Color(39, 174, 96), e -> {
                    player.notifyDecision(new Action(m));
                    showWaitingMessage();
                }));
            }
        }

        actionPanel.add(grid, BorderLayout.CENTER);
        addStyledBackButton(player);
        refreshActionPanel();
    }

    private void addStyledBackButton(Player p) {
        JPanel backContainer = new JPanel(new BorderLayout());
        backContainer.setOpaque(false);
        backContainer.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JButton back = createFullColorButton("RETOUR", Color.GRAY, e -> showMainMenu(p, null));
        back.setPreferredSize(new Dimension(140, 0));

        backContainer.add(back, BorderLayout.CENTER);
        actionPanel.add(backContainer, BorderLayout.EAST);
    }

    private JButton createFullColorButton(String text, Color bg, java.awt.event.ActionListener listener) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 18));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.addActionListener(listener);
        return b;
    }

    private void addBackButton(Player p) {
        JButton back = createFullColorButton("RETOUR", Color.GRAY, e -> showMainMenu(p, null));
        back.setPreferredSize(new Dimension(120, 0));
        actionPanel.add(back, BorderLayout.EAST);
    }

    private void showWaitingMessage() {
        actionPanel.removeAll();
        messageLabel.setText("Calcul du tour...");
        actionPanel.add(messageLabel, BorderLayout.CENTER);
        refreshActionPanel();
    }

    private void refreshActionPanel() {
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private Color getColorForType(String typeName) {
        switch (typeName) {
            case "Fire": return new Color(231, 76, 60);
            case "Water": return new Color(52, 152, 219);
            case "Electric": return new Color(241, 196, 15);
            case "Plant":
            case "Nature": return new Color(46, 204, 113);
            case "Normal": return new Color(149, 165, 166);
            default: return new Color(127, 140, 141);
        }
    }
}