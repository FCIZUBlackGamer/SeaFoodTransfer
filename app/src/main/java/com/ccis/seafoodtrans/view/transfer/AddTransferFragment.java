package com.ccis.seafoodtrans.view.transfer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.AddProductToTransferPopupBinding;
import com.ccis.seafoodtrans.databinding.AddTransferFragmentBinding;
import com.ccis.seafoodtrans.model.ListProduct;
import com.ccis.seafoodtrans.model.ListUser;
import com.ccis.seafoodtrans.model.Product;
import com.ccis.seafoodtrans.model.Transfer;
import com.ccis.seafoodtrans.model.TransferItems;
import com.ccis.seafoodtrans.viewmodel.ProductViewModel;
import com.ccis.seafoodtrans.viewmodel.TransferViewModel;
import com.ccis.seafoodtrans.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddTransferFragment extends Fragment {
    TransferViewModel model;
    AddTransferFragmentBinding binding;
    RecyclerView.Adapter adapter;
    Transfer transfer;
    ListUser list;
    UserViewModel uModel;
    ProductViewModel productViewModel;
    AddProductToTransferPopupBinding popupBinding;
    List<TransferItems> transferItems = new ArrayList<>();
    List<String> spinnerArray = new ArrayList<String>();
    List<String> productsArray = new ArrayList<String>();
    ArrayAdapter<String> productAdapter;
    MutableLiveData<String> client_id = new MutableLiveData<>();
    List<Product> productList = new ArrayList<>();

    public static AddTransferFragment newInstance(Transfer transfer) {
        AddTransferFragment fragment = new AddTransferFragment();
        fragment.transfer = transfer;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        model = ViewModelProviders.of(this).get(TransferViewModel.class);
        uModel = ViewModelProviders.of(this).get(UserViewModel.class);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.add_transfer_fragment, container, false);
        binding.rec.setLayoutManager(new LinearLayoutManager(requireActivity()));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter = new TransferPackageAdapter(requireActivity(), transferItems);
        binding.rec.setAdapter(adapter);
        productViewModel.listProduct("0").observe(requireActivity(), new Observer<ListProduct>() {
            @Override
            public void onChanged(ListProduct listProduct) {
                productList = listProduct.getData();
                for (int i = 0; i < listProduct.getData().size(); i++) {
                    productsArray.add(listProduct.getData().get(i).getName());
                }
                productAdapter = new ArrayAdapter<String>(
                        requireActivity(), android.R.layout.simple_spinner_item, productsArray);
                productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
        });
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
                    binding.clientNameSp.setAdapter(adapter);
                    client_id.setValue(binding.clientNameSp.getSelectedItem().toString());
                }
            }
        });
        client_id.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (list != null) {
                    for (int i = 0; i < list.getData().size(); i++) {
                        if (s.equals(list.getData().get(i).getName())) {
                            client_id.setValue(list.getData().get(i).getId());
                            return;
                        }
                    }
                }
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                popupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.add_product_to_transfer_popup, null, false);
                builder.setView(popupBinding.getRoot());
                builder.setTitle("Add Item");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TransferItems it = new TransferItems();
                        it.setAmount(popupBinding.amountEditText.getText().toString());
                        for (int j = 0; j < productList.size(); j++) {
                            if (productList.get(j).getName().equals(popupBinding.spinner.getSelectedItem().toString())){
                                it.setProduct_id(productList.get(j).getId());
                                it.setName(productList.get(j).getName());
                            }
                        }
                        transferItems.add(it);
                        adapter.notifyDataSetChanged();
                        Log.e("Package Size", transferItems.size()+"");
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                if (productAdapter != null)
                    popupBinding.spinner.setAdapter(productAdapter);
            }
        });

        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> productIds = new ArrayList<>();
                List<String> amounts = new ArrayList<>();
                for (int i = 0; i < transferItems.size(); i++) {
                    productIds.add(transferItems.get(i).getProduct_id());
                    amounts.add(transferItems.get(i).getAmount());
                }
                model.addTransfer(productIds, amounts, client_id.getValue()).observe(requireActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals("Ok")){
                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.flFragment, TransferFragment.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    }
                });
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flFragment, TransferFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
