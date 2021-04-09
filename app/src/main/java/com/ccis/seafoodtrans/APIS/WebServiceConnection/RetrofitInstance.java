package com.ccis.seafoodtrans.APIS.WebServiceConnection;

import android.util.Log;

import com.ccis.seafoodtrans.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit = null;
    private static final String TAG = "RetrofitInstance";

    public static ApiConfig getService() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .addInterceptor(interceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();

            Log.i(TAG, "retrofit instance will be created");
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(Constants.BASE_JSON_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        }
        Log.i(TAG, "retrofit instance was created");

        return retrofit.create(ApiConfig.class);
    }
}
