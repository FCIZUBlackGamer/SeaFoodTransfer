package com.ccis.seafoodtrans.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ccis.seafoodtrans.model.ListContract;
import com.ccis.seafoodtrans.repository.ContractRepository;

public class ContractViewModel extends ViewModel {
    ContractRepository contractRepository = new ContractRepository();

    public MutableLiveData<String> addContract(String start_date, String end_date, String subject, String user_id) {
        return contractRepository.addContract(start_date, end_date, subject, user_id);
    }

    public MutableLiveData<String> updateContract(String id, String start_date, String end_date, String subject, String user_id) {
        return contractRepository.updateContract(id, start_date, end_date, subject, user_id);
    }

    public MutableLiveData<String> updateContractStatus(String id, String active) {
        return contractRepository.updateContractStatus(id, active);
    }

    public MutableLiveData<String> deleteContract(String id) {
        return contractRepository.deleteContract(id);
    }

    public MutableLiveData<ListContract> listContract(String id) {
        return contractRepository.listContract(id);
    }
}
