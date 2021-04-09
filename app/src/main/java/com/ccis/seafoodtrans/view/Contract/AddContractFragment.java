package com.ccis.seafoodtrans.view.Contract;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccis.seafoodtrans.R;
import com.ccis.seafoodtrans.databinding.AddContractLayoutBinding;
import com.ccis.seafoodtrans.databinding.ContractListFragmentBinding;
import com.ccis.seafoodtrans.model.ListComment;
import com.ccis.seafoodtrans.model.ListContract;
import com.ccis.seafoodtrans.model.ListProduct;
import com.ccis.seafoodtrans.model.ListUser;
import com.ccis.seafoodtrans.view.comment.CommentAdapter;
import com.ccis.seafoodtrans.view.transfer.AddTransferFragment;
import com.ccis.seafoodtrans.viewmodel.ContractViewModel;
import com.ccis.seafoodtrans.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddContractFragment extends Fragment {

    private ContractViewModel model;
    UserViewModel userViewModel;
    AddContractLayoutBinding binding;
    ListUser list;
    List<String> spinnerArray = new ArrayList<String>();
    private MutableLiveData<String> userId = new MutableLiveData<>();
    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    boolean calledObserver;

    public static AddContractFragment newInstance() {

        Bundle args = new Bundle();

        AddContractFragment fragment = new AddContractFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        model = ViewModelProviders.of(this).get(ContractViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.add_contract_layout, container, false);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flFragment, ContractAdminFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        userViewModel.listAllUsers().observe(requireActivity(), new Observer<ListUser>() {
            @Override
            public void onChanged(ListUser listUser) {
                if (listUser != null && listUser.getData() != null) {
                    list = listUser;
                    for (int i = 0; i < listUser.getData().size(); i++) {
                        spinnerArray.add(listUser.getData().get(i).getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            requireActivity(), android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinner.setAdapter(adapter);
                    userId.setValue(listUser.getData().get(0).getName());
                }
            }
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userId.setValue(binding.spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        userId.observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (list != null) {
                    for (int i = 0; i < list.getData().size(); i++) {
                        if (s.equals(list.getData().get(i).getName())) {
                            userId.setValue(list.getData().get(i).getId());
                            return;
                        }
                    }
                }
            }
        });

        binding.dateFromTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField(binding.dateFromTv);
            }
        });

        binding.dateToTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField(binding.dateToTv);
            }
        });

        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!calledObserver) {
                    calledObserver = true;
                    model.addContract(binding.dateFromTv.getText().toString(), binding.dateToTv.getText().toString(), binding.subjectTIED.getText().toString(), userId.getValue()).observe(requireActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.flFragment, ContractAdminFragment.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                } else {
                    model.addContract(binding.dateFromTv.getText().toString(), binding.dateToTv.getText().toString(), binding.subjectTIED.getText().toString(), userId.getValue());
                }
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.flFragment, ContractAdminFragment.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }

    private void setDateTimeField(View btn) {
        Calendar newCalendar = dateSelected;
        datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected.set(year, monthOfYear, dayOfMonth, 0, 0);
                ((Button) btn).setText(String.format("%d-%d-%d", year, monthOfYear, dayOfMonth));
                datePickerDialog.dismiss();
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}