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
    public void testQRCodeObject() throws NoSuchAlgorithmException {
        String mockData1 = "Random String";
        String mockData2 = "Another Random String";

        String hash1 = QRCodeHasher.hash(mockData1);
        QRCode qrCode = MockQRCode(mockData1);
        assertEquals(qrCode.getHash(), hash1);

        String hash2 = QRCodeHasher.hash(mockData2);
        QRCode qrCode2 = MockQRCode(mockData2);
        assertEquals(qrCode2.getHash(), hash2);

        assertNotEquals(qrCode.getHash(), qrCode2.getHash());

        assertNotNull(qrCode.getFace());
        assertNotNull(qrCode.getName());
        assertNotNull(qrCode.getScore());

        assertNotEquals(qrCode.getFace(), qrCode2.getFace());
        assertNotEquals(qrCode.getName(), qrCode2.getName());
        assertNotEquals(qrCode.getScore(), qrCode2.getScore());
    }




}
