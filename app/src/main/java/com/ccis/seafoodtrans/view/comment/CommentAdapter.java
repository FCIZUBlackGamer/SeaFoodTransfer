package com.ccis.seafoodtrans.view.comment;

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
import com.ccis.seafoodtrans.databinding.CoomentItemListBinding;
import com.ccis.seafoodtrans.listner.DeleteComment;
import com.ccis.seafoodtrans.listner.UpdateContractStatus;
import com.ccis.seafoodtrans.model.Comment;
import com.ccis.seafoodtrans.model.Contract;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.view.AdminActivity;
import com.ccis.seafoodtrans.view.ClientActivity;
import com.ccis.seafoodtrans.view.FishermanActivity;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Vholder> {


    private Context context;
    public List<Comment> list;
    DeleteComment deleteComment;
    CoomentItemListBinding binding;

    public CommentAdapter(Context context, List<Comment> talabats) {
        this.context = context;
        this.list = talabats;
        deleteComment = (AdminActivity) context;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.cooment_item_list, parent, false);

        Log.e("Internal Offer Size", list.size() + "");

        return new Vholder(binding);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {
        binding.clientNameTv.setText(list.get(position).getClient_name()+":");
        binding.commentTv.setText(list.get(position).getSubject());

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
                                if (deleteComment != null)
                                    deleteComment.deleteComment(list.get(position).getId());
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

        CoomentItemListBinding contractItemListBinding;

        Vholder(CoomentItemListBinding itemView) {
            super(itemView.getRoot());
            contractItemListBinding = itemView;
        }
    }

}
