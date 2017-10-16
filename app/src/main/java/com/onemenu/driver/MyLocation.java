package com.onemenu.driver;

import java.io.Serializable;

public class MyLocation implements Serializable {
    private String mId;
    private String mAddress;
    private String mLatitude;
    private String mLongitude;

    public MyLocation(String id, String addr, String latitude, String longitude) {
        mId = id;
        mAddress = addr;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }
}
