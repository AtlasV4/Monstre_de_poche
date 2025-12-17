package com.esiea.pootp.data;

import com.esiea.pootp.attacks.Attack;
import com.esiea.pootp.monsters.*;
import com.esiea.pootp.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterFactory {

    private static final Random RANDOM = new Random();

    private static int generateRandomStat(StatRange range) {
        return RANDOM.nextInt(range.max - range.min + 1) + range.min;
    }

    public static Monster createMonsterInstance(MonsterMap model, ArrayList<Attack> monsterAttacks) {

        int hp = generateRandomStat(model.hp);
        int speed = generateRandomStat(model.speed);
        int attack = generateRandomStat(model.attack);
        int defense = generateRandomStat(model.defense);


        String imagePath = model.imagePath;

        double paralysisChance = model.specialAttributes.getOrDefault("Paralysis", 0.0);
        double floodChance = model.specialAttributes.getOrDefault("Flood", 0.0);
        double fallChance = model.specialAttributes.getOrDefault("Fall", 0.0);
        double burnChance = model.specialAttributes.getOrDefault("Burn", 0.0);

        switch (model.type) {
            case "Electric":

                return new ElectricMonster(model.name, hp, speed, attack, defense, monsterAttacks, paralysisChance, imagePath);

            case "Water":
                return new WaterMonster(model.name, hp, speed, attack, defense, monsterAttacks, floodChance, fallChance, imagePath);

            case "Fire":
                return new FireMonster(model.name, hp, speed, attack, defense, monsterAttacks, burnChance, imagePath);

            case "Plant":
            case "Nature":
                return new PlantMonster(model.name, hp, speed, attack, defense, monsterAttacks, imagePath);

            case "Soil":
                return new SoilMonster(model.name, hp, speed, attack, defense, monsterAttacks, imagePath);

            default:
                return new NormalMonster(model.name, hp, speed, attack, defense, monsterAttacks, imagePath);        }
    }

    public static Attack createAttackInstance(AttackMap blueprint) {
        int accuracy = (int) Math.round((1.0 - blueprint.failChance) * 100);

        Type resolvedType = TypeResolver.resolve(blueprint.type);

        return new Attack(blueprint.name, resolvedType, blueprint.nbUse, accuracy);
    }
}