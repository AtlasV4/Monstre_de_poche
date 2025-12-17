package com.esiea.pootp.objects;

import com.esiea.pootp.monsters.Monster;

public abstract class Consumable {
    protected String name;

    public Consumable(String name) {
        this.name = name;
    }

    public abstract void use(Monster target, Monster user);

    public String getName() {
        return name;
    }
}