package com.esiea.pootp.monsters;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.types.NormalType;
import com.esiea.pootp.types.Type;

import java.util.ArrayList;

public class NormalMonster extends Monster{
    public NormalMonster(String name, int hp, int speed, int attack, int defense, ArrayList<Attack> attacks, String imagePath) {
        super(name, hp, speed, attack, defense, new NormalType(), attacks, imagePath);
    }

    @Override
    public int doAttack(Monster target, Attack attack) {
        return attack.doAttack(this,target);
    }
}
