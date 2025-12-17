package com.esiea.pootp.objects;

import com.esiea.pootp.monsters.Monster;
import com.esiea.pootp.monsters.MonsterState;
import com.esiea.pootp.objects.Consumable;

public class Medicine extends Consumable {

    private MedicineAction action;

    public Medicine(String name, MedicineAction action) {
        super(name);
        this.action = action;
    }

    @Override
    public void use(Monster target, Monster user) {

        System.out.println(user.getName() + " utilise " + this.getName() + ".");

        switch (action) {
            case CURE_BURN:
                target.cureStatus(MonsterState.BURNED);
                break;

            case CURE_PARALYSIS:
                target.cureStatus(MonsterState.PARALYZED);
                break;

            case DRY_FIELD:
                if (Monster.isFlooded()) {
                    Monster.setFlooded(false, null, 0);
                    System.out.println("Le terrain est asséché !");
                } else {
                    System.out.println("Le terrain est déjà sec.");
                }
                break;

            default:
                System.out.println("Le médicament n'a pas d'effet connu.");
        }
    }
}