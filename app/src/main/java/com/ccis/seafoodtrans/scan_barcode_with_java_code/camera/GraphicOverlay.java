package com.ccis.seafoodtrans.scan_barcode_with_java_code.camera;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.common.images.Size;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class GraphicOverlay extends View {
    private final Object lock;
    private int previewWidth = 0;
    private float widthScaleFactor = 1.0f;
    private int previewHeight = 0;
    private float heightScaleFactor = 1.0f;
    private final ArrayList<Graphic> graphics;

    /**
     * Removes all graphics from the overlay.
     */
    public final void clear() {
        synchronized (lock) {
            this.graphics.clear();
        }
        this.postInvalidate();
    }

    /**
     * Adds a graphic to the overlay.
     */
    public final void add(@NotNull Graphic graphic) {
        synchronized (lock) {
            graphics.add(graphic);
        }
    }

    /**
     * Sets the camera attributes for size and facing direction, which informs how to transform image
     * coordinates later.
     */
    public void setCameraInfo(@NotNull CameraSource cameraSource) {
        Size previewSize = cameraSource.getPreviewSize();
        if (previewSize != null) {
            if (Utils.isPortraitMode(this.getContext())) {
                this.previewWidth = previewSize.getHeight();
                this.previewHeight = previewSize.getWidth();
            } else {
                this.previewWidth = previewSize.getWidth();
                this.previewHeight = previewSize.getHeight();
            }

        }
    }

    public final float translateX(float x) {
        return x * this.widthScaleFactor;
    }

    public final float translateY(float y) {
        return y * this.heightScaleFactor;
    }

    /**
     * Adjusts the `rect`'s coordinate from the preview's coordinate system to the view
     * coordinate system.
     */
    @NotNull
    public final RectF translateRect(@NotNull Rect rect) {
        return new RectF(this.translateX((float) rect.left), this.translateY((float) rect.top), this.translateX((float) rect.right), this.translateY((float) rect.bottom));
    }
    /** Draws the overlay with its associated graphic objects.  */
    protected void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);
        if (this.previewWidth > 0 && this.previewHeight > 0) {
            this.widthScaleFactor = (float) this.getWidth() / (float) this.previewWidth;
            this.heightScaleFactor = (float) this.getHeight() / (float) this.previewHeight;
        }

        synchronized (lock) {
            graphics.forEach(it -> it.draw(canvas));
        }
    }

    /**
     * Base class for a custom graphics object to be rendered within the graphic overlay. Subclass
     * this and implement the [Graphic.draw] method to define the graphics element. Add
     * instances to the overlay using [GraphicOverlay.add].
     */
    public GraphicOverlay(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);
        this.lock = new Object();
        this.widthScaleFactor = 1.0F;
        this.heightScaleFactor = 1.0F;
        this.graphics = new ArrayList();
    }

    public abstract static class Graphic {
        @NotNull
        private final Context context;
        @NotNull
        private final GraphicOverlay overlay;

        @NotNull
        protected final Context getContext() {
            return this.context;
        }

        /** Draws the graphic on the supplied canvas.  */
        public abstract void draw(@NotNull Canvas var1);


        protected Graphic(@NotNull GraphicOverlay overlay) {
            super();
            this.overlay = overlay;
            context = this.overlay.getContext();
        }

        @NotNull
        public GraphicOverlay getOverlay() {
            return overlay;
        }
    }

}

