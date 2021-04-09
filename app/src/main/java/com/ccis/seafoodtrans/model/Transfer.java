package com.ccis.seafoodtrans.model;

import java.util.List;

public class Transfer {
    String id;
    String user_name;
    String status;
    List<TransferItems> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setItems(List<TransferItems> items) {
        this.items = items;
    }

    public List<TransferItems> getItems() {
        return items;
    }
}
