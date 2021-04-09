package com.ccis.seafoodtrans.view.user;

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
import com.ccis.seafoodtrans.databinding.ContractListFragmentBinding;
import com.ccis.seafoodtrans.databinding.ProductAdminListLayoutBinding;
import com.ccis.seafoodtrans.model.ListComment;
import com.ccis.seafoodtrans.model.ListProduct;
import com.ccis.seafoodtrans.model.ListUser;
import com.ccis.seafoodtrans.viewmodel.CommentViewModel;
import com.ccis.seafoodtrans.viewmodel.ProductViewModel;
import com.ccis.seafoodtrans.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class UsersListFragment extends Fragment {

    ContractListFragmentBinding binding;
    UserViewModel model;
    RecyclerView.Adapter adapter;

    public static UsersListFragment newInstance() {

        Bundle args = new Bundle();
        UsersListFragment fragment = new UsersListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.contract_list_fragment, container, false);
        model = ViewModelProviders.of(this).get(UserViewModel.class);
        binding.rec.setLayoutManager(new LinearLayoutManager(requireActivity()));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        model.listAllUsers().observe(requireActivity(), new Observer<ListUser>() {
            @Override
            public void onChanged(ListUser listUser) {
                if (listUser != null && listUser.getData() != null) {
                    adapter = new UsersAdapter(requireActivity(), listUser.getData());
                    binding.rec.setAdapter(adapter);
                }
            }
        });

    }
}
