package com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.WorkflowModel;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/** Displays the bottom sheet to present barcode fields contained in the detected barcode.  */
public final class BarcodeResultFragment extends DialogFragment {
    private static final String TAG = "BarcodeResultFragment";
    private static final String ARG_BARCODE_FIELD_LIST = "arg_barcode_field_list";
    View view;
    ArrayList<BarcodeField> barcodeFieldList ;
    @NotNull
    public View onCreateView(@NotNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {

            view = layoutInflater.inflate(R.layout.barcode_bottom_sheet, viewGroup);
            if (getArguments() != null) {
                if (getArguments().containsKey(ARG_BARCODE_FIELD_LIST)) {
                    barcodeFieldList = getArguments().getParcelableArrayList(ARG_BARCODE_FIELD_LIST);
                    if (barcodeFieldList == null) {
                        barcodeFieldList = new ArrayList();
                    }

                }
                else {
                    Log.e(TAG, "No barcode field list passed in!");
                    barcodeFieldList = new ArrayList();
                }
            }
        RecyclerView barcodeRecyclerView = view.findViewById(R.id.barcode_field_recycler_view);;
        barcodeRecyclerView.setHasFixedSize(true);
        barcodeRecyclerView.setLayoutManager((RecyclerView.LayoutManager) (new LinearLayoutManager((Context) this.getActivity())));
        barcodeRecyclerView.setAdapter((RecyclerView.Adapter) (new BarcodeFieldAdapter(barcodeRecyclerView.getContext(), (List) barcodeFieldList)));
        return view;
    }

    public void onDismiss(@NotNull DialogInterface dialogInterface) {
        if (getActivity() != null) {
            // Back to working state after the bottom sheet is dismissed.
            ((WorkflowModel) ViewModelProviders.of(requireActivity()).get(WorkflowModel.class)).setWorkflowState(WorkflowModel.WorkflowState.DETECTING);
            EventBus.getDefault().post("DETECTING");
        }
        super.onDismiss(dialogInterface);
    }

    public static final void show(@NotNull FragmentManager fragmentManager, @NotNull ArrayList barcodeFieldArrayList) {
        BarcodeResultFragment barcodeResultFragment = new BarcodeResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_BARCODE_FIELD_LIST, barcodeFieldArrayList);
        barcodeResultFragment.setArguments(bundle);
        barcodeResultFragment.show(fragmentManager, "BarcodeResultFragment");
    }

    public static final void dismiss(@NotNull FragmentManager fragmentManager) {
        BarcodeResultFragment resultFragment = (BarcodeResultFragment)fragmentManager.findFragmentByTag(TAG);
        if (resultFragment != null) {
            resultFragment.dismiss();
        }

    }

}

