package com.example.qrgame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.security.NoSuchAlgorithmException;

public class QRCodeUnitTest {

    private QRCode MockQRCode(String mockData) throws NoSuchAlgorithmException {
        return new QRCode(mockData);
    }

    @Test
    public void testHash() throws NoSuchAlgorithmException {
        String mockData1 = "Random String";
        String mockData2 = "Another Random String";

        String hash1 = QRCodeHasher.hash(mockData1);
        assertNotEquals(mockData1, hash1);
        String hash2 = QRCodeHasher.hash(mockData2);
        assertNotEquals(mockData2, hash2);
        assertNotEquals(hash1, hash2);

        String mockData1Copy = "Random String";
        String hash3 = QRCodeHasher.hash(mockData1Copy);
        assertEquals(hash1, hash3);
    }

    @Test
    public void test


}
