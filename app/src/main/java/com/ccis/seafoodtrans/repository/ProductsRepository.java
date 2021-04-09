package com.ccis.seafoodtrans.repository;

import androidx.lifecycle.MutableLiveData;

import com.ccis.seafoodtrans.APIS.WebServiceConnection.RetrofitInstance;
import com.ccis.seafoodtrans.model.ListProduct;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsRepository {
    MutableLiveData<String> addProductMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> updateProductMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> deleteProductMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ListProduct> listProductMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ListProduct> searchBarcodeProductMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<String> addProduct(String name, String price, String barcode, String weight, String amount, String fishing_date, String fisherman_id, MultipartBody.Part image, RequestBody file_name) {
        Call<String> call = RetrofitInstance.getService().uploadImage(image, file_name);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    Call<String> call2 = RetrofitInstance.getService().addProduct(name, price, barcode, weight, amount, fishing_date, fisherman_id, response.body());
                    call2.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            addProductMutableLiveData.setValue(response.body());
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            t.printStackTrace();
                            addProductMutableLiveData.setValue(null);
                        }
                    });
                }else {
                    addProductMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                addProductMutableLiveData.setValue(null);
            }
        });
        return addProductMutableLiveData;
    }

    public MutableLiveData<String> addProduct(String name, String price, String barcode, String weight, String amount, String fishing_date, String fisherman_id) {
        Call<String> call = RetrofitInstance.getService().addProduct(name, price, barcode, weight, amount, fishing_date, fisherman_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                addProductMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                addProductMutableLiveData.setValue(null);
            }
        });
        return addProductMutableLiveData;
    }

    public MutableLiveData<String> updateProduct(String id, String name, String price, String weight, String amount, String fisherman_id) {
        Call<String> call = RetrofitInstance.getService().updateProduct(id, name, price, weight, amount, fisherman_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                updateProductMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                updateProductMutableLiveData.setValue(null);
            }
        });
        return updateProductMutableLiveData;
    }

    public MutableLiveData<String> deleteProduct(String id) {
        Call<String> call = RetrofitInstance.getService().deleteProduct(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                deleteProductMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                deleteProductMutableLiveData.setValue(null);
            }
        });
        return deleteProductMutableLiveData;
    }

    public MutableLiveData<ListProduct> searchBarcode(String id) {
        Call<ListProduct> call = RetrofitInstance.getService().searchBarcode(id);
        call.enqueue(new Callback<ListProduct>() {
            @Override
            public void onResponse(Call<ListProduct> call, Response<ListProduct> response) {
                searchBarcodeProductMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListProduct> call, Throwable t) {
                t.printStackTrace();
                searchBarcodeProductMutableLiveData.setValue(null);
            }
        });
        return searchBarcodeProductMutableLiveData;
    }

    public MutableLiveData<ListProduct> listProduct(String id) {
        Call<ListProduct> call = RetrofitInstance.getService().listProduct(id);
        call.enqueue(new Callback<ListProduct>() {
            @Override
            public void onResponse(Call<ListProduct> call, Response<ListProduct> response) {
                listProductMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListProduct> call, Throwable t) {
                t.printStackTrace();
                listProductMutableLiveData.setValue(null);
            }
        });
        return listProductMutableLiveData;
    }

}
