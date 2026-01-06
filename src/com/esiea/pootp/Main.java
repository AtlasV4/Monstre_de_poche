package com.esiea.pootp;

import com.esiea.pootp.gui.LauncherMenu;

public class Main {
    public static void main(String[] args) {
        System.out.println("Lancement de POOTP...");

        javax.swing.SwingUtilities.invokeLater(() -> {
            new LauncherMenu();
        });
    }
}