package com.pochih.currencycalculator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pochih.currencycalculator.AppApplication;
import com.pochih.currencycalculator.R;
import com.pochih.currencycalculator.entity.Currency;
import com.pochih.currencycalculator.entity.Exchange;
import com.pochih.currencycalculator.utility.ImageHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PoChih on 2017/12/20.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tvBaseToTarget)
    TextView tvBaseToTarget;
    @BindView(R.id.civBaseCurrencyFlag)
    CircleImageView civBaseCurrencyFlag;
    @BindView(R.id.tvBaseCurrencyCode)
    TextView tvBaseCurrencyCode;
    @BindView(R.id.etBaseCurrencyAmount)
    EditText etBaseCurrencyAmount;
    @BindView(R.id.llBaseCurrency)
    LinearLayout llBaseCurrency;
    @BindView(R.id.tvTargetToBase)
    TextView tvTargetToBase;
    @BindView(R.id.civTargetCurrencyFlag)
    CircleImageView civTargetCurrencyFlag;
    @BindView(R.id.tvTargetCurrencyCode)
    TextView tvTargetCurrencyCode;
    @BindView(R.id.etTargetCurrencyAmount)
    EditText etTargetCurrencyAmount;
    @BindView(R.id.llTargetCurrency)
    LinearLayout llTargetCurrency;

    private ProgressDialog mDialog;
    private double rateBaseToTarget = 1.0;
    private double rateTargetToBase = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);

            //region System
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            //endregion


            //region Setup ProgressDialog
            mDialog = new ProgressDialog(this);
            mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            mDialog.setCancelable(false);
            mDialog.setCancelable(false);
            mDialog.setTitle(getString(R.string.text_Loading));
            mDialog.setMessage(getString(R.string.text_Wait_while_loading));
            //endregion

            //region Setup event of views
            etBaseCurrencyAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (getCurrentFocus().getId() == etBaseCurrencyAmount.getId()) {
                        if (!s.toString().isEmpty()) {
                            try {
                                double input = Double.valueOf(s.toString());
                                etTargetCurrencyAmount.setText(AppApplication.instance.getDecimals().format(input * rateBaseToTarget));
                            } catch (Exception ex) {
                                Log.e(TAG, ex.getMessage());
                            }
                        } else {
                            etTargetCurrencyAmount.setText("");
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etTargetCurrencyAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (getCurrentFocus().getId() == etTargetCurrencyAmount.getId()) {
                        if (!s.toString().isEmpty()) {
                            try {
                                double input = Double.valueOf(s.toString());
                                etBaseCurrencyAmount.setText(AppApplication.instance.getDecimals().format(input * rateTargetToBase));
                            } catch (Exception ex) {
                                Log.e(TAG, ex.getMessage());
                            }
                        } else {
                            etBaseCurrencyAmount.setText("");
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            //endregion
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            //region Http API call
            mDialog.show();

            //region Init image cache
            Call<List<Currency>> call = AppApplication.currencyService.getCurrency();
            call.enqueue(new Callback<List<Currency>>() {

                @Override
                public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                    final List<Currency> currencies = response.body();
                    for (int i = 0; i < currencies.size(); i++) {
                        final String key = currencies.get(i).getCode().toLowerCase();
                        final String url = AppApplication.instance.getBaseUrl() + currencies.get(i).getFlagPath();
                        if (AppApplication.instance.getCacheImg(key) == null) {
                            new ImageDownloadAnsyTask().execute(url, key);

                        } else {
                            // Setup base flag image
                            if (currencies.get(i).getCode().toLowerCase().equals(AppApplication.instance.getBaseCode().toLowerCase())) {
                                civBaseCurrencyFlag.setImageBitmap(AppApplication.instance.getCacheImg(AppApplication.instance.getBaseCode().toLowerCase()));
                            }

                            // Setup target flag image
                            if (currencies.get(i).getCode().toLowerCase().equals(AppApplication.instance.getTargetCode().toLowerCase())) {
                                civTargetCurrencyFlag.setImageBitmap(AppApplication.instance.getCacheImg(AppApplication.instance.getTargetCode().toLowerCase()));
                            }
                        }
                    }
                    getExchange();
                }

                @Override
                public void onFailure(Call<List<Currency>> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    Toast.makeText(getApplicationContext(), getString(R.string.text_Please_check_your_network_connection), Toast.LENGTH_LONG).show();
                    mDialog.dismiss();
                }
            });
            //endregion

            //endregion
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            mDialog.dismiss();
        }
    }

    @OnClick({R.id.llBaseCurrency, R.id.llTargetCurrency, R.id.etBaseCurrencyAmount, R.id.etTargetCurrencyAmount})
    public void onViewClicked(View view) {

        Intent intent = new Intent(MainActivity.this, ChangeCurrencyActivity.class);

        switch (view.getId()) {
            case R.id.llBaseCurrency:
                intent.putExtra(ChangeCurrencyActivity.INTENT_EXTRA_SELECT_TYPE, ChangeCurrencyActivity.SELECT_TYPE_BASE_CURRENCY);
                startActivity(intent);
                break;
            case R.id.llTargetCurrency:
                intent.putExtra(ChangeCurrencyActivity.INTENT_EXTRA_SELECT_TYPE, ChangeCurrencyActivity.SELECT_TYPE_TARGET_CURRENCY);
                startActivity(intent);
                break;

            case R.id.etBaseCurrencyAmount:
                break;

            case R.id.etTargetCurrencyAmount:
                break;
        }
    }

    private void getExchange() {
        Call<Exchange> call = AppApplication.currencyService.getExchange(AppApplication.instance.getBaseCode(), AppApplication.instance.getTargetCode());
        call.enqueue(new Callback<Exchange>() {

            @Override
            public void onResponse(Call<Exchange> call, Response<Exchange> response) {
                try {
                    Exchange result = response.body();
                    // Base currency UI
                    rateBaseToTarget = result.getRate();
                    tvBaseCurrencyCode.setText(result.getBaseCode().toUpperCase());
                    tvBaseToTarget.setText("1 " + result.getBaseCode().toUpperCase() + " = " + rateBaseToTarget + " " + result.getTargetCode().toUpperCase());
                    etBaseCurrencyAmount.setText("");
                    // Target currency UI
                    tvTargetCurrencyCode.setText(result.getTargetCode().toUpperCase());
                    rateTargetToBase = 1 / result.getRate();
                    tvTargetToBase.setText("1 " + result.getTargetCode().toUpperCase() + " = " + AppApplication.instance.getDecimals().format(rateTargetToBase) + " " + result.getBaseCode().toUpperCase());
                    etTargetCurrencyAmount.setText("");

                    mDialog.dismiss();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    mDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Exchange> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(getApplicationContext(), getString(R.string.text_Please_check_your_network_connection), Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });
    }

    private class ImageDownloadAnsyTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {

            try {
                String imgUrl = strings[0];
                String key = strings[1];
                Bitmap bmp = ImageHelper.decodeBitmap(imgUrl, 200);
                AppApplication.instance.putCacheImg(key, bmp);
                return key;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                super.onPostExecute(result);
                if (result != null) {
                    // Setup base flag image
                    if (result.toLowerCase().equals(AppApplication.instance.getBaseCode().toLowerCase())) {
                        civBaseCurrencyFlag.setImageBitmap(AppApplication.instance.getCacheImg(AppApplication.instance.getBaseCode().toLowerCase()));
                    }

                    // Setup target flag image
                    if (result.toLowerCase().equals(AppApplication.instance.getTargetCode().toLowerCase())) {
                        civTargetCurrencyFlag.setImageBitmap(AppApplication.instance.getCacheImg(AppApplication.instance.getTargetCode().toLowerCase()));
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_currency) {
            Toast.makeText(getApplicationContext(), "Currency", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_LONG).show();
        } else {
            // do nothing
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
