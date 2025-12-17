package com.esiea.pootp.data;

import java.util.Map;

public class MonsterMap {
    public final String name;
    public final String type;
    public String imagePath;

    public StatRange hp;
    public StatRange speed;
    public StatRange attack;
    public StatRange defense;

    public final Map<String, Double> specialAttributes;


    public MonsterMap(String name, String type, StatRange hp, StatRange speed,
                      StatRange attack, StatRange defense, Map<String, Double> specialAttributes, String imagePath) {
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.speed = speed;
        this.attack = attack;
        this.defense = defense;
        this.specialAttributes = specialAttributes;
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return String.format("--- %s (%s) ---\nHP: %d-%d, ATK: %d-%d, Image: %s, Specials: %s\n",
                name, type, hp.min, hp.max, attack.min, attack.max, imagePath, specialAttributes);
    }
}