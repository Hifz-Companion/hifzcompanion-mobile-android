package com.nnems.hifzcompanion.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nnems.hifzcompanion.R;
import com.nnems.hifzcompanion.models.DueCard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DueForRevisionRecyclerAdapter extends RecyclerView.Adapter<DueForRevisionRecyclerAdapter.MyViewHolder> {

    Context mContext;
    private final Map<String, Object> mMemo;
    private final ArrayList<Map<String, Object>> mCards;
    private final String mUserId;
    private final FirebaseFirestore mDb;
    ArrayList<DueCard> mDueCards;
    private static final String TAG = "DueForRevisionRA";

    public DueForRevisionRecyclerAdapter(Context context, Map<String, Object> memo, ArrayList<DueCard> dueCards, String userId) {
        mContext = context;
        mMemo = memo;
        mDueCards = dueCards;
        mCards = (ArrayList<Map<String, Object>>) memo.get("cards");
        mUserId = userId;
        mDb = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public DueForRevisionRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.item_due_for_revision_list, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull DueForRevisionRecyclerAdapter.MyViewHolder holder, int position) {
        holder.page.setText("Quran Page: " + mDueCards.get(position).getPageNo());
        holder.surah.setText("Surah: " + mDueCards.get(position).getSurahId() + ". " + mDueCards.get(position).getSurahName());
        holder.ayah.setText("1st Ayah: " + mDueCards.get(position).getAyah());
    }

    @Override
    public int getItemCount() {
        return mDueCards.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView page, surah, ayah;
        Button again, good, hard, easy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            page = itemView.findViewById(R.id.due_for_revision_card_quran_page);
            surah = itemView.findViewById(R.id.due_for_revision_card_surah);
            ayah = itemView.findViewById(R.id.due_for_revision_card_first_ayah);
            again = itemView.findViewById(R.id.again_button);
            good = itemView.findViewById(R.id.good_button);
            hard = itemView.findViewById(R.id.hard_button);
            easy = itemView.findViewById(R.id.easy_button);

            again.setOnClickListener(this);
            good.setOnClickListener(this);
            hard.setOnClickListener(this);
            easy.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (v.getId()) {
                case R.id.again_button:
                    setAgain(position);
                    break;
                case R.id.good_button:
                    setGood(position);
                    break;
                case R.id.hard_button:
                    setHard(position);
                    break;
                case R.id.easy_button:
                    setEasy(position);
                    break;
                default:
                    break;

            }
        }
    }

    private void setGood(int position) {
        int cardId = (int) mDueCards.get(position).getId();
        Map<String, Object> card = mCards.get(cardId - 1);
        Calendar c = Calendar.getInstance();
        Date dateNow = c.getTime();

        double cardEase = (double) card.get("ease");
        double cardInterval = (long) card.get("interval");
        long newInterval = (long) (cardInterval * cardEase);
        card.put("interval", newInterval);
        int newIntervalInDays = (int) (newInterval / 86_400_000L);
        card.put("intervalInDays", newIntervalInDays);

        long newDueDate = newInterval + dateNow.getTime();
        card.put("dueDate", newDueDate);
        card.put("dueDateInWords", new Date(newDueDate).toString());

        mCards.set(cardId - 1, card);
        mMemo.put("cards", mCards);

        Map<String, Object> updatedMemo = mMemo;

        mDb.collection("users")
                .document(mUserId)
                .update("memo", updatedMemo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "This page has been rescheduled for revision", Toast.LENGTH_LONG).show();
                Log.d(TAG, mUserId + " Memo updated");
                mDueCards.remove(position);
                notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Log.d(TAG, mUserId + "Memo not added");
            }
        });


    }

    private void setAgain(int position) {
        int cardId = (int) mDueCards.get(position).getId();
        Map<String, Object> card = mCards.get(cardId - 1);
        Calendar c = Calendar.getInstance();
        Date dateNow = c.getTime();

        card.put("phase", "memorizing");
        card.put("interval", 1);

        double newEase = (double) card.get("ease") * 0.85d;
        card.put("ease", newEase);
        card.put("grade", 1);

        long newLapes = (long) card.get("lapses") + 1;
        card.put("lapses", newLapes);
        card.put("dueDate", 0);

        mCards.set(cardId - 1, card);
        mMemo.put("cards", mCards);

        Map<String, Object> updatedMemo = mMemo;

        mDb.collection("users")
                .document(mUserId)
                .update("memo", updatedMemo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "This page has been scheduled for re-memorization", Toast.LENGTH_LONG).show();
                Log.d(TAG, mUserId + " Memo updated");
                mDueCards.remove(position);
                notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Log.d(TAG, mUserId + "Memo not added");
            }
        });

    }

    private void setHard(int position) {
        int cardId = (int) mDueCards.get(position).getId();
        Map<String, Object> card = mCards.get(cardId - 1);
        Calendar c = Calendar.getInstance();
        Date dateNow = c.getTime();

        double newEase = (double) card.get("ease") * 0.8d;
        card.put("ease", newEase);
        double cardInterval = (long) card.get("interval");
        long newInterval = (long) (cardInterval * newEase);

        long intervalLimit = (long) (1.2d * 86400000.0);

        if (newInterval > intervalLimit) {
            card.put("interval", newInterval);
        } else {
            long baseInterval = (long) (cardInterval * 1.2d);
            newInterval = baseInterval;
            card.put("interval", newInterval);
        }

        int newIntervalInDays = (int) (newInterval / 86_400_000L);
        card.put("intervalInDays", newIntervalInDays);

        long newDueDate = newInterval + dateNow.getTime();
        card.put("dueDate", newDueDate);
        card.put("dueDateInWords", new Date(newDueDate).toString());

        mCards.set(cardId - 1, card);
        mMemo.put("cards", mCards);

        Map<String, Object> updatedMemo = mMemo;

        mDb.collection("users")
                .document(mUserId)
                .update("memo", updatedMemo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "This page has been rescheduled for revision", Toast.LENGTH_LONG).show();
                Log.d(TAG, mUserId + " Memo updated");
                mDueCards.remove(position);
                notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Log.d(TAG, mUserId + "Memo not added");
            }
        });


    }

    private void setEasy(int position) {
        int cardId = (int) mDueCards.get(position).getId();
        Map<String, Object> card = mCards.get(cardId - 1);
        Calendar c = Calendar.getInstance();
        Date dateNow = c.getTime();

        double newEase = (double) card.get("ease") * 1.15d;
        card.put("ease", newEase);
        double cardInterval = (long) card.get("interval");
        long newInterval = (long) (cardInterval * newEase);

        int newIntervalInDays = (int) (newInterval / 86_400_000L);
        card.put("intervalInDays", newIntervalInDays);

        long newDueDate = newInterval + dateNow.getTime();
        card.put("dueDate", newDueDate);
        card.put("dueDateInWords", new Date(newDueDate).toString());

        mCards.set(cardId - 1, card);
        mMemo.put("cards", mCards);

        Map<String, Object> updatedMemo = mMemo;

        mDb.collection("users")
                .document(mUserId)
                .update("memo", updatedMemo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "This page has been rescheduled for revision", Toast.LENGTH_LONG).show();
                Log.d(TAG, mUserId + " Memo updated");
                mDueCards.remove(position);
                notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Log.d(TAG, mUserId + "Memo not added");
            }
        });


    }
}
