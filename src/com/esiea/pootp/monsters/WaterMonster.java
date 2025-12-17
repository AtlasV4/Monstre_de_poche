package com.esiea.pootp.monsters;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.types.Type;
import com.esiea.pootp.types.WaterType;

import java.util.ArrayList;
import java.util.Random;

public class WaterMonster extends Monster {

    private double floodChance;
    private double fallChance;
    public WaterMonster(String name, int hp, int speed, int attack, int defense, ArrayList<Attack> attacks, double fallChance, double floodChance, String imagePath) {
        super(name, hp, speed, attack, defense, new WaterType(), attacks, imagePath);
        this.fallChance = fallChance;
        this.floodChance = floodChance;
    }

    public double getFallChance() {
        return this.fallChance;
    }

    @Override
    public int doAttack(Monster target, Attack attack) {

        int dmg = attack.doAttack(this,target);

        if (!Monster.isFlooded() && Math.random() < this.floodChance) {

            Random rand = new Random();
            int turns = rand.nextInt(3) + 1;

            Monster.setFlooded(true, this, turns);
        }

        return dmg;
    }
}
