package com.pochih.currencycalculator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.pochih.currencycalculator.AppApplication;
import com.pochih.currencycalculator.R;
import com.pochih.currencycalculator.adapter.CurrencyAdapter;
import com.pochih.currencycalculator.entity.Currency;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeCurrencyActivity extends AppCompatActivity {
    private static final String TAG = ChangeCurrencyActivity.class.getSimpleName();

    public static final String INTENT_EXTRA_SELECT_TYPE = "selectType";
    public static final int SELECT_TYPE_BASE_CURRENCY = 1;
    public static final int SELECT_TYPE_TARGET_CURRENCY = 2;


    @BindView(R.id.rvCurrency)
    RecyclerView rvCurrency;

    @BindView(R.id.etCurrency)
    EditText etCurrency;

    private ProgressDialog mDialog;
    private CurrencyAdapter adapter;
    private int selectType = 0;
    private List<Currency> currencies = new ArrayList<>();
    private List<Currency> resultCurrencies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_change_currency);
            ButterKnife.bind(this);

            Intent intent = this.getIntent();
            selectType = intent.getIntExtra(INTENT_EXTRA_SELECT_TYPE, 0);
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
                    currencies.addAll(response.body());
                    resultCurrencies.addAll(response.body());
                    adapter = new CurrencyAdapter(ChangeCurrencyActivity.this, resultCurrencies, selectType);
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

            //region
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            etCurrency.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        if (currencies != null) {
                            //currencies.stream().filter(c -> c.getName().contains(s.toString())).collect(Collectors.toList());
                            //currencies.removeIf(c-> c.getName().contentEquals(s));

                            String keyword = String.valueOf(s);
                            resultCurrencies.clear();
                            if (!keyword.isEmpty()) {
                                for (int i = 0; i < currencies.size(); i++) {
                                    if (currencies.get(i).getName().toLowerCase().contains(keyword.toLowerCase()) || currencies.get(i).getCode().toLowerCase().contains(keyword.toLowerCase())) {
                                        resultCurrencies.add(currencies.get(i));
                                    }
                                }

                                //adapter = new CurrencyAdapter(ChangeCurrencyActivity.this, resultCurrencies, selectType);
                            } else {
                                resultCurrencies.addAll(currencies);
                                //adapter = new CurrencyAdapter(ChangeCurrencyActivity.this, resultCurrencies, selectType);
                            }

                            adapter.notifyDataSetChanged();
                            //rvCurrency.notify();
                        } else {

                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

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
