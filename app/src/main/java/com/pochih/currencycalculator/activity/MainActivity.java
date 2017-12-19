package com.pochih.currencycalculator.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.pochih.currencycalculator.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }


    class ApiAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
