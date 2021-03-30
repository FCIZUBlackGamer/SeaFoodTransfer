package com.ccis.seafoodtrans.scan_barcode_with_java_code.camera;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.mlkit.vision.barcode.Barcode;

import org.jetbrains.annotations.NotNull;

public final class WorkflowModel extends AndroidViewModel {
    @NotNull
    private final MutableLiveData<WorkflowState> workflowState;
    @NotNull
    private final MutableLiveData<Barcode> detectedBarcode;
    private boolean isCameraLive;

    @NotNull
    public final MutableLiveData<WorkflowState> getWorkflowState() {
        return this.workflowState;
    }


    @NotNull
    public final MutableLiveData<Barcode> getDetectedBarcode() {
        return this.detectedBarcode;
    }

    public final boolean isCameraLive() {
        return this.isCameraLive;
    }

    private Context getContext() {
        return getApplication().getApplicationContext();
    }

    @MainThread
    public final void setWorkflowState(@NotNull WorkflowState workflowState) {
        this.workflowState.setValue(workflowState);
    }


    public final void markCameraLive() {
        this.isCameraLive = true;
    }

    public final void markCameraFrozen() {
        this.isCameraLive = false;
    }

    protected void onCleared() {
        super.onCleared();
        Log.d("TEST", "clear");
    }

    public WorkflowModel(@NotNull Application application) {
        super(application);
        this.workflowState = new MutableLiveData();
        this.detectedBarcode = new MutableLiveData();
    }

    public static enum WorkflowState {
        NOT_STARTED,
        DETECTING,
        DETECTED,
        CONFIRMING,
        CONFIRMED,
        SEARCHING,
        SEARCHED;
    }
}

