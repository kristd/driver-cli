package com.onemenu.driver;

import java.io.Serializable;

public class Customer implements Serializable {
    private String mId;
    private String mName;
    private String mPhone;

    public Customer(String id, String name, String phone) {
        mId = id;
        mName = name;
        mPhone = phone;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getId() {
        return mId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getPhone() {
        return mPhone;
    }
}
