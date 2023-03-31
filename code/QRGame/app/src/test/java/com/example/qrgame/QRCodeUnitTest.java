package com.example.qrgame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class QRCodeUnitTest {
    String mockData;
    QRCode qrCode;
    String mockUsername;

    private QRCode MockQRCode(String mockData){
        return new QRCode(mockData);
    }

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        mockData = "Random String";
        String hash1 = QRCodeHasher.hash(mockData);
        qrCode = MockQRCode(hash1);
        mockUsername = "test";
    }

    @Test
    public void testHash() throws NoSuchAlgorithmException {

        String mockData2 = "Another Random String";

        String hash1 = qrCode.getHash();
        String hash2 = QRCodeHasher.hash(mockData2);
        assertNotEquals(mockData2, hash2);
        assertNotEquals(hash1, hash2);

        String mockData1Copy = "Random String";
        String hash3 = QRCodeHasher.hash(mockData1Copy);
        assertEquals(hash1, hash3);
    }

    @Test
    public void testQRCodeObject() throws NoSuchAlgorithmException {
        String mockData2 = "Another Random String";

        // Test if hash is saved correctly
        assertEquals(qrCode.getHash(), QRCodeHasher.hash(mockData));

        String hash1 = QRCodeHasher.hash(mockData2);
        QRCode qrCode1 = MockQRCode(hash1);
        assertEquals(qrCode1.getHash(), hash1);

        // Test to make sure two different strings do not have the same hash
        assertNotEquals(qrCode.getHash(), qrCode1.getHash());

        // Test to make sure values were set for QR code
        assertNotNull(qrCode.getFace());
        assertNotNull(qrCode.getName());

        // Test to make sure generated fields are not the same over two QR codes
        assertNotEquals(qrCode.getFace(), qrCode1.getFace());
        assertNotEquals(qrCode.getName(), qrCode1.getName());
        assertNotEquals(qrCode.getScore(), qrCode1.getScore());

        // Test two strings with values close to each other
        String closeToMockData = "Random Strings";
        QRCode qrCode2 = new QRCode(QRCodeHasher.hash(closeToMockData));
        assertNotEquals(qrCode.getHash(), qrCode2.getHash());
    }

    @Test
    public void testUsers(){
        // Test actual username
        String mockUsername = "test";
        qrCode.addUsers(mockUsername);
        assertEquals(1, qrCode.getUsers().size());

        // Test empty string
        String unacceptedUser = "";
        qrCode.addUsers(unacceptedUser);
        assertEquals(1, qrCode.getUsers().size());
    }

    @Test
    public void testComments () {
        String comment = "Random Comment";
        int currentSize = qrCode.getComments().size();
        qrCode.addComments(mockUsername, comment);
        // Test if comment was added
        assertEquals(currentSize + 1, qrCode.getComments().size());
        currentSize++;
        // Test to make sure comment is the same
        String commentFromQRCode = qrCode.getComments().get(mockUsername);
        assertEquals(comment, commentFromQRCode);

        // Test empty user string
        String unacceptedUser = "";
        qrCode.addComments(unacceptedUser, comment);
        assertEquals(currentSize, qrCode.getComments().size());

        String comment1 = "Another Random Comment";

        // Test if user can enter 2 comments for one QR code
        qrCode.addComments(mockUsername, comment1);
        assertEquals(currentSize, qrCode.getComments().size());

        // Test another user commenting on the same QR code
        String mockUsername1 = "test1";
        qrCode.addComments(mockUsername1, comment1);
        assertEquals(currentSize + 1, qrCode.getComments().size());
    }
}
