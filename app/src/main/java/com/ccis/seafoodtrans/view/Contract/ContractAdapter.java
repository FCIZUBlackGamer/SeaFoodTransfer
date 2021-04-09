package com.ccis.seafoodtrans.view.Contract;

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
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.ContractItemListBinding;
import com.ccis.seafoodtrans.listner.UpdateContractStatus;
import com.ccis.seafoodtrans.model.Contract;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.view.AdminActivity;
import com.ccis.seafoodtrans.view.ClientActivity;
import com.ccis.seafoodtrans.view.FishermanActivity;

import java.util.List;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.Vholder> {


    private Context context;
    public List<Contract> list;
    String user;
    ContractItemListBinding binding;
    UpdateContractStatus updateContractStatus;

    public ContractAdapter(Context context, List<Contract> talabats, @Nullable String s) {
        this.context = context;
        this.list = talabats;
        user = s;
        if (PreferenceController.getInstance(context).get(PreferenceController.PREF_USER_TYPE).equals("Fisherman")) {
            updateContractStatus = (FishermanActivity) context;
        } else if (PreferenceController.getInstance(context).get(PreferenceController.PREF_USER_TYPE).equals("Client")){
            updateContractStatus = (ClientActivity) context;
        } else if (PreferenceController.getInstance(context).get(PreferenceController.PREF_USER_TYPE).equals("Admin")){
            updateContractStatus = (AdminActivity) context;
        }
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.contract_item_list, parent, false);

        Log.e("Internal Offer Size", list.size() + "");

        return new Vholder(binding);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {
        binding.dateFromTv.setText(list.get(position).getStart_date());
        binding.dateToTv.setText(list.get(position).getEnd_date());
        binding.subjectTv.setText(list.get(position).getSubject());
        if (user.equals("admin")){
            binding.usernameTv.setVisibility(View.VISIBLE);
            binding.textView2.setVisibility(View.VISIBLE);
            binding.usernameTv.setText(list.get(position).getUser_name());
        }
        if (list.get(position).getActive().equals("0") || //not active
                list.get(position).getActive().equals("2") // pended to activate
        ) {
            binding.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_iconfinder_no));
            binding.renewBtn.setVisibility(View.VISIBLE);
            binding.textViewOptions.setVisibility(View.INVISIBLE);
        } else {
            binding.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_iconfinder_yes));
            binding.renewBtn.setVisibility(View.GONE);
            binding.textViewOptions.setVisibility(View.VISIBLE);
        }

        binding.renewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferenceController.getInstance(context).get(PreferenceController.PREF_USER_TYPE).equals("Admin")){
                    if (updateContractStatus != null)
                        updateContractStatus.updateStatus(list.get(position).getId(), "1");
                }else {
                    if (updateContractStatus != null)
                        updateContractStatus.updateStatus(list.get(position).getId(), "2");
                }
            }
        });

        binding.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, binding.textViewOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.contract_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_delete:
                                if (PreferenceController.getInstance(context).get(PreferenceController.PREF_USER_TYPE).equals("Admin")){
                                    if (updateContractStatus != null)
                                        updateContractStatus.updateStatus(list.get(position).getId(), "0");
                                }else {
                                    if (updateContractStatus != null)
                                        updateContractStatus.updateStatus(list.get(position).getId(), "3");
                                }
                                return true;
                            default:
                                return false;
                        }
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

        ContractItemListBinding contractItemListBinding;

        Vholder(ContractItemListBinding itemView) {
            super(itemView.getRoot());
            contractItemListBinding = itemView;
        }
    }

}
