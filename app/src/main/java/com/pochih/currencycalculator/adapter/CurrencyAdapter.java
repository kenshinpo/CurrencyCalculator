package com.pochih.currencycalculator.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pochih.currencycalculator.AppApplication;
import com.pochih.currencycalculator.R;
import com.pochih.currencycalculator.activity.ChangeCurrencyActivity;
import com.pochih.currencycalculator.entity.Currency;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 2017/12/20.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = CurrencyAdapter.class.getSimpleName();

    private Context mContext;
    private List<Currency> currencies;
    private int selectType = 0;

    private OnItemClickListener mOnItemClickListener = null;

    public CurrencyAdapter(Context mContext, List<Currency> currencies, int selectType) {
        this.mContext = mContext;
        this.currencies = currencies;
        this.selectType = selectType;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        try {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, Integer.parseInt(v.getTag().toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_currencies_adapter, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        itemView.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            Currency currency = currencies.get(position);
            holder.itemView.setTag(position);

            //region Data binding
            Picasso.with(mContext).load(AppApplication.instance.getBaseUrl() + currency.getFlagPath()).into(holder.civBaseCurrencyFlag);
            holder.tvCurrencyCode.setText(currency.getCode().toUpperCase());
            holder.tvCurrencyName.setText(currency.getName());
            //endregion

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Currency c = currencies.get(position);
                    if (selectType == ChangeCurrencyActivity.SELECT_TYPE_BASE_CURRENCY) {
                        AppApplication.instance.setBaseCode(c.getCode());
                    } else if (selectType == ChangeCurrencyActivity.SELECT_TYPE_TARGET_CURRENCY) {
                        AppApplication.instance.setTargetCode(c.getCode());
                    } else {
                        // do nothing
                    }
                    ((Activity) mContext).finish();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout llBaseCurrency;
        public CircleImageView civBaseCurrencyFlag;
        public TextView tvCurrencyCode, tvCurrencyName;

        public ViewHolder(View itemView) {
            super(itemView);
            llBaseCurrency = itemView.findViewById(R.id.llBaseCurrency);
            civBaseCurrencyFlag = itemView.findViewById(R.id.civBaseCurrencyFlag);
            tvCurrencyCode = itemView.findViewById(R.id.tvCurrencyCode);
            tvCurrencyName = itemView.findViewById(R.id.tvCurrencyName);
        }
    }
}
