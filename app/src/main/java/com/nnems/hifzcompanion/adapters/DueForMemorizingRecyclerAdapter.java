package com.nnems.hifzcompanion.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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

public class DueForMemorizingRecyclerAdapter extends RecyclerView.Adapter<DueForMemorizingRecyclerAdapter.MyViewHolder> {

    private final Context mContext;
    private final Map<String, Object> mMemo;
    private final ArrayList<Map<String, Object>> mCards;
    private final ArrayList<DueCard> mDueCards;
    private final String mUserId;
    private final FirebaseFirestore mDb;
    private static final String TAG = "DueForMemorizingRA";


    public DueForMemorizingRecyclerAdapter(Context context, Map<String, Object> memo, ArrayList<DueCard> dueCards, String userId) {
        mContext = context;
        mMemo = memo;
        mDueCards = dueCards;
        mCards = (ArrayList<Map<String, Object>>) memo.get("cards");
        mUserId = userId;
        mDb = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.item_due_for_memorizing_list, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText("Quran Page " + mDueCards.get(position).getPageNo());
        holder.surah.setText("Surah: " + mDueCards.get(position).getSurahId() + ". " + mDueCards.get(position).getSurahName());
        holder.ayah.setText("1st Ayah: " + mDueCards.get(position).getAyah());
        holder.page.setText("Quran Page: " + mDueCards.get(position).getPageNo());
    }

    @Override
    public int getItemCount() {
        return mDueCards.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, surah, ayah, page;
        Button finishMemorizngButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.card_title);
            surah = itemView.findViewById(R.id.due_for_memorizing_card_surah);
            ayah = itemView.findViewById(R.id.due_for_memorizing_card_first_ayah);
            page = itemView.findViewById(R.id.due_for_memorizing_card_quran_page);
            finishMemorizngButton = itemView.findViewById(R.id.due_for_memorizing_finsh_button);

            finishMemorizngButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (v.getId()) {
                case R.id.due_for_memorizing_finsh_button:
                    finishMemorizing(position);
                    break;
                default:
                    break;
            }
        }
    }


    private void finishMemorizing(int position) {
        Log.d(TAG, "finishMemorizing: Tapped");
        int cardId = (int) mDueCards.get(position).getId();
        Map<String, Object> card = mCards.get(cardId - 1);
        Calendar c = Calendar.getInstance();
        Date dateNow = c.getTime();
        card.put("phase", "reviewing");
        Long interval = 86_400_000L;
        card.put("interval", interval);
        Long newDueDate = interval + dateNow.getTime();
        card.put("dueDate", newDueDate);
        card.put("dueDateInWords", new Date(newDueDate).toString());
        long intervalInDays = (interval / 86_400_000L);
        card.put("intervalInDays", intervalInDays);

        mCards.set(cardId - 1, card);
        mMemo.put("cards", mCards);


        Map<String, Object> updatedMemo = mMemo;

        mDb.collection("users")
                .document(mUserId)
                .update("memo", updatedMemo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "Congratulations. Page is added to review deck", Toast.LENGTH_LONG).show();
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
