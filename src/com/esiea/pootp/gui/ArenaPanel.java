package com.esiea.pootp.gui;

import com.esiea.pootp.monsters.Monster;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import javax.imageio.ImageIO;

public class ArenaPanel extends JPanel {
    private Image backgroundImage;
    private Monster m1, m2;
    private java.util.HashMap<String, ImageIcon> cache = new java.util.HashMap<>();

    public ArenaPanel(String arenaUrl) {
        loadBackground(arenaUrl);
    }

    public ArenaPanel() {
        this("https://pokemonrevolution.net/forum/uploads/monthly_2021_03/DVMT-6OXcAE2rZY.jpg.afab972f972bd7fbd4253bc7aa1cf27f.jpg");
    }

    private void loadBackground(String path) {
        try {
            if (path.startsWith("http")) {
                URL url = new URL(path);
                this.backgroundImage = ImageIO.read(url);
            } else {
                this.backgroundImage = new ImageIcon(path).getImage();
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement fond : " + e.getMessage());
        }
    }

    public void setMonsters(Monster m1, Monster m2) {
        this.m1 = m1;
        this.m2 = m2;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        int p2X = getWidth() - 320, p2Y = 150;
        int p1X = 100, p1Y = getHeight() - 320;

        if (m2 != null) drawMonster(g2, m2, p2X, p2Y, 180);
        if (m1 != null) drawMonster(g2, m1, p1X, p1Y, 240);

        if (m2 != null) drawStatusBox(g2, p2X+40, p2Y - 70, m2, false);
        if (m1 != null) drawStatusBox(g2, p1X-60, p1Y+220, m1, true);
    }

    private void drawMonster(Graphics2D g, Monster m, int x, int y, int size) {
        ImageIcon icon = getIcon(m.getImagePath(), size);
        if (icon != null) {
            g.drawImage(icon.getImage(), x, y, this);
        }
    }

    private void drawStatusBox(Graphics2D g, int x, int y, Monster m, boolean isPlayer) {
        int boxW = 250;
        int boxH = 70;

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRoundRect(x, y, boxW, boxH, 15, 15);

        g.setColor(new Color(53, 100, 173));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(x, y, boxW, boxH, 15, 15);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(m.getName().toUpperCase(), x + 15, y + 25);

        int barX = x + 15;
        int barY = y + 35;
        int barW = 220;
        int barH = 12;

        g.setColor(Color.BLACK);
        g.fillRoundRect(barX, barY, barW, barH, 5, 5);

        double hpRatio = (double) Math.max(0, m.getHp()) / m.getMaxHp();
        if (hpRatio > 0.5) g.setColor(new Color(46, 204, 113));
        else if (hpRatio > 0.2) g.setColor(new Color(241, 196, 15));
        else g.setColor(new Color(231, 76, 60));

        g.fillRoundRect(barX, barY, (int) (barW * hpRatio), barH, 5, 5);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 12));
        String hpTxt = m.getHp() + " / " + m.getMaxHp() + " HP";
        g.drawString(hpTxt, x + boxW - g.getFontMetrics().stringWidth(hpTxt) - 15, y + 62);
    }

    private ImageIcon getIcon(String path, int size) {
        if (path == null) return null;
        if (cache.containsKey(path + "_" + size)) return cache.get(path + "_" + size);

        try {
            Image img;
            if (path.startsWith("http")) img = ImageIO.read(new URL(path));
            else img = new ImageIcon(path).getImage();

            ImageIcon scaled = new ImageIcon(img.getScaledInstance(size, size, Image.SCALE_SMOOTH));
            cache.put(path + "_" + size, scaled);
            return scaled;
        } catch (Exception e) { return null; }
    }
}