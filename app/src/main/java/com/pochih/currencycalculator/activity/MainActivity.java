package com.pochih.currencycalculator.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pochih.currencycalculator.R;
import com.pochih.currencycalculator.http.service.ICurrencyService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://52.221.53.204:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ICurrencyService currencyService = retrofit.create(ICurrencyService.class);
//            Call<List<Currency>> call = currencyService.getCurrency();
//            call.enqueue(new Callback<List<Currency>>() {
//                @Override
//                public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
//                    Log.i(TAG, "成功");
//                    Log.i(TAG, "response.size: " + response.body().size());
//                }
//
//                @Override
//                public void onFailure(Call<List<Currency>> call, Throwable t) {
//                    Log.e(TAG, t.getMessage());
//                }
//            });


//            Call<Exchange> call = currencyService.getExchange("JPY", "INR");
//            call.enqueue(new Callback<Exchange>() {
//                @Override
//                public void onResponse(Call<Exchange> call, Response<Exchange> response) {
//                    Log.i(TAG, "成功");
//                    Log.i(TAG,  response.body().getBaseCode() + " : " + response.body().getTargetCode() + " = " + response.body().getRate());
//                }
//
//                @Override
//                public void onFailure(Call<Exchange> call, Throwable t) {
//                    Log.e(TAG, t.getMessage());
//                }
//            });



        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }
}
