package com.esiea.pootp.data;

public class AttackMap {
    public final String name;
    public final String type;
    public final int power;
    public final int nbUse;
    public final double failChance;

    public AttackMap(String name, String type, int power, int nbUse, double failChance) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.nbUse = nbUse;
        this.failChance = failChance;
    }

    @Override
    public String toString() {
        return String.format("[Attack: %s, Type: %s, Power: %d, Fail: %.2f]", name, type, power, failChance);
    }
}