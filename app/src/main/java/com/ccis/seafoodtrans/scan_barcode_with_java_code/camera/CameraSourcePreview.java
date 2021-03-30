package com.ccis.seafoodtrans.scan_barcode_with_java_code.camera;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.common.images.Size;
import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class CameraSourcePreview extends FrameLayout {
    private final SurfaceView surfaceView;
    private GraphicOverlay graphicOverlay;
    private boolean startRequested;
    private boolean surfaceAvailable;
    private CameraSource cameraSource;
    private Size cameraPreviewSize;
    private static final String TAG = "CameraSourcePreview";

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.graphicOverlay = findViewById(R.id.camera_preview_graphic_overlay);
    }

    public final void start(@NotNull CameraSource cameraSource) throws IOException {
        this.cameraSource = cameraSource;
        this.startRequested = true;
        this.startIfReady();
    }

    public final void stop() {
        if (cameraSource != null) {
            this.cameraSource.stop();
            this.cameraSource =  null;
            this.startRequested = false;
        }

    }

    private final void startIfReady() throws IOException {
        if (this.startRequested && this.surfaceAvailable) {
            if (cameraSource != null) {
                cameraSource.start(surfaceView.getHolder());
            }
            this.requestLayout();
            if(graphicOverlay!= null) {
                if (cameraSource != null) {
                    graphicOverlay.setCameraInfo(cameraSource);
                }
                graphicOverlay.clear();
            }
            this.startRequested = false;
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int layoutWidth = right - left;
        int layoutHeight = bottom - top;
        float previewSizeRatio;
        if (this.cameraSource != null) {
            if (this.cameraSource.getPreviewSize() != null) {
                this.cameraPreviewSize = cameraSource.getPreviewSize();
            }
        }

        if (cameraPreviewSize != null) {

            previewSizeRatio = Utils.isPortraitMode(getContext()) ? (float) cameraPreviewSize.getHeight() / (float) cameraPreviewSize.getWidth() : (float) cameraPreviewSize.getWidth() / (float) cameraPreviewSize.getHeight();
        } else {
            previewSizeRatio = (float) layoutWidth / (float) layoutHeight;
        }

        // Match the width of the child view to its parent.
        int childHeight = (int) (layoutWidth / previewSizeRatio);
        if (childHeight <= layoutHeight) {

            for ( int i = 0; i < getChildCount(); i++) {
                this.getChildAt(i).layout(0, 0, layoutWidth, childHeight);
            }
        } else {
            // When the child view is too tall to be fitted in its parent: If the child view is
            // static overlay view container (contains views such as bottom prompt chip), we apply
            // the size of the parent view to it. Otherwise, we offset the top/bottom position
            // equally to position it in the center of the parent.
           int excessLenInHalf = (childHeight - layoutHeight) / 2;
            for (int i = 0; i<getChildCount(); i++) {
                View childView = this.getChildAt(i);
                switch (childView.getId()) {
                    case R.id.static_overlay_container :
                        childView.layout(0, 0, layoutWidth, layoutHeight);
                        break;
                    default:
                        childView.layout(0, -excessLenInHalf, layoutWidth, layoutHeight + excessLenInHalf);
                }
            }
        }

        try {
            this.startIfReady();
        } catch (IOException e) {
            Log.e(TAG, "Could not start camera source.", e);
        }

    }

    public CameraSourcePreview(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);
        this.surfaceView = new SurfaceView(context);
        this.surfaceView.getHolder().addCallback (new SurfaceCallback());
        this.addView(surfaceView);
    }

    private final class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceCreated(@NotNull SurfaceHolder surface) {
            surfaceAvailable = true;

            try {
                startIfReady();
            } catch (IOException e) {
                Log.e(TAG, "Could not start camera source.", e);
            }

        }

        public void surfaceDestroyed(@NotNull SurfaceHolder surface) {
            surfaceAvailable = false;
        }

        public void surfaceChanged(@NotNull SurfaceHolder holder, int format, int width, int height) {
        }

        public SurfaceCallback() {
        }
    }

}

