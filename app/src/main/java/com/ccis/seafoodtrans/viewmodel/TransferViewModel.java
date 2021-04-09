package com.ccis.seafoodtrans.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ccis.seafoodtrans.model.ListContract;
import com.ccis.seafoodtrans.model.ListTransfer;
import com.ccis.seafoodtrans.repository.TransferRepository;
import com.google.gson.Gson;

import java.util.List;

public class TransferViewModel extends ViewModel {
    TransferRepository transferRepository = new TransferRepository();
    Gson gson = new Gson();

    public MutableLiveData<String> addTransfer(List<String> productIds, List<String> amounts, String client_id) {
        return transferRepository.addTransfer(gson.toJson(productIds), gson.toJson(amounts), client_id);
    }

    public MutableLiveData<String> updateTransfer(String id, String status) {
        return transferRepository.updateTransfer(id, status);
    }

    public MutableLiveData<String> deleteTransfer(String id) {
        return transferRepository.deleteTransfer(id);
    }

    public MutableLiveData<ListTransfer> listTransfer(String id) {
        return transferRepository.listTransfer(id);
    }
}
