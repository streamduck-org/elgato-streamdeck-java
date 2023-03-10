package org.streamduck.elgato_streamdeck.info;

import java.util.HexFormat;

/**
 * Enum describing various kinds of Stream Decks out there
 */
public enum DeviceKind {
    ORIGINAL,
    ORIGINAL_V2,
    MINI,
    XL,
    XL_V2,
    MK2,
    MINI_MK2,
    PEDAL;

    /**
     * Gets key count
     * @return Amount of buttons that the device has
     */
    public short getKeyCount() {
        return switch (this) {
            case ORIGINAL, ORIGINAL_V2, MK2 -> 15;
            case MINI, MINI_MK2 -> 6;
            case XL, XL_V2 -> 32;
            case PEDAL -> 3;
        };
    }

    /**
     * Gets row count
     * @return Amount of button rows that the device has
     */
    public short getRowCount() {
        return switch (this) {
            case ORIGINAL, ORIGINAL_V2, MK2 -> 3;
            case XL, XL_V2 -> 4;
            case MINI, MINI_MK2 -> 2;
            case PEDAL -> 1;
        };
    }

    /**
     * Gets column count
     * @return Amount of button columns that the device has
     */
    public short getColumnCount() {
        return switch (this) {
            case ORIGINAL, ORIGINAL_V2, MK2 -> 5;
            case MINI, MINI_MK2, PEDAL -> 3;
            case XL, XL_V2 -> 8;
        };
    }

    /**
     * Checks if device has a screen
     * @return If it has a screen or not
     */
    public boolean hasScreen() {
        return this != DeviceKind.PEDAL;
    }

    /**
     * Gets device key layout
     * @return Button row and column count of the device
     */
    public KeyLayout getKeyLayout() {
        return new KeyLayout(getRowCount(), getColumnCount());
    }

    /**
     * Gets image format
     * @return Format of images that the device uses
     */
    public ImageFormat getImageFormat() {
        return switch (this) {
            case ORIGINAL -> new ImageFormat(
                    ImageMode.BMP,
                    new ImageSize(72, 72),
                    ImageRotation.ROT_0,
                    ImageMirroring.BOTH
            );

            case ORIGINAL_V2, MK2 -> new ImageFormat(
                    ImageMode.JPEG,
                    new ImageSize(72, 72),
                    ImageRotation.ROT_0,
                    ImageMirroring.BOTH
            );

            case MINI, MINI_MK2 -> new ImageFormat(
                    ImageMode.BMP,
                    new ImageSize(80, 80),
                    ImageRotation.ROT_90,
                    ImageMirroring.Y
            );

            case XL, XL_V2 -> new ImageFormat(
                    ImageMode.JPEG,
                    new ImageSize(96, 96),
                    ImageRotation.ROT_0,
                    ImageMirroring.BOTH
            );

            case PEDAL -> ImageFormat.empty();
        };
    }

    private static final String BMP_HEADER = "424df63c000000000000360000002800000048000000480000000100180000000000c03c0000c40e0000c40e00000000000000000000";

    private static final byte[] ORIGINAL_BLANK = HexFormat.of().parseHex(
            BMP_HEADER + "00".repeat(72 * 72 * 3)
    );

    private static final byte[] MINI_BLANK = HexFormat.of().parseHex(
            BMP_HEADER + "00".repeat(80 * 80 * 3)
    );

    private static final byte[] ORIGINAL_V2_BLANK = HexFormat.of().parseHex("ffd8ffe000104a46494600010100000100010000ffdb004300080606070605080707070909080a0c140d0c0b0b0c1912130f141d1a1f1e1d1a1c1c20242e2720222c231c1c2837292c30313434341f27393d38323c2e333432ffdb0043010909090c0b0c180d0d1832211c213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232ffc00011080048004803012200021101031101ffc4001f0000010501010101010100000000000000000102030405060708090a0bffc400b5100002010303020403050504040000017d01020300041105122131410613516107227114328191a1082342b1c11552d1f02433627282090a161718191a25262728292a3435363738393a434445464748494a535455565758595a636465666768696a737475767778797a838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae1e2e3e4e5e6e7e8e9eaf1f2f3f4f5f6f7f8f9faffc4001f0100030101010101010101010000000000000102030405060708090a0bffc400b51100020102040403040705040400010277000102031104052131061241510761711322328108144291a1b1c109233352f0156272d10a162434e125f11718191a262728292a35363738393a434445464748494a535455565758595a636465666768696a737475767778797a82838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae2e3e4e5e6e7e8e9eaf2f3f4f5f6f7f8f9faffda000c03010002110311003f00f9fe8a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a00fffd9");

    private static final byte[] XL_BLANK = HexFormat.of().parseHex("ffd8ffe000104a46494600010100000100010000ffdb004300080606070605080707070909080a0c140d0c0b0b0c1912130f141d1a1f1e1d1a1c1c20242e2720222c231c1c2837292c30313434341f27393d38323c2e333432ffdb0043010909090c0b0c180d0d1832211c213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232ffc00011080060006003012200021101031101ffc4001f0000010501010101010100000000000000000102030405060708090a0bffc400b5100002010303020403050504040000017d01020300041105122131410613516107227114328191a1082342b1c11552d1f02433627282090a161718191a25262728292a3435363738393a434445464748494a535455565758595a636465666768696a737475767778797a838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae1e2e3e4e5e6e7e8e9eaf1f2f3f4f5f6f7f8f9faffc4001f0100030101010101010101010000000000000102030405060708090a0bffc400b51100020102040403040705040400010277000102031104052131061241510761711322328108144291a1b1c109233352f0156272d10a162434e125f11718191a262728292a35363738393a434445464748494a535455565758595a636465666768696a737475767778797a82838485868788898a92939495969798999aa2a3a4a5a6a7a8a9aab2b3b4b5b6b7b8b9bac2c3c4c5c6c7c8c9cad2d3d4d5d6d7d8d9dae2e3e4e5e6e7e8e9eaf2f3f4f5f6f7f8f9faffda000c03010002110311003f00f9fe8a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a0028a28a00fffd9");

    /**
     * Provides byte array that can be used as a blank image on the respective device
     * @return Returned byte array
     */
    public byte[] getBlankImage() {
        return switch (this) {
            case ORIGINAL -> ORIGINAL_BLANK;
            case MINI, MINI_MK2 -> MINI_BLANK;
            case ORIGINAL_V2, MK2 -> ORIGINAL_V2_BLANK;
            case XL, XL_V2 -> XL_BLANK;
            case PEDAL -> new byte[]{};
        };
    }
}
