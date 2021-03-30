package com.ccis.seafoodtrans.scan_barcode_with_java_code.camera;


import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.mlkit.vision.common.InputImage;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.ScopedExecutor;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection.CameraInputInfo;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection.InputInfo;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/** Abstract base class of [FrameProcessor].  */
public abstract class FrameProcessorBase<T> implements FrameProcessor {
    // To keep the latest frame and its metadata.
    @GuardedBy("this")
    private ByteBuffer latestFrame;

    @GuardedBy("this")
    private FrameMetadata latestFrameMetaData;

    // To keep the frame and metadata in process.
    @GuardedBy("this")
    private ByteBuffer processingFrame;
    @GuardedBy("this")
    private FrameMetadata processingFrameMetaData;
    private final ScopedExecutor executor;
    private static final String TAG = "FrameProcessorBase";

    public synchronized void process(@NotNull ByteBuffer data, @NotNull FrameMetadata frameMetadata, @NotNull GraphicOverlay graphicOverlay) {
        this.latestFrame = data;
        this.latestFrameMetaData = frameMetadata;
        if (this.processingFrame == null && this.processingFrameMetaData == null) {
            this.processLatestFrame(graphicOverlay);
        }

    }

    private final synchronized void processLatestFrame(final GraphicOverlay graphicOverlay) {
        this.processingFrame = this.latestFrame;
        this.processingFrameMetaData = this.latestFrameMetaData;
        this.latestFrame = null;
        this.latestFrameMetaData = null;
        if (processingFrame != null) {
            final ByteBuffer frame = processingFrame;
            if (processingFrameMetaData != null) {
                final FrameMetadata frameMetaData = processingFrameMetaData;
                InputImage image = InputImage.fromByteBuffer(frame, frameMetaData.getWidth(), frameMetaData.getHeight(), frameMetaData.getRotation(), InputImage.IMAGE_FORMAT_NV21);
                final long startMs = SystemClock.elapsedRealtime();
                detectInImage(image).addOnSuccessListener(executor, new OnSuccessListener<T>() {
                    @Override
                    public void onSuccess(T t) {
                        Log.d(TAG, "Latency is: "+ (SystemClock.elapsedRealtime() - startMs));
                        onSuccessBase( new CameraInputInfo(frame, frameMetaData), t, graphicOverlay);
                                processLatestFrame(graphicOverlay);
                    }
                })
                .addOnFailureListener(executor, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onFailureBase(e);
                    }
                });

            }
        }
    }

    public void stop() {
        this.executor.shutdown();
    }

    @NotNull
    protected abstract Task<T> detectInImage(@NotNull InputImage var1);

    protected abstract void onSuccessBase(@NotNull InputInfo var1, T var2, @NotNull GraphicOverlay var3);

    protected abstract void onFailureBase(@NotNull Exception var1);

    public FrameProcessorBase() {
        this.executor = new ScopedExecutor(TaskExecutors.MAIN_THREAD);
    }

}

