package com.ccis.seafoodtrans.view;

import android.os.Bundle;
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

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.ProfileFragmentBinding;
import com.ccis.seafoodtrans.model.ListUser;
import com.ccis.seafoodtrans.model.User;
import com.ccis.seafoodtrans.utils.PreferenceController;
import com.ccis.seafoodtrans.view.Contract.ContractAdminFragment;
import com.ccis.seafoodtrans.view.Contract.ContractListFragment;
import com.ccis.seafoodtrans.view.huntingprocess.HuntingProcessFragment;
import com.ccis.seafoodtrans.view.user.UsersListFragment;
import com.ccis.seafoodtrans.view.viewontrace.ProductScanner;
import com.ccis.seafoodtrans.viewmodel.UserViewModel;

import static com.ccis.seafoodtrans.view.ClientActivity.bottomNavigation;

public class ProfileFragment extends Fragment {
    ProfileFragmentBinding binding;
    UserViewModel model;
    boolean observerCalled;
    User user;

    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        fragment.user = user;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);
        model = ViewModelProviders.of(this).get(UserViewModel.class);
        binding.emailEditText.setEnabled(false);
        disableViews();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_TYPE).equals("Fisherman")) {
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.flFragment, HuntingProcessFragment.newInstance());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    bottomNavigation.getMenu().findItem(R.id.home).setChecked(true);
                } else if (PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_TYPE).equals("Client")) {
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.flFragment, ProductScanner.newInstance());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    bottomNavigation.getMenu().findItem(R.id.home).setChecked(true);
                } else if (PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_TYPE).equals("Admin")) {
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.flFragment, ContractAdminFragment.newInstance());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
        return binding.getRoot();
    }

    private void disableViews() {
        binding.nameEditText.setEnabled(false);
        binding.phoneEditText.setEnabled(false);
        binding.spinner.setEnabled(false);
        binding.cancelBtn.setVisibility(View.GONE);
        binding.okBtn.setText(getString(R.string.edit));
    }

    private void enableViews() {
        binding.nameEditText.setEnabled(true);
        binding.phoneEditText.setEnabled(true);
        binding.spinner.setEnabled(true);
        binding.cancelBtn.setVisibility(View.VISIBLE);
        binding.okBtn.setText(getString(R.string.save));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (user != null) {
            binding.emailEditText.setText(user.getEmail());
            binding.emailEditText.setEnabled(false);
            binding.nameEditText.setText(user.getName());
            binding.phoneEditText.setText(user.getPhone());
            if (user.getType().equals("Fisherman")) {
                binding.spinner.setSelection(0);
            } else if (user.getType().equals("Client")) {
                binding.spinner.setSelection(1);
            }
            binding.okBtn.setText(getString(R.string.save));
            binding.cancelBtn.setVisibility(View.VISIBLE);
            binding.phoneEditText.setEnabled(true);
            binding.nameEditText.setEnabled(true);
            binding.emailEditText.setEnabled(true);
            binding.spinner.setEnabled(true);
        }
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.okBtn.getText().equals(getString(R.string.edit))) {
                    enableViews();
                } else {
                    if (!observerCalled) {
                        observerCalled = true;
                        if (user == null)
                            model.updateProfile(PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID), binding.nameEditText.getText().toString(), binding.phoneEditText.getText().toString(), binding.spinner.getSelectedItem().toString()).observe(getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    if (s != null) {
                                        if (s.equals("Done")) {
                                            disableViews();
                                        }
                                        Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        else
                            model.updateProfile(user.getId(), binding.nameEditText.getText().toString(), binding.phoneEditText.getText().toString(), binding.spinner.getSelectedItem().toString()).observe(getViewLifecycleOwner(), new Observer<String>() {
                                @Override
                                public void onChanged(String s) {
                                    if (s != null) {
                                        if (s.equals("Done")) {
                                            disableViews();
                                        }
                                        Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    } else {
                        if (user == null)
                            model.updateProfile(PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID), binding.nameEditText.getText().toString(), binding.phoneEditText.getText().toString(), binding.spinner.getSelectedItem().toString());
                        else
                            model.updateProfile(user.getId(), binding.nameEditText.getText().toString(), binding.phoneEditText.getText().toString(), binding.spinner.getSelectedItem().toString());

                    }
                }
            }
        });
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableViews();
                if (user != null){
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.flFragment, UsersListFragment.newInstance());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        if (!PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_TYPE).equals("Admin")) {
            model.getProfile(PreferenceController.getInstance(requireActivity()).get(PreferenceController.PREF_USER_ID)).observe(getViewLifecycleOwner(), new Observer<ListUser>() {
                @Override
                public void onChanged(ListUser listUser) {
                    if (listUser != null && listUser.getData() != null) {
                        binding.nameEditText.setText(listUser.getData().get(0).getName());
                        binding.emailEditText.setText(listUser.getData().get(0).getEmail());
                        binding.phoneEditText.setText(listUser.getData().get(0).getPhone());
                        if (listUser.getData().get(0).getType().equals("Fisherman")) {
                            binding.spinner.setSelection(0);
                        } else if (listUser.getData().get(0).getType().equals("Client")) {
                            binding.spinner.setSelection(1);
                        }
                    }
                }
            });
        }
    }
}
