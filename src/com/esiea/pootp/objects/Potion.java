package com.esiea.pootp.objects;

import com.esiea.pootp.monsters.Monster;

public class Potion extends Consumable {

    private PotionEffect effect;
    private int amount;

    public Potion(String name, PotionEffect effect, int amount) {
        super(name);
        this.effect = effect;
        this.amount = amount;
    }

    @Override
    public void use(Monster target, Monster user) {

        System.out.println(user.getName() + " utilise " + this.getName() + " sur " + target.getName() + ".");

        switch (effect) {
            case HEAL:
                target.heal(amount);
                System.out.println(target.getName() + " se soigne de " + amount + " PV. PV actuels: " + target.getHp());
                break;

            case BOOST_ATTACK:
                int newAttack = target.getAttack() + amount;
                target.setAttack(newAttack);

                System.out.println(target.getName() + " augmente son ATTAQUE de " + amount + " ! Nouvelle ATK : " + newAttack);
                break;

            case BOOST_DEFENSE:
                int newDefense = target.getDefense() + amount;
                target.setDefense(newDefense);
                System.out.println(target.getName() + " augmente sa DÉFENSE de " + amount + " ! Nouvelle DEF : " + newDefense);
                break;

            default:
                System.out.println("La potion n'a pas d'effet connu.");
        }
    }
}