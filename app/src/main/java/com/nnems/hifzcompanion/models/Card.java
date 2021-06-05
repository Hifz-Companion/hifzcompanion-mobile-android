package com.nnems.hifzcompanion.models;

public class Card {
    int mId;
    int mPageNo;
    String mTitle;
    Surah mSurah;
    int mJuz;
    int mAyah;
    long mDueDate;
    String mDueDateInWords;
    int mLapses;
    long mInterval;
    double mEase;
    String mPhase;

    public Card() {
    }

    public Card(int id, int pageNo, String title, Surah surah, int juz, int ayah, long dueDate, String dueDateInWords) {
        mId = id;
        mPageNo = pageNo;
        mTitle = title;
        mSurah = surah;
        mJuz = juz;
        mAyah = ayah;
        mDueDate = dueDate;
        mDueDateInWords = dueDateInWords;
        mLapses = 0;
        mInterval = 1;
        mEase = 2.5;
        mPhase = "memorizing";
    }

    public Card(int id, int pageNo, String title, int juz, long dueDate, String dueDateInWords) {
        mId = id;
        mPageNo = pageNo;
        mTitle = title;
        mJuz = juz;
        mDueDate = dueDate;
        mDueDateInWords = dueDateInWords;
        mLapses = 0;
        mInterval = 1;
        mEase = 2.5;
        mPhase = "memorizing";
    }


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getPageNo() {
        return mPageNo;
    }

    public void setPageNo(int pageNo) {
        mPageNo = pageNo;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Surah getSurah() {
        return mSurah;
    }

    public void setSurah(Surah surah) {
        mSurah = surah;
    }

    public int getJuz() {
        return mJuz;
    }

    public void setJuz(int juz) {
        mJuz = juz;
    }

    public int getAyah() {
        return mAyah;
    }

    public void setAyah(int ayah) {
        mAyah = ayah;
    }

    public long getDueDate() {
        return mDueDate;
    }

    public void setDueDate(long dueDate) {
        mDueDate = dueDate;
    }

    public String getDueDateInWords() {
        return mDueDateInWords;
    }

    public void setDueDateInWords(String dueDateInWords) {
        mDueDateInWords = dueDateInWords;
    }

    public int getLapses() {
        return mLapses;
    }

    public void setLapses(int lapses) {
        mLapses = lapses;
    }

    public long getInterval() {
        return mInterval;
    }

    public void setInterval(long interval) {
        mInterval = interval;
    }

    public double getEase() {
        return mEase;
    }

    public void setEase(double ease) {
        mEase = ease;
    }

    public String getPhase() {
        return mPhase;
    }

    public void setPhase(String phase) {
        mPhase = phase;
    }
}
