package com.nnems.hifzcompanion.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nnems.hifzcompanion.R;
import com.nnems.hifzcompanion.fragments.DueForMemorizationFragment;
import com.nnems.hifzcompanion.fragments.DueForRevisionFragment;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private DueForMemorizationFragment mDueForMemorizationFragment;
    private DueForRevisionFragment mDueForRevisionFragment;

    private FloatingActionButton mAddFab, mAddQuranPlanFab, mAddJuzPlanFab, mAddSurahPlanFab;
    private TextView mAddQuranPlanText, mAddJuzPlanText, mAddSurahPlanText;

    private DrawerLayout mDrawer;
//    private Button mAddPlanButton;
    private FirebaseAuth mAuth;

    private FirebaseFirestore mDb;

    private static final String TAG = "DashboardActivity";

    private boolean isOpen;

    private Animation mFabOpenAnim, mFabCloseAnim;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mDb = FirebaseFirestore.getInstance();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);
//        mAddPlanButton = findViewById(R.id.addPlanButton);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);

        mDueForMemorizationFragment = new DueForMemorizationFragment();
        mDueForRevisionFragment = new DueForRevisionFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),0);
        viewPagerAdapter.addFragment(mDueForMemorizationFragment,"Due For Memorizing");
        viewPagerAdapter.addFragment(mDueForRevisionFragment,"Due For Revision");
        viewPager.setAdapter(viewPagerAdapter);

        mAddFab = findViewById(R.id.fabAddPlan);
        mAddQuranPlanFab = findViewById(R.id.fabQuranPlan);
        mAddJuzPlanFab = findViewById(R.id.fabJuzPlan);
        mAddSurahPlanFab = findViewById(R.id.fabSurahPlan);

        mAddQuranPlanText = findViewById(R.id.textViewAddQuranPlan);
        mAddJuzPlanText = findViewById(R.id.textViewAddJuzPlan);
        mAddSurahPlanText = findViewById(R.id.textViewAddSurahPlan);

            isOpen = false;

            mFabOpenAnim = AnimationUtils.loadAnimation(this,R.anim.fab_open);
            mFabCloseAnim = AnimationUtils.loadAnimation(this,R.anim.fab_close);

            mAddFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isOpen){
                        mAddFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_fab_open));
                        mAddQuranPlanFab.setAnimation(mFabCloseAnim);
                        mAddJuzPlanFab.setAnimation(mFabCloseAnim);
                        mAddSurahPlanFab.setAnimation(mFabCloseAnim);

                        mAddQuranPlanText.setVisibility(View.INVISIBLE);
                        mAddJuzPlanText.setVisibility(View.INVISIBLE);
                        mAddSurahPlanText.setVisibility(View.INVISIBLE);

                        isOpen = false;
                    } else{

                        mAddFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_fab_close));
                        mAddQuranPlanFab.setAnimation(mFabOpenAnim);
                        mAddJuzPlanFab.setAnimation(mFabOpenAnim);
                        mAddSurahPlanFab.setAnimation(mFabOpenAnim);

                        mAddQuranPlanText.setVisibility(View.VISIBLE);
                        mAddJuzPlanText.setVisibility(View.VISIBLE);
                        mAddSurahPlanText.setVisibility(View.VISIBLE);
                        isOpen = true;
                    }
                }
            });


//        mAddPlanButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseUser user = mAuth.getCurrentUser();
//                String email = user.getEmail();
//                String userId = user.getUid();
//
//
//                DocumentReference docRef = mDb.collection("users").document(userId);
//
//
//                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            assert document != null;
//                            if (document.exists()) {
//                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                               long registrationDate = document.getLong("registrationDate");
//                                Log.i(TAG, String.valueOf(registrationDate));
//
//                                Map<String,Object> memo = new HashMap<>();
//                                QuranMetaDatabase quranMetaDatabase = new QuranMetaDatabase(DashboardActivity.this);
//                                Plan.Quran quranPlan = new Plan.Quran(quranMetaDatabase);
//                                ArrayList<Map<String,Object>> cards = quranPlan.getOneYearPlan(registrationDate);
//
//                                memo.put("plan","oneYear");
//                                memo.put("target","quran");
//                                memo.put("cards", cards);
//
//                                Map<String,Object> userPlanUpdate = new HashMap<>();
//                                userPlanUpdate.put("email", email);
//                                userPlanUpdate.put("memo", memo);
//                                userPlanUpdate.put("registrationDate", registrationDate);
//
//                                mDb.collection("users")
//                                        .document(userId)
//                                        .update(userPlanUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Log.i(email,"data updated..Plan added");
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Log.i(email,"data not updated..Plan not added");
//                                    }
//                                });
//
//                            } else {
//                                Log.d(TAG, "No such document");
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with ", task.getException());
//                        }
//
//                    }
//                });
//
//
//
//
//            }
//        });

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_logout:
                mAuth.signOut();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}