package com.esiea.pootp.types;

public abstract class Type {
    protected String name;
    protected String strength;
    protected String weakness;

    public String getStrength() {
        return this.strength;
    };

    public String getWeakness() {
        return this.weakness;
    };

    public String getName() {
        return this.name;
    }
}
