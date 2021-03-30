package com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection;

import android.graphics.Bitmap;

import com.ccis.seafoodtrans.scan_barcode_with_java_code.Utils;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.FrameMetadata;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class CameraInputInfo implements InputInfo {
    private Bitmap bitmap;
    private final ByteBuffer frameByteBuffer;
    private final FrameMetadata frameMetadata;

    @NotNull
    public synchronized Bitmap getBitmap() {
        if (bitmap == null) {
            this.bitmap = Utils.convertToBitmap(this.frameByteBuffer, this.frameMetadata.getWidth(), this.frameMetadata.getHeight(), this.frameMetadata.getRotation());
        }
        return bitmap;
    }

    public CameraInputInfo(@NotNull ByteBuffer frameByteBuffer, @NotNull FrameMetadata frameMetadata) {
        super();
        this.frameByteBuffer = frameByteBuffer;
        this.frameMetadata = frameMetadata;
    }
}
