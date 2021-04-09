package com.ccis.seafoodtrans.repository;

import androidx.lifecycle.MutableLiveData;

import com.ccis.seafoodtrans.APIS.WebServiceConnection.RetrofitInstance;
import com.ccis.seafoodtrans.model.ListUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UsersRepository {
    MutableLiveData<ListUser> loginMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> updateProfileMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> deleteUserMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> registerMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ListUser> listUserMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ListUser> listClientMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ListUser> listFishermenMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ListUser> userProfileDataMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<ListUser> login(String email, String password) {
        Call<ListUser> call = RetrofitInstance.getService().login(email, password);
        call.enqueue(new Callback<ListUser>() {
            @Override
            public void onResponse(Call<ListUser> call, Response<ListUser> response) {
                if (response.body() != null && response.body().getData() != null && !response.body().getData().get(0).getId().equals("0")) {
                    loginMutableLiveData.setValue(response.body());
                }else {
                    loginMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ListUser> call, Throwable t) {
                t.printStackTrace();
                loginMutableLiveData.setValue(null);
            }
        });
        return loginMutableLiveData;
    }

    public MutableLiveData<String> register(String email, String password, String name, String phone, String type) {
        Call<String> call = RetrofitInstance.getService().register(email, password, name, phone, type);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                registerMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                registerMutableLiveData.setValue(null);
            }
        });
        return registerMutableLiveData;
    }

    public MutableLiveData<String> updateProfile(String id, String name, String phone, String type) {
        Call<String> call = RetrofitInstance.getService().updateProfile(id, name, phone, type);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                updateProfileMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                updateProfileMutableLiveData.setValue(null);
            }
        });
        return updateProfileMutableLiveData;
    }

    public MutableLiveData<String> deleteUser(String id) {
        Call<String> call = RetrofitInstance.getService().deleteUser(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                deleteUserMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                deleteUserMutableLiveData.setValue(null);
            }
        });
        return deleteUserMutableLiveData;
    }

    public MutableLiveData<ListUser> listAllUsers() {
        Call<ListUser> call = RetrofitInstance.getService().listAllUsers();
        call.enqueue(new Callback<ListUser>() {
            @Override
            public void onResponse(Call<ListUser> call, Response<ListUser> response) {
                listUserMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListUser> call, Throwable t) {
                t.printStackTrace();
                listUserMutableLiveData.setValue(null);
            }
        });
        return listUserMutableLiveData;
    }

    public MutableLiveData<ListUser> listClients() {
        Call<ListUser> call = RetrofitInstance.getService().listClients();
        call.enqueue(new Callback<ListUser>() {
            @Override
            public void onResponse(Call<ListUser> call, Response<ListUser> response) {
                listClientMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListUser> call, Throwable t) {
                t.printStackTrace();
                listClientMutableLiveData.setValue(null);
            }
        });
        return listClientMutableLiveData;
    }

    public MutableLiveData<ListUser> listFishermen() {
        Call<ListUser> call = RetrofitInstance.getService().listFishermen();
        call.enqueue(new Callback<ListUser>() {
            @Override
            public void onResponse(Call<ListUser> call, Response<ListUser> response) {
                listFishermenMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListUser> call, Throwable t) {
                t.printStackTrace();
                listFishermenMutableLiveData.setValue(null);
            }
        });
        return listFishermenMutableLiveData;
    }

    public MutableLiveData<ListUser> getProfile(String id) {
        Call<ListUser> call = RetrofitInstance.getService().getProfile(id);
        call.enqueue(new Callback<ListUser>() {
            @Override
            public void onResponse(Call<ListUser> call, Response<ListUser> response) {
                userProfileDataMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListUser> call, Throwable t) {
                t.printStackTrace();
                userProfileDataMutableLiveData.setValue(null);
            }
        });
        return userProfileDataMutableLiveData;
    }


}
