package com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection;


import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.CameraReticleAnimator;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.FrameProcessorBase;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.GraphicOverlay;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.WorkflowModel;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.settings.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public final class BarcodeProcessor extends FrameProcessorBase<List<Barcode>> {
    private final BarcodeScanner scanner;
    private final CameraReticleAnimator cameraReticleAnimator;
    private final WorkflowModel workflowModel;
    private static final String TAG = "BarcodeProcessor";

    @NotNull
    protected Task<List<Barcode>> detectInImage(@NotNull InputImage image) {
        return this.scanner.process(image);
    }

    private final ValueAnimator createLoadingAnimator(GraphicOverlay graphicOverlay, Barcode barcode) {
        float endProgress = 0.0000001f;
        ValueAnimator var4 = ValueAnimator.ofFloat(new float[]{0.0F, endProgress});
        var4.setDuration(1L);
        var4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (((Float) animation.getAnimatedValue()) >= (endProgress)) {
                    graphicOverlay.clear();
                    workflowModel.setWorkflowState(WorkflowModel.WorkflowState.SEARCHED);
                    workflowModel.getDetectedBarcode().setValue(barcode);
                } else {
                    graphicOverlay.invalidate();
                }
            }
        });
        return var4;
    }

    @Override
    protected void onFailureBase(@NotNull Exception e) {
        Log.e(TAG, "Barcode detection failed!", e);
    }

    public void stop() {
        super.stop();
        this.scanner.close();
    }

    @Override
    protected void onSuccessBase(@NotNull InputInfo inputInfo, @NotNull List<Barcode> results, @NotNull GraphicOverlay graphicOverlay) {
        if (this.workflowModel.isCameraLive()) {
            Log.d(TAG, "Barcode result size: " + results.size());
            // Picks the barcode, if exists, that covers the center of graphic overlay.

            Iterable iterable = (Iterable) results;
            Iterator var7 = iterable.iterator();
            Barcode barcodeInCenter;

            while (true) {
                if (var7.hasNext()) {
                    Object element$iv = var7.next();
                    Barcode barcode = (Barcode) element$iv;
                    Rect var15 = barcode.getBoundingBox();
                    boolean var17;
                    if (var15 != null) {
                        Rect boundingBox = var15;
                        RectF var16 = graphicOverlay.translateRect(boundingBox);
                        RectF box = var16;
                        var17 = box.contains((float) graphicOverlay.getWidth() / 2.0F, (float) graphicOverlay.getHeight() / 2.0F);
                    } else {
                        var17 = false;
                    }

                    if (!var17) {
                        continue;
                    }

                    barcodeInCenter = (Barcode) element$iv;
                    break;
                }

                barcodeInCenter = null;
                break;
            }
            graphicOverlay.clear();
            if (barcodeInCenter == null) {
                this.cameraReticleAnimator.start();
                graphicOverlay.add((GraphicOverlay.Graphic) (new BarcodeReticleGraphic(graphicOverlay, this.cameraReticleAnimator)));
                this.workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTING);
            } else {
                this.cameraReticleAnimator.cancel();
                float sizeProgress = PreferenceUtils.getProgressToMeetBarcodeSizeRequirement(graphicOverlay, barcodeInCenter);
                if (sizeProgress < (float) 1) {
                    graphicOverlay.add((GraphicOverlay.Graphic) (new BarcodeConfirmingGraphic(graphicOverlay, barcodeInCenter)));
                    this.workflowModel.setWorkflowState(WorkflowModel.WorkflowState.CONFIRMING);
                } else if (PreferenceUtils.shouldDelayLoadingBarcodeResult(graphicOverlay.getContext())) {
                    ValueAnimator loadingAnimator = this.createLoadingAnimator(graphicOverlay, barcodeInCenter);
                    loadingAnimator.start();
                    graphicOverlay.add((GraphicOverlay.Graphic) (new BarcodeLoadingGraphic(graphicOverlay, loadingAnimator)));
                    this.workflowModel.setWorkflowState(WorkflowModel.WorkflowState.SEARCHING);
                } else {
                    this.workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTED);
                    this.workflowModel.getDetectedBarcode().setValue(barcodeInCenter);
                }
            }

            graphicOverlay.invalidate();
        }
    }

    public BarcodeProcessor(@NotNull GraphicOverlay graphicOverlay, @NotNull WorkflowModel workflowModel) {
        super();
        this.workflowModel = workflowModel;
        BarcodeScanner var10001 = BarcodeScanning.getClient();
        this.scanner = var10001;
        this.cameraReticleAnimator = new CameraReticleAnimator(graphicOverlay);
    }


}

