package com.ccis.seafoodtrans.view.viewontrace;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.ProductScannerLayoutBinding;
import com.ccis.seafoodtrans.viewmodel.ProductViewModel;

public class ProductScanner extends Fragment {
    ProductScannerLayoutBinding binding;
    ProductViewModel model;
    private static final int PERMISSION_REQUEST_CODE = 5;
    boolean doubleBackToExitPressedOnce = false;


    public static ProductScanner newInstance() {

        Bundle args = new Bundle();

        ProductScanner fragment = new ProductScanner();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.product_scanner_layout, container, false);
        model = ViewModelProviders.of(this).get(ProductViewModel.class);
        if (!checkPermission()) {
            requestPermission();
        }
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
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
    }


    private boolean checkPermission() {
//        Toast.makeText(getActivity(), "Start checkPermission", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            checkPermission();
            return false;
        } else {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                showMessageOKCancel("You need to allow access permissions",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission();
                            }
                        });
            }

        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(requireActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void requestPermission() {

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

        }
    }

}
