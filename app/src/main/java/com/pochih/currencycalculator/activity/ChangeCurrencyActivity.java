package com.pochih.currencycalculator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.pochih.currencycalculator.AppApplication;
import com.pochih.currencycalculator.R;
import com.pochih.currencycalculator.adapter.CurrencyAdapter;
import com.pochih.currencycalculator.entity.Currency;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeCurrencyActivity extends AppCompatActivity {
    private static final String TAG = ChangeCurrencyActivity.class.getSimpleName();

    public static final int SELECT_TYPE_BASE_CURRENCY = 1;
    public static final int SELECT_TYPE_TARGET_CURRENCY = 2;

    @BindView(R.id.rvCurrency)
    RecyclerView rvCurrency;

    private ProgressDialog mDialog;
    private CurrencyAdapter adapter;
    private int selectType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_change_currency);
            ButterKnife.bind(this);

            Intent intent = this.getIntent();
            selectType = intent.getIntExtra("selectType", 0);
            if (selectType != SELECT_TYPE_BASE_CURRENCY && selectType != SELECT_TYPE_TARGET_CURRENCY) {
                Log.e(TAG, "selectType value is invalid! selectType: " + selectType);
                Toast.makeText(getApplicationContext(), "Please check network", Toast.LENGTH_LONG).show();
                finish();
            }

            //region Setup ProgressDialog
            mDialog = new ProgressDialog(this);
            mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            mDialog.setCancelable(false);
            mDialog.setCancelable(false);
            mDialog.setTitle(getString(R.string.text_Loading));
            mDialog.setMessage(getString(R.string.text_Wait_while_loading));
            //endregion

            rvCurrency.setLayoutManager(new LinearLayoutManager(this));
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            //region Http call
            mDialog.show();
            Call<List<Currency>> call = AppApplication.currencyService.getCurrency();
            call.enqueue(new Callback<List<Currency>>() {

                @Override
                public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                    List<Currency> currencies = response.body();
                    adapter = new CurrencyAdapter(ChangeCurrencyActivity.this, currencies, selectType);
                    rvCurrency.setAdapter(adapter);
                    mDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Currency>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    mDialog.dismiss();
                }
            });
            //endregion
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private CurrencyAdapter.OnItemClickListener rvCurrencyOnItemClickListener = new CurrencyAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

        }
    };
}
