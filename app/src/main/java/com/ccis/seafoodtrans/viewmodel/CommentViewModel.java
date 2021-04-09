package com.ccis.seafoodtrans.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ccis.seafoodtrans.model.ListComment;
import com.ccis.seafoodtrans.repository.CommentRepository;

public class CommentViewModel extends ViewModel {
    CommentRepository commentRepository = new CommentRepository();

    public MutableLiveData<String> addComment(String subject, String clientId, String product_id) {
        return commentRepository.addComment(subject, clientId, product_id);
    }

    public MutableLiveData<String> deleteComment(String id) {
        return commentRepository.deleteComment(id);
    }

    public MutableLiveData<ListComment> listComment(String id) {
        return commentRepository.listComment(id);
    }
}
