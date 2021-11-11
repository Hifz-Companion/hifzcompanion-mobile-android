package com.nnems.hifzcompanion.activities;

import static com.nnems.hifzcompanion.CONSTANTS.CHANNEL_ID;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nnems.hifzcompanion.R;
import com.nnems.hifzcompanion.adapters.DueForMemorizingRecyclerAdapter;
import com.nnems.hifzcompanion.models.DueCard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class NotificationReceiver extends BroadcastReceiver {
    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;
    private Map<String, Object> mMemo;
    private ArrayList<DueCard> mDueCards;
    private String mUserEmail;
    private String mUserId;
    private ArrayList<Map<String, Object>> mCards;
    private long mDateNow;
    private static final String TAG = "NotificationReceiver";
    private Boolean mResult;

    @Override
    public void onReceive(Context context, Intent intent) {
        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        mUserEmail = user.getEmail();
        mUserId = user.getUid();


        checkCardDueForMemoOrRevision();
            if(mResult){
                sendNotification(context);
            }
            else return;
    }

    private Boolean checkCardDueForMemoOrRevision() {

        DocumentReference docRef = mDb.collection("users").document(mUserId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    mMemo = (Map<String, Object>) document.get("memo");
                    if ( mMemo.size() > 0 ) {

                        mCards = (ArrayList<Map<String, Object>>) mMemo.get("cards");
                        Calendar c = Calendar.getInstance();
                        Date date = c.getTime();
                        mDateNow = date.getTime();

                        Log.d(TAG, "DateNow: " + mDateNow);

                        mDueCards.clear();

                        for (int i = 0; i < mCards.size(); i++) {
                            Map<String, Object> card = mCards.get(i);
                            Map<String, Object> cardSurah = (Map<String, Object>) mCards.get(i).get("surah");
                            String phase = (String) card.get("phase");
                            if ((long) card.get("dueDate") <= mDateNow && phase.equals("memorizing")) {
                                DueCard dueCard = new DueCard();
                                dueCard.setId((Long) card.get("id"));
                                dueCard.setAyah((Long) card.get("ayah"));
                                dueCard.setPageNo((Long) card.get("pageNo"));
                                dueCard.setSurahId((Long) cardSurah.get("id"));
                                dueCard.setSurahName((String) cardSurah.get("name"));

                                mDueCards.add(dueCard);
                            }
                        }
                        if (mDueCards.size() >=1) {
                          mResult = true;
                        }
                    }
                }
            }
        });
        return mResult;
    }

    private void sendNotification(Context context) {
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_quran_notification_icon)
                .setContentTitle("Hifz Companion")
                .setContentText("Card due for memorization")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());
                
    }
}
