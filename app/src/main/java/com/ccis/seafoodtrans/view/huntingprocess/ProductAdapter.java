package com.ccis.seafoodtrans.view.huntingprocess;

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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ccis.seafoodtrans.APIS.API;
import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.ContractItemListBinding;
import com.ccis.seafoodtrans.databinding.ProductItemListBinding;
import com.ccis.seafoodtrans.listner.DeleteProduct;
import com.ccis.seafoodtrans.listner.UpdateContractStatus;
import com.ccis.seafoodtrans.model.Contract;
import com.ccis.seafoodtrans.model.Product;
import com.ccis.seafoodtrans.view.AdminActivity;
import com.ccis.seafoodtrans.view.ClientActivity;
import com.ccis.seafoodtrans.view.FishermanActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Vholder> {


    private Context context;
    public List<Product> list;
    ProductItemListBinding binding;
    DeleteProduct deleteProduct;

    public ProductAdapter(Context context, List<Product> talabats, String s) {
        this.context = context;
        this.list = talabats;
        if (s.equals("admin"))
            deleteProduct = (AdminActivity) context;
        else
            deleteProduct = (FishermanActivity) context;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.product_item_list, parent, false);

        Log.e("Internal Offer Size", list.size() + "");

        return new Vholder(binding);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {
        binding.productnameTv.setText(list.get(position).getName());
        binding.amountTv.setText(list.get(position).getAmount());
        binding.weightTv.setText(list.get(position).getWeight());
        binding.priceTv.setText(list.get(position).getPrice());
        binding.dateTv.setText(list.get(position).getFishing_date());

        Glide.with(context)
                .load(API.BASE_URL + "images/" + list.get(position).getImage())
                .centerCrop()
                .into(binding.productImg);


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
                                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.flFragment, AddProductFragment.newInstance(list.get(position)));
                                transaction.addToBackStack(null);
                                transaction.commit();
                                return true;
                            case R.id.action_delete:
                                if (deleteProduct != null)
                                    deleteProduct.deleteProduct(list.get(position).getId());
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

        ProductItemListBinding productItemListBinding;

        Vholder(ProductItemListBinding itemView) {
            super(itemView.getRoot());
            productItemListBinding = itemView;
        }
    }

}
