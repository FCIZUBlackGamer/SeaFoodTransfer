package com.ccis.seafoodtrans.view.Contract;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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
import com.ccis.seafoodtrans.model.ListContract;
import com.ccis.seafoodtrans.viewmodel.ContractViewModel;

public class ContractAdminFragment extends Fragment {

    private ContractViewModel model;
    ContractListFragmentBinding binding;
    RecyclerView.Adapter adapter ;
    private boolean doubleBackToExitPressedOnce;

    public static ContractAdminFragment newInstance() {
        
        Bundle args = new Bundle();

        ContractAdminFragment fragment = new ContractAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        model = ViewModelProviders.of(this).get(ContractViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.contract_list_fragment, container, false);
        binding.rec.setLayoutManager(new LinearLayoutManager(requireActivity()));
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (doubleBackToExitPressedOnce) {
                    // Finish current activity
                    // Go back to previous activity or closes app if last activity
                    requireActivity().finish();

                    // Finish all activities in stack and app closes
                    requireActivity().finishAffinity();

                    // Takes you to home screen but app isnt closed
                    // Opening app takes you back to this current activity (if it hasnt been destroyed)
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    return;
                }

                doubleBackToExitPressedOnce = true;
                Toast.makeText(requireActivity(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        model.listContract("0").observe(requireActivity(), new Observer<ListContract>() {
            @Override
            public void onChanged(ListContract listContract) {
                if (listContract != null && listContract.getData() != null){
                    adapter = new ContractAdapter(requireActivity(), listContract.getData(), "admin");
                    binding.rec.setAdapter(adapter);
                }
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flFragment, AddContractFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}