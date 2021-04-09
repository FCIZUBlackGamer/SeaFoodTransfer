package com.ccis.seafoodtrans.view.transfer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.ContractListFragmentBinding;
import com.ccis.seafoodtrans.model.ListTransfer;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.viewmodel.TransferViewModel;


public class TransferFragment extends Fragment {

    TransferViewModel model;
    ContractListFragmentBinding binding;
    RecyclerView.Adapter adapter;

    public static TransferFragment newInstance() {
        TransferFragment fragment = new TransferFragment();
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        model = ViewModelProviders.of(this).get(TransferViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.contract_list_fragment, container, false);
        binding.rec.setLayoutManager(new LinearLayoutManager(requireActivity()));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flFragment, AddTransferFragment.newInstance(null));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        if (PreferenceController.getInstance(requireContext()).get(PreferenceController.PREF_USER_TYPE).equals("Admin")) {
            binding.fab.setVisibility(View.VISIBLE);
            model.listTransfer("0").observe(requireActivity(), new Observer<ListTransfer>() {
                @Override
                public void onChanged(ListTransfer listTransfer) {
                    if (listTransfer != null && listTransfer.getData() != null) {
                        adapter = new TransferAdapter(requireContext(), listTransfer.getData(), "admin");
                        binding.rec.setAdapter(adapter);
                    }
                }
            });
        } else {
            binding.fab.setVisibility(View.GONE);
            model.listTransfer(PreferenceController.getInstance(requireContext()).get(PreferenceController.PREF_USER_ID)).observe(requireActivity(), new Observer<ListTransfer>() {
                @Override
                public void onChanged(ListTransfer listTransfer) {
                    if (listTransfer != null && listTransfer.getData() != null) {
                        adapter = new TransferAdapter(requireContext(), listTransfer.getData(), "client");
                        binding.rec.setAdapter(adapter);
                    }
                }
            });
        }
    }
}