package com.ccis.seafoodtrans.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.listner.DeleteTransfer;
import com.ccis.seafoodtrans.listner.UpdateContractStatus;
import com.ccis.seafoodtrans.listner.UpdateTransfer;
import com.ccis.seafoodtrans.scan_barcode_with_java_code.camera.WorkflowModel;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.view.Contract.ContractListFragment;
import com.ccis.seafoodtrans.view.transfer.TransferFragment;
import com.ccis.seafoodtrans.view.viewontrace.ProductScanner;
import com.ccis.seafoodtrans.viewmodel.ContractViewModel;
import com.ccis.seafoodtrans.viewmodel.TransferViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ClientActivity extends AppCompatActivity implements UpdateContractStatus, UpdateTransfer {

    public static BottomNavigationView bottomNavigation;
    ContractViewModel model;
    TransferViewModel tModel;
    private WorkflowModel workflowModel = null;
    boolean contractObserverCalled;

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.home:
                            openFragment(ProductScanner.newInstance());
                            return true;
                        case R.id.contract:
                            openFragment(ContractListFragment.newInstance());
                            return true;
                        case R.id.transfer:
                            openFragment(TransferFragment.newInstance());
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
        setContentView(R.layout.client_fragment);
        model = ViewModelProviders.of(this).get(ContractViewModel.class);
        tModel = ViewModelProviders.of(this).get(TransferViewModel.class);
        workflowModel = ViewModelProviders.of(this).get(WorkflowModel.class);
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        askPermissions();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, ProductScanner.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultReceived(String object) {
        if (object.contains("\"")) {
            String val = object.replace("\"", "");
            workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTING);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.flFragment, ProductProfileFragment.newInstance(val));
            transaction.addToBackStack(null);
            transaction.commit();
        }
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
        if (item.getItemId() == R.id.action_logout) {
            PreferenceController.getInstance(this).persist(PreferenceController.PREF_USER_ID, "0");
            startActivity(new Intent(this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateStatus(String id, String active) {
        Log.e("Contract Status", active);
        if (!contractObserverCalled) {
            contractObserverCalled = true;
            model.updateContractStatus(id, active).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null) {
                        Toast.makeText(ClientActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            model.updateContractStatus(id, active);
        }
    }

    @Override
    public void deleteContract(String id) {

    }

    @Override
    public void updateTransfer(String id, String status) {
        tModel.updateTransfer(id, status).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(ClientActivity.this, s, Toast.LENGTH_SHORT).show();
                openFragment(TransferFragment.newInstance());
            }
        });
    }
}