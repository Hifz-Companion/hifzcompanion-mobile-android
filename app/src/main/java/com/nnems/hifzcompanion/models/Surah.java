package com.nnems.hifzcompanion.models;

public class Surah {
    int mId;
    String mName;

    public Surah() {
    }

    public Surah(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
