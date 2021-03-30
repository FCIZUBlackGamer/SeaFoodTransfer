package com.ccis.seafoodtrans.scan_barcode_with_java_code.camera;
/** Metadata info of a camera frame.  */

public final class FrameMetadata {
    private final int width;
    private final int height;
    private final int rotation;

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getRotation() {
        return this.rotation;
    }

    public FrameMetadata(int width, int height, int rotation) {
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
}
