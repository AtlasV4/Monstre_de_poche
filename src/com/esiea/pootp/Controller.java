package com.esiea.pootp;

import com.esiea.pootp.gui.BattleFrame;
import com.esiea.pootp.monsters.Monster;

public class Controller {
    private final Player player1;
    private final Player player2;
    private final BattleFrame gui;


    public Controller(Player p1, Player p2, BattleFrame gui) {
        this.player1 = p1;
        this.player2 = p2;
        this.gui = gui;
    }

    public void startBattle() {
        log("--- DÉBUT DU COMBAT : " + player1.getName() + " vs " + player2.getName() + " ---");

        int turn = 1;
        while (player1.hasActiveMonsters() && player2.hasActiveMonsters()) {
            log("\n*** TOUR " + turn + " ***");
            updateUI();


            Action action1 = player1.chooseAction(player2.getActiveMonster());
            Action action2 = player2.chooseAction(player1.getActiveMonster());

            executeTurn(action1, action2);

            checkFaintAndSwitch(player1, player2);
            checkFaintAndSwitch(player2, player1);

            pause(1000);
            turn++;
        }
        declareWinner();
    }

    private void executeTurn(Action action1, Action action2) {
        Monster.updateFloodStatus();

        executeSwitches(player1, action1);
        executeSwitches(player2, action2);
        updateUI();
        pause(500);

        boolean p1Switched = action1.isSwitch();
        boolean p2Switched = action2.isSwitch();

        executeItems(player1, action1, p1Switched);
        executeItems(player2, action2, p2Switched);
        updateUI();
        pause(500);

        executeAttacks(player1, action1, p1Switched, player2, action2, p2Switched);
    }

    private void executeSwitches(Player player, Action action) {
        if (action.isSwitch()) {
            player.switchMonster(action.getMonsterToSwitch());
            log(player.getName() + " change de monstre !");
        }
    }

    private void executeItems(Player player, Action action, boolean wasSwitched) {
        if (!wasSwitched && action.isConsumableUse()) {
            log(player.getName() + " utilise un objet...");
            action.getConsumable().use(action.getConsumableTarget(), player.getActiveMonster());
            player.removeConsumable(action.getConsumable());
        }
    }

    private void executeAttacks(Player p1, Action a1, boolean p1Switched, Player p2, Action a2, boolean p2Switched) {
        Monster m1 = p1.getActiveMonster();
        Monster m2 = p2.getActiveMonster();


        boolean canAttack1 = a1.isAttack() && !p1Switched && !a2.isConsumableUse();
        boolean canAttack2 = a2.isAttack() && !p2Switched && !a1.isConsumableUse();

        Monster firstAttacker = null, secondAttacker = null;
        Action firstAction = null, secondAction = null;


        if (canAttack1 && canAttack2) {
            if (m1.getSpeed() >= m2.getSpeed()) {
                firstAttacker = m1; firstAction = a1; secondAttacker = m2; secondAction = a2;
            } else {
                firstAttacker = m2; firstAction = a2; secondAttacker = m1; secondAction = a1;
            }
        } else if (canAttack1) {
            firstAttacker = m1; firstAction = a1;
        } else if (canAttack2) {
            firstAttacker = m2; firstAction = a2;
        }


        if (firstAttacker != null) {

            Monster target = (firstAttacker == m1) ? m2 : m1;

            log("\n[" + firstAttacker.getName().toUpperCase() + " AGIT]");
            log(firstAttacker.getName() + " lance " + firstAction.getAttack().getName() + " !");


            firstAttacker.chooseAndDoAttack(target, firstAction.getAttack());

            updateUI();
            pause(1000);


            if (secondAttacker != null && target.getHp() > 0) {

                Monster secondTarget = (secondAttacker == m1) ? m2 : m1;

                log("\n[" + secondAttacker.getName().toUpperCase() + " RÉPLIQUE]");
                log(secondAttacker.getName() + " lance " + secondAction.getAttack().getName() + " !");

                secondAttacker.chooseAndDoAttack(secondTarget, secondAction.getAttack());

                updateUI();
                pause(1000);
            } else if (secondAttacker != null) {
                log(secondAttacker.getName() + " ne peut pas attaquer car il est KO !");
            }
        }
    }

    private void checkFaintAndSwitch(Player player, Player opponent) {
        Monster active = player.getActiveMonster();
        if (active != null && active.getHp() <= 0) {
            log(active.getName() + " est mis K.O. !");
            Monster nextMonster = player.getTeam().stream()
                    .filter(m -> m.getHp() > 0 && m != active)
                    .findFirst().orElse(null);

            if (nextMonster != null) {
                player.switchMonster(nextMonster);
                updateUI();
            } else {
                log(player.getName() + " n'a plus de monstres !");
            }
        }
    }

    private void declareWinner() {
        if (player1.hasActiveMonsters()) {
            log("\nVICTOIRE ! " + player1.getName() + " a gagné !");
        } else if (player2.hasActiveMonsters()) {
            log("\nVICTOIRE ! " + player2.getName() + " a gagné !");
        } else {
            log("\nMatch nul !");
        }
        updateUI();
    }



    private void log(String message) {
        gui.addLog(message);
    }

    private void updateUI() {
        gui.updateUI(player1, player2);
    }

    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}