package com.esiea.pootp.gui;

import com.esiea.pootp.Player;
import com.esiea.pootp.Controller;
import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.data.*;
import com.esiea.pootp.monsters.Monster;
import com.esiea.pootp.objects.Potion;
import com.esiea.pootp.objects.PotionEffect;
import com.esiea.pootp.objects.Consumable;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;

public class LauncherMenu extends JFrame {
    private JList<String> listP1, listP2;
    private JCheckBox botCheckBox;
    private JComboBox<String> arenaBox;
    private ArrayList<MonsterMap> monsterModels;
    private ArrayList<AttackMap> attackModels;
    private static final int MAX_TEAM_SIZE = 3;

    private final Color PKM_BLUE = new Color(53, 100, 173);
    private final Color PKM_YELLOW = new Color(255, 203, 5);
    private final Color PKM_TEXT = new Color(60, 60, 60);
    private final Color PKM_RED = new Color(255, 0, 0);

    public LauncherMenu() {
        this.monsterModels = MonsterLoader.loadModels("monsters.txt");
        this.attackModels = AttackLoader.loadAttacks("attacks.txt");

        if (monsterModels.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Erreur : monsters.txt vide !");
        }

        setTitle("POOTP - MONSTRE DE POCHE");
        setSize(850, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BackgroundPanel mainPanel = new BackgroundPanel("assets/mdp.jpg");
        mainPanel.setLayout(new BorderLayout(10, 10));
        setContentPane(mainPanel);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (MonsterMap m : monsterModels) listModel.addElement(m.name);

        JPanel selectionPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        selectionPanel.setOpaque(false);
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(60, 40, 10, 40));

        listP1 = createStyledList(listModel);
        selectionPanel.add(createPlayerPanel(" EQUIPE JOUEUR 1 ", listP1));

        listP2 = createStyledList(listModel);
        selectionPanel.add(createPlayerPanel(" EQUIPE JOUEUR 2 / BOT ", listP2));

        add(selectionPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(15, 15));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 30, 60));

        JPanel glassPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        glassPanel.setBackground(new Color(0, 0, 0, 160));
        glassPanel.setBorder(BorderFactory.createLineBorder(PKM_YELLOW, 2));

        JLabel arenaLabel = new JLabel("ARENE :");
        arenaLabel.setForeground(PKM_YELLOW);
        arenaLabel.setFont(new Font("Monospaced", Font.BOLD, 16));

        arenaBox = new JComboBox<>(new String[]{"Plaine", "Grotte", "Stade"});
        arenaBox.setBackground(Color.WHITE);

        botCheckBox = new JCheckBox("MODE SOLO (VS CPU)");
        botCheckBox.setFont(new Font("Monospaced", Font.BOLD, 16));
        botCheckBox.setForeground(PKM_YELLOW);
        botCheckBox.setOpaque(false);
        botCheckBox.addActionListener(e -> listP2.setEnabled(!botCheckBox.isSelected()));

        glassPanel.add(arenaLabel);
        glassPanel.add(arenaBox);
        glassPanel.add(botCheckBox);
        bottomPanel.add(glassPanel, BorderLayout.NORTH);

        JButton startBtn = new JButton("DUEL !");
        styleStartButton(startBtn);
        startBtn.addActionListener(e -> launch());
        bottomPanel.add(startBtn, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void launch() {
        List<String> selP1 = listP1.getSelectedValuesList();
        if (selP1.isEmpty() || selP1.size() > MAX_TEAM_SIZE) {
            JOptionPane.showMessageDialog(this, "Choisis entre 1 et 3 monstres !");
            return;
        }

        ArrayList<Attack> allAttacks = new ArrayList<>();
        for (AttackMap am : attackModels) {
            com.esiea.pootp.types.Type t = com.esiea.pootp.types.Type.get(am.type);
            if (t != null) allAttacks.add(new Attack(am.name, t, am.nbUse, am.power));
        }

        ArrayList<Monster> team1 = createTeam(selP1, allAttacks);
        ArrayList<Monster> team2;
        String p2Name;

        if (botCheckBox.isSelected()) {
            p2Name = "ORDINATEUR";
            team2 = new ArrayList<>();
            Random rand = new Random();
            for (int i = 0; i < selP1.size(); i++) {
                MonsterMap randomModel = monsterModels.get(rand.nextInt(monsterModels.size()));
                team2.add(createSingleMonster(randomModel, allAttacks));
            }
        } else {
            List<String> selP2 = listP2.getSelectedValuesList();
            if (selP2.isEmpty() || selP2.size() > MAX_TEAM_SIZE) {
                JOptionPane.showMessageDialog(this, "Le Joueur 2 doit aussi choisir ses monstres !");
                return;
            }
            p2Name = "Joueur 2";
            team2 = createTeam(selP2, allAttacks);
        }

        ArrayList<Consumable> inv1 = new ArrayList<>();
        inv1.add(new Potion("Potion", PotionEffect.HEAL, 20));
        inv1.add(new Potion("Super Potion", PotionEffect.HEAL,50));

        ArrayList<Consumable> inv2 = new ArrayList<>();
        inv2.add(new Potion("Potion", PotionEffect.HEAL,20));

        Player p1 = new Player("Joueur 1", team1, inv1);
        Player p2 = new Player(p2Name, team2, inv2);
        if (botCheckBox.isSelected()) p2.setIsBot(true);

        String arenaUrl = getArenaUrl((String) arenaBox.getSelectedItem());
        BattleFrame gui = new BattleFrame(p1, p2, arenaUrl);

        p1.setGui(gui);
        p2.setGui(gui);

        PrintStream guiStream = new PrintStream(new OutputStream() {
            @Override public void write(int b) { gui.addLog(String.valueOf((char) b)); }
            @Override public void write(byte[] b, int off, int len) { gui.addLog(new String(b, off, len)); }
        });
        System.setOut(guiStream);

        Controller controller = new Controller(p1, p2, gui);
        new Thread(controller::startBattle).start();
        this.dispose();
    }

    private String getArenaUrl(String name) {
        switch (name) {
            case "Grotte": return "assets/grotte.jpg";
            case "Stade":  return "assets/stade.jpg";
            default:       return "assets/plaine.jpg";
        }
    }

    class BackgroundPanel extends JPanel {
        private Image img;
        public BackgroundPanel(String path) { this.img = new ImageIcon(path).getImage(); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JList<String> createStyledList(DefaultListModel<String> model) {
        JList<String> list = new JList<>(model);
        list.setBackground(new Color(255, 255, 255, 220));
        list.setFont(new Font("Monospaced", Font.BOLD, 16));
        list.setSelectionBackground(PKM_RED);
        list.setSelectionForeground(Color.WHITE);
        list.setFixedCellHeight(30);
        return list;
    }

    private JPanel createPlayerPanel(String title, JList<String> list) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(PKM_BLUE, 3), title);
        tb.setTitleColor(PKM_BLUE);
        tb.setTitleFont(new Font("Monospaced", Font.BOLD, 16));
        p.setBorder(new CompoundBorder(tb, BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        p.add(new JScrollPane(list), BorderLayout.CENTER);
        return p;
    }

    private void styleStartButton(JButton b) {
        b.setFont(new Font("Arial Black", Font.PLAIN, 32));
        b.setBackground(PKM_RED);
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private ArrayList<Monster> createTeam(List<String> names, ArrayList<Attack> allAttacks) {
        ArrayList<Monster> team = new ArrayList<>();
        for (String name : names) {
            for (MonsterMap mm : monsterModels) {
                if (mm.name.equalsIgnoreCase(name)) {
                    team.add(createSingleMonster(mm, allAttacks));
                    break;
                }
            }
        }
        return team;
    }

    private Monster createSingleMonster(MonsterMap mm, List<Attack> allAttacks) {
        List<Attack> compatible = new ArrayList<>();
        for (Attack a : allAttacks) {
            if (a.getType().getName().equalsIgnoreCase(mm.type) || a.getType().getName().equalsIgnoreCase("Normal")) {
                compatible.add(a);
            }
        }
        Collections.shuffle(compatible);
        ArrayList<Attack> finalAttacks = new ArrayList<>();
        for (int i = 0; i < Math.min(4, compatible.size()); i++) {
            Attack o = compatible.get(i);
            finalAttacks.add(new Attack(o.getName(), o.getType(), o.getNbUse(), o.getPower()));
        }
        return MonsterFactory.createMonsterInstance(mm, finalAttacks);
    }
}