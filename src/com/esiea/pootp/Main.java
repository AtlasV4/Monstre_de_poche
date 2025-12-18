package com.esiea.pootp;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.data.*;
import com.esiea.pootp.gui.BattleFrame;
import com.esiea.pootp.monsters.Monster;
import com.esiea.pootp.objects.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

    private static final int MAX_ATTACKS_PER_MONSTER = 4;
    private static final int TEAM_SIZE = 3;

    public static void main(String[] args) {
        System.out.println("Lancement de POOTP GUI...");


        ArrayList<AttackMap> attackBlueprints = AttackLoader.loadAttacks("attacks.txt");
        ArrayList<MonsterMap> monsterBlueprints = MonsterLoader.loadModels("monsters.txt");

        if (attackBlueprints.isEmpty() || monsterBlueprints.isEmpty()) {
            System.err.println("Erreur : Fichiers de données introuvables ou vides.");
            return;
        }

        Player player1 = initializePlayer("Joueur Alpha", monsterBlueprints, attackBlueprints);
        Player player2 = initializePlayer("Joueur Bêta", monsterBlueprints, attackBlueprints);

        BattleFrame gui = new BattleFrame(player1, player2);


        PrintStream guiStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                gui.addLog(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                gui.addLog(new String(b, off, len));
            }
        });
        System.setOut(guiStream);

        player1.setGui(gui);
        player2.setGui(gui);


        Controller controller = new Controller(player1, player2, gui);



        new Thread(() -> {
            controller.startBattle();
        }).start();
    }

    private static Player initializePlayer(String name, List<MonsterMap> blueprints, ArrayList<AttackMap> attackBlueprints) {
        List<Monster> team = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < TEAM_SIZE; i++) {
            MonsterMap model = blueprints.get(rand.nextInt(blueprints.size()));

            List<Attack> compatibleAttacks = attackBlueprints.stream()
                    .filter(attackModel -> attackModel.type.equals(model.type) || attackModel.type.equals("Normal"))
                    .map(MonsterFactory::createAttackInstance)
                    .collect(Collectors.toList());

            int attacksToSelect = Math.min(MAX_ATTACKS_PER_MONSTER, compatibleAttacks.size());
            ArrayList<Attack> selectedAttacks = rand.ints(0, compatibleAttacks.size())
                    .distinct()
                    .limit(attacksToSelect)
                    .mapToObj(compatibleAttacks::get)
                    .collect(Collectors.toCollection(ArrayList::new));

            team.add(MonsterFactory.createMonsterInstance(model, selectedAttacks));
        }

        List<Consumable> inventory = new ArrayList<>();
        inventory.add(new Potion("Super Potion", PotionEffect.HEAL, 50));
        inventory.add(new Potion("Protéine", PotionEffect.BOOST_ATTACK, 10));
        inventory.add(new Medicine("Antidote", MedicineAction.CURE_POISON));
        inventory.add(new Medicine("Assécheur", MedicineAction.DRY_FIELD));

        return new Player(name, team, inventory);
    }
}