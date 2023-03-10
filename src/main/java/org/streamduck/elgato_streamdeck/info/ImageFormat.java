package org.streamduck.elgato_streamdeck.info;

public record ImageFormat(
        ImageMode mode,
        ImageSize size,
        ImageRotation rotation,
        ImageMirroring mirroring
) {
    /**
     * Empty image format
     * @return Empty image format
     */
    public static ImageFormat empty() {
        return new ImageFormat(
                ImageMode.NONE,
                new ImageSize(0, 0),
                ImageRotation.ROT_0,
                ImageMirroring.NONE
        );
    }
}
