package com.example.qrgame;


public class QRCode {

    private int score;
    private String hash;
    private String name;

    QRCode() {
        hash = QRCodeHasher.hash();
        calcScore();
        generateName();
    }

    private void calcScore() {
        String previous = String.valueOf(hash.charAt(0));
        String current;
        int current_value = 1;

        for (int i = 1; i < hash.length(); i++) {
            current = String.valueOf(hash.charAt(i));
            if (current.equals(previous)) {
                current_value *= (Integer.parseInt(current, 16));
            } else {
                score += current_value;
                current_value = 1;
            }
            previous = current;
        }
    }
    public int getScore() {
        return score;
    }

    private void generateName() {
        name = NameFaceScheme.generateName(hash);
    }

    public String getName() {
        return name;
    }

}
