package com.esiea.pootp.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AttackLoader {

    public static ArrayList<AttackMap> loadAttacks(String filePath) {
        ArrayList<AttackMap> attacks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.equalsIgnoreCase("Attack")) {
                    AttackMap model = parseSingleAttack(br);
                    if (model != null) {
                        attacks.add(model);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier d'attaques: " + e.getMessage());
        }
        return attacks;
    }

    private static AttackMap parseSingleAttack(BufferedReader br) throws IOException {
        String name = null;
        String type = null;
        Integer power = null;
        Integer nbUse = null;
        Double failChance = null;

        String line;
        while ((line = br.readLine()) != null && !line.trim().equalsIgnoreCase("EndAttack")) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            if (parts.length < 2) continue;

            String key = parts[0];
            String value = parts[1];

            try {
                switch (key) {
                    case "Name":
                        name = value;
                        break;
                    case "Type":
                        type = value;
                        break;
                    case "Power":
                        power = Integer.parseInt(value);
                        break;
                    case "NbUse":
                        nbUse = Integer.parseInt(value);
                        break;
                    case "Fail":
                        failChance = Double.parseDouble(value);
                        break;

                }
            } catch (NumberFormatException e) {
                System.err.println("Erreur de format numérique pour l'attaque. Ligne: " + line);
            }
        }


        if (name == null || type == null || power == null || nbUse == null || failChance == null) {
            System.err.println("Avertissement: Attaque incomplète trouvée. Nécessite tous les champs.");
            return null;
        }

        return new AttackMap(name, type, power, nbUse, failChance);
    }
}