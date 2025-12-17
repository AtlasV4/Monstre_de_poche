package com.esiea.pootp.monsters;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.types.PlantType;

import java.util.ArrayList;

public class PlantMonster extends Monster {

    private double selfHealChance = 0.20;

    public PlantMonster(String name, int hp, int speed, int attack, int defense, ArrayList<Attack> attacks, String imagePath) {
        super(name, hp, speed, attack, defense, new PlantType(), attacks, imagePath);
    }

    @Override
    public int doAttack(Monster target, Attack attack) {

        int dmg = attack.doAttack(this, target);

        if (this.state != MonsterState.NORMAL) {

            if (Math.random() < this.selfHealChance) {

                MonsterState oldState = this.state;
                this.state = MonsterState.NORMAL;
                this.turnsParalyzed = 0;

            }
        }

        return dmg;
    }
}