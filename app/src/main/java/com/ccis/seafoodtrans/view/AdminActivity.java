package com.ccis.seafoodtrans.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.listner.DeleteComment;
import com.ccis.seafoodtrans.listner.DeleteProduct;
import com.ccis.seafoodtrans.listner.DeleteTransfer;
import com.ccis.seafoodtrans.listner.DeleteUser;
import com.ccis.seafoodtrans.listner.UpdateContractStatus;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.view.Contract.ContractAdminFragment;
import com.ccis.seafoodtrans.view.product.ProductAdminFragment;
import com.ccis.seafoodtrans.view.transfer.TransferFragment;
import com.ccis.seafoodtrans.view.user.UsersListFragment;
import com.ccis.seafoodtrans.viewmodel.CommentViewModel;
import com.ccis.seafoodtrans.viewmodel.ContractViewModel;
import com.ccis.seafoodtrans.viewmodel.ProductViewModel;
import com.ccis.seafoodtrans.viewmodel.TransferViewModel;
import com.ccis.seafoodtrans.viewmodel.UserViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdminActivity extends AppCompatActivity implements UpdateContractStatus, DeleteTransfer, DeleteProduct, DeleteComment, DeleteUser {

    private AppBarConfiguration mAppBarConfiguration;
    TransferViewModel model;
    ContractViewModel viewModel;
    ProductViewModel productViewModel;
    CommentViewModel commentViewModel;
    UserViewModel userViewModel;
    private boolean contractObserverCalled;
    private boolean productObserverCalled;
    private boolean commentObserverCalled;
    private boolean userObserverCalled;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        model = ViewModelProviders.of(this).get(TransferViewModel.class);
        viewModel = ViewModelProviders.of(this).get(ContractViewModel.class);
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_contract, R.id.nav_product, R.id.nav_transfer, R.id.nav_user, R.id.nav_comment, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.flFragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_logout){
                    PreferenceController.getInstance(AdminActivity.this).persist(PreferenceController.PREF_USER_ID,"0");
                    startActivity(new Intent(AdminActivity.this, LoginActivity.class));
                }else if (item.getItemId() == R.id.nav_contract){
                    toolbar.setTitle("Contracts");
                    openFragment(ContractAdminFragment.newInstance());
                }else if (item.getItemId() == R.id.nav_transfer){
                    toolbar.setTitle("Transfer");
                    openFragment(TransferFragment.newInstance());
                }else if (item.getItemId() == R.id.nav_product){
                    toolbar.setTitle("Products");
                    openFragment(ProductAdminFragment.newInstance());
                }else if (item.getItemId() == R.id.nav_user){
                    toolbar.setTitle("Users");
                    openFragment(UsersListFragment.newInstance());
                }
                return true;
            }
        });
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        drawer.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.flFragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void deleteTransfer(String id) {
        model.deleteTransfer(id).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(AdminActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void updateStatus(String id, String active) {
        Log.e("Contract Status",active);
        if (!contractObserverCalled){
            contractObserverCalled = true;
            viewModel.updateContractStatus(id, active).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null){
                        Toast.makeText(AdminActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            viewModel.updateContractStatus(id, active);
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
                        Toast.makeText(AdminActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            productViewModel.deleteProduct(id);
        }
    }

    @Override
    public void deleteComment(String id) {
        Log.e("Comment Id",id);
        if (!commentObserverCalled){
            commentObserverCalled = true;
            commentViewModel.deleteComment(id).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null){
                        Toast.makeText(AdminActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            commentViewModel.deleteComment(id);
        }
    }

    @Override
    public void deleteUser(String id) {
        Log.e("User Id",id);
        if (!userObserverCalled){
            userObserverCalled = true;
            userViewModel.deleteUser(id).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null){
                        Toast.makeText(AdminActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            userViewModel.deleteUser(id);
        }
    }
}