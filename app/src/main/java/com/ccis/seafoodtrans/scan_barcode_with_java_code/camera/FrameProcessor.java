package com.ccis.seafoodtrans.scan_barcode_with_java_code.camera;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public interface FrameProcessor {
    void process(@NotNull ByteBuffer var1, @NotNull FrameMetadata var2, @NotNull GraphicOverlay var3);
    void stop();
}

