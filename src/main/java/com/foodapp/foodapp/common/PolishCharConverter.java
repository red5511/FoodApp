package com.foodapp.foodapp.common;

import java.text.Normalizer;

public class PolishCharConverter {

    public static String removePolishDiacritics(String input) {
        if (input == null) {
            return null;
        }
        String temp = input.replace("ł", "l").replace("Ł", "L");
        String normalized = Normalizer.normalize(temp, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }
}
