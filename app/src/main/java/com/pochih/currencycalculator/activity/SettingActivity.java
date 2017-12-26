package com.pochih.currencycalculator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pochih.currencycalculator.AppApplication;
import com.pochih.currencycalculator.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PoChih on 2017/12/20.
 */

public class SettingActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.etBaseUrl)
    EditText etBaseUrl;

    @BindView(R.id.spDecimals)
    Spinner spDecimals;

    @BindView(R.id.btnSave)
    Button btnSave;

    private final String[] decimals = {"0", "1", "2", "3", "4", "5", "6"};
    private int currentDecimal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_setting);
            ButterKnife.bind(this);

            initializeToolbar();

            //region UI
            etBaseUrl.setText(AppApplication.instance.getBaseUrl());
            ArrayAdapter<String> decimalList = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    decimals);
            spDecimals.setAdapter(decimalList);
            spDecimals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        currentDecimal = Integer.valueOf(decimals[position]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            for (int i = 0; i < decimals.length; i++) {
                if (Integer.valueOf(decimals[i]) == AppApplication.instance.getDecimal()) {
                    spDecimals.setSelection(i);
                    break;
                }
            }

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (String.valueOf(etBaseUrl.getText()).isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please enter base url", Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (!String.valueOf(etBaseUrl.getText()).contains("http")) {
                            Toast.makeText(getApplicationContext(), "Don't forget http:// or https://", Toast.LENGTH_LONG).show();
                            return;
                        }

                        AppApplication.instance.setBaseUrl(String.valueOf(etBaseUrl.getText()));
                        AppApplication.instance.setDecimal(currentDecimal);

                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            //endregion


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
