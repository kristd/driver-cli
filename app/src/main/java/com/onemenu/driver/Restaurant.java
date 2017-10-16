package com.onemenu.driver;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private String mId;
    private MyLocation mlocation;
    private String mName;
    private String mPhone;

    public Restaurant(String id, MyLocation location, String name, String phone) {
        mId = id;
        mlocation = location;
        mName = name;
        mPhone = phone;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setLocation(MyLocation mlocation) {
        this.mlocation = mlocation;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getId() {
        return mId;
    }

    public MyLocation getLocation() {
        return mlocation;
    }

    public String getName() {
        return mName;
    }
}
