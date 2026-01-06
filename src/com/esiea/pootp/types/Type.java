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

    public static Type get(String typeName) {
        if (typeName == null) return null;

        switch (typeName.toLowerCase()) {
            case "fire":     return new FireType();
            case "water":    return new WaterType();
            case "electric": return new ElectricType();

            case "nature":
            case "plant":    return new PlantType();

            case "insect":   return new InsectType();
            case "soil":     return new SoilType();
            case "normal":   return new NormalType();
            default:
                System.err.println("Type inconnu : " + typeName);
                return null;
        }
    }
}
