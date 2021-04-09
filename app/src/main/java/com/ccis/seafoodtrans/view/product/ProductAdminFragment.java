package com.ccis.seafoodtrans.view.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.ProductAdminListLayoutBinding;
import com.ccis.seafoodtrans.model.ListProduct;
import com.ccis.seafoodtrans.model.ListUser;
import com.ccis.seafoodtrans.view.huntingprocess.ProductAdapter;
import com.ccis.seafoodtrans.viewmodel.ProductViewModel;
import com.ccis.seafoodtrans.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;


public class ProductAdminFragment extends Fragment {

    private ProductViewModel model;
    private UserViewModel uModel;
    ProductAdminListLayoutBinding binding;
    List<String> spinnerArray = new ArrayList<String>();
    MutableLiveData<String> fisherman_id = new MutableLiveData<>();
    ListUser list;
    RecyclerView.Adapter adapter;
    boolean calledObserver;

    public static ProductAdminFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ProductAdminFragment fragment = new ProductAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        model = ViewModelProviders.of(this).get(ProductViewModel.class);
        uModel = ViewModelProviders.of(this).get(UserViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.product_admin_list_layout, container, false);
        binding.rec.setLayoutManager(new LinearLayoutManager(requireActivity()));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        uModel.listFishermen().observe(requireActivity(), new Observer<ListUser>() {
            @Override
            public void onChanged(ListUser listUser) {
                if (listUser != null && listUser.getData() != null) {
                    list = listUser;
                    for (int i = 0; i < listUser.getData().size(); i++) {
                        spinnerArray.add(listUser.getData().get(i).getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            requireActivity(), android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinner.setAdapter(adapter);
                    fisherman_id.setValue(listUser.getData().get(0).getName());
                }
            }
        });


        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fisherman_id.setValue(binding.spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fisherman_id.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (list != null){
                    for (int i = 0; i < list.getData().size(); i++) {
                        if (s.equals(list.getData().get(i).getName())){
                            if (!calledObserver) {
                                calledObserver = true;
                                model.listProduct(list.getData().get(i).getId()).observe(requireActivity(), new Observer<ListProduct>() {
                                    @Override
                                    public void onChanged(ListProduct listProduct) {
                                        if (listProduct !=null && listProduct.getData() != null){
                                            adapter = new ProductAdapter(requireActivity(), listProduct.getData(), "admin");
                                            binding.rec.setAdapter(adapter);
                                        }
                                    }
                                });
                            }else {
                                model.listProduct(list.getData().get(i).getId());
                            }
                            return;
                        }
                    }
                }
            }
        });
    }
}