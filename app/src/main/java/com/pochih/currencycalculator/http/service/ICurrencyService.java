package com.pochih.currencycalculator.http.service;

import com.pochih.currencycalculator.entity.Currency;
import com.pochih.currencycalculator.entity.Exchange;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by PoChih on 2017/12/19.
 */

public interface ICurrencyService {
    @GET("api/currency")
    Call<List<Currency>> getCurrency();

    @GET("api/exchange")
    Call<Exchange> getExchange(@Query("baseCode") String baseCode, @Query("targetCode") String targetCode);
}
