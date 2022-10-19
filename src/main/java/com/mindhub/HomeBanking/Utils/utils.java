package com.mindhub.HomeBanking.Utils;

import net.bytebuddy.utility.RandomString;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public static String generateToken() {
        String token = RandomString.make(38);

        return token;

    }
}

