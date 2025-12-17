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

    public ArenaPanel() {

        try {

            String bgPath = "https://pokemonrevolution.net/forum/uploads/monthly_2021_03/DVMT-6OXcAE2rZY.jpg.afab972f972bd7fbd4253bc7aa1cf27f.jpg";
            URL url = new URL(bgPath);
            this.backgroundImage = ImageIO.read(url);
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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (m2 != null) drawMonster(g2, m2, getWidth() - 320, 150, 180);
        if (m1 != null) drawMonster(g2, m1, 100, getHeight() - 280, 220);

        if (m2 != null) drawStatusBox(g2, getWidth() - 250, 10, m2, false);
        if (m1 != null) drawStatusBox(g2, 50, getHeight() - 70, m1, true);
    }

    private void drawMonster(Graphics2D g, Monster m, int x, int y, int size) {
        ImageIcon icon = getIcon(m.getImagePath(), size);
        if (icon != null) g.drawImage(icon.getImage(), x, y, this);
    }

    private void drawStatusBox(Graphics2D g, int x, int y, Monster m, boolean isPlayer) {
        int boxW = 220;
        int boxH = 60;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(x, y, boxW, boxH, 15, 15);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(x, y, boxW, boxH, 15, 15);

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(m.getName(), x + 15, y + 25);

        int barX = x + 15;
        int barY = y + 35;
        int barW = 190;
        int barH = 10;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barW, barH);

        double hpRatio = (double) m.getHp() / m.getMaxHp();
        if (hpRatio > 0.5) g.setColor(Color.GREEN);
        else if (hpRatio > 0.2) g.setColor(Color.ORANGE);
        else g.setColor(Color.RED);

        g.fillRect(barX, barY, (int) (barW * hpRatio), barH);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        String hpTxt = m.getHp() + " / " + m.getMaxHp();
        g.drawString(hpTxt, x + boxW - g.getFontMetrics().stringWidth(hpTxt) - 15, y + 53);
    }


    private ImageIcon getIcon(String path, int size) {
        if (path == null) return null;
        if (cache.containsKey(path)) return cache.get(path);
        try {
            Image img;
            if (path.startsWith("http")) img = ImageIO.read(new URL(path));
            else img = new ImageIcon(path).getImage();

            ImageIcon scaled = new ImageIcon(img.getScaledInstance(size, size, Image.SCALE_SMOOTH));
            cache.put(path, scaled);
            return scaled;
        } catch (Exception e) { return null; }
    }

    private void drawHealthBar(Graphics2D g, int x, int y, Monster m) {
        int barWidth = 150;
        int barHeight = 12;

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(m.getName(), x, y - 5);

        g.setColor(Color.BLACK);
        g.fillRect(x, y, barWidth, barHeight);

        double hpRatio = (double) m.getHp() / m.getMaxHp();
        int currentHpWidth = (int) (barWidth * hpRatio);

        if (hpRatio > 0.5) g.setColor(Color.GREEN);
        else if (hpRatio > 0.2) g.setColor(Color.ORANGE);
        else g.setColor(Color.RED);

        g.fillRect(x + 1, y + 1, Math.max(0, currentHpWidth - 2), barHeight - 2);
    }
}