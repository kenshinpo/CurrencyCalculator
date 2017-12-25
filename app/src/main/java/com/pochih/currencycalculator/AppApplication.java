package com.pochih.currencycalculator;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.pochih.currencycalculator.http.service.ICurrencyService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PoChih on 2017/12/20.
 */

public class AppApplication extends Application {
    private static final String TAG = AppApplication.class.getSimpleName();

    private static final String SETTINGS = "settings";
    private static final String SETTINGS_BASE_URL = "baseUrl";
    private static final String SETTINGS_BASE_CODE = "baseCode";
    private static final String SETTINGS_TARGET_CODE = "targetCode";
    private static final String SETTINGS_IS_INITIAL = "isInitial";

    private static final String DEFAULT_BASE_URL = "http://52.221.53.204:8080";
    private static final String DEFAULT_BASE_CODE = "USD";
    private static final String DEFAULT_TARGET_CODE = "SGD";

    public static AppApplication instance;
    public static ICurrencyService currencyService;
    public static LruCache<String, Bitmap> flagCache;

    @Override
    public void onCreate() {
        try {
            super.onCreate();

            //region Step 1. Initial settings
            instance = this;
            setBaseUrl(DEFAULT_BASE_URL);
            flagCache = new LruCache<>(100); // Flag image cache
            //endregion

            //region Step 2. Initial Http call setting
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppApplication.instance.getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            currencyService = retrofit.create(ICurrencyService.class);
            //endregion

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public String getBaseUrl() {
        try {
            return getSharedPreferences(SETTINGS, MODE_PRIVATE)
                    .getString(SETTINGS_BASE_URL, DEFAULT_BASE_URL);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return DEFAULT_BASE_URL;
        }
    }

    public void setBaseUrl(String baseUrl) {
        try {
            getSharedPreferences(SETTINGS, MODE_PRIVATE)
                    .edit()
                    .putString(SETTINGS_BASE_URL, baseUrl)
                    .commit();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public String getBaseCode() {
        try {
            return getSharedPreferences(SETTINGS, MODE_PRIVATE)
                    .getString(SETTINGS_BASE_CODE, DEFAULT_BASE_CODE);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return DEFAULT_BASE_CODE;
        }
    }

    public void setBaseCode(String baseCode) {
        try {
            getSharedPreferences(SETTINGS, MODE_PRIVATE)
                    .edit()
                    .putString(SETTINGS_BASE_CODE, baseCode)
                    .commit();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public String getTargetCode() {
        try {
            return getSharedPreferences(SETTINGS, MODE_PRIVATE)
                    .getString(SETTINGS_TARGET_CODE, DEFAULT_TARGET_CODE);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return DEFAULT_TARGET_CODE;
        }
    }

    public void setTargetCode(String targetCode) {
        try {
            getSharedPreferences(SETTINGS, MODE_PRIVATE)
                    .edit()
                    .putString(SETTINGS_TARGET_CODE, targetCode)
                    .commit();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean isInitial() {
        try {
            return getSharedPreferences(SETTINGS, MODE_PRIVATE)
                    .getBoolean(SETTINGS_IS_INITIAL, true);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public void setIsInitial(boolean isInitial) {
        try {
            getSharedPreferences(SETTINGS, MODE_PRIVATE)
                    .edit()
                    .putBoolean(SETTINGS_IS_INITIAL, isInitial)
                    .commit();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void putCacheImg(String key, Bitmap bitmap) {
        try {
            this.flagCache.put(key, bitmap);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public Bitmap getCacheImg(String key) {
        return this.flagCache.get(key);
    }
}
