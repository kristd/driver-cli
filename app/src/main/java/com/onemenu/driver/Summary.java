package com.onemenu.driver;

import java.io.Serializable;

public class Summary implements Serializable {
    private int mTotalOrders;
    private double mTotalTips;
    private String mTimePeriod;

    public Summary(int mTotalOrders, double mTotalTips, String mTimePeriod) {
        this.mTimePeriod = mTimePeriod;
        this.mTotalOrders = mTotalOrders;
        this.mTotalTips = mTotalTips;
    }

    public void setTimePeriod(String mTimePeriod) {
        this.mTimePeriod = mTimePeriod;
    }

    public void setTotalCount(int mTotalCount) {
        this.mTotalOrders = mTotalCount;
    }

    public void setTotalTips(double mTotalTips) {
        this.mTotalTips = mTotalTips;
    }

    public double getTotalTips() {
        return mTotalTips;
    }

    public int getTotalCount() {
        return mTotalOrders;
    }

    public String getTimePeriod() {
        return mTimePeriod;
    }
}

