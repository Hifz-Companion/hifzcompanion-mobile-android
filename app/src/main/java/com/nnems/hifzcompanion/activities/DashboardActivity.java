package com.nnems.hifzcompanion.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nnems.hifzcompanion.CONSTANTS;
import com.nnems.hifzcompanion.Plan;
import com.nnems.hifzcompanion.R;
import com.nnems.hifzcompanion.database.QuranMetaDatabase;
import com.nnems.hifzcompanion.fragments.DueForMemorizationFragment;
import com.nnems.hifzcompanion.fragments.DueForRevisionFragment;
import com.nnems.hifzcompanion.models.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DashboardActivity";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private DueForMemorizationFragment mDueForMemorizationFragment;
    private DueForRevisionFragment mDueForRevisionFragment;
    private FloatingActionButton mFabAdd, mFabAddQuranPlan, mFabAddJuzPlan, mFabAddSurahPlan;
    private TextView mAddQuranPlanText, mAddJuzPlanText, mAddSurahPlanText;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    //    private Button mAddPlanButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;
    private boolean isOpen;

    private Animation mFabOpenAnim, mFabCloseAnim;

    private AlertDialog show;

    private String mQuranPlan, mJuzPlan, mSurahPlan, mTarget;
    private int mJuzNumber, mSurahNumber;
    private ArrayList<String> mSurahNameList;
    private long mRegistrationDate;
    private String mEmail;
    private String mUserId;
    private ProgressDialog mProgressDialog;
    private Map<String, Object> mMemo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mProgressDialog = new ProgressDialog(this);
        mNavigationView = findViewById(R.id.navigation_view);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mDb = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        mEmail = user.getEmail();
        mUserId = user.getUid();

        View headerView = mNavigationView.getHeaderView(0);
        TextView userEmailDisplay = headerView.findViewById(R.id.header_user_email_display);
        userEmailDisplay.setText(mEmail);

        mSurahNameList = new ArrayList<>();
        mSurahNameList.add("Choose Surah");
        QuranMetaDatabase quranMetaDatabase = new QuranMetaDatabase(this);
        for (int i = 0; i < CONSTANTS.SURAHS; i++) {
            int index = i + 1;
            String surahNames = quranMetaDatabase.getSurahNameList(index);
//            Log.d(TAG, (surahNames + "\n"));
            mSurahNameList.add(surahNames);
        }


        DocumentReference docRef = mDb.collection("users").document(mUserId);


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        mRegistrationDate = document.getLong("registrationDate");
                        mMemo = (Map<String, Object>) document.get("memo");
                        Log.i(TAG, String.valueOf(mRegistrationDate));

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);
//        mAddPlanButton = findViewById(R.id.addPlanButton);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);

        mDueForMemorizationFragment = new DueForMemorizationFragment();
        mDueForRevisionFragment = new DueForRevisionFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(mDueForMemorizationFragment, "Due For Memorizing");
        viewPagerAdapter.addFragment(mDueForRevisionFragment, "Due For Revision");
        viewPager.setAdapter(viewPagerAdapter);

        mFabAdd = findViewById(R.id.fabAddPlan);
        mFabAddQuranPlan = findViewById(R.id.fabQuranPlan);
        mFabAddJuzPlan = findViewById(R.id.fabJuzPlan);
        mFabAddSurahPlan = findViewById(R.id.fabSurahPlan);

        mAddQuranPlanText = findViewById(R.id.textViewAddQuranPlan);
        mAddJuzPlanText = findViewById(R.id.textViewAddJuzPlan);
        mAddSurahPlanText = findViewById(R.id.textViewAddSurahPlan);

        isOpen = false;

        mFabOpenAnim = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        mFabCloseAnim = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    mFabAdd.setImageDrawable(getResources().getDrawable(R.drawable.ic_fab_open));
                    mFabAddQuranPlan.setAnimation(mFabCloseAnim);
                    mFabAddJuzPlan.setAnimation(mFabCloseAnim);
                    mFabAddSurahPlan.setAnimation(mFabCloseAnim);

                    mAddQuranPlanText.setVisibility(View.INVISIBLE);
                    mAddJuzPlanText.setVisibility(View.INVISIBLE);
                    mAddSurahPlanText.setVisibility(View.INVISIBLE);

                    isOpen = false;
                } else {

                    mFabAdd.setImageDrawable(getResources().getDrawable(R.drawable.ic_fab_close));
                    mFabAddQuranPlan.setAnimation(mFabOpenAnim);
                    mFabAddJuzPlan.setAnimation(mFabOpenAnim);
                    mFabAddSurahPlan.setAnimation(mFabOpenAnim);

                    mAddQuranPlanText.setVisibility(View.VISIBLE);
                    mAddJuzPlanText.setVisibility(View.VISIBLE);
                    mAddSurahPlanText.setVisibility(View.VISIBLE);
                    isOpen = true;
                }
            }
        });

        mFabAddQuranPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(DashboardActivity.this);
                builder.setTitle("CHOOSE QURAN PLAN");

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.choose_quran_plan, null);
                builder.setView(dialogView);
                show = builder.show();

                Spinner mSpinnerQuranPlan = dialogView.findViewById(R.id.quranPlanSpinner);
                ArrayAdapter<CharSequence> quranPlanAdapter = ArrayAdapter.createFromResource(DashboardActivity.this,
                        R.array.quran_plans, android.R.layout.simple_spinner_item);
                quranPlanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerQuranPlan.setAdapter(quranPlanAdapter);

                mSpinnerQuranPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mQuranPlan = "error";
                                break;
                            case 1:
                                mQuranPlan = "oneYear";
                                break;
                            case 2:
                                mQuranPlan = "twoYears";
                                break;
                            case 3:
                                mQuranPlan = "threeYears";
                                break;
                            case 4:
                                mQuranPlan = "fourYears";
                                break;
                            case 5:
                                mQuranPlan = "fiveYears";
                                break;
                            case 6:
                                mQuranPlan = "sixYears";
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                Button createMyPlanButton = dialogView.findViewById(R.id.btn_create_my_quran_plan);
                createMyPlanButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProgressDialog progressDialog = new ProgressDialog(DashboardActivity.this);

                        Plan.Quran quranPlan = new Plan.Quran(DashboardActivity.this);
                        ArrayList<Card> cards = new ArrayList<>();
                        mTarget = "quran";


                        switch (mQuranPlan) {
                            case "oneYear":
                                cards = quranPlan.getOneYearPlan(mRegistrationDate);
                                setUserPlan(cards, mQuranPlan, mTarget);
                                break;
                            case "twoYears":
                                cards = quranPlan.getTwoYearPlan(mRegistrationDate);
                                setUserPlan(cards, mQuranPlan, mTarget);
                                break;
                            case "threeYears":
                                cards = quranPlan.getThreeYearPlan(mRegistrationDate);
                                setUserPlan(cards, mQuranPlan, mTarget);
                                break;
                            case "fourYears":
                                cards = quranPlan.getFourYearPlan(mRegistrationDate);
                                setUserPlan(cards, mQuranPlan, mTarget);
                                break;
                            case "fiveYears":
                                cards = quranPlan.getFiveYearPlan(mRegistrationDate);
                                setUserPlan(cards, mQuranPlan, mTarget);
                                break;
                            case "sixYears":
                                cards = quranPlan.getSixYearPlan(mRegistrationDate);
                                setUserPlan(cards, mQuranPlan, mTarget);
                                break;
                            case "error":
                                Toast.makeText(DashboardActivity.this,
                                        "Please Choose A Plan", Toast.LENGTH_LONG).show();
                                Log.i(TAG, "Error");
                                break;


                        }
                        show.dismiss();
                    }
                });


            }
        });

        mFabAddJuzPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(DashboardActivity.this);
                builder.setTitle("CHOOSE JUZ PLAN");

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.choose_juz_plan, null);
                builder.setView(dialogView);
                show = builder.show();

                Spinner mSpinnerJuz = dialogView.findViewById(R.id.juzSpinner);
                ArrayAdapter<CharSequence> juzAdapter = ArrayAdapter.createFromResource(DashboardActivity.this,
                        R.array.juzs, android.R.layout.simple_spinner_item);
                juzAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerJuz.setAdapter(juzAdapter);

                mSpinnerJuz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mJuzNumber = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Spinner mSpinnerJuzPlan = dialogView.findViewById(R.id.juzPlanSpinner);
                ArrayAdapter<CharSequence> juzPlanAdapter = ArrayAdapter.createFromResource(DashboardActivity.this,
                        R.array.juz_plans, android.R.layout.simple_spinner_item);
                juzPlanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerJuzPlan.setAdapter(juzPlanAdapter);

                mSpinnerJuzPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mJuzPlan = "error";
                            case 1:
                                mJuzPlan = "twoWeeks";
                                break;
                            case 2:
                                mJuzPlan = "threeWeeks";
                                break;
                            case 3:
                                mJuzPlan = "fiveWeeks";
                                break;
                            case 4:
                                mJuzPlan = "sixWeeks";
                                break;
                            case 5:
                                mJuzPlan = "sevenWeeks";
                                break;
                            case 6:
                                mJuzPlan = "nineWeeks";
                                break;

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Button createMyPlanButton = dialogView.findViewById(R.id.btn_create_my_juz_plan);
                createMyPlanButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Plan.Juz juzPlan = new Plan.Juz(DashboardActivity.this, mJuzNumber);
                        ArrayList<Card> cards = new ArrayList<>();
                        mTarget = "juz";

                        if (mJuzNumber == 0) {
                            Toast.makeText(DashboardActivity.this, "Please Choose Juz", Toast.LENGTH_LONG).show();
                        } else {

                            switch (mJuzPlan) {
                                case "twoWeeks":
                                    cards = juzPlan.getTwoWeeksPlan(mRegistrationDate);
                                    setUserPlan(cards, mJuzPlan, mTarget);
                                    break;
                                case "threeWeeks":
                                    cards = juzPlan.getThreeWeeksPlan(mRegistrationDate);
                                    setUserPlan(cards, mJuzPlan, mTarget);
                                    break;
                                case "fiveWeeks":
                                    cards = juzPlan.getFiveWeeksPlan(mRegistrationDate);
                                    setUserPlan(cards, mJuzPlan, mTarget);
                                    break;
                                case "sixWeeks":
                                    cards = juzPlan.getSixWeeksPlan(mRegistrationDate);
                                    setUserPlan(cards, mJuzPlan, mTarget);
                                    break;
                                case "sevenWeeks":
                                    cards = juzPlan.getSevenWeeksPlan(mRegistrationDate);
                                    setUserPlan(cards, mJuzPlan, mTarget);
                                    break;
                                case "nineWeeks":
                                    cards = juzPlan.getNineWeeksPlan(mRegistrationDate);
                                    setUserPlan(cards, mJuzPlan, mTarget);
                                    break;
                                case "error":
                                    Toast.makeText(DashboardActivity.this,
                                            "Please Choose A Plan", Toast.LENGTH_LONG).show();
                                    Log.i(TAG, "Error");
                                    break;
                            }
                        }


                        show.dismiss();
                    }
                });
            }
        });

        mFabAddSurahPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(DashboardActivity.this);
                builder.setTitle("CHOOSE SURAH PLAN");

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.choose_surah_plan, null);
                builder.setView(dialogView);
                show = builder.show();

                Spinner mSpinnerSurah = dialogView.findViewById(R.id.surahSpinner);
                ArrayAdapter<String> surahAdapter = new ArrayAdapter<>(DashboardActivity.this, android.R.layout.simple_spinner_item, mSurahNameList);
                surahAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerSurah.setAdapter(surahAdapter);

                mSpinnerSurah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mSurahNumber = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Spinner mSpinnerSurahPlan = dialogView.findViewById(R.id.surahPlanSpinner);
                ArrayAdapter<CharSequence> surahPlanAdapter = ArrayAdapter.createFromResource(DashboardActivity.this,
                        R.array.surah_plans, android.R.layout.simple_spinner_item);
                surahPlanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerSurahPlan.setAdapter(surahPlanAdapter);

                mSpinnerSurahPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mSurahPlan = "error";
                                break;
                            case 1:
                                mSurahPlan = "twoP/D";
                                break;
                            case 2:
                                mSurahPlan = "oneP/D";
                                break;
                            case 3:
                                mSurahPlan = "oneP/2D";
                                break;
                            case 4:
                                mSurahPlan = "oneP/3D";
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                Button createMyPlanButton = dialogView.findViewById(R.id.btn_create_my_surah_plan);
                createMyPlanButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Plan.Surah surahPlan = new Plan.Surah(DashboardActivity.this, mSurahNumber);
                        ArrayList<Card> cards = new ArrayList<>();
                        mTarget = "surah";

                        if (mSurahNumber == 0) {
                            Toast.makeText(DashboardActivity.this, "Please Choose Surah", Toast.LENGTH_SHORT).show();
                        } else {
                            switch (mSurahPlan) {
                                case "twoP/D":
                                    cards = surahPlan.getTwoPagesPerDayPlan(mRegistrationDate);
                                    setUserPlan(cards, mSurahPlan, mTarget);
                                    break;
                                case "oneP/D":
                                    cards = surahPlan.getOnePagePerDayPlan(mRegistrationDate);
                                    setUserPlan(cards, mSurahPlan, mTarget);
                                    break;
                                case "oneP/2D":
                                    cards = surahPlan.getOnePagePerTwoDaysPlan(mRegistrationDate);
                                    setUserPlan(cards, mSurahPlan, mTarget);
                                    break;
                                case "oneP/3D":
                                    cards = surahPlan.getOnePagePerThreeDaysPlan(mRegistrationDate);
                                    setUserPlan(cards, mSurahPlan, mTarget);
                                    break;
                                case "error":
                                    Toast.makeText(DashboardActivity.this,
                                            "Please Choose A Plan", Toast.LENGTH_LONG).show();
                                    Log.i(TAG, "Error");
                                    break;

                            }
                        }


                        show.dismiss();
                    }
                });

            }
        });


    }

    private void setUserPlan(ArrayList<Card> cards, String plan, String target) {

        Map<String, Object> memo = new HashMap<>();

        long startDate = mRegistrationDate + 86400000L;

        memo.put("plan", plan);
        memo.put("target", target);
        memo.put("cards", cards);
        memo.put("startDate", startDate);

        Map<String, Object> userPlan = new HashMap<>();
        userPlan.put("email", mEmail);
        userPlan.put("memo", memo);
        userPlan.put("registrationDate", mRegistrationDate);


        mDb.collection("users")
                .document(mUserId)
                .update(userPlan).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                showSnackbar(getString(R.string.hifz_plan_created_snackbar_text));
                Log.d(TAG, mEmail + " Plan added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Log.d(TAG, mEmail + " Plan not added");
            }
        });

    }

    private void showSnackbar(String snackbarMessage) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, snackbarMessage, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.purple_500));
        snackbar.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                mAuth.signOut();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.nav_help:
                Intent helpActivity = new Intent(DashboardActivity.this, HelpActivity.class);
                startActivity(helpActivity);
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_current_plan:
                deleteCurrentPlan();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteCurrentPlan() {
        MaterialAlertDialogBuilder deletePlanDialog = new MaterialAlertDialogBuilder(DashboardActivity.this);
        deletePlanDialog.setTitle("Delete Current Plan?")
                .setIcon(R.drawable.ic_delete_red)
                .setMessage(R.string.delete_plan_dialog_message);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_plan_dialog, null);

        deletePlanDialog.setView(dialogView)
                .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNeutralButton("DELETE PLAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText deleteEditText = dialogView.findViewById(R.id.delete_plan_dialog_edittextView);

                if (deleteEditText.getText().equals("delete")) {
                    if (mMemo != null) {
                        mDb.collection("users")
                                .document(mUserId).update("memo", FieldValue.delete()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DashboardActivity.this, "Your current plan has been deleted", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DashboardActivity.this, "PLan not deleted. Try Again", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "onFailure: " + e.getMessage());

                            }
                        });
                    } else {
                        Toast.makeText(DashboardActivity.this, "You have no ongoing plans.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "Please enter 'delete' to proceed", Toast.LENGTH_LONG).show();
                }
            }
        }).show();


    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
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
}