package com.ccis.seafoodtrans.view.huntingprocess;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.HuntingProcessFragmentBinding;
import com.ccis.seafoodtrans.model.ListProduct;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.viewmodel.ProductViewModel;

public class HuntingProcessFragment extends Fragment {
    HuntingProcessFragmentBinding binding;
    ProductViewModel model;
    RecyclerView.Adapter adapter;
    boolean doubleBackToExitPressedOnce;

    public static HuntingProcessFragment newInstance() {
        Bundle args = new Bundle();
        HuntingProcessFragment fragment = new HuntingProcessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.hunting_process_fragment, container, false);
        model = ViewModelProviders.of(this).get(ProductViewModel.class);
        binding.rec.setLayoutManager(new LinearLayoutManager(requireActivity()));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flFragment, AddProductFragment.newInstance(null));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
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
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        model.listProduct(PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID)).observe(getViewLifecycleOwner(), new Observer<ListProduct>() {
            @Override
            public void onChanged(ListProduct listProduct) {
                if (listProduct != null && listProduct.getData() != null){
                    adapter = new ProductAdapter(requireActivity(), listProduct.getData(),"normal");
                    binding.rec.setAdapter(adapter);
                }
            }
        });
    }
}
