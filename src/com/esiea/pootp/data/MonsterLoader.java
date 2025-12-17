package com.esiea.pootp.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MonsterLoader {

    public static ArrayList<MonsterMap> loadModels(String filePath) {
        ArrayList<MonsterMap> models = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.equalsIgnoreCase("Monster")) {
                    MonsterMap model = parseSingleModel(br);
                    if (model != null) {
                        models.add(model);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier de monstres: " + e.getMessage());
        }
        return models;
    }

    private static MonsterMap parseSingleModel(BufferedReader br) throws IOException {
        String name = null;
        String type = null;
        String imagePath = null;
        StatRange hp = null, speed = null, attack = null, defense = null;
        Map<String, Double> specialAttributes = new HashMap<>();

        String line;
        while ((line = br.readLine()) != null && !line.trim().equalsIgnoreCase("EndMonster")) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            if (parts.length < 2) continue;

            String key = parts[0];

            try {
                switch (key) {
                    case "Name":
                        name = parts[1];
                        break;
                    case "Type":
                        type = parts[1];
                        break;
                    case "Image":
                        imagePath = parts[1];
                        break;
                    case "HP":
                        hp = parseRange(parts);
                        break;
                    case "Speed":
                        speed = parseRange(parts);
                        break;
                    case "Attack":
                        attack = parseRange(parts);
                        break;
                    case "Defense":
                        defense = parseRange(parts);
                        break;
                    default:
                        specialAttributes.put(key, Double.parseDouble(parts[1]));
                        break;
                }
            } catch (Exception e) {
                System.err.println("Erreur de format pour le champ '" + key + "'. Ignoré.");
            }
        }


        if (imagePath == null) {
            imagePath = "res/default.png";
        }

        if (name == null || type == null || hp == null || speed == null || attack == null || defense == null) {
            System.err.println("Avertissement: Monstre incomplet trouvé.");
            return null;
        }


        return new MonsterMap(name, type, hp, speed, attack, defense, specialAttributes, imagePath);
    }

    private static StatRange parseRange(String[] parts) {
        if (parts.length < 3) {
            throw new IllegalArgumentException("La stat doit contenir deux valeurs numériques (Min et Max).");
        }
        int min = Integer.parseInt(parts[1]);
        int max = Integer.parseInt(parts[2]);
        return new StatRange(min, max);
    }
}