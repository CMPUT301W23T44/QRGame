package com.example.qrgame;

import java.util.ArrayList;
import java.util.Collection;

public class NameFaceScheme {

    private static String[] hex0 = new String[]{"Concerned ",
                                                "Ambiguous ",
                                                "Far ",
                                                "Hallowed ",
                                                "Wide-eyed ",
                                                "Tawdry ",
                                                "Venomous ",
                                                "Industrious "};
    private static String[] hex1= new String[]{"Flo",
                                               "Ble",
                                               "Cla",
                                               "Ni",
                                               "Wes",
                                               "Dea",
                                               "Cse",
                                               "Try"};
    private static String[] hex2= new String[] {"ck",
                                                "lim",
                                                "her",
                                                "ism",
                                                "ly",
                                                "ede",
                                                "tar",
                                                "dim"};
    private static String[] hex3 = new String[] {"it",
                                                "ely",
                                                "dum",
                                                "cey",
                                                "ght",
                                                "ism",
                                                "ould",
                                                "hey"};


    public static String generateName(String hash) {
        String name = "";
        int hashFraction = 0;
        int section = 1;
        int currentLength = 0;

        for (int i = 0; i < hash.length(); i++) {
            hashFraction += Integer.parseInt(String.valueOf(hash.charAt(i)), 16);
            currentLength++;
            if (currentLength%16 == 0) {
                int index = hashFraction%8;
                if (section == 1) {
                    name += hex0[index];
                    section++;
                } else if (section == 2) {
                    name += hex1[index];
                    section++;
                } else if (section == 2) {
                    name += hex2[index];
                    section++;
                } else {
                    name += hex3[index];
                }
                hashFraction = 0;
            }
        }
        return name;
    }
}
