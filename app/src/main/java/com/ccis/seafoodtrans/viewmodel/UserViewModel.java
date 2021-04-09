package com.ccis.seafoodtrans.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ccis.seafoodtrans.model.ListUser;
import com.ccis.seafoodtrans.repository.UsersRepository;

public class UserViewModel extends ViewModel {
    UsersRepository usersRepository = new UsersRepository();

    public MutableLiveData<ListUser> login(String email, String password) {
        return usersRepository.login(email, password);
    }

    public MutableLiveData<String> register(String email, String password, String name, String phone, String type) {
        return usersRepository.register(email, password, name, phone, type);
    }

    public MutableLiveData<String> updateProfile(String id, String name, String phone, String type) {
        return usersRepository.updateProfile(id, name, phone, type);
    }

    public MutableLiveData<String> deleteUser(String id) {
        return usersRepository.deleteUser(id);
    }

    public MutableLiveData<ListUser> listAllUsers() {
        return usersRepository.listAllUsers();
    }

    public MutableLiveData<ListUser> listClients() {
        return usersRepository.listClients();
    }

    public MutableLiveData<ListUser> listFishermen() {
        return usersRepository.listFishermen();
    }

    public MutableLiveData<ListUser> getProfile(String id) {
        return usersRepository.getProfile(id);
    }
}
