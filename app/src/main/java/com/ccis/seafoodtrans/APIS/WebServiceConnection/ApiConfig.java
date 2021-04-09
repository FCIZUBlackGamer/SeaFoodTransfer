package com.ccis.seafoodtrans.APIS.WebServiceConnection;

import com.ccis.seafoodtrans.APIS.API;
import com.ccis.seafoodtrans.model.ListComment;
import com.ccis.seafoodtrans.model.ListContract;
import com.ccis.seafoodtrans.model.ListProduct;
import com.ccis.seafoodtrans.model.ListTransfer;
import com.ccis.seafoodtrans.model.ListUser;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiConfig {

    @FormUrlEncoded
    @POST(API.LOGIN_URL)
    Call<ListUser> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST(API.REGISTER_URL)
    Call<String> register(@Field("email") String email, @Field("password") String password, @Field("name") String name, @Field("phone") String phone, @Field("type") String type);

    @FormUrlEncoded
    @POST(API.UPDATE_PROFILE_URL)
    Call<String> updateProfile(@Field("id") String id, @Field("name") String name, @Field("phone") String phone, @Field("type") String type);

    @FormUrlEncoded
    @POST(API.DELETE_USER_URL)
    Call<String> deleteUser(@Field("id") String id);

    @FormUrlEncoded
    @POST(API.ADD_PRODUCT_URL)
    Call<String> addProduct(@Field("name") String name, @Field("price") String price, @Field("barcode") String barcode, @Field("weight") String weight, @Field("amount") String amount, @Field("fishing_date") String fishing_date, @Field("fisherman_id") String fisherman_id, @Field("file_name") String file_name);

    @Multipart
    @POST(API.ADD_PRODUCT_IMAGE_URL)
    Call<String> uploadImage(@Part MultipartBody.Part image, @Part("file") RequestBody file_name);

    @FormUrlEncoded
    @POST(API.ADD_PRODUCT_URL)
    Call<String> addProduct(@Field("name") String name, @Field("price") String price, @Field("barcode") String barcode,@Field("weight") String weight, @Field("amount") String amount, @Field("fishing_date") String fishing_date, @Field("fisherman_id") String fisherman_id);

    @FormUrlEncoded
    @POST(API.ADD_COMMENT_URL)
    Call<String> addComment(@Field("subject") String subject, @Field("client_id") String client_id, @Field("product_id") String product_id);

    @FormUrlEncoded
    @POST(API.ADD_CONTRACT_URL)
    Call<String> addContract(@Field("start_date") String start_date, @Field("end_date") String end_date, @Field("subject") String subject, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(API.ADD_TRANSFER_URL)
    Call<String> addTransfer(@Field("product_ids") String product_ids, @Field("amounts") String amounts, @Field("client_id") String client_id);


    @FormUrlEncoded
    @POST(API.UPDATE_PRODUCT_URL)
    Call<String> updateProduct(@Field("id") String id, @Field("name") String name, @Field("price") String price ,@Field("weight") String weight, @Field("amount") String amount, @Field("fisherman_id") String fisherman_id);

    @FormUrlEncoded
    @POST(API.UPDATE_CONTRACT_URL)
    Call<String> updateContract(@Field("id") String id, @Field("start_date") String start_date, @Field("end_date") String end_date, @Field("subject") String subject, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(API.UPDATE_CONTRACT_STATUS_URL)
    Call<String> updateContractStatus(@Field("id") String id, @Field("active") String active);

    @FormUrlEncoded
    @POST(API.UPDATE_TRANSFER_URL)
    Call<String> updateTransfer(@Field("id") String id,@Field("status") String status);

    @FormUrlEncoded
    @POST(API.LIST_PRODUCT_URL)
    Call<ListProduct> listProduct(@Field("id") String id);

    @FormUrlEncoded
    @POST(API.SEARCH_BARCODE_PRODUCT_URL)
    Call<ListProduct> searchBarcode(@Field("id") String id);

    /*
    	if ($id == "0") {
		$getproductsql="SELECT * FROM product ;";
	}else{
		$getproductsql="SELECT * FROM product where fisherman_id = '$id';";
	}

    * */
    @FormUrlEncoded
    @POST(API.LIST_COMMENT_URL)
    Call<ListComment> listComment(@Field("product_id") String product_id);

    @FormUrlEncoded
    @POST(API.LIST_CONTRACT_URL)
    Call<ListContract> listContract(@Field("id") String id);

    @FormUrlEncoded
    @POST(API.LIST_TRANSFER_URL)
    Call<ListTransfer> listTransfer(@Field("client_id") String client_id);
    /*
    if ($id == "0") {//List All transfer_package
		$getproductsql="SELECT * FROM transfer_package ;";
	}else{
		$getproductsql="SELECT * FROM transfer_package where client_id = '$id';";
	}


    * */

    @POST(API.LIST_ALL_USERS_CONTRACT_URL)
    Call<ListUser> listAllUsers();

    @POST(API.LIST_CLIENTS_URL)
    Call<ListUser> listClients();

    @POST(API.LIST_FISHERMEN_URL)
    Call<ListUser> listFishermen();

    @FormUrlEncoded
    @POST(API.LIST_UserProfile_URL)
    Call<ListUser> getProfile(@Field("id") String id);

    @FormUrlEncoded
    @POST(API.DELETE_COMMENT_URL)
    Call<String> deleteComment(@Field("id") String id);
    @FormUrlEncoded
    @POST(API.DELETE_CONTRACT_URL)
    Call<String> deleteContract(@Field("id") String id);
    @FormUrlEncoded
    @POST(API.DELETE_PRODUCT_URL)
    Call<String> deleteProduct(@Field("id") String id);
    @FormUrlEncoded
    @POST(API.DELETE_TRANSFER_URL)
    Call<String> deleteTransfer(@Field("id") String id);

}
