package com.nnems.hifzcompanion;

import android.content.Context;

import com.nnems.hifzcompanion.database.QuranMetaDatabase;
import com.nnems.hifzcompanion.models.Card;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Plan {

    public static class Quran {
        private static final String TAG = "Quran";
        private final QuranMetaDatabase mQuranMetaDatabase;
        private final JSONObject mJSONObject;
        private long mMidNight;

        public Quran(Context context) {
            mQuranMetaDatabase = new QuranMetaDatabase(context);
            mJSONObject = mQuranMetaDatabase.getQuranMeta();

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Date dateNow = c.getTime();


            mMidNight= dateNow.getTime();
        }

        public ArrayList<Card> getOneYearPlan() {
            ArrayList<Card> cards = new ArrayList<>();

                for (int i = 0; i < CONSTANTS.PAGES; i++) {

                    int cardNumber = i + 1; //same as pageNumber , id and title

                    //   accessing data for surah object
                    int id = mQuranMetaDatabase.getPageSurahNumber(i);
                    String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                    com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(id, surahEnglishName);

                    int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);

                    float f = (float) (cardNumber) / 2.0f;
                    int dayToMemorize = Math.round(f);
                    long dueDate = mMidNight + ((long) dayToMemorize * 86400000L);

                    Date date = new Date(dueDate);
                    String dueDateInWords = date.toString();

                    cards.add(new Card(cardNumber, cardNumber, "Quran Page " + cardNumber, surah, 0, ayah, dueDate, dueDateInWords));
                }

            return cards;
        }

        public ArrayList<Card> getTwoYearPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            for (int i = 0; i < CONSTANTS.PAGES; i++) {

                int cardNumber = i + 1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(id, surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);

                long dueDate = mMidNight + ((long) cardNumber * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, "Quran Page " + cardNumber, surah, 0, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getThreeYearPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            ArrayList<Integer> daysToMemoList = new ArrayList<>();
            int noOfDaysToMemo = 906;
            for (int i = 0; i < noOfDaysToMemo; i++) {
                if ((i + 1) % 3 != 0) {
                    daysToMemoList.add(i + 1);
                }
            }

            for (int i = 0; i < CONSTANTS.PAGES; i++) {

                int cardNumber = i + 1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(id, surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);

                long dayToMemorize = daysToMemoList.get(i);
                long dueDate = mMidNight + (dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, "Quran Page " + cardNumber, surah, 0, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getFourYearPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();
            int noOfDaysToMemo = 1208;
            for (int i = 0; i < noOfDaysToMemo; i++) {
                if ((i + 1) % 2 != 0) {
                    daysToMemorizeList.add(i + 1);
                }
            }

            for (int i = 0; i < CONSTANTS.PAGES; i++) {
                int cardNumber = i + 1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(id, surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);

                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = mMidNight + (dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, "Quran Page " + cardNumber, surah, 0, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getFiveYearPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            ArrayList<Integer> initialDaysToMemorizeList = new ArrayList<>();
            int noOfDaysToMemo = 1409;
            for (int i = 0; i < noOfDaysToMemo; i++) {
                if ((i + 1) % 2 != 0) {
                    initialDaysToMemorizeList.add(i + 1);
                }
            }

            int initialListLength = initialDaysToMemorizeList.size();
            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();
            for (int i = 0; i < initialListLength; i++) {
                if ((initialDaysToMemorizeList.get(i)) % 7 != 0) {
                    daysToMemorizeList.add(i + 1);
                }
            }

            for (int i = 0; i < CONSTANTS.PAGES; i++) {


                int cardNumber = i + 1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(id, surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);

                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = mMidNight + (dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, "Quran Page " + cardNumber, surah, 0, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getSixYearPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();

            int noOfDaysToMemo = 1812;

            for (int i = 0; i < noOfDaysToMemo; i++) {
                if (i == 0) {
                    daysToMemorizeList.add(i + 1);
                }
                if ((i + 1) % 3 == 0) {
                    daysToMemorizeList.add(i + 2);
                }
            }

            for (int i = 0; i < CONSTANTS.PAGES; i++) {


                int cardNumber = i + 1; //same as pageNumber , id and title

                //   accessing data for surah object
                int id = mQuranMetaDatabase.getPageSurahNumber(i);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(id);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(id, surahEnglishName);


                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(i);


                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = mMidNight + (dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, "Quran Page " + cardNumber, surah, 0, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }


    }

    public static class Juz {
        private final QuranMetaDatabase mQuranMetaDatabase;
        private final int mJuzNumber;
        private final int mFirstPageOfJuz;
        private int mJuzPages;
        private long mMidNight;

        public Juz(Context context, int juzNumber) {
            mQuranMetaDatabase = new QuranMetaDatabase(context);
            mJuzNumber = juzNumber;
            mJuzPages = 20;

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Date dateNow = c.getTime();


            mMidNight = dateNow.getTime();

            if (juzNumber == 1) {
                mJuzPages = 21;
            } else if (juzNumber == 30) {
                mJuzPages = 23;
            }
            mFirstPageOfJuz = mQuranMetaDatabase.getFirstPageOfJuzNumber(juzNumber);
        }

        public ArrayList<Card> getTwoWeeksPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            for (int i = 0; i < mJuzPages; i++) {

                int cardNumber = i + 1; //same as pageNumber , id and title
                int pageNo = mFirstPageOfJuz + i;
                String title = "Juz " + mJuzNumber + ", Page " + i + 1;

                int surahId = mQuranMetaDatabase.getPageSurahNumber(pageNo - 1);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(surahId);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(surahId, surahEnglishName);

                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(pageNo - 1);

                float f = (float) (cardNumber) / 2;
                int dayToMemorize = Math.round(f);
                long dueDate = mMidNight + ((long) dayToMemorize * 86400000);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, title, surah, mJuzNumber, ayah, dueDate, dueDateInWords));

            }
            return cards;
        }

        public ArrayList<Card> getThreeWeeksPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            for (int i = 0; i < mJuzPages; i++) {

                int cardNumber = i + 1; //same as pageNumber , id and title
                int pageNo = mFirstPageOfJuz + i;
                String title = "Juz " + mJuzNumber + ", Page " + i + 1;

                int surahId = mQuranMetaDatabase.getPageSurahNumber(pageNo - 1);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(surahId);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(surahId, surahEnglishName);


                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(pageNo - 1);

                long dueDate = mMidNight  + ((long) cardNumber * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, title, surah, mJuzNumber, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getFiveWeeksPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();

            int noOfDaysToMemo = (mJuzPages / 2) * 3;

            for (int i = 0; i < noOfDaysToMemo; i++) {
                if ((i + 1) % 3 != 0) {
                    daysToMemorizeList.add(i + 1);
                }
            }

            for (int i = 0; i < mJuzPages; i++) {
                int cardNumber = i + 1; //same as pageNumber , id and title
                int pageNo = mFirstPageOfJuz + i;
                String title = "Juz " + mJuzNumber + ", Page " + i + 1;

                int surahId = mQuranMetaDatabase.getPageSurahNumber(pageNo - 1);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(surahId);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(surahId, surahEnglishName);


                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(pageNo - 1);

                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = mMidNight + (dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, title, surah, mJuzNumber, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getSixWeeksPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();

            int noOfDaysToMemo = mJuzPages * 2;

            for (int i = 0; i < noOfDaysToMemo; i++) {
                if ((i + 1) % 2 != 0) {
                    daysToMemorizeList.add(i + 1);
                }
            }

            for (int i = 0; i < mJuzPages; i++) {

                int cardNumber = i + 1; //same as pageNumber , id and title
                int pageNo = mFirstPageOfJuz + i;
                String title = "Juz " + mJuzNumber + ", Page " + i + 1;

                int surahId = mQuranMetaDatabase.getPageSurahNumber(pageNo - 1);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(surahId);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(surahId, surahEnglishName);


                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(pageNo - 1);

                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = mMidNight + (dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, title, surah, mJuzNumber, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getSevenWeeksPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            ArrayList<Integer> initialDaysToMemorizeList = new ArrayList<>();

            int noOfDaysToMemo = Math.round(((float) mJuzPages / 3) * 7);

            for (int i = 0; i < noOfDaysToMemo; i++) {
                if ((i + 1) % 2 != 0) {
                    initialDaysToMemorizeList.add(i + 1);
                }
            }

            int initialListLength = initialDaysToMemorizeList.size();
            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();

            for (int x = 0; x < initialListLength; x++) {
                if ((initialDaysToMemorizeList.get(x)) % 7 != 0) {
                    daysToMemorizeList.add(daysToMemorizeList.get(x));
                }
            }

            for (int i = 0; i < mJuzPages; i++) {
                int cardNumber = i + 1; //same as pageNumber , id and title
                int pageNo = mFirstPageOfJuz + i;
                String title = "Juz " + mJuzNumber + ", Page " + i + 1;

                int surahId = mQuranMetaDatabase.getPageSurahNumber(pageNo - 1);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(surahId);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(surahId, surahEnglishName);


                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(pageNo - 1);

                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = mMidNight + (dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, title, surah, mJuzNumber, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getNineWeeksPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();

            int noOfDaysToMemo = mJuzPages * 3;

            for (int i = 0; i < noOfDaysToMemo; i++) {
                if (i == 0) {
                    daysToMemorizeList.add(i + 1);
                }
                if ((i + 1) % 3 == 0) {
                    daysToMemorizeList.add(i + 2);
                }
            }

            for (int i = 0; i < mJuzPages; i++) {

                int cardNumber = i + 1; //same as pageNumber , id and title
                int pageNo = mFirstPageOfJuz + i;
                String title = "Juz " + mJuzNumber + ", Page " + i + 1;

                int surahId = mQuranMetaDatabase.getPageSurahNumber(pageNo - 1);
                String surahEnglishName = mQuranMetaDatabase.getSurahEnglishName(surahId);
                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(surahId, surahEnglishName);


                int ayah = mQuranMetaDatabase.getPageFirstAyahNumber(pageNo - 1);

                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = mMidNight + (dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, title, surah, mJuzNumber, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

    }

    public static class Surah {
        private final QuranMetaDatabase mQuranMetaDatabase;
        private final int mSurahNumber;
        private int mFirstPageOfSurah;
        private int mSurahPages;
        private long mMidNight;

        public Surah(Context context, int surahNumber) {
            mQuranMetaDatabase = new QuranMetaDatabase(context);
            mSurahNumber = surahNumber;

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            Date dateNow = c.getTime();


            mMidNight = dateNow.getTime();

            mSurahPages = mQuranMetaDatabase.getSurahPagesList(surahNumber).size();

            if (mSurahPages == 0) {
                mSurahPages++;
                if (surahNumber > 90) {
                    for (int i = 0; i < CONSTANTS.PAGES; i++) {
                        if (mQuranMetaDatabase.getPageSurahNumber(i) == surahNumber - 2) {
                            mFirstPageOfSurah = i + 1;
                        } else if (mQuranMetaDatabase.getPageSurahNumber(i) == surahNumber - 1) {
                            mFirstPageOfSurah = i + 1;
                        }
                    }
                }
            } else {
                try {
                    if (mQuranMetaDatabase.getSurahPagesList(surahNumber).get(0).getInt("ayah") != 1) {
                        mSurahPages++;
                        for (int i = 0; i < CONSTANTS.PAGES; i++) {
                            if (mQuranMetaDatabase.getPageSurahNumber(i) == mQuranMetaDatabase.getSurahPagesList(surahNumber).get(0).getInt("surah")
                                    && mQuranMetaDatabase.getPageFirstAyahNumber(i) == mQuranMetaDatabase.getSurahPagesList(surahNumber).get(0).getInt("ayah")) {
                                mFirstPageOfSurah = i;
                            }
                        }
                    } else {
                        for (int i = 0; i < CONSTANTS.PAGES; i++) {
                            if (mQuranMetaDatabase.getPageSurahNumber(i) == mQuranMetaDatabase.getSurahPagesList(surahNumber).get(0).getInt("surah")
                                    && mQuranMetaDatabase.getPageFirstAyahNumber(i) == mQuranMetaDatabase.getSurahPagesList(surahNumber).get(0).getInt("ayah")) {
                                mFirstPageOfSurah = i + 1;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        public ArrayList<Card> getTwoPagesPerDayPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            for (int i = 0; i < mSurahPages; i++) {

                int cardNumber = i + 1; //same as pageNumber , id and title
                int pageNo = mFirstPageOfSurah + i;
                String title = mSurahNumber + "." + mQuranMetaDatabase.getSurahEnglishName(mSurahNumber) + ", Page " + i + 1;

                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(mSurahNumber, mQuranMetaDatabase.getSurahEnglishName(mSurahNumber));

                int ayah = 0;
                if (i == 0 || mQuranMetaDatabase.getPageSurahNumber(pageNo - 1) != mSurahNumber) {
                    ayah = 1;
                } else ayah = mQuranMetaDatabase.getPageFirstAyahNumber(pageNo - 1);


                float f = (float) (cardNumber) / 2;
                int dayToMemorize = Math.round(f);
                long dueDate = mMidNight + ((long) dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, title, surah, 0, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getOnePagePerDayPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            for (int i = 0; i < mSurahPages; i++) {

                int cardNumber = i + 1; //same as pageNumber , id and title
                int pageNo = mFirstPageOfSurah + i;
                String title = mSurahNumber + "." + mQuranMetaDatabase.getSurahEnglishName(mSurahNumber) + ", Page " + i + 1;

                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(mSurahNumber, mQuranMetaDatabase.getSurahEnglishName(mSurahNumber));

                int ayah = 0;
                if (i == 0 || mQuranMetaDatabase.getPageSurahNumber(pageNo - 1) != mSurahNumber) {
                    ayah = 1;
                } else ayah = mQuranMetaDatabase.getPageFirstAyahNumber(pageNo - 1);

                long dueDate = mMidNight + ((long) cardNumber * 86400000);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, title, surah, 0, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getOnePagePerTwoDaysPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();

            int noOfDaysToMemo = mSurahPages * 2;

            for (int i = 0; i < noOfDaysToMemo; i++) {
                if ((i + 1) % 2 != 0) {
                    daysToMemorizeList.add(i + 1);
                }
            }

            for (int i = 0; i < mSurahPages; i++) {

                int cardNumber = i + 1; //same as pageNumber , id and title
                int pageNo = mFirstPageOfSurah + i;
                String title = mSurahNumber + "." + mQuranMetaDatabase.getSurahEnglishName(mSurahNumber) + ", Page " + i + 1;

                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(mSurahNumber, mQuranMetaDatabase.getSurahEnglishName(mSurahNumber));


                int ayah = 0;
                if (i == 0 || mQuranMetaDatabase.getPageSurahNumber(pageNo - 1) != mSurahNumber) {
                    ayah = 1;
                } else ayah = mQuranMetaDatabase.getPageFirstAyahNumber(pageNo - 1);

                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = mMidNight + (dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, title, surah, 0, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

        public ArrayList<Card> getOnePagePerThreeDaysPlan() {
            ArrayList<Card> cards = new ArrayList<>();

            ArrayList<Integer> daysToMemorizeList = new ArrayList<>();

            int noOfDaysToMemo = mSurahPages * 3;

            for (int i = 0; i < noOfDaysToMemo; i++) {
                if (i == 0) {
                    daysToMemorizeList.add(i + 1);
                }
                if ((i + 1) % 3 == 0) {
                    daysToMemorizeList.add(i + 2);
                }
            }

            for (int i = 0; i < mSurahPages; i++) {

                int cardNumber = i + 1; //same as pageNumber , id and title
                int pageNo = mFirstPageOfSurah + i;
                String title = mSurahNumber + "." + mQuranMetaDatabase.getSurahEnglishName(mSurahNumber) + ", Page " + i + 1;

                com.nnems.hifzcompanion.models.Surah surah = new com.nnems.hifzcompanion.models.Surah(mSurahNumber, mQuranMetaDatabase.getSurahEnglishName(mSurahNumber));


                int ayah = 0;
                if (i == 0 || mQuranMetaDatabase.getPageSurahNumber(pageNo - 1) != mSurahNumber) {
                    ayah = 1;
                } else ayah = mQuranMetaDatabase.getPageFirstAyahNumber(pageNo - 1);

                long dayToMemorize = daysToMemorizeList.get(i);
                long dueDate = mMidNight + (dayToMemorize * 86400000L);

                Date date = new Date(dueDate);
                String dueDateInWords = date.toString();

                cards.add(new Card(cardNumber, cardNumber, title, surah, 0, ayah, dueDate, dueDateInWords));
            }
            return cards;
        }

    }
}
