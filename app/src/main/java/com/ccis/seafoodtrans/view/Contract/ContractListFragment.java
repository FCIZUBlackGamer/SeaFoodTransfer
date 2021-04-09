package com.ccis.seafoodtrans.view.Contract;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.ContractListFragmentBinding;
import com.ccis.seafoodtrans.model.ListContract;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.view.huntingprocess.HuntingProcessFragment;
import com.ccis.seafoodtrans.view.viewontrace.ProductScanner;
import com.ccis.seafoodtrans.viewmodel.ContractViewModel;

import static com.ccis.seafoodtrans.view.ClientActivity.bottomNavigation;

public class ContractListFragment extends Fragment {

    ContractListFragmentBinding binding;
    ContractViewModel model;
    RecyclerView.Adapter adapter ;

    public static ContractListFragment newInstance() {

        Bundle args = new Bundle();
        ContractListFragment fragment = new ContractListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.contract_list_fragment, container, false);
        model = ViewModelProviders.of(this).get(ContractViewModel.class);
        binding.rec.setLayoutManager(new LinearLayoutManager(requireActivity()));
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_TYPE).equals("Fisherman")) {
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.flFragment, HuntingProcessFragment.newInstance());
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_TYPE).equals("Client")) {
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.flFragment, ProductScanner.newInstance());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                bottomNavigation.getMenu().findItem(R.id.home).setChecked(true);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        model.listContract(PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID)).observe(getViewLifecycleOwner(), new Observer<ListContract>() {
            @Override
            public void onChanged(ListContract listContract) {
                if (listContract != null && listContract.getData() != null){
                    adapter = new ContractAdapter(requireActivity(), listContract.getData(), "normal");
                    binding.rec.setAdapter(adapter);
                }
            }
        });
    }
}
