package com.ccis.seafoodtrans.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.ccis.seafoodtrans.APIS.API;
import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.ProductProfileBinding;
import com.ccis.seafoodtrans.databinding.ProfileFragmentBinding;
import com.ccis.seafoodtrans.model.ListProduct;
import com.ccis.seafoodtrans.model.ListUser;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.view.viewontrace.ProductScanner;
import com.ccis.seafoodtrans.viewmodel.CommentViewModel;
import com.ccis.seafoodtrans.viewmodel.ProductViewModel;
import com.ccis.seafoodtrans.viewmodel.UserViewModel;

import static com.ccis.seafoodtrans.view.ClientActivity.bottomNavigation;

public class ProductProfileFragment extends Fragment {
    ProductProfileBinding binding;
    ProductViewModel model;
    CommentViewModel cModel;
    boolean observerCalled;
    String barcode = "";
    String productId = "";

    public static ProductProfileFragment newInstance(String barcode) {
        ProductProfileFragment fragment = new ProductProfileFragment();
        fragment.barcode = barcode;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.product_profile, container, false);
        model = ViewModelProviders.of(this).get(ProductViewModel.class);
        cModel = ViewModelProviders.of(this).get(CommentViewModel.class);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flFragment, ProductScanner.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
                bottomNavigation.getMenu().findItem(R.id.home).setChecked(true);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!observerCalled) {
                    observerCalled = true;
                    Log.e("Id", productId+"");
                    if (productId != null) {
                        cModel.addComment(binding.nameEditText.getText().toString(), PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID), productId).observe(getViewLifecycleOwner(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s != null) {
                                    binding.nameEditText.setText("");
                                    Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(requireActivity(), "Loading Product Info..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Id", productId+"");
                    if (productId != null)
                        cModel.addComment(binding.nameEditText.getText().toString(), PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID), productId);
                    else
                        Toast.makeText(requireActivity(), "Loading Product Info..", Toast.LENGTH_SHORT).show();
                }
            }

        });

        if (barcode != null)
            model.searchBarcode(barcode).observe(getViewLifecycleOwner(), new Observer<ListProduct>() {
                @Override
                public void onChanged(ListProduct listUser) {
                    if (listUser != null && listUser.getData() != null) {
                        binding.productnameTv.setText(listUser.getData().get(0).getName());
                        binding.weightTv.setText(listUser.getData().get(0).getWeight());
                        binding.priceTv.setText(listUser.getData().get(0).getPrice());
                        binding.dateTv.setText(listUser.getData().get(0).getFishing_date());
                        Glide.with(requireActivity())
                                .load(API.BASE_URL + "images/" + listUser.getData().get(0).getImage())
                                .centerCrop()
                                .into(binding.productImg);
                        productId = listUser.getData().get(0).getId();
                    }
                }
            });
    }
}
