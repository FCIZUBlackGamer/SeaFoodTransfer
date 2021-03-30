package com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import androidx.core.content.ContextCompat;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.GraphicOverlay;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.settings.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

public abstract class BarcodeGraphicBase extends GraphicOverlay.Graphic {
    private final Paint boxPaint;
    private final Paint scrimPaint;
    private final Paint eraserPaint;
    private final float boxCornerRadius;
    @NotNull
    private final Paint pathPaint;
    @NotNull
    private final RectF boxRect;

    public final float getBoxCornerRadius() {
        return this.boxCornerRadius;
    }

    @NotNull
    public final Paint getPathPaint() {
        return this.pathPaint;
    }

    @NotNull
    public final RectF getBoxRect() {
        return this.boxRect;
    }

    public void draw(@NotNull Canvas canvas) {
        // Draws the dark background scrim and leaves the box area clear.
        canvas.drawRect(0.0F, 0.0F, (float) canvas.getWidth(), (float) canvas.getHeight(), this.scrimPaint);
        // As the stroke is always centered, so erase twice with FILL and STROKE respectively to clear
        // all area that the box rect would occupy.
        this.eraserPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(this.boxRect, this.boxCornerRadius, this.boxCornerRadius, this.eraserPaint);
        this.eraserPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(this.boxRect, this.boxCornerRadius, this.boxCornerRadius, this.eraserPaint);
        // Draws the box.
        canvas.drawRoundRect(this.boxRect, this.boxCornerRadius, this.boxCornerRadius, this.boxPaint);
    }

    public BarcodeGraphicBase(@NotNull GraphicOverlay overlay) {
        super(overlay);
        boxPaint = new Paint();
        boxPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.barcode_reticle_stroke));
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth((float) this.getContext().getResources().getDimensionPixelOffset(R.dimen.barcode_reticle_stroke_width));
        scrimPaint = new Paint();
        scrimPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.barcode_reticle_background));
        eraserPaint = new Paint();
        eraserPaint.setStrokeWidth(boxPaint.getStrokeWidth());
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        boxCornerRadius = this.getContext().getResources().getDimensionPixelOffset(R.dimen.barcode_reticle_corner_radius);
        pathPaint = new Paint();
        pathPaint.setColor(Color.WHITE);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(boxPaint.getStrokeWidth());
        pathPaint.setPathEffect(new CornerPathEffect(boxCornerRadius));
        boxRect = new RectF(PreferenceUtils.getBarcodeReticleBox(overlay));

    }
}

