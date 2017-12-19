package com.pochih.currencycalculator.entity;

/**
 * Created by PoChih on 2017/12/19.
 */

public class Exchange {
    private String baseCode;
    private String targetCode;
    private double rate;

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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
