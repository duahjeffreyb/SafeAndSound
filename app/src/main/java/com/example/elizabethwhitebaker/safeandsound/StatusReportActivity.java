package com.example.elizabethwhitebaker.safeandsound;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class StatusReportActivity extends AppCompatActivity {
//    private static final String TAG = "StatusReportActivity";

    private ArrayList<TextView> names;
    private ArrayList<TextView> reportStatuses;
    private ArrayList<TextView> responses;
    private ConstraintLayout scrollView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_report);

        DBHandler handler = new DBHandler(this);

        final int initID = getIntent().getIntExtra("initID", 0);
        String eventName = getIntent().getStringExtra("event");
        String eventGroup = eventName + " Group";

        TextView statusReport = findViewById(R.id.statusReportTextView);
        TextView groupName = findViewById(R.id.groupNameTextView);

        scrollView = findViewById(R.id.scrollViewConstraintLayout);

        Button btnGoBack = findViewById(R.id.goBackButton);

        statusReport.setText("Status Report for " + eventName);
        groupName.setText("Responders from " + eventGroup + ":");

        Group g = handler.findHandlerGroup(eventGroup);
        ArrayList<GroupMember> gms = handler.findHandlerGroupMembers(g.getGroupID());

        for(GroupMember gm : gms) {
            Member m = handler.findHandlerMember(gm.getMemberID());
            createMemberNameTextView(m.getFirstName() + " " + m.getLastName());
        }



        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StatusReportActivity.this, HomeScreenActivity.class);
                i.putExtra("initID", initID);
                startActivity(i);
            }
        });
    }

    private void createReponseTextView(String memberResponse) {
        TextView response = new TextView(getApplicationContext());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        response.setId(View.generateViewId());
        scrollView.addView(response, params);
        response.setText(memberResponse);
        ConstraintSet set = new ConstraintSet();
        set.clone(scrollView);
        set.connect(response.getId(), ConstraintSet.START, R.id.guideline5, ConstraintSet.RIGHT, 8);
        set.connect(response.getId(), ConstraintSet.END, R.id.scrollViewConstraintLayout, ConstraintSet.RIGHT, 8);
        set.setHorizontalBias(response.getId(), 0.0f);
        set.connect(response.getId(), ConstraintSet.BOTTOM, R.id.scrollViewConstraintLayout, ConstraintSet.BOTTOM, 16);
        if(responses.size() == 0)
            set.connect(response.getId(), ConstraintSet.TOP, R.id.groupNameTextView, ConstraintSet.BOTTOM, 16);
        else {
            set.clear(responses.get(responses.size() - 1).getId(), ConstraintSet.BOTTOM);
            set.connect(response.getId(), ConstraintSet.TOP, responses.get(responses.size() - 1).getId(), ConstraintSet.BOTTOM, 16);
        }
    }

    private void createMemberNameTextView(String memberName) {
        TextView name = new TextView(getApplicationContext());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        name.setId(View.generateViewId());
        scrollView.addView(name, params);
        name.setText(memberName);
        ConstraintSet set = new ConstraintSet();
        set.clone(scrollView);
        set.connect(name.getId(), ConstraintSet.LEFT,
                R.id.scrollViewConstraintLayout, ConstraintSet.LEFT, 16);
        set.connect(name.getId(), ConstraintSet.BOTTOM,
                R.id.scrollViewConstraintLayout, ConstraintSet.BOTTOM, 16);
        if(names.size() == 0)
            set.connect(name.getId(), ConstraintSet.TOP,
                    R.id.groupNameTextView, ConstraintSet.BOTTOM);
        else
            set.connect(name.getId(), ConstraintSet.TOP,
                    names.get(names.size() - 1).getId(), ConstraintSet.BOTTOM);
    }
}
