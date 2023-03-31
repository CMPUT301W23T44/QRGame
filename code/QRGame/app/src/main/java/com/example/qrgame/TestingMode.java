package com.example.qrgame;

/**
 * Used to set up environment to be able to perform unit tests
 */
public class TestingMode {

    // Sets the environment into test mode
    public static Boolean testingMode = false;

    // Random default values
    public static String hash = "d9b8576e8b9f9c16273dbe09cb5293654161c957d7313ec815183ddac34a6534";
    public static String testUser = "testUser";
    public static String imageString = "iVBORw0KGgoAAAANSUhEUgAAAHgAAACgCAIAAABIaz";

    /**
     * Provides a mock QR code that should already exist
     * @return
     *      QR code to be used as a mock object
     */
    public static QRCode MockExistingQRCode() {
        QRCode qrCode = new QRCode(hash);
        qrCode.addComments(testUser, "Test Comment");
        qrCode.setLatLong(100, 100);
        qrCode.addUsers(testUser);
        qrCode.setLocation_image(imageString);
        return qrCode;
    }

    /**
     * Provides a mock QR code that should not exist
     * @return
     *      QR code to be used as a mock object
     */
    public static QRCode MockNewQRCode() {
        QRCode qrCode = new QRCode(hash);
        return qrCode;
    }
}
