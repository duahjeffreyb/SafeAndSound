package com.example.elizabethwhitebaker.safeandsound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {
//    private static final String TAG = "HomeScreenActivity";

    private int initID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org);

        DBHandler handler = new DBHandler(getApplicationContext());
        Member liz = new Member("Elizabeth", "Baker", "+13366181185");
        Member codie = new Member("Codie", "Nichols", "+19105201955");
        handler.addHandler(liz);
        handler.addHandler(codie);
        handler.close();

        initID = getIntent().getIntExtra("initID", 0);

        Button btnBuildGroup = findViewById(R.id.buildGroupButton);
        Button btnChangeGroup = findViewById(R.id.changeGroupButton);
        Button btnSendMsgs = findViewById(R.id.sendMsgsButton);
        Button btnSignOut = findViewById(R.id.signOutButton);
        Button btnCheckEvent = findViewById(R.id.checkEventButton);
        Button btnCreateEvent = findViewById(R.id.createEventButton);

        btnSendMsgs.setEnabled(false);
        btnCreateEvent.setEnabled(false);
        btnCheckEvent.setEnabled(false);
        btnChangeGroup.setEnabled(false);

        ArrayList<Group> groups = handler.getAllGroups();
        ArrayList<Event> events = handler.getAllEvents();

        if(groups.size() > 0) {
            btnCreateEvent.setEnabled(true);
            btnChangeGroup.setEnabled(true);
            btnSendMsgs.setEnabled(true);
        }
        if(events.size() > 0) {
            btnCheckEvent.setEnabled(true);
        }

        btnBuildGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreenActivity.this, BuildGroupActivity.class);
                i.putExtra("initID", initID);
                startActivity(i);
            }
        });

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreenActivity.this, CreateEventActivity.class);
                i.putExtra("initID", initID);
                startActivity(i);
            }
        });

        btnChangeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreenActivity.this, ChangeGroupActivity.class);
                i.putExtra("initID", initID);
                startActivity(i);
            }
        });

        btnCheckEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreenActivity.this, CheckEventActivity.class);
                i.putExtra("initID", initID);
                startActivity(i);
            }
        });

        btnSendMsgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeScreenActivity.this, SendMessagesActivity.class);
                i.putExtra("initID", initID);
                startActivity(i);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, MainActivity.class));
            }
        });

    }
}
