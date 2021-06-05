package com.nnems.hifzcompanion.models;

public class DueCard {
    long mId;
    long mAyah;
    long mPageNo;
    long mSurahId;
    double mEase;
    String mSurahName;

    public DueCard() {
    }

    public DueCard(long id, long ayah, long pageNo, long surahId, double ease, String surahName) {
        mId = id;
        mAyah = ayah;
        mPageNo = pageNo;
        mSurahId = surahId;
        mEase = ease;
        mSurahName = surahName;
    }

    public DueCard(long id, long ayah, long pageNo, long surahId, String surahName) {
        mId = id;
        mAyah = ayah;
        mPageNo = pageNo;
        mSurahId = surahId;
        mSurahName = surahName;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getAyah() {
        return mAyah;
    }

    public void setAyah(long ayah) {
        mAyah = ayah;
    }

    public long getPageNo() {
        return mPageNo;
    }

    public void setPageNo(long pageNo) {
        this.mPageNo = pageNo;
    }

    public long getSurahId() {
        return mSurahId;
    }

    public void setSurahId(long surahId) {
        mSurahId = surahId;
    }

    public double getEase() {
        return mEase;
    }

    public void setEase(double ease) {
        mEase = ease;
    }

    public String getSurahName() {
        return mSurahName;
    }

    public void setSurahName(String surahName) {
        mSurahName = surahName;
    }
}
