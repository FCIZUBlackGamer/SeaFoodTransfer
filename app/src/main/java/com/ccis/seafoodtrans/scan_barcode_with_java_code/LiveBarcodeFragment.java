package com.ccis.seafoodtrans.scan_barcode_with_java_code;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection.BarcodeField;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection.BarcodeProcessor;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection.BarcodeResultFragment;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.CameraSource;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.CameraSourcePreview;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.GraphicOverlay;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.WorkflowModel;
import com.google.mlkit.vision.barcode.Barcode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;

public class LiveBarcodeFragment extends Fragment {

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview = null;
    private GraphicOverlay graphicOverlay = null;
    private AnimatorSet promptChipAnimator = null;
    private WorkflowModel workflowModel = null;
    private WorkflowModel.WorkflowState currentWorkflowState = null;
    public static final String TAG = LiveBarcodeFragment.class.getSimpleName();
    static int currentFlag = -1;

    Observer observer = new Observer<WorkflowModel.WorkflowState>() {

        @Override
        public void onChanged(WorkflowModel.WorkflowState o) {

            if (currentWorkflowState != null && o == null || currentWorkflowState.equals(o)) {
                return;
            }

            currentWorkflowState = o;
            Log.d(TAG, "Current workflow state:" + currentWorkflowState.toString());

            switch (currentWorkflowState) {
                case DETECTING:
                case CONFIRMED:
                    startCameraPreview();
                    break;
                case SEARCHING:
                case DETECTED:
                case SEARCHED:
                    stopCameraPreview();
                    break;

                default:
                    if (promptChipAnimator != null) {
                        if (!promptChipAnimator.isRunning())
                            promptChipAnimator.start();
                    }
            }

        }
    };


    public static void setFlag(int flag) {
        currentFlag = flag;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_live_barcode_kotlin, container, false);
        preview = view.findViewById(R.id.camera_preview);
        graphicOverlay = view.findViewById(R.id.camera_preview_graphic_overlay);
        cameraSource = new CameraSource(graphicOverlay);
        promptChipAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(requireActivity(), R.animator.bottom_prompt_chip_enter);
        Log.d(TAG, "Current method: onCreateView");
        setUpWorkflowModel();
        return view;
    }

    private void setUpWorkflowModel() {
        workflowModel = ViewModelProviders.of(this).get(WorkflowModel.class);

        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowModel.getWorkflowState().observe(getViewLifecycleOwner(), observer);

        workflowModel.getDetectedBarcode().observe(getViewLifecycleOwner(), new Observer<Barcode>() {
            @Override
            public void onChanged(Barcode barcode) {
                if (barcode != null) {
                    ArrayList<BarcodeField> barcodeFieldList = new ArrayList<BarcodeField>();
                    barcodeFieldList.add(new BarcodeField(getString(R.string.app_name), barcode.getRawValue() + ""));
                    Log.d("Raw Value", barcode.getRawValue() + "");
                    EventBus.getDefault().post(barcode.getRawValue());
                    BarcodeResultFragment.show(requireActivity().getSupportFragmentManager(), barcodeFieldList);
                    Log.e("unitList", barcode.getRawValue() + "");


                }
            }
        });
    }

    private void startCameraPreview() {
        Log.d(TAG, "Current method: startCameraPreview");
        WorkflowModel workflowModel = this.workflowModel;
        CameraSource cameraSource = this.cameraSource;
        if (!workflowModel.isCameraLive()) {
            try {
                workflowModel.markCameraLive();
                preview.start(cameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Failed to start camera preview!", e);
                cameraSource.release();
                this.cameraSource = null;
            }
        }
    }

    private void stopCameraPreview() {
        Log.d(TAG, "Current method: stopCameraPreview");
        WorkflowModel workflowModel = this.workflowModel;
        if (workflowModel.isCameraLive()) {
            workflowModel.markCameraFrozen();
            preview.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Current method: onResume");
        workflowModel.markCameraFrozen();
        currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED;
        cameraSource.setFrameProcessor(new BarcodeProcessor(graphicOverlay, workflowModel));
        workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTING);
        BarcodeResultFragment.dismiss(requireActivity().getSupportFragmentManager());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Current method: onPause");
        currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED;
        stopCameraPreview();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Current method: onDestroy");
        workflowModel.getDetectedBarcode().removeObserver(observer);
        cameraSource.release();
        cameraSource = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(String object) {
        if (object.equals("DETECTING")) {
            workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTING);
        }
    }

}