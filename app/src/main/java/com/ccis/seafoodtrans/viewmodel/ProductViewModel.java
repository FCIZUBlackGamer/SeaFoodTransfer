package com.ccis.seafoodtrans.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ccis.seafoodtrans.model.ListProduct;
import com.ccis.seafoodtrans.repository.ProductsRepository;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProductViewModel extends ViewModel {
    ProductsRepository productsRepository = new ProductsRepository();

    public MutableLiveData<String> addProduct(String name, String price, String barcode, String weight, String amount, String fishing_date, String fisherman_id, String mediaPath) {
        File file = new File(mediaPath);
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        return productsRepository.addProduct(name, price, barcode, weight, amount, fishing_date, fisherman_id, fileToUpload, filename);
    }

    public MutableLiveData<String> addProduct(String name, String price, String barcode, String weight, String amount, String fishing_date, String fisherman_id) {

        return productsRepository.addProduct(name, price, barcode, weight, amount, fishing_date, fisherman_id);
    }

    public MutableLiveData<String> updateProduct(String id, String name, String price, String weight, String amount, String fisherman_id) {
        return productsRepository.updateProduct(id, name, price, weight, amount, fisherman_id);
    }

    public MutableLiveData<String> deleteProduct(String id) {
        return productsRepository.deleteProduct(id);
    }

    public MutableLiveData<ListProduct> listProduct(String id) {
        return productsRepository.listProduct(id);
    }

    public MutableLiveData<ListProduct> searchBarcode(String id) {
        return productsRepository.searchBarcode(id);
    }
}
