package com.pochih.currencycalculator.http.request;

/**
 * Created by PoChih on 2017/12/19.
 */

public class ExchangeRequest {
    private String baseCode;
    private String targetCode;

    public String getBaseCode() {
        return baseCode;
    }

    public void setBaseCode(String baseCode) {
        this.baseCode = baseCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }
}
