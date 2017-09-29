package com.app.onemenu;

import java.io.Serializable;

public class Avatar implements Serializable {
    private String mUrl;
    private int mHeight;
    private int mWidth;

    public Avatar(String mUrl, int mHeight, int mWidth) {
        this.mUrl = mUrl;
        this.mHeight = mHeight;
        this.mWidth = mWidth;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public String getUrl() {
        return mUrl;
    }
}
