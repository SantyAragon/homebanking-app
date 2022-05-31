package com.mindhub.HomeBanking.Utils;

import java.util.ArrayList;
import java.util.List;

public class utils {
    private static List<Integer> creados = new ArrayList<>();

    public static int randomNumber(int min, int max) {
        if (max > 999) {
            int number;
            do {
                number = (int) (Math.random() * (max - min) + min);

            } while (creados.contains(number));

            creados.add(number);
            return number;
        } else {
            int number = (int) (Math.random() * (max - min) + min);
            return number;
        }
    }
}

