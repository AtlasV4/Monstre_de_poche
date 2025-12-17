package com.esiea.pootp.types;

public class TypeResolver {
    public static Type resolve(String typeName) {
        switch (typeName) {
            case "Fire": return new FireType();
            case "Electric": return new ElectricType();
            case "Water": return new WaterType();
            case "Plant": return new PlantType();
            case "Insect": return new InsectType();
            case "Soil": return new SoilType();
            case "Normal": return new NormalType();
            case "Nature": return new NatureType();
            default: throw new IllegalArgumentException("Type de monstre inconnu: " + typeName);
        }
    }
}