package com.ccis.seafoodtrans.view.comment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
import com.ccis.seafoodtrans.model.ListComment;
import com.ccis.seafoodtrans.model.ListProduct;
import com.ccis.seafoodtrans.viewmodel.CommentViewModel;
import com.ccis.seafoodtrans.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class CommentListFragment extends Fragment {

    ProductAdminListLayoutBinding binding;
    CommentViewModel model;
    ProductViewModel productViewModel;
    RecyclerView.Adapter adapter ;
    List<String> spinnerArray = new ArrayList<String>();
    private MutableLiveData<String> productId = new MutableLiveData<>();
    ListProduct product;
    private boolean calledObserver ;

    public static CommentListFragment newInstance() {

        Bundle args = new Bundle();
        CommentListFragment fragment = new CommentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.product_admin_list_layout, container, false);
        model = ViewModelProviders.of(this).get(CommentViewModel.class);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        binding.rec.setLayoutManager(new LinearLayoutManager(requireActivity()));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        productViewModel.listProduct("0").observe(requireActivity(), new Observer<ListProduct>() {
            @Override
            public void onChanged(ListProduct listUser) {
                if (listUser != null && listUser.getData() != null) {
                    product = listUser;
                    for (int i = 0; i < listUser.getData().size(); i++) {
                        spinnerArray.add(listUser.getData().get(i).getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            requireActivity(), android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinner.setAdapter(adapter);
                    productId.setValue(listUser.getData().get(0).getName());
                }
            }
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productId.setValue(binding.spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        productId.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (product != null){
                    for (int i = 0; i < product.getData().size(); i++) {
                        if (s.equals(product.getData().get(i).getName())){
                            if (!calledObserver) {
                                calledObserver = true;
                                model.listComment(product.getData().get(i).getId()).observe(getViewLifecycleOwner(), new Observer<ListComment>() {
                                    @Override
                                    public void onChanged(ListComment listContract) {
                                        if (listContract != null && listContract.getData() != null){
                                            adapter = new CommentAdapter(requireActivity(), listContract.getData());
                                            binding.rec.setAdapter(adapter);
                                        }
                                    }
                                });
                            }else {
                                model.listComment(product.getData().get(i).getId());
                            }
                            return;
                        }
                    }
                }
            }
        });

    }
}
