package com.ccis.seafoodtrans.view.transfer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.PackageInTransferItemListBinding;
import com.ccis.seafoodtrans.model.TransferItems;

import java.util.List;

public class TransferPackageAdapter extends RecyclerView.Adapter<TransferPackageAdapter.Vholder> {


    private Context context;
    public List<TransferItems> list;
    PackageInTransferItemListBinding binding;

    public TransferPackageAdapter(Context context, List<TransferItems> talabats) {
        this.context = context;
        this.list = talabats;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.package_in_transfer_item_list, parent, false);

        Log.e("Internal Offer Size", list.size() + "");

        return new Vholder(binding);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {
        binding.productnameTv.setText(list.get(position).getName());
        binding.productamountTv.setText(list.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Vholder extends RecyclerView.ViewHolder {

        PackageInTransferItemListBinding contractItemListBinding;

        Vholder(PackageInTransferItemListBinding itemView) {
            super(itemView.getRoot());
            contractItemListBinding = itemView;
        }
    }

}
