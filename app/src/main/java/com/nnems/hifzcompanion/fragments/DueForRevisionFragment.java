package com.nnems.hifzcompanion.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nnems.hifzcompanion.R;
import com.nnems.hifzcompanion.adapters.DueForRevisionRecyclerAdapter;
import com.nnems.hifzcompanion.adapters.NoRecyclerAdapter;
import com.nnems.hifzcompanion.models.DueCard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


public class DueForRevisionFragment extends Fragment {
    private RecyclerView mDueForRevisionRecyclerView;
    private DueForRevisionRecyclerAdapter mDueForRevisionRecyclerAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;
    private Map<String, Object> mMemo;
    private String mUserEmail;
    private String mUserId;

    private static final String TAG = "DueForRevisionFragment";
    private long mDateNow;
    private ArrayList<Map<String, Object>> mCards;

    private TextView mNoDueRevisionCardsTV;
    private ArrayList<DueCard> mMDueCards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_due_for_revision, container, false);

        mNoDueRevisionCardsTV = view.findViewById(R.id.no_due_revision_cards_textview);

        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        mUserEmail = user.getEmail();
        mUserId = user.getUid();

        DocumentReference docRef = mDb.collection("users").document(mUserId);

        mDueForRevisionRecyclerView = view.findViewById(R.id.due_for_revision_recyclerView);
        mDueForRevisionRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mDueForRevisionRecyclerView.setAdapter(new NoRecyclerAdapter());
        mMDueCards = new ArrayList<>();

        mSwipeRefreshLayout = view.findViewById(R.id.due_for_revision_fragment_swiperefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecyclerView(docRef);

            }
        });

        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        mDateNow = date.getTime();


        loadRecyclerView(docRef);

        return view;
    }

    private void loadRecyclerView(DocumentReference docRef) {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        if (document.get("memo") != null) {

                            mMemo = (Map<String, Object>) document.get("memo");


                            mCards = (ArrayList<Map<String, Object>>) mMemo.get("cards");


                            mMDueCards.clear();

                            for (int i = 0; i < mCards.size(); i++) {
                                Map<String, Object> card = mCards.get(i);
                                Map<String, Object> cardSurah = (Map<String, Object>) mCards.get(i).get("surah");
                                String phase = (String) card.get("phase");
                                if ((long) card.get("dueDate") <= mDateNow && phase.equals("reviewing")) {
                                    DueCard dueCard = new DueCard();
                                    dueCard.setId((Long) card.get("id"));
                                    dueCard.setAyah((Long) card.get("ayah"));
                                    dueCard.setPageNo((Long) card.get("pageNo"));
                                    dueCard.setEase((Double) card.get("ease"));
                                    dueCard.setSurahId((Long) cardSurah.get("id"));
                                    dueCard.setSurahName((String) cardSurah.get("name"));

                                    mMDueCards.add(dueCard);

                                    String title = (String) card.get("title");
                                    double ease = (Double) card.get("ease");
                                    Log.d(TAG, "DateNow: " + mDateNow);
                                    Log.d(TAG, "onComplete: sucess -> " + title + ", ease: " + ease);

                                    if (mSwipeRefreshLayout.isRefreshing()) {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }

                                }
                            }
                            if (!mMDueCards.isEmpty()) {
                                mDueForRevisionRecyclerAdapter = new DueForRevisionRecyclerAdapter(getActivity(), mMemo, mMDueCards, mUserId);
                                mDueForRevisionRecyclerView.setAdapter(mDueForRevisionRecyclerAdapter);
                            } else {
                                mNoDueRevisionCardsTV.setVisibility(View.VISIBLE);
                            }

                        } else {
                            mNoDueRevisionCardsTV.setText(R.string.no_ongoing_plan_create_plan);
                            mNoDueRevisionCardsTV.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    mNoDueRevisionCardsTV.setVisibility(View.VISIBLE);
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });
    }
}