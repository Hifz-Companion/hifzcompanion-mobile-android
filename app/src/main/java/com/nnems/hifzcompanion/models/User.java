package com.nnems.hifzcompanion.models;

import java.util.Map;

public class User {
    String mEmail;
    Long mRegistrationDate;
    Map<String,Object> mMemo;


    public User() {
    }

    public User(String email, Long registrationDate, Map memo) {
        mEmail = email;
        mRegistrationDate = registrationDate;
        mMemo = memo;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public Long getRegistrationDate() {
        return mRegistrationDate;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.mRegistrationDate = registrationDate;
    }

    public Map getMemo() {
        return mMemo;
    }

    public void setMemo(Map memo) {
        mMemo = memo;
    }
}
