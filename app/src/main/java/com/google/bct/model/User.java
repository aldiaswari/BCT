package com.google.bct.model;

public class User {
    public String mId;
    public String mLat;
    public String mLon;
    public String mTitle;

    public User(){

    }

    public User(String mId, String mLat, String mLon, String mTitle) {
        this.mId = mId;
        this.mLat = mLat;
        this.mLon = mLon;
        this.mTitle = mTitle;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getmLon() {
        return mLon;
    }

    public void setmLon(String mLon) {
        this.mLon = mLon;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
