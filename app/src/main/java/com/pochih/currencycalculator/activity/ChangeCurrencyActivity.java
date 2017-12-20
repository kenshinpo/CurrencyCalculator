package com.pochih.currencycalculator.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.pochih.currencycalculator.R;

import butterknife.ButterKnife;

public class ChangeCurrencyActivity extends AppCompatActivity {
    private static final String TAG = ChangeCurrencyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_change_currency);
            ButterKnife.bind(this);

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
}
