package com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.core.content.ContextCompat;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.CameraReticleAnimator;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.GraphicOverlay;

import org.jetbrains.annotations.NotNull;

public final class BarcodeReticleGraphic extends BarcodeGraphicBase {
    private final Paint ripplePaint;
    private final int rippleSizeOffset;
    private final int rippleStrokeWidth;
    private final int rippleAlpha;
    private final CameraReticleAnimator animator;

    public void draw(@NotNull Canvas canvas) {
        super.draw(canvas);
        this.ripplePaint.setAlpha((int)((float)this.rippleAlpha * this.animator.getRippleAlphaScale()));
        this.ripplePaint.setStrokeWidth((float)this.rippleStrokeWidth * this.animator.getRippleStrokeWidthScale());
        float offset = (float)this.rippleSizeOffset * this.animator.getRippleSizeScale();
        RectF rippleRect = new RectF(this.getBoxRect().left - offset, this.getBoxRect().top - offset, this.getBoxRect().right + offset, this.getBoxRect().bottom + offset);
        canvas.drawRoundRect(rippleRect, this.getBoxCornerRadius(), this.getBoxCornerRadius(), this.ripplePaint);
    }

    public BarcodeReticleGraphic(@NotNull GraphicOverlay overlay, @NotNull CameraReticleAnimator animator) {
        super(overlay);
        this.animator = animator;
        Resources resources = overlay.getResources();
        this.ripplePaint = new Paint();
        this.ripplePaint.setStyle(Paint.Style.STROKE);
        this.ripplePaint.setColor(ContextCompat.getColor(this.getContext(),  R.color.reticle_ripple));
        this.rippleSizeOffset = resources.getDimensionPixelOffset(R.dimen.barcode_reticle_ripple_size_offset);
        this.rippleStrokeWidth = resources.getDimensionPixelOffset(R.dimen.barcode_reticle_ripple_stroke_width);
        this.rippleAlpha = this.ripplePaint.getAlpha();
    }
}

