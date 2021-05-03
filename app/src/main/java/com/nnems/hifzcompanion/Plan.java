package com.nnems.hifzcompanion;

import com.nnems.hifzcompanion.database.QuranMetaDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Plan {


    public static class Quran{
        public Quran(QuranMetaDatabase quranMetaDatabase) {
            mQuranMetaDatabase = quranMetaDatabase;
        }

        private QuranMetaDatabase mQuranMetaDatabase;

        public ArrayList<Map<String, Object>> getOneYearPlan(long registrationDate){
            ArrayList<Map<String, Object>> cards = new ArrayList<Map<String, Object>>();


            for(int i = 0; i < CONSTANTS.PAGES; i++){
                Map<String,Object> card = new HashMap<>();

                int cardNumber = i+1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                Map<String,Object> surah = new HashMap<>();
                surah.put("id",id);
                surah.put("name",surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);

                float f = (float)(cardNumber)/2;
                long dayToMemorize = Math.round(f);
                long dueDate = registrationDate +(dayToMemorize * 86400000);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                setQuranCard(card, cardNumber, cardNumber,"Quran Page "+cardNumber, surah, ayah, dueDate, dueDateInWords);

                cards.add(card);
            }


            return cards;
        }

        public ArrayList<Map<String, Object>> getTwoYearPlan(long registrationDate){
            ArrayList<Map<String, Object>> cards = new ArrayList<Map<String, Object>>();

            for(int i =0; i<CONSTANTS.PAGES; i++){
                Map<String,Object> card = new HashMap<>();

                int cardNumber = i+1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                Map<String,Object> surah = new HashMap<>();
                surah.put("id",id);
                surah.put("name",surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);

                long dueDate = registrationDate +(cardNumber * 86400000);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                setQuranCard(card, cardNumber, cardNumber,"Quran Page "+cardNumber, surah, ayah, dueDate, dueDateInWords);

                cards.add(card);

            }

            return cards;
        }

        public ArrayList<Map<String, Object>> getThreeYearPlan(long registrationDate){
            ArrayList<Map<String, Object>> cards = new ArrayList<Map<String, Object>>();

            ArrayList<Integer> daysToMemoList = new ArrayList<>();
            int noOfDaysToMemo = 906;
            for(int i =0; i < noOfDaysToMemo; i++){
                if((i+1)%3 !=0 ){
                    daysToMemoList.add(i+1);
                }
            }

            for(int i =0; i<CONSTANTS.PAGES; i++){
                Map<String,Object> card = new HashMap<>();

                int cardNumber = i+1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                Map<String,Object> surah = new HashMap<>();
                surah.put("id",id);
                surah.put("name",surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);

                long dayToMemorize = daysToMemoList.get(i);
                long dueDate = registrationDate +(dayToMemorize * 86400000);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                setQuranCard(card, cardNumber, cardNumber,"Quran Page "+cardNumber, surah, ayah, dueDate, dueDateInWords);

                cards.add(card);

            }

            return cards;
        }

        public ArrayList<Map<String, Object>> getFourYearPlan(long registrationDate){
            ArrayList<Map<String, Object>> cards = new ArrayList<Map<String, Object>>();

            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();
            int noOfDaysToMemo = 1208;
            for(int i =0; i < noOfDaysToMemo; i++){
                if((i+1)%2 !=0 ){
                    daysToMemorizeList.add(i+1);
                }
            }

            for(int i =0; i<CONSTANTS.PAGES; i++){
                Map<String,Object> card = new HashMap<>();

                int cardNumber = i+1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                Map<String,Object> surah = new HashMap<>();
                surah.put("id",id);
                surah.put("name",surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);


                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = registrationDate +(dayToMemorize * 86400000);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                setQuranCard(card, cardNumber, cardNumber,"Quran Page "+cardNumber, surah, ayah, dueDate, dueDateInWords);

                cards.add(card);

            }

            return cards;
        }

        public ArrayList<Map<String, Object>> getFiveYearPlan(long registrationDate){
            ArrayList<Map<String, Object>> cards = new ArrayList<Map<String, Object>>();

            ArrayList<Integer> initialDaysToMemorizeList = new ArrayList<>();
            int noOfDaysToMemo = 1409;
            for(int i =0; i < noOfDaysToMemo; i++){
                if((i+1)%2 !=0 ){
                    initialDaysToMemorizeList.add(i+1);
                }
            }

            int initialListLength = initialDaysToMemorizeList.size();
            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();
            for(int i = 0; i< initialListLength; i++){
                if((initialDaysToMemorizeList.get(i))%7 != 0){
                    daysToMemorizeList.add(i+1);
                }
            }

            for(int i =0; i<CONSTANTS.PAGES; i++){
                Map<String,Object> card = new HashMap<>();

                int cardNumber = i+1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                Map<String,Object> surah = new HashMap<>();
                surah.put("id",id);
                surah.put("name",surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);

                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = registrationDate +(dayToMemorize * 86400000);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                setQuranCard(card, cardNumber, cardNumber,"Quran Page "+cardNumber, surah, ayah, dueDate, dueDateInWords);

                cards.add(card);

            }

            return cards;
        }

        public ArrayList<Map<String, Object>> getSixYearPlan(long registrationDate){
            ArrayList<Map<String, Object>> cards = new ArrayList<Map<String, Object>>();

            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();

            int noOfDaysToMemo = 1812;

            for(int i=0; i<noOfDaysToMemo; i++){
                if(i == 0){
                    daysToMemorizeList.add(i+1);
                }
                if((i+1)%3 == 0) {
                    daysToMemorizeList.add(i+2);
                }
            }

            for(int i =0; i<CONSTANTS.PAGES; i++){
                Map<String,Object> card = new HashMap<>();

                int cardNumber = i+1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                Map<String,Object> surah = new HashMap<>();
                surah.put("id",id);
                surah.put("name",surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);



                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = registrationDate +(dayToMemorize * 86400000);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                setQuranCard(card, cardNumber, cardNumber,"Quran Page "+cardNumber, surah, ayah, dueDate, dueDateInWords);

                cards.add(card);

            }

            return cards;
        }

        private void setQuranCard(Map<String,
                Object> card, int id, int pageNo, String title, Map<String,
                Object> surah, int ayah,
                                  long dueDate, String dueDateInWords) {
            card.put("id",id);
            card.put("pageNo",pageNo);
            card.put("title",title);
            card.put("surah",surah);
            card.put("juz", "");
            card.put("ayah",ayah);
            card.put("dueDate",dueDate);
            card.put("dueDateInWords",dueDateInWords);
            card.put("lapses", 0);
            card.put("interval", 1);
            card.put("ease", 2.5);
            card.put("phase","memorizing");
        }


    }

    public static class Juz{

    }

    public static class Surah{

    }
}
