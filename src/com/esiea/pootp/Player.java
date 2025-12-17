package com.esiea.pootp;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.monsters.Monster;
import com.esiea.pootp.objects.Consumable;
import com.esiea.pootp.gui.BattleFrame;

import java.util.List;
import java.util.stream.Collectors;

public class Player {
    private final String name;
    private final List<Monster> team;
    private Monster activeMonster;
    private final List<Consumable> inventory;


    private Action selectedAction;
    private final Object lock = new Object();
    private BattleFrame gui;

    private static final int MAX_MONSTERS = 3;
    private static final int MAX_ITEMS = 5;

    public Player(String name, List<Monster> initialTeam, List<Consumable> initialInventory) {
        this.name = name;
        this.team = initialTeam.stream().limit(MAX_MONSTERS).collect(Collectors.toList());
        this.inventory = initialInventory.stream().limit(MAX_ITEMS).collect(Collectors.toList());

        if (!this.team.isEmpty()) {
            this.activeMonster = this.team.get(0);
        }
    }


    public void setGui(BattleFrame gui) {
        this.gui = gui;
    }

    /**
     * Cette méthode remplace la boucle Scanner.
     * Elle affiche les boutons et "attend" que selectedAction soit rempli par un clic.
     */
    public Action chooseAction(Monster opponent) {

        if (this.activeMonster == null || this.activeMonster.getHp() <= 0) {
            Monster nextMonster = team.stream()
                    .filter(m -> m.getHp() > 0 && m != this.activeMonster)
                    .findFirst().orElse(null);
            return (nextMonster != null) ? new Action(nextMonster) : new Action();
        }


        selectedAction = null;


        if (gui != null) {
            gui.showMainMenu(this, opponent);
        }


        synchronized (lock) {
            while (selectedAction == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        return selectedAction;
    }

    /**
     * Appelée par les boutons de la GUI pour débloquer chooseAction
     */
    public void notifyDecision(Action action) {
        synchronized (lock) {
            this.selectedAction = action;
            lock.notifyAll();
        }
    }



    public void switchMonster(Monster newMonster) {
        if (newMonster != null && newMonster.getHp() > 0 && this.team.contains(newMonster)) {
            this.activeMonster = newMonster;
        }
    }

    public void removeConsumable(Consumable item) {
        this.inventory.remove(item);
    }

    public boolean hasActiveMonsters() {
        return team.stream().anyMatch(m -> m.getHp() > 0);
    }

    public String getName() { return name; }
    public Monster getActiveMonster() { return activeMonster; }
    public List<Monster> getTeam() { return team; }
    public List<Consumable> getInventory() { return inventory; }
}