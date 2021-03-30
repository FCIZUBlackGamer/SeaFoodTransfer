package com.ccis.seafoodtrans.scan_barcode_with_java_code.camera;


import android.hardware.Camera;

import com.google.android.gms.common.images.Size;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class CameraSizePair {
    private final Size preview;
    private Size picture = null;

    @NotNull
    public final Size getPreview() {
        return this.preview;
    }

    @Nullable
    public final Size getPicture() {
        return this.picture;
    }

    public CameraSizePair(@NotNull Camera.Size previewSize, @Nullable Camera.Size pictureSize) {
        super();
        this.preview = new Size(previewSize.width, previewSize.height);
        if(pictureSize!=null)
        this.picture = new  Size(pictureSize.width, pictureSize.height);
    }

    public CameraSizePair(@NotNull Size previewSize, @Nullable Size pictureSize) {
        super();
        this.preview = previewSize;
        this.picture = pictureSize;
    }
}
