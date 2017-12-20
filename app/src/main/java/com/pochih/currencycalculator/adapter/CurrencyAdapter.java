package com.pochih.currencycalculator.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pochih.currencycalculator.R;
import com.pochih.currencycalculator.entity.Currency;

import java.util.List;

/**
 * Created by admin on 2017/12/20.
 */

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Currency> currencies;

    private OnItemClickListener mOnItemClickListener = null;

    public CurrencyAdapter(Context mContext, List<Currency> currencies) {
        this.mContext = mContext;
        this.currencies = currencies;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Currency currency = currencies.get(position);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        public ImageView ivProduct;
//        public TextView tvProductName;
//        public TextView tvProductSn;
//
        public ViewHolder(View itemView) {
            super(itemView);
//            ivProduct = itemView.findViewById(R.id.ivProduct);
//            tvProductName = itemView.findViewById(R.id.tvProductName);
//            tvProductSn = itemView.findViewById(R.id.tvProductSn);
        }
    }
}
