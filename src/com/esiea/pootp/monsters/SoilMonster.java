package com.esiea.pootp.monsters;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.types.SoilType;
import com.esiea.pootp.types.WaterType;

import java.util.ArrayList;
import java.util.Random;

public class SoilMonster extends Monster {

    private boolean isUnderground = false;
    private int turnsUndergroundLeft = 0;
    private int originalDefense = -1;

    public SoilMonster(String name, int hp, int speed, int attack, int defense, ArrayList<Attack> attacks, String imagePath) {
        super(name, hp, speed, attack, defense, new WaterType(), attacks, imagePath);
        this.type = new SoilType();
    }

    public void updateUndergroundStatus() {
        if (this.isUnderground) {
            this.turnsUndergroundLeft--;

            if (this.turnsUndergroundLeft <= 0) {
                this.isUnderground = false;

                if (this.originalDefense != -1) {
                    this.defense = this.originalDefense;
                    this.originalDefense = -1;
                }
            } else {
            }
        }
    }


    @Override
    public int doAttack(Monster target, Attack attack) {

        int dmg = attack.doAttack(this,target);

        if (attack.getType() instanceof SoilType && !this.isUnderground) {

            double chanceDeFuite = 0.05;

            if (Math.random() < chanceDeFuite) {
                Random rand = new Random();
                this.turnsUndergroundLeft = rand.nextInt(3) + 1;
                this.isUnderground = true;

                this.originalDefense = this.defense;
                this.defense *= 2;

            }
        }

        return 0;
    }

    public boolean isUnderground() {
        return this.isUnderground;
    }

    public int getTurnsUndergroundLeft() {
        return this.turnsUndergroundLeft;
    }
}
