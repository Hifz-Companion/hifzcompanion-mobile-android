package com.nnems.hifzcompanion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nnems.hifzcompanion.R;
import com.nnems.hifzcompanion.models.User;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText mSignUpEmailfield, mSignUpPasswordField, mSignUpConfirmPasswordField;
    TextView mSignInTextButton;
    Button mSignUpButton;
    TextInputLayout mSignUpEmailTextInputLayout, mSignUpPasswordTextInputLayout,
            mSignUpConfirmPasswordTextInputLayout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();


//        EditTextView
        mSignUpEmailfield = findViewById(R.id.signUpEmailField);
        mSignUpPasswordField = findViewById(R.id.signUpPasswordField);
        mSignUpConfirmPasswordField = findViewById(R.id.signUpConfirmPasswordField);

//      TextViews
        mSignInTextButton = findViewById(R.id.textView_signIn);

//        Buttons
        mSignUpButton = findViewById(R.id.signUpButton);

//        TextInputLayouts
        mSignUpEmailTextInputLayout = findViewById(R.id.signUpEmailtextInputLayout);
        mSignUpPasswordTextInputLayout = findViewById(R.id.signUpPasswordtextInputLayout);
        mSignUpConfirmPasswordTextInputLayout = findViewById(R.id.signUpConfirmPasswordtextInputLayout);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });


        mSignInTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//                finish();
            }
        });
    }

    private void signUp() {
        String email = mSignUpEmailfield.getText().toString();
        String password = mSignUpPasswordField.getText().toString();
        String confirmPassword = mSignUpConfirmPasswordField.getText().toString();
        String fieldEmptySnackbarMessage = "Please fill out empty fields";
        String passWordUmatch = "Password do not match";

        if (email.trim().isEmpty()) {
            mSignUpEmailTextInputLayout.setError("Email should not be empty");
            showSnackbar(fieldEmptySnackbarMessage);
        } else if (password.trim().isEmpty()) {
            mSignUpPasswordTextInputLayout.setError("Password should not be empty");
            showSnackbar(fieldEmptySnackbarMessage);
        } else if (confirmPassword.trim().isEmpty()) {
            mSignUpPasswordTextInputLayout.setError("Password should not be empty");
            showSnackbar(fieldEmptySnackbarMessage);
        } else {
            if (passwordsMatch()) {

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String email = user.getEmail();
                                    String userId = user.getUid();
                                    Calendar c = Calendar.getInstance();
                                    Date dateNow = c.getTime();

                                    long registrationDate = dateNow.getTime();

                                    Map<String, Object> memo = new HashMap<>();

                                    User newUser = new User(email, registrationDate, memo);

                                    mDb.collection("users")
                                            .document(userId)
                                            .set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i(email, "data added");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i(email, "data not added");
                                        }
                                    });

                                    Toast.makeText(SignUpActivity.this, "Account Created. Sign In",
                                            Toast.LENGTH_SHORT).show();

//                                    mAuth.signOut();

                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    mSignUpEmailfield.setText("");
                                    mSignUpPasswordField.setText("");
                                    mSignUpConfirmPasswordField.setText("");
                                }
                            }
                        });


            } else {
                showSnackbar(passWordUmatch);
                mSignUpPasswordField.setText("");
                mSignUpConfirmPasswordField.setText("");

            }

        }

    }

    private boolean passwordsMatch() {
        return mSignUpPasswordField.getText().toString().equals(mSignUpConfirmPasswordField.getText().toString());

    }

    private void showSnackbar(String snackbarMessage) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, snackbarMessage, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();
    }
}