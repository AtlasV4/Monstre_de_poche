package com.esiea.pootp.monsters;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.types.InsectType;
import com.esiea.pootp.types.NatureType;

import java.util.ArrayList;

public class InsectMonster extends Monster {

    private int attackCount = 0;

    private final int POISON_FREQUENCY = 3;

    public InsectMonster(String name, int hp, int speed, int attack, int defense, ArrayList<Attack> attacks, String imagePath) {
        super(name, hp, speed, attack, defense, new InsectType(), attacks, imagePath);
    }

    @Override
    public int doAttack(Monster target, Attack attack) {

        int dmg = attack.doAttack(this, target);

        if (attack.getType() instanceof NatureType) {

            this.attackCount++;

            if (this.attackCount % POISON_FREQUENCY == 0 && target.getState() != MonsterState.POISONED) {

                target.setState(MonsterState.POISONED);

                this.attackCount = 0;
            }
        }

        return dmg;
    }

}