package com.ccis.seafoodtrans.repository;

import androidx.lifecycle.MutableLiveData;

import com.ccis.seafoodtrans.APIS.WebServiceConnection.RetrofitInstance;
import com.ccis.seafoodtrans.model.ListTransfer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferRepository {
    MutableLiveData<String> addTransferMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> updateTransferMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> deleteTransferMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ListTransfer> listTransferMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<String> addTransfer(String productIds, String amounts, String client_id) {
        Call<String> call = RetrofitInstance.getService().addTransfer(productIds, amounts, client_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                addTransferMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                addTransferMutableLiveData.setValue(null);
            }
        });
        return addTransferMutableLiveData;
    }

    public MutableLiveData<String> updateTransfer(String id, String status) {
        Call<String> call = RetrofitInstance.getService().updateTransfer(id, status);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                updateTransferMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                updateTransferMutableLiveData.setValue(null);
            }
        });
        return updateTransferMutableLiveData;
    }

    public MutableLiveData<String> deleteTransfer(String id) {
        Call<String> call = RetrofitInstance.getService().deleteTransfer(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                deleteTransferMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                deleteTransferMutableLiveData.setValue(null);
            }
        });
        return deleteTransferMutableLiveData;
    }

    public MutableLiveData<ListTransfer> listTransfer(String clientId) {
        Call<ListTransfer> call = RetrofitInstance.getService().listTransfer(clientId);
        call.enqueue(new Callback<ListTransfer>() {
            @Override
            public void onResponse(Call<ListTransfer> call, Response<ListTransfer> response) {
                listTransferMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListTransfer> call, Throwable t) {
                t.printStackTrace();
                listTransferMutableLiveData.setValue(null);
            }
        });
        return listTransferMutableLiveData;
    }
}
