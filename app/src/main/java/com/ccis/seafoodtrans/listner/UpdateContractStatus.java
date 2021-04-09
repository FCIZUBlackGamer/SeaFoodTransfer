package com.ccis.seafoodtrans.listner;

public interface UpdateContractStatus {
    void updateStatus(String id, String active);
    void deleteContract(String id);
}
