package com.esiea.pootp.attacks;

import com.esiea.pootp.monsters.Monster;
import com.esiea.pootp.types.Type;

import java.util.Random;

public class Attack {

    private String name;

    private Type type;

    private int nbUse;

    private int power;

    private double fail;

    public Attack(String name, Type type, int nbUse, int power) {
        this.name = name;
        this.type = type;
        this.nbUse = nbUse;
        this.power = power;

    }


    public Type getType() {
        return this.type;
    }

    public int getDmg(Monster attacker,Monster target) {
        Random rand = new Random();
        double coef = 0.85 + (1.0 - 0.85) * rand.nextDouble();

        double advantage = 1;

        if(target.getType().getWeakness() == this.type.getName()) {
            advantage = 2;
        } else if (target.getType().getStrength() == this.type.getName()) {
            advantage = 0.5;
        }

        int dmg;

        dmg = (int)Math.round(((11*attacker.getAttack()*power) / (25*attacker.getDefense()) + 2) * advantage * coef);

        return dmg;
    }

    public int doAttack(Monster attacker,Monster target) {
        Random rand = new Random();
        double coef = 0.85 + (1.0 - 0.85) * rand.nextDouble();

        double advantage = 1;

        if (nbUse <= 0) {
            System.out.println("Il n'y a plus de points d'usage pour " + this.name + " !");
            return 0;
        }

        this.nbUse--;

        if(target.getType().getWeakness() == this.type.getName()) {
            advantage = 2;
            System.out.println("C'est super efficace !");
        } else if (target.getType().getStrength() == this.type.getName()) {
            advantage = 0.5;
            System.out.println("Ce n'est très efficace !");
        }

        int dmg;

        dmg = (int)Math.round(((11*attacker.getAttack()*power) / (25*attacker.getDefense()) + 2) * advantage * coef);

        target.decreaseHp(dmg);

        return dmg;
    }


    public String getName() {
        return this.name;
    }

    public int getPower() {
        return this.power;
    }

    public int getNbUse() {
        return this.nbUse;
    }
}
