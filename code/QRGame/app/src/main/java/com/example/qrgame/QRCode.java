package com.example.qrgame;


import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class QRCode implements Comparable, Serializable {

    private int score = 0;
    private String hash;
    private String name;
    private String face;

    QRCode(String data) throws NoSuchAlgorithmException {
        hash = QRCodeHasher.hash(data);
        calcScore();
        generate();
    }

    private void calcScore() {
        String previousChar = String.valueOf(hash.charAt(0));
        String currentChar;
        int currentScoreValue = 1;

        for (int i = 1; i < hash.length(); i++) {
            currentChar = String.valueOf(hash.charAt(i));
            if (currentChar.equals(previousChar)) {
                if (currentChar == "0") {
                    currentScoreValue *= 20;
                } else {
                    currentScoreValue *= (Integer.parseInt(currentChar, 16));
                }
            } else {
                score += currentScoreValue;
                currentScoreValue = 1;
            }
            previousChar = currentChar;
        }
    }
    public int getScore() {
        return score;
    }

    private void generate() {
        name = NameFaceScheme.generateName(hash);
        face = NameFaceScheme.generateFace(hash);
    }

    public String getName() {
        return name;
    }

    public String getFace() { return face;}

    @Override
    public int compareTo(Object o) {
        if (this.score > ((QRCode) o).getScore()) {
            return 1;
        }
        return 0;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "QRCode{" +
                "name='" + name + '\'' +
                '}';
    }
}
