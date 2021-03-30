package com.ccis.seafoodtrans.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

public class PreferenceController {
    private static final String DATABASE_NAME = "RSDApp";
    public static final String LANGUAGE = "lang";
    public static final String UNIT_LIST = "unitList";
    public static final String ERROR_RESULT_LIST_LIST = "errorResultList";
    public static final String ERROR_RESPONSE_CODE_LIST = "errorResponseCodeList";
    private static PreferenceController instance;
    private SharedPreferences preferences;

    public static PreferenceController getInstance(Context context) {
        if (instance == null) instance = new PreferenceController(context, DATABASE_NAME);
        return instance;
    }

    private PreferenceController(Context context, String databaseName) {
        preferences = context.getSharedPreferences(databaseName, 0);
    }

    public void persist(String key, String val) {
        preferences.edit().putString(key, val).apply();
    }

    public void persist(String key, int val) {
        preferences.edit().putInt(key, val).apply();
    }

    public String get(String key) {
        return preferences.getString(key, "");
    }
    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }
    public void clear(String key) {
        persist(key, "");
    }

    public void persistScannedBarCodes(String key, Set<String> barCodes) {
        preferences.edit().putStringSet(key, barCodes);
    }

    public <T> void setList(String key, ArrayList<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        preferences.edit().putString(key, json).commit();

    }


}

