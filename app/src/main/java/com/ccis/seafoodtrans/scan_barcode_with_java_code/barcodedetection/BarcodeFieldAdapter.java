package com.ccis.seafoodtrans.scan_barcode_with_java_code.barcodedetection;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.BarcodeFieldBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public final class BarcodeFieldAdapter extends RecyclerView.Adapter<BarcodeFieldAdapter.BarcodeFieldViewHolder> {
    private final List<BarcodeField> barcodeFieldList;
    BarcodeFieldBinding binding;
    private Context mContext;

    public BarcodeFieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.barcode_field, parent, false);
        return new BarcodeFieldViewHolder(binding);
    }

    public void onBindViewHolder(@NotNull BarcodeFieldViewHolder holder, int position) {
        holder.binding.barcodeFieldLabel.setText(mContext.getString(R.string.sscc));
        holder.binding.barcodeFieldValue.setText(barcodeFieldList.get(position).getValue());
    }


    public int getItemCount() {
        return this.barcodeFieldList.size();
    }

    public BarcodeFieldAdapter(Context context, List<BarcodeField> barcodeFieldList) {
        this.mContext = context;
        this.barcodeFieldList = barcodeFieldList;
    }


    public static final class BarcodeFieldViewHolder extends RecyclerView.ViewHolder {

        private BarcodeFieldBinding binding;

        private BarcodeFieldViewHolder(BarcodeFieldBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }
}
