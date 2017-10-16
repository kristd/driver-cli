package com.onemenu.driver;

import java.io.Serializable;

//cardview item
public class OrderItem implements Serializable {
    private int mInTime;
    private long mCreateTime;
    private Customer mCustomer;
    private MyLocation mCustLocation;
    private String mId;
    private String mLast5Code;
    private String mCode;
    private Restaurant mRestaurant;
    private DriverItem mDriver;
    private int mStatus;

    public OrderItem(int mInTime, long mCreateTime, Customer mCustomer, MyLocation mCustLocation, String mId, String mLast5Code, String mCode, Restaurant mRestaurant, DriverItem mDriver, int mStatus) {
        this.mCreateTime = mCreateTime;
        this.mCustLocation = mCustLocation;
        this.mCustomer = mCustomer;
        this.mId = mId;
        this.mInTime = mInTime;
        this.mLast5Code = mLast5Code;
        this.mCode = mCode;
        this.mRestaurant = mRestaurant;
        this.mDriver = mDriver;
        this.mStatus = mStatus;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setCreateTime(long mCreateTime) {
        this.mCreateTime = mCreateTime;
    }

    public void setCustLocation(MyLocation mCustLocation) {
        this.mCustLocation = mCustLocation;
    }

    public void setCustomer(Customer mCustomer) {
        this.mCustomer = mCustomer;
    }

    public void setInTime(int mInTime) {
        this.mInTime = mInTime;
    }

    public void setLast5Code(String mLast5Code) {
        this.mLast5Code = mLast5Code;
    }

    public void setRestaurant(Restaurant mRestaurant) {
        this.mRestaurant = mRestaurant;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getId() {
        return mId;
    }

    public Customer getCustomer() {
        return mCustomer;
    }

    public int getInTime() {
        return mInTime;
    }

    public int getStatus() {
        return mStatus;
    }

    public MyLocation getCustomerLocation() {
        return mCustLocation;
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public Restaurant getRestaurant() {
        return mRestaurant;
    }

    public String getLast5Code() {
        return mLast5Code;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public String getCode() {
        return mCode;
    }

    public void setDriver(DriverItem mDriver) {
        this.mDriver = mDriver;
    }

    public DriverItem getDriver() {
        return mDriver;
    }
}
