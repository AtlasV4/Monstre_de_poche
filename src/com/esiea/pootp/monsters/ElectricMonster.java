package com.esiea.pootp.monsters;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.types.ElectricType;

import java.util.ArrayList;

public class ElectricMonster extends Monster{

    private double paralysisChance;

    public ElectricMonster(String name, int hp, int speed, int attack, int defense, ArrayList<Attack> attacks, double paralysisChance, String imagePath) {
        super(name,hp,speed,attack,defense,new ElectricType(), attacks, imagePath);
        this.paralysisChance = paralysisChance;
    }
    @Override
    public int doAttack(Monster target, Attack attack) {

        int dmg = attack.doAttack(this,target);

        double randomValue = Math.random();
        if(randomValue <= this.paralysisChance) {
            target.setState(MonsterState.PARALYZED);
        }

        return dmg;
    }
}
