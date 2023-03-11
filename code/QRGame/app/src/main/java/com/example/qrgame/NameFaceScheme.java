package com.example.qrgame;

import org.checkerframework.checker.units.qual.A;

/**
 * Generates a unique face and name based off a hash
 */
public class NameFaceScheme {

    static final private String[] STRING_0 = new String[]{"Concerned ",
                                                "Ambiguous ",
                                                "Far ",
                                                "Hallowed ",
                                                "Wide-eyed ",
                                                "Tawdry ",
                                                "Venomous ",
                                                "Industrious "};
    static final private String[] STRING_1 = new String[]{"Flo",
                                               "Ble",
                                               "Cla",
                                               "Ni",
                                               "Wes",
                                               "Dea",
                                               "Cse",
                                               "Try"};
    static final private String[] STRING_2 = new String[] {"ck",
                                                "lim",
                                                "her",
                                                "ism",
                                                "ly",
                                                "ede",
                                                "tar",
                                                "dim"};
    static final private String[] STRING_3 = new String[] {"it",
                                                "ely",
                                                "dum",
                                                "cey",
                                                "ght",
                                                "ism",
                                                "ould",
                                                "hey"};

    static final private String[] FACE_0 = new String[] {"<--->",
                                                        "/_________\\",
                                                        ".~~~~~~~~~~.",
                                                        "T__________T",
                                                        "I----------I",
                                                        "/0000000000\\",
                                                        "|----|",
                                                        "''''''''''"};
    static final private String[] FACE_1 = new String[] {"$|  @  @  |$",
                                                            "$|    ^   ^   |$",
                                                            "%|  U U  |%",
                                                            "(  OO  )",
                                                            "<(  ?  ?  )>",
                                                            "^| I  I |^",
                                                            "%/    O    O   /%",
                                                            "$(  U U  )$"};
    static final private String[] FACE_2 = new String[] {"|   ^   |",
                                                        "/   V   \\",
                                                        "\\    .    //",
                                                        "|   []   |",
                                                        "(   v   )",
                                                        "<    x    >",
                                                        "[     #     ]",
                                                        "{    V    }"};
    static final private String[] FACE_3 = new String[] {"(    w    )",
                                                        "|    xxxxx    |",
                                                        "/  uu  \\",
                                                        "(  \\________/  )",
                                                        "|  v-----v  |",
                                                        "/    ~~~~~~~   \\",
                                                        "\\  /-----\\  /",
                                                        "|  _    _  |"};

    static final private String FACE_FILLER = "|                  |";
    static final private String FACE_CHIN = "\\___/";
    static final private String NEWLINE_CARRIAGE = "\n";

    static final private int HASH_FRACTION_LENGTH = 16;
    static final private int ARRAY_LENGTH = 8;
    static private int RADIX = 16;

    /**
     * Generates a name based on the hexadecimal digits of the hash provided
     * @param hash - SHA-256 hash based on a QR code
     * @return
     *      A unique name based on the hash provided
     */
    public static String generateName(String hash) {
        StringBuilder name = new StringBuilder();
        int hashFraction = 0;
        int section = 1;
        int currentLength = 0;

        // Adds values from the hash until there are HASH_FRACTION_LENGTH values
        for (int i = 0; i < hash.length(); i++) {
            hashFraction += Integer.parseInt(String.valueOf(hash.charAt(i)), RADIX);
            currentLength++;
            if (currentLength%HASH_FRACTION_LENGTH == 0) {
                int index = hashFraction%ARRAY_LENGTH;
                // Takes the index based on the amount of options in the arrays
                if (section == 1) {
                    name.append(STRING_0[index]);
                    section++;
                } else if (section == 2) {
                    name.append(STRING_1[index]);
                    section++;
                } else if (section == 3) {
                    name.append(STRING_2[index]);
                    section++;
                } else {
                    name.append(STRING_3[index]);
                }
                hashFraction = 0;
            }
        }
        return name.toString();
    }

    /**
     * Generates a face based on the hash provided
     * @param hash - SHA-256 hash based on a QR code
     * @return
     *      A unique face based on the hash provided
     */
    public static String generateFace(String hash) {
        StringBuilder face = new StringBuilder();
        int hashFraction = 0;
        int section = 1;
        int currentLength = 0;

        // Adds values from the hash until there are HASH_FRACTION_LENGTH values
        for (int i = 0; i < hash.length(); i++) {
            hashFraction += Integer.parseInt(String.valueOf(hash.charAt(i)), RADIX);
            currentLength++;
            if (currentLength%HASH_FRACTION_LENGTH == 0) {
                int index = hashFraction%ARRAY_LENGTH;
                // Takes the index based on the amount of options in the arrays
                if (section == 1) {
                    face.append(FACE_0[index]).append(NEWLINE_CARRIAGE);
                    face.append(FACE_FILLER + NEWLINE_CARRIAGE);
                    section++;
                } else if (section == 2) {
                    face.append(FACE_1[index]).append(NEWLINE_CARRIAGE);
                    face.append(FACE_FILLER + NEWLINE_CARRIAGE);
                    section++;
                } else if (section == 3) {
                    face.append(FACE_2[index]).append(NEWLINE_CARRIAGE);
                    section++;
                } else {
                    face.append(FACE_3[index]).append(NEWLINE_CARRIAGE);
                    face.append(FACE_FILLER + NEWLINE_CARRIAGE);
                    face.append(FACE_CHIN);
                }
                hashFraction = 0;
            }
        }
        return face.toString();
    }
}
