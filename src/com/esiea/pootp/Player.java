package com.esiea.pootp;

import com.esiea.pootp.monsters.Monster;
import com.esiea.pootp.objects.Consumable;
import com.esiea.pootp.gui.BattleFrame;
import com.esiea.pootp.attacks.Attack;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Player {
    private final String name;
    private final List<Monster> team;
    private Monster activeMonster;
    private final List<Consumable> inventory;

    private boolean isBot = false;
    private final Random random = new Random();
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

    public void setIsBot(boolean isBot) {
        this.isBot = isBot;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setGui(BattleFrame gui) {
        this.gui = gui;
    }

    public Action chooseAction(Monster opponent) {
        if (this.activeMonster == null || this.activeMonster.getHp() <= 0) {
            Monster nextMonster = team.stream()
                    .filter(m -> m.getHp() > 0 && m != this.activeMonster)
                    .findFirst().orElse(null);
            return (nextMonster != null) ? new Action(nextMonster) : new Action();
        }

        if (this.isBot) {
            return chooseBotAction();
        } else {
            return chooseHumanAction(opponent);
        }
    }


    private Action chooseBotAction() {
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        List<Attack> availableAttacks = activeMonster.getAttacks().stream()
                .filter(a -> a.getNbUse() > 0)
                .collect(Collectors.toList());

        if (!availableAttacks.isEmpty()) {
            Attack chosen = availableAttacks.get(random.nextInt(availableAttacks.size()));
            return new Action(chosen);
        }

        Monster backup = team.stream().filter(m -> m.getHp() > 0 && m != activeMonster).findFirst().orElse(null);
        if (backup != null) return new Action(backup);

        return new Action();
    }

    private Action chooseHumanAction(Monster opponent) {
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