/************************************************************************
 * Code from https://www.baeldung.com/sha-256-hashing-java
 * Created by baeldung https://github.com/Baeldung
 ***********************************************************************/

package com.example.qrgame;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashes QR code based off a provided string of data based on the SHA-256 algorithm
 */
public class QRCodeHasher {

    /**
     * Hashes QR code based on the SHA-256 algorithm
     * @param s - String to be hashed
     * @return
     *      A unique hash based on the string provided
     * @throws NoSuchAlgorithmException
     */
    public static String hash(String s) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(
                s.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    /**
     * Converts byte array to a single string
     * @param hash - Byte array containing the hash to be coverted
     * @return
     *      String representation of the provided byte array
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
