package com.onemenu.driver;


import java.io.Serializable;
import java.sql.Struct;

public class User implements Serializable {
    private int mStatus;
    private String mPhone;
    private String mPasswd;
    private String mToken;
    private String mName;
    private String mId;
    private int mType;
    private Avatar mAvatar;
    private boolean isLogin;
    private String notiToken;

    private static User mInstant;



    static public void init(String phone, String passwd, String token, int stat, String name, String id, Avatar avatar, int type) {
        if (mInstant != null) {
            mInstant.setPhone(phone);
            mInstant.setPasswd(passwd);
            mInstant.setToken(token);
            mInstant.setStatus(stat);
            mInstant.setName(name);
            mInstant.setId(id);
            mInstant.setAvatar(avatar);
            mInstant.setType(type);
        } else {
            mInstant = new User(phone, passwd, token, stat, name, id, avatar, type);
        }
    }

    static public User getInstant() {
        if (mInstant == null) {
            mInstant = new User();
        }

        return mInstant;
    }

    private User() {}

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

    public void setLoginStat(boolean stat) {
        this.isLogin = stat;
    }

    public boolean getLoginStat() {
        return this.isLogin;
    }

    public void setNotiToken(String notiToken) {
        this.notiToken = notiToken;
    }

    public String getNotiToken() {
        return notiToken;
    }
}
