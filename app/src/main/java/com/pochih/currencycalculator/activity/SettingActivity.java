package com.pochih.currencycalculator.activity;

import android.os.Bundle;

import com.pochih.currencycalculator.R;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initializeToolbar();
    }

}
