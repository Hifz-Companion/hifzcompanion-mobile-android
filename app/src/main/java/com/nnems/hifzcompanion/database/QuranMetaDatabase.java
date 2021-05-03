package com.nnems.hifzcompanion.database;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class QuranMetaDatabase {

    private Context mContext;
//    private


    public QuranMetaDatabase(Context context) {
        mContext = context;
    }

    public String jsonDataFromAssets(){

        String json = null;

        try{
            InputStream inputStream = mContext.getAssets().open("quranmetadata.json");
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();

            json = new String(bufferData,"UTF-8");
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public JSONObject getQuranMeta(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonDataFromAssets());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public JSONObject getSurahMeta(){
        JSONObject jsonObject = getQuranMeta();
        JSONObject surahMeta = null;
        try {
           surahMeta = jsonObject.getJSONObject("surahs");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return surahMeta;
    }

    private JSONObject getPagesMeta() {
        JSONObject jsonObject = getQuranMeta();
        JSONObject pagesMeta = null;
        try {
            pagesMeta = jsonObject.getJSONObject("pages");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pagesMeta;
    }

    public JSONArray getSurahReferences(){
        JSONObject jsonObject = getSurahMeta();
        JSONArray surahReferneces = null;

        try {
            surahReferneces = jsonObject.getJSONArray("references");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return surahReferneces;
    }

    private JSONArray getPageReferences() {
        JSONObject jsonObject = getPagesMeta();
        JSONArray pageReferences = null;

        try {
            pageReferences = jsonObject.getJSONArray("references");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pageReferences;
    }


    public String getSurahEnglishName(int surahNumber) {
        JSONArray jsonArray = getSurahReferences();
        JSONObject surahRefernce = null;
        try {
            surahRefernce = jsonArray.getJSONObject(surahNumber-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String surahEnglishName = null;
        try {
            surahEnglishName = surahRefernce.getString("englishName");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return surahEnglishName;
    }



    public  int getPageSurahNumber(int indexOfCard) {
        JSONArray jsonArray = getPageReferences();
        JSONObject pageRefernce = null;

        try {
            pageRefernce = jsonArray.getJSONObject(indexOfCard);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int surahNumber = 0;
        try {
            surahNumber = pageRefernce.getInt("surah");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return surahNumber;
    }

    public  int getPageFirstAyahNumber(int indexOfCard) {
        JSONArray jsonArray = getPageReferences();
        JSONObject pageRefernce = null;

        try {
            pageRefernce = jsonArray.getJSONObject(indexOfCard);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int ayahNumber = 0;
        try {
            ayahNumber = pageRefernce.getInt("ayah");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ayahNumber;
    }

}
