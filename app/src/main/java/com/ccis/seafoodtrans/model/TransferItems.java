package com.ccis.seafoodtrans.model;

public class TransferItems {
    String id;
    String transfer_package_id;
    String product_id;
    String name;
    String amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransfer_package_id() {
        return transfer_package_id;
    }

    public void setTransfer_package_id(String transfer_package_id) {
        this.transfer_package_id = transfer_package_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
