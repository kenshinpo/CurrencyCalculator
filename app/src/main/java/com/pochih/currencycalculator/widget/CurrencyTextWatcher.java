package com.pochih.currencycalculator.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.pochih.currencycalculator.R;

/**
 * Created by admin on 2017/12/20.
 */

public class CurrencyTextWatcher  implements TextWatcher {
    private View view;

    public CurrencyTextWatcher(View view) {
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
