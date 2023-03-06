package com.example.qrgame;

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

    static final private String FACE_FILLER = "|              |";
    static final private String FACE_CHIN = "\\___/";
    static final private String NEWLINE_CARRIAGE = "\n";



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
                    name += STRING_0[index];
                    section++;
                } else if (section == 2) {
                    name += STRING_1[index];
                    section++;
                } else if (section == 3) {
                    name += STRING_2[index];
                    section++;
                } else {
                    name += STRING_3[index];
                }
                hashFraction = 0;
            }
        }
        return name;
    }

    public static String generateFace(String hash) {
        String face = "";
        int hashFraction = 0;
        int section = 1;
        int currentLength = 0;

        for (int i = 0; i < hash.length(); i++) {
            hashFraction += Integer.parseInt(String.valueOf(hash.charAt(i)), 16);
            currentLength++;
            if (currentLength%16 == 0) {
                int index = hashFraction%8;
                if (section == 1) {
                    face += FACE_0[index] + NEWLINE_CARRIAGE;
                    face += FACE_FILLER + NEWLINE_CARRIAGE;
                    section++;
                } else if (section == 2) {
                    face += FACE_1[index] + NEWLINE_CARRIAGE;
                    face += FACE_FILLER + NEWLINE_CARRIAGE;
                    section++;
                } else if (section == 3) {
                    face += FACE_2[index] + NEWLINE_CARRIAGE;
                    section++;
                } else {
                    face += FACE_3[index] + NEWLINE_CARRIAGE;
                    face += FACE_FILLER + NEWLINE_CARRIAGE;
                    face += FACE_CHIN;
                }
                hashFraction = 0;
            }
        }
        return face;
    }
}
