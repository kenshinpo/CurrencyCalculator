package com.pochih.currencycalculator.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pochih.currencycalculator.AppApplication;
import com.pochih.currencycalculator.R;
import com.pochih.currencycalculator.entity.Currency;
import com.pochih.currencycalculator.entity.Exchange;
import com.pochih.currencycalculator.tool.ImageHelper;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

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

    DecimalFormat df = new DecimalFormat("#.###");

    private double rateBaseToTarget = 1.0;
    private double rateTargetToBase = 1.0;

    HandlerThread mHandlerThread;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);

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
                                etTargetCurrencyAmount.setText(df.format(input * rateBaseToTarget));
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
                                etBaseCurrencyAmount.setText(df.format(input * rateTargetToBase));
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

            mHandlerThread = new HandlerThread("LRU Cache Handler");
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper());

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

                }
            });
            //endregion

            //endregion
        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
                    rateBaseToTarget = result.getRate();
                    tvBaseCurrencyCode.setText(result.getBaseCode().toUpperCase());
                    tvBaseToTarget.setText("1 " + result.getBaseCode().toUpperCase() + " = " + rateBaseToTarget + " " + result.getTargetCode().toUpperCase());
                    etBaseCurrencyAmount.setText("");

                    tvTargetCurrencyCode.setText(result.getTargetCode().toUpperCase());
                    rateTargetToBase = 1 / result.getRate();
                    tvTargetToBase.setText("1 " + result.getTargetCode().toUpperCase() + " = " + df.format(rateTargetToBase) + " " + result.getBaseCode().toUpperCase());
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
}
