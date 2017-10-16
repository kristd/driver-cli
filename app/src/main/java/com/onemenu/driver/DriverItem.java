package com.onemenu.driver;

import java.io.Serializable;

//cardview item
public class DriverItem implements Serializable {
    private String mAddr;
    private String mId;
    private String mName;
    private String mPhone;
    private String latitude;
    private String longitude;
    private int mStat;

    public DriverItem(String addr, String id, String name, String phone, String latitude, String longitude, int stat) {
        mAddr = addr;
        mId = id;
        mName = name;
        mPhone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        mStat = stat;
    }

    public void setStat(int stat) {
        mStat = stat;
    }

    public int getStat() {
        return mStat;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getId() {
        return mId;
    }

    public String getAddr() {
        return mAddr;
    }

    public String getName() {
        return mName;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setAddr(String mAddr) {
        this.mAddr = mAddr;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
