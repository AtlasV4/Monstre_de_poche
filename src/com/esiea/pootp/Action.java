package com.esiea.pootp;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.objects.Consumable;
import com.esiea.pootp.monsters.Monster;

enum ActionType {
    ATTACK,
    USE_ITEM,
    SWITCH_MONSTER,
    NONE
}

public class Action {
    private final ActionType type;
    private final Attack attack;
    private final Consumable item;
    private final Monster itemTarget;
    private final Monster monsterToSwitch;


    public Action(Attack attack) {
        this.type = ActionType.ATTACK;
        this.attack = attack;
        this.item = null;
        this.itemTarget = null;
        this.monsterToSwitch = null;
    }

    public Action(Consumable item, Monster target) {
        this.type = ActionType.USE_ITEM;
        this.item = item;
        this.itemTarget = target;
        this.attack = null;
        this.monsterToSwitch = null;
    }

    public Action(Monster newMonster) {
        this.type = ActionType.SWITCH_MONSTER;
        this.monsterToSwitch = newMonster;
        this.attack = null;
        this.item = null;
        this.itemTarget = null;
    }

    public Action() {
        this.type = ActionType.NONE;
        this.attack = null;
        this.item = null;
        this.itemTarget = null;
        this.monsterToSwitch = null;
    }

    public boolean isAttack() {
        return this.type == ActionType.ATTACK;
    }
    public boolean isConsumableUse() {
        return this.type == ActionType.USE_ITEM;
    }
    public boolean isSwitch() {
        return this.type == ActionType.SWITCH_MONSTER;
    }
    public boolean isNone() {
        return this.type == ActionType.NONE;
    }

    public Attack getAttack() {
        return attack;
    }
    public Consumable getConsumable() {
        return item;
    }
    public Monster getConsumableTarget() {
        return itemTarget;
    }
    public Monster getMonsterToSwitch() {
        return monsterToSwitch;
    }

}