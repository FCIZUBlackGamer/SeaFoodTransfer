package com.ccis.seafoodtrans.view.transfer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.ContractItemListBinding;
import com.ccis.seafoodtrans.databinding.TransferItemListBinding;
import com.ccis.seafoodtrans.listner.DeleteTransfer;
import com.ccis.seafoodtrans.listner.UpdateContractStatus;
import com.ccis.seafoodtrans.listner.UpdateTransfer;
import com.ccis.seafoodtrans.model.Contract;
import com.ccis.seafoodtrans.model.Transfer;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.view.AdminActivity;
import com.ccis.seafoodtrans.view.ClientActivity;
import com.ccis.seafoodtrans.view.FishermanActivity;

import java.util.List;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.Vholder> {


    private Context context;
    public List<Transfer> list;
    TransferItemListBinding binding;
    DeleteTransfer deleteTransfer;
    UpdateTransfer updateTransfer;
    String user;

    public TransferAdapter(Context context, List<Transfer> talabats, String s) {
        this.context = context;
        this.list = talabats;
        if (PreferenceController.getInstance(context).get(PreferenceController.PREF_USER_TYPE).equals("Admin")) {
            deleteTransfer = (AdminActivity) context;
        }else if (PreferenceController.getInstance(context).get(PreferenceController.PREF_USER_TYPE).equals("Client")){
            updateTransfer = (ClientActivity) context;
        }
        user = s;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.transfer_item_list, parent, false);

        Log.e("Internal Offer Size", list.size() + "");

        return new Vholder(binding);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {
        binding.clientNameTv.setText(list.get(position).getUser_name());
        binding.statusTv.setText(list.get(position).getStatus());
        binding.rec.setLayoutManager(new LinearLayoutManager(context));
        RecyclerView.Adapter adapter = new TransferPackageAdapter(context, list.get(position).getItems());
        binding.rec.setAdapter(adapter);

        if (list.get(position).getStatus().equals("Received")) {
            binding.textViewOptions.setVisibility(View.INVISIBLE);
        } else {
            binding.textViewOptions.setVisibility(View.VISIBLE);
        }
        if (user.toLowerCase().equals("client")){
            binding.linCon.setVisibility(View.VISIBLE);
        }

        binding.receivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateTransfer != null)
                    updateTransfer.updateTransfer(list.get(position).getId(), "Received");
            }
        });

        binding.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, binding.textViewOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.transfer_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_delete) {
                            if (deleteTransfer != null)
                                deleteTransfer.deleteTransfer(list.get(position).getId());
                            return true;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Vholder extends RecyclerView.ViewHolder {

        TransferItemListBinding contractItemListBinding;

        Vholder(TransferItemListBinding itemView) {
            super(itemView.getRoot());
            contractItemListBinding = itemView;
        }
    }

}
