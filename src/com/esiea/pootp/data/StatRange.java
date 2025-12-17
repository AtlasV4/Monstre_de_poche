package com.esiea.pootp.data;

public class StatRange {
    public final int min;
    public final int max;

    public StatRange(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("La valeur minimale ne peut pas être supérieure à la valeur maximale.");
        }
        this.min = min;
        this.max = max;
    }
}