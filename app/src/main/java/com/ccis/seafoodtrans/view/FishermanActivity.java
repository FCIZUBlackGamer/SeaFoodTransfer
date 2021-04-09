package com.ccis.seafoodtrans.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.listner.DeleteProduct;
import com.ccis.seafoodtrans.listner.UpdateContractStatus;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.view.Contract.ContractListFragment;
import com.ccis.seafoodtrans.view.huntingprocess.HuntingProcessFragment;
import com.ccis.seafoodtrans.view.viewontrace.ProductScanner;
import com.ccis.seafoodtrans.viewmodel.ContractViewModel;
import com.ccis.seafoodtrans.viewmodel.ProductViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FishermanActivity extends AppCompatActivity implements UpdateContractStatus, DeleteProduct {

    public static BottomNavigationView bottomNavigation;
    ContractViewModel model;
    ProductViewModel productViewModel;
    boolean contractObserverCalled, productObserverCalled;

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            openFragment(HuntingProcessFragment.newInstance());
                            return true;
                        case R.id.contract:
                            openFragment(ContractListFragment.newInstance());
                            return true;
                        case R.id.profile:
                            openFragment(ProfileFragment.newInstance(null));
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fisherman_fragment);
        model = ViewModelProviders.of(this).get(ContractViewModel.class);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        askPermissions();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, HuntingProcessFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.CAMERA"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout){
            PreferenceController.getInstance(this).persist(PreferenceController.PREF_USER_ID,"0");
            startActivity(new Intent(this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateStatus(String id, String active) {
        Log.e("Contract Status",active);
        if (!contractObserverCalled){
            contractObserverCalled = true;
            model.updateContractStatus(id, active).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null){
                        Toast.makeText(FishermanActivity.this, s, Toast.LENGTH_SHORT).show();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.flFragment, HuntingProcessFragment.newInstance());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }else {
            model.updateContractStatus(id, active);
        }
    }

    @Override
    public void deleteContract(String id) {

    }

    @Override
    public void deleteProduct(String id) {
        Log.e("Product Id",id);
        if (!productObserverCalled){
            productObserverCalled = true;
            productViewModel.deleteProduct(id).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null){
                        Toast.makeText(FishermanActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            productViewModel.deleteProduct(id);
        }
    }
}