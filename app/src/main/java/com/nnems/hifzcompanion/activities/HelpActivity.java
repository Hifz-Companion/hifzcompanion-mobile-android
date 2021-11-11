package com.nnems.hifzcompanion.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.nnems.hifzcompanion.CONSTANTS;
import com.nnems.hifzcompanion.R;

public class HelpActivity extends AppCompatActivity {
    TextView aboutAppTV,plansTV,contactTV,sendFeebackTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aboutAppTV = findViewById(R.id.help_about_app_textView);
        plansTV = findViewById(R.id.help_plans_textView);
        contactTV = findViewById(R.id.help_contact_textView);
        sendFeebackTV = findViewById(R.id.help_send_feedback_textView);

        aboutAppTV.setMovementMethod(LinkMovementMethod.getInstance());
        plansTV.setMovementMethod(LinkMovementMethod.getInstance());
        contactTV.setMovementMethod(LinkMovementMethod.getInstance());

        sendFeebackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:musahseidu@gmail.com"));
                intent.putExtra(Intent.EXTRA_EMAIL, CONSTANTS.FEEDBACKEMAILADDRESS);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}