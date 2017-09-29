package com.app.onemenu;


import java.io.Serializable;

public class User implements Serializable {
    private int mStatus;
    private String mPhone;
    private String mPasswd;
    private String mToken;
    private String mName;
    private String mId;
    private int mType;
    private Avatar mAvatar;
    private static User mInstant;

    static public void init() {}

    static public void init(String phone, String passwd, String token, int stat, String name, String id, Avatar avatar, int type) {
        mInstant = new User(phone, passwd, token, stat, name, id, avatar, type);
    }

    static public User getInstant() {
        return mInstant;
    }

    private User(String phone, String passwd, String token, int stat, String name, String id, Avatar avatar, int type) {
        mPhone = phone;
        mPasswd = passwd;
        mToken = token;
        mStatus = stat;
        mName = name;
        mId = id;
        mAvatar = avatar;
        mType = type;

//        mPhone = "18620904899";
//        mPasswd = "12123";
//        mToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJvbmVtZW51LnVzQGdtYWlsLmNvbSIsInN1YiI6IjAyM2IxN2QzNDJiZDRjMTliZWE5NzZlZDMwNTVjZjI2IiwiaXNzIjoiYXV0aDAiLCJleHAiOjE1MDY3MTkwNjAsImlhdCI6MTUwNjExNDI2MH0.--9-yB9cXyucXG_MjlNJEZo24kZSkdBgQYgihghcCFo";
//        mStatus = 1;
//        mName = "YANG JIAHONG";
//        mId = "023b17d342bd4c19bea976ed3055cf26";
//        mAvatar = null;
//        mType = DriverType.Manager;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getPasswd() {
        return mPasswd;
    }

    public String getToken() {
        return mToken;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public void setPasswd(String mPasswd) {
        this.mPasswd = mPasswd;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getId() {
        return mId;
    }

    public void setAvatar(Avatar mAvatar) {
        this.mAvatar = mAvatar;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public Avatar getAvatar() {
        return mAvatar;
    }

    public int getType() {
        return mType;
    }
}
