package com.nnems.hifzcompanion.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nnems.hifzcompanion.R;

public class MainActivity extends AppCompatActivity {

    EditText mSignInEmailField, mSignInPasswordField;
    TextView mRegisterTextButton, mResetPasswordTextButton;
    Button mSignInButton;
    TextInputLayout mSignInEmailTextInputLayout, mSignInPasswordTextInputLayout;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() != null) {
//                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                    finishAffinity();
//                }
//
//            }
//        };

        mSignInEmailField = findViewById(R.id.singInEmailField);
        mSignInPasswordField = findViewById(R.id.signgInPasswordField);

        mRegisterTextButton = findViewById(R.id.textView_register);
        mResetPasswordTextButton = findViewById(R.id.textViewResetPassword);

        mSignInButton = findViewById(R.id.signInButton);

        mSignInEmailTextInputLayout = findViewById(R.id.signInEmailtextInputLayout);
        mSignInPasswordTextInputLayout = findViewById(R.id.signInPasswordtextInputLayout);



        mRegisterTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mResetPasswordTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resetEmailAddress = mSignInEmailField.getText().toString();
                    if(resetEmailAddress.trim().isEmpty()){
                        mSignInEmailTextInputLayout.setError("Please enter your email");
                    }else{
                        mAuth.sendPasswordResetEmail(resetEmailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.i(TAG,"Password reset email sent");
                                    Toast.makeText(MainActivity.this, "Check your email for reset link",
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, "Could not send reset link ",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();


            }

            private void signIn() {
                String emailAddress = mSignInEmailField.getText().toString();
                String password = mSignInPasswordField.getText().toString();
                String fieldEmptySnackbarMessage = "Please fill out empty fields";

                if (emailAddress.trim().isEmpty()) {

                   showSnackbar(fieldEmptySnackbarMessage);
                    mSignInEmailTextInputLayout.setError("Email should not be empty");
                } else if (password.trim().isEmpty()) {
                   showSnackbar(fieldEmptySnackbarMessage);
                    mSignInPasswordTextInputLayout.setError("Password should not be empty");
                } else {
                    mAuth.signInWithEmailAndPassword(emailAddress, password)
                            .addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.i(TAG, "signInWithEmail:success");
//                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.i(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });


                }

            }
        });
        
    }

            private void showSnackbar( String snackbarMessage){
                View parentLayout = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(parentLayout,snackbarMessage, Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(getResources().getColor(R.color.red));
                snackbar.show();
            }

    @Override
    public void onStart() {
        super.onStart();
//         Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
        }
    }

}