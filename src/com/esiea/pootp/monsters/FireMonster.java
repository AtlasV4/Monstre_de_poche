package com.esiea.pootp.monsters;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.types.FireType;
import com.esiea.pootp.types.Type;

import java.util.ArrayList;

public class FireMonster extends Monster {

    private double burnChance;

    public FireMonster(String name, int hp, int speed, int attack, int defense, ArrayList<Attack> attacks, double burnChance, String imagePath) {
        super(name, hp, speed, attack, defense, new FireType(), attacks, imagePath);
        this.burnChance = burnChance;
    }

    @Override
    public int doAttack(Monster target, Attack attack) {

        int dmg = attack.doAttack(this, target);

        if (attack.getType() instanceof FireType && target.getState() != MonsterState.BURNED) {

            if (Math.random() < this.burnChance) {
                target.setState(MonsterState.BURNED);
            }
        }

        return dmg;
    }

}