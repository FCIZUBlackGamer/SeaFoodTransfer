package com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection;

import android.graphics.Canvas;
import android.graphics.Path;

import com.google.mlkit.vision.barcode.Barcode;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.GraphicOverlay;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.settings.PreferenceUtils;

import org.jetbrains.annotations.NotNull;


public final class BarcodeConfirmingGraphic extends BarcodeGraphicBase {
    private final Barcode barcode;
    private GraphicOverlay overlay;

    public void draw(@NotNull Canvas canvas) {
        super.draw(canvas);
        float sizeProgress = PreferenceUtils.getProgressToMeetBarcodeSizeRequirement(this.overlay, this.barcode);
        Path path = new Path();
        if (sizeProgress > 0.95F) {
            path.moveTo(this.getBoxRect().left, this.getBoxRect().top);
            path.lineTo(this.getBoxRect().right, this.getBoxRect().top);
            path.lineTo(this.getBoxRect().right, this.getBoxRect().bottom);
            path.lineTo(this.getBoxRect().left, this.getBoxRect().bottom);
            path.close();
        } else {
            path.moveTo(this.getBoxRect().left, this.getBoxRect().top + this.getBoxRect().height() * sizeProgress);
            path.lineTo(this.getBoxRect().left, this.getBoxRect().top);
            path.lineTo(this.getBoxRect().left + this.getBoxRect().width() * sizeProgress, this.getBoxRect().top);
            path.moveTo(this.getBoxRect().right, this.getBoxRect().bottom - this.getBoxRect().height() * sizeProgress);
            path.lineTo(this.getBoxRect().right, this.getBoxRect().bottom);
            path.lineTo(this.getBoxRect().right - this.getBoxRect().width() * sizeProgress, this.getBoxRect().bottom);
        }

        canvas.drawPath(path, this.getPathPaint());
    }

    public BarcodeConfirmingGraphic(@NotNull GraphicOverlay overlay, @NotNull Barcode barcode) {
        super(overlay);
        this.overlay = overlay;
        this.barcode = barcode;
    }
}

