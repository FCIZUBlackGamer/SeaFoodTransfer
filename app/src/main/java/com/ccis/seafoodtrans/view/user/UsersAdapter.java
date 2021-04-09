package com.ccis.seafoodtrans.view.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.CoomentItemListBinding;
import com.ccis.seafoodtrans.databinding.UsersItemListBinding;
import com.ccis.seafoodtrans.listner.DeleteComment;
import com.ccis.seafoodtrans.listner.DeleteUser;
import com.ccis.seafoodtrans.model.Comment;
import com.ccis.seafoodtrans.model.User;
import com.ccis.seafoodtrans.view.AdminActivity;
import com.ccis.seafoodtrans.view.ProfileFragment;
import com.ccis.seafoodtrans.view.huntingprocess.HuntingProcessFragment;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.Vholder> {


    private Context context;
    public List<User> list;
    DeleteUser deleteUser;
    UsersItemListBinding binding;

    public UsersAdapter(Context context, List<User> talabats) {
        this.context = context;
        this.list = talabats;
        deleteUser = (AdminActivity) context;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.users_item_list, parent, false);

        Log.e("Internal Offer Size", list.size() + "");

        return new Vholder(binding);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {
        binding.usernameTv.setText(list.get(position).getName());
        if (list.get(position).getType().equals("Fisherman")){
            binding.userImg.setImageDrawable(context.getDrawable(R.drawable.ic_iconfinder_fisherman));
        }else{
            binding.userImg.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_person_24));
        }

        binding.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, binding.textViewOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.product_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                FragmentTransaction transaction = ((AdminActivity)context).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.flFragment, ProfileFragment.newInstance(list.get(position)));
                                transaction.addToBackStack(null);
                                transaction.commit();
                                return true;
                            case R.id.action_delete:
                                if (deleteUser != null)
                                    deleteUser.deleteUser(list.get(position).getId());
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

        UsersItemListBinding contractItemListBinding;

        Vholder(UsersItemListBinding itemView) {
            super(itemView.getRoot());
            contractItemListBinding = itemView;
        }
    }

}
