package com.example.elizabethwhitebaker.safeandsound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreenActivity extends AppCompatActivity {
//    private static final String TAG = "HomeScreenActivity";
    Intent intent;
    private int initID;
    private DBHandler handler;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org);

        String senderClass = getIntent().getStringExtra("senderClass");
        TextView welcomeTV = findViewById(R.id.welcomeTextView);

        if(senderClass.equals("SignInActivity") || senderClass.equals("SignUpActivity")) {
            int init = 1;
            initID = getIntent().getIntExtra("initID", init);
            String firstName = getIntent().getStringExtra("first");
            welcomeTV.setText("Welcome " + firstName + "!");
        } else if(senderClass.equals("CreateEventActivityCreate")) {

        }

        Button btnSendMsgs = findViewById(R.id.sendMsgsButton);
        Button btnSignOut = findViewById(R.id.signOutButton);
        Button btnCheckEvent = findViewById(R.id.checkEventButton);
        Button btnCreateEvent = findViewById(R.id.createEventButton);

        btnSendMsgs.setEnabled(false);
        btnCreateEvent.setEnabled(false);
        btnCheckEvent.setEnabled(false);

        btnSendMsgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(HomeScreenActivity.this, SendMessagesActivity.class);
                intent.putExtra("initID", initID);
                startActivity(intent);
            }
        });

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, CreateEventActivity.class));
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, SignInActivity.class));
            }
        });

    }
}
