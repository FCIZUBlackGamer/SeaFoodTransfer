package com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;

public class BitmapInputInfo implements InputInfo {
    private final Bitmap bitmap;

    @NotNull
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public BitmapInputInfo(@NotNull Bitmap bitmap) {
        super();
        this.bitmap = bitmap;
    }
}
