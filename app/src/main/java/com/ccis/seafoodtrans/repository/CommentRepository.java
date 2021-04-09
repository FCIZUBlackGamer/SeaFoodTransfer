package com.ccis.seafoodtrans.repository;

import androidx.lifecycle.MutableLiveData;

import com.ccis.seafoodtrans.APIS.WebServiceConnection.RetrofitInstance;
import com.ccis.seafoodtrans.model.ListComment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentRepository {
    MutableLiveData<String> addCommentMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> deleteCommentMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ListComment> listCommentMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<String> addComment(String subject, String clientId, String product_id) {
        Call<String> call = RetrofitInstance.getService().addComment(subject, clientId, product_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                addCommentMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                addCommentMutableLiveData.setValue(null);
            }
        });
        return addCommentMutableLiveData;
    }

    public MutableLiveData<String> deleteComment(String clientId) {
        Call<String> call = RetrofitInstance.getService().deleteComment(clientId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                deleteCommentMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                deleteCommentMutableLiveData.setValue(null);
            }
        });
        return deleteCommentMutableLiveData;
    }

    public MutableLiveData<ListComment> listComment(String productId) {
        Call<ListComment> call = RetrofitInstance.getService().listComment(productId);
        call.enqueue(new Callback<ListComment>() {
            @Override
            public void onResponse(Call<ListComment> call, Response<ListComment> response) {
                listCommentMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListComment> call, Throwable t) {
                t.printStackTrace();
                listCommentMutableLiveData.setValue(null);
            }
        });
        return listCommentMutableLiveData;
    }
}
