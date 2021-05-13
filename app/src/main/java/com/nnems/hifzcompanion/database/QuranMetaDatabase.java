package com.nnems.hifzcompanion.database;

import android.content.Context;

import com.nnems.hifzcompanion.CONSTANTS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

    private JSONObject getJuzMeta() {
        JSONObject jsonObject = getQuranMeta();
        JSONObject juzMeta = null;
        try {
            juzMeta = jsonObject.getJSONObject("juzs");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return juzMeta;
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

    public JSONArray getJuzReferences(){
        JSONObject jsonObject = getJuzMeta();
        JSONArray juzReferences = null;

        try {
            juzReferences = jsonObject.getJSONArray("references");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return juzReferences;
    }

    public JSONArray getSurahReferences(){
        JSONObject jsonObject = getSurahMeta();
        JSONArray surahReferences = null;

        try {
            surahReferences = jsonObject.getJSONArray("references");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return surahReferences;
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
        JSONObject surahReference = null;
        try {
            surahReference = jsonArray.getJSONObject(surahNumber-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String surahEnglishName = null;
        try {
            surahEnglishName = surahReference.getString("englishName");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return surahEnglishName;
    }

    public  int getPageSurahNumber(int indexOfPage) {
        JSONArray jsonArray = getPageReferences();
        JSONObject pageReference = null;

        try {
            pageReference = jsonArray.getJSONObject(indexOfPage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int surahNumber = 0;
        try {
            surahNumber = pageReference.getInt("surah");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return surahNumber;
    }

    public  int getPageFirstAyahNumber(int indexOfPage) {
        JSONArray jsonArray = getPageReferences();
        JSONObject pageReference = null;

        try {
            pageReference = jsonArray.getJSONObject(indexOfPage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int ayahNumber = 0;
        try {
            ayahNumber = pageReference.getInt("ayah");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ayahNumber;
    }

    public int getFirstPageOfJuzNumber(int juzNumber){
        int firstPageOfJuzNumber = 0;



        JSONArray jsonArray = getJuzReferences();
        JSONObject juzReference = null;

        try {
            juzReference = jsonArray.getJSONObject(juzNumber-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            firstPageOfJuzNumber = juzReference.getInt("page");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return firstPageOfJuzNumber;
    }

//    public int getFirstPageOfJuzSurahNumber(int juzNumber){
//        int firstPageOfJuzSurahNumber = 0;
//
//        JSONArray jsonArray = getJuzReferences();
//        JSONObject juzRefernce = null;
//
//        try {
//            juzRefernce = jsonArray.getJSONObject(juzNumber-1);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            firstPageOfJuzSurahNumber = juzRefernce.getInt("surah");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        return firstPageOfJuzSurahNumber;
//    }
//
//    public int getFirstPageOfJuzAyahNumber(int juzNumber){
//        int firstPageOfJuzAyahNumber = 0;
//
//        JSONArray jsonArray = getJuzReferences();
//        JSONObject juzRefernce = null;
//
//        try {
//            juzRefernce = jsonArray.getJSONObject(juzNumber-1);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            firstPageOfJuzAyahNumber = juzRefernce.getInt("ayah");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return firstPageOfJuzAyahNumber;
//    }

    public ArrayList<JSONObject> getSurahPagesList(int surahNumber){
        ArrayList<JSONObject> surahPagesList = new ArrayList<>();

        JSONArray jsonArray = getPageReferences();

        JSONObject pageReference;

        for(int i =0; i < CONSTANTS.PAGES; i++){
            try {
               pageReference = jsonArray.getJSONObject(i);
                if(pageReference.getInt("surah") == surahNumber){
                    surahPagesList.add(pageReference);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return surahPagesList;
    }

}
