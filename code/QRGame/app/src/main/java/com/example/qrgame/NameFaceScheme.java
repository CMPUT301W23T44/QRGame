package com.example.qrgame;

public class NameFaceScheme {

    public static String[] hex0;
    public static String[] hex1;
    public static String[] hex2;
    public static String[] hex3;
    public static String[] hex4;
    public static String[] hex5;

    NameFaceScheme() {
        hex0 = [""]
    }

    public static String generateName(String hash) {
        String name = "";

        name += hex0[generateString(hash,0)];
        name += hex1[generateString(hash,1)];
        name += hex2[generateString(hash,2)];
        name += hex3[generateString(hash,3)];
        name += hex4[generateString(hash,4)];
        name += hex5[generateString(hash,5)];
        return name;
    }

    private static int generateString(String hash, int index) {
        return Integer.parseInt(String.valueOf(hash.charAt(index)), 16);
    }
}
