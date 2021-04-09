package com.ccis.seafoodtrans.repository;

import androidx.lifecycle.MutableLiveData;

import com.ccis.seafoodtrans.APIS.WebServiceConnection.RetrofitInstance;
import com.ccis.seafoodtrans.model.ListContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractRepository {
    MutableLiveData<String> addContractMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> updateContractMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> updateContractStatusMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> deleteContractMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ListContract> listContractMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<String> addContract(String start_date, String end_date, String subject, String user_id) {
        Call<String> call = RetrofitInstance.getService().addContract(start_date, end_date, subject, user_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                addContractMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                addContractMutableLiveData.setValue(null);
            }
        });
        return addContractMutableLiveData;
    }

    public MutableLiveData<String> updateContract(String id, String start_date, String end_date, String subject, String user_id) {
        Call<String> call = RetrofitInstance.getService().updateContract(id, start_date, end_date, subject, user_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                updateContractMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                updateContractMutableLiveData.setValue(null);
            }
        });
        return updateContractMutableLiveData;
    }

    public MutableLiveData<String> updateContractStatus(String id, String active) {
        Call<String> call = RetrofitInstance.getService().updateContractStatus(id, active);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                updateContractStatusMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                updateContractStatusMutableLiveData.setValue(null);
            }
        });
        return updateContractStatusMutableLiveData;
    }

    public MutableLiveData<String> deleteContract(String id) {
        Call<String> call = RetrofitInstance.getService().deleteContract(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                deleteContractMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                deleteContractMutableLiveData.setValue(null);
            }
        });
        return deleteContractMutableLiveData;
    }

    public MutableLiveData<ListContract> listContract(String id) {
        Call<ListContract> call = RetrofitInstance.getService().listContract(id);
        call.enqueue(new Callback<ListContract>() {
            @Override
            public void onResponse(Call<ListContract> call, Response<ListContract> response) {
                listContractMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListContract> call, Throwable t) {
                t.printStackTrace();
                listContractMutableLiveData.setValue(null);
            }
        });
        return listContractMutableLiveData;
    }
}
