package com.app.onemenu;

import java.io.Serializable;

//cardview item
public class ReportItem implements Serializable {
    private String mOrderId;
    private String mOrderCode;
    private double mTipsFee;
    private int mTipsType;
    private String mDate;
    private int mUsedTime;

    public ReportItem(String orderId, String orderCode, double tipsFee, int tipsType, String date, int usedTime) {
        mOrderId = orderId;
        mOrderCode = orderCode;
        mTipsFee = tipsFee;
        mTipsType = tipsType;
        mDate = date;
        mUsedTime = usedTime;
    }

    public double getTipsFee() {
        return mTipsFee;
    }

    public int getTipsType() {
        return mTipsType;
    }

    public int getUsedTime() {
        return mUsedTime;
    }

    public String getDate() {
        return mDate;
    }

    public String getOrderCode() {
        return mOrderCode;
    }

    public String getOrderId() {
        return mOrderId;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public void setUsedTime(int mUsedTime) {
        this.mUsedTime = mUsedTime;
    }

    public void setOrderCode(String mOrderCode) {
        this.mOrderCode = mOrderCode;
    }

    public void setOrderId(String mOrderId) {
        this.mOrderId = mOrderId;
    }

    public void setTipsFee(double mTipsFee) {
        this.mTipsFee = mTipsFee;
    }

    public void setTipsType(int mTipsType) {
        this.mTipsType = mTipsType;
    }
}
