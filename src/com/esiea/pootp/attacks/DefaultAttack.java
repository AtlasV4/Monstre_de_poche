package com.esiea.pootp.attacks;

import com.esiea.pootp.monsters.Monster;
import com.esiea.pootp.types.NormalType;

public class DefaultAttack extends Attack {

    public DefaultAttack() {
        super("Mains Nues", new NormalType(), -1, 20);
    }

    @Override
    public int doAttack(Monster attacker, Monster target) {

        double coef = 0.85 + (1.0 - 0.85) * Math.random();

        double dmgDouble = 20 * ((double)attacker.getAttack() / target.getDefense()) * coef;

        int dmg = (int) Math.round(dmgDouble);

        target.decreaseHp(dmg);

        return dmg;
    }
}