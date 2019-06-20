package com.example.elizabethwhitebaker.safeandsound;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.Manifest.permission.READ_SMS;

public class StatusReportActivity extends AppCompatActivity {
//    private static final String TAG = "StatusReportActivity";
    private static final int REQUEST_READ_SMS = 1;
    private static final String YES = "yes";
    private static final String NO = "no";
    private int initID;
    private ArrayList<TextView> names;
    private ArrayList<TextView> statuses;
    protected ArrayList<TextView> responses;
    private ArrayList<ImageView> stopLights;
    private ConstraintLayout scrollView;
    private ArrayList<String> phones, msgs;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_report);

        initID = getIntent().getIntExtra("initID", 0);
        String eventName = getIntent().getStringExtra("event");
        String eventGroup = eventName + " Group";

        TextView statusReport = findViewById(R.id.statusReportTextView);
        TextView groupName = findViewById(R.id.groupNameTextView);

        scrollView = findViewById(R.id.scrollViewConstraintLayout);

        Button btnGoBack = findViewById(R.id.doneButton);

        responses = new ArrayList<>();
        statuses = new ArrayList<>();
        stopLights = new ArrayList<>();
        names = new ArrayList<>();

        ArrayList<ArrayList<String>> data = readSMS();
        phones = data.get(0);
        msgs = data.get(1);

        DBHandler handler = new DBHandler(getApplicationContext());

        Group g = handler.findHandlerGroup(eventGroup);
        ArrayList<GroupMember> gms = handler.findHandlerGroupMembers(g.getGroupID());
        ArrayList<Member> ms = new ArrayList<>();
        ArrayList<String> numbersCompare = new ArrayList<>();

        for(int i = 0; i < gms.size(); i++) {
            Member m = handler.findHandlerMember(gms.get(i).getMemberID());
            numbersCompare.add(m.getPhoneNumber());
            ms.add(m);
        }

        comparePhones(numbersCompare);
        getRawMessages(data);

        statusReport.setText("Status Report for " + eventName);
        groupName.setText("Responders from " + eventGroup + ":");

        for(int i = 0; i < ms.size(); i++) {
            createResponseTextView(ms.get(i).getResponse());
            int count = 0;
            while(count < phones.size()) {
                if(ms.get(i).getPhoneNumber().equals(phones.get(count))) {
                    if(msgs.get(count).toLowerCase().startsWith(YES)) {
                        ms.get(i).setReplyStatus("GOOD");
                        ms.get(i).setResponse(msgs.get(count));
                        createReplyStatusTextView("GOOD");
                        createStopLightImageView("Green");
                    } else if(msgs.get(count).toLowerCase().startsWith(NO)) {
                        ms.get(i).setReplyStatus("BAD");
                        ms.get(i).setResponse(msgs.get(count));
                        createReplyStatusTextView("BAD");
                        createStopLightImageView("Red");
                    }
                    count = phones.size();
                } else if(count == phones.size() - 1) {
                    ms.get(i).setReplyStatus("UNK");
                    ms.get(i).setResponse("N/A");
                    createReplyStatusTextView("UNK");
                    createStopLightImageView("Yellow");
                }
                count++;
            }
            createMemberNameTextView(ms.get(i).getFirstName() + " " + ms.get(i).getLastName());
            if(stopLights.get(i).hasOverlappingRendering()) {
                ConstraintSet set = new ConstraintSet();
                set.clone(scrollView);
                set.connect(stopLights.get(i).getId(), ConstraintSet.START,
                        names.get(stopLights.size()).getId(), ConstraintSet.END, 16);
            }
        }

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StatusReportActivity.this, HomeScreenActivity.class);
                i.putExtra("initID", initID);
                startActivity(i);
            }
        });
    }

    private ArrayList<ArrayList<String>> readSMS() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        Cursor c = getContentResolver().query(Uri.parse("content://sms/inbox"),
                null, null, null, null);
        if (c != null && c.moveToFirst()) {
            phones = new ArrayList<>();
            msgs = new ArrayList<>();
            for(int i = 0; i < c.getColumnCount(); i++) {
                if (c.getColumnName(i).equalsIgnoreCase("address"))
                    phones.add(c.getString(i));
                else if(c.getColumnName(i).equalsIgnoreCase("body"))
                    msgs.add(c.getString(i));
            }
            c.close();
            data.add(phones);
            data.add(msgs);
        }
        return data;
    }

    private void comparePhones(ArrayList<String> numCompares) {
        for (String numCompare : numCompares)
            for (String phone : phones)
                if (numCompare.equals(phone))
                    phones.add(numCompare);
        for(int i = 0; i < phones.size(); i++)
            for(int j = 0; j < phones.size(); j++)
                if(phones.get(i).equals(phones.get(j)) && i != j)
                    //noinspection SuspiciousListRemoveInLoop
                    phones.remove(j);
    }

    private void getRawMessages(ArrayList<ArrayList<String>> data) {
        for(int i = 0; i < phones.size(); i++)
            for(int j = 0; j < phones.size(); j++)
                if(data.get(0).get(i).equals(phones.get(j)))
                    msgs.add(data.get(1).get(i));
        for(int i = 0; i < msgs.size(); i++)
            for(int j = 0; j < msgs.size(); j++)
                if(msgs.get(i).equals(msgs.get(j)) && i != j)
                    //noinspection SuspiciousListRemoveInLoop
                    msgs.remove(j);
    }

    private void createResponseTextView(String memberResponse) {
        TextView response = new TextView(getApplicationContext());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        response.setId(View.generateViewId());
        response.setText(memberResponse);
        scrollView.addView(response, params);
        ConstraintSet set = new ConstraintSet();
        set.clone(scrollView);
        set.connect(response.getId(), ConstraintSet.START,
                R.id.StatusReportGuideline, ConstraintSet.RIGHT, 8);
        set.connect(response.getId(), ConstraintSet.END,
                R.id.scrollViewConstraintLayout, ConstraintSet.RIGHT, 8);
        set.setHorizontalBias(response.getId(), 0.0f);
        set.connect(response.getId(), ConstraintSet.BOTTOM,
                R.id.scrollViewConstraintLayout, ConstraintSet.BOTTOM, 16);
        if(responses.size() == 0) {
            set.clear(R.id.groupNameTextView, ConstraintSet.BOTTOM);
            set.connect(response.getId(), ConstraintSet.TOP,
                    R.id.groupNameTextView, ConstraintSet.BOTTOM, 16);
        } else {
            set.clear(responses.get(responses.size() - 1).getId(), ConstraintSet.BOTTOM);
            set.connect(response.getId(), ConstraintSet.TOP,
                    responses.get(responses.size() - 1).getId(), ConstraintSet.BOTTOM, 16);
        }
    }

    private void createReplyStatusTextView(String status) {
        TextView replyStat = new TextView(getApplicationContext());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        replyStat.setId(View.generateViewId());
        if(status.equalsIgnoreCase("good"))
            replyStat.setText(R.string.g);
        else if(status.equalsIgnoreCase("bad"))
            replyStat.setText(R.string.b);
        else
            replyStat.setText(R.string.u);
        scrollView.addView(replyStat, params);
        ConstraintSet set = new ConstraintSet();
        set.clone(scrollView);
        set.connect(replyStat.getId(), ConstraintSet.BASELINE,
                responses.get(statuses.size()).getId(), ConstraintSet.BASELINE, 16);
        set.connect(replyStat.getId(), ConstraintSet.RIGHT,
                R.id.StatusReportGuideline, ConstraintSet.LEFT, 16);
        set.applyTo(scrollView);
        statuses.add(replyStat);
    }

    private void createStopLightImageView(String color) {
        ImageView stopLight = new ImageView(getApplicationContext());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        stopLight.setId(View.generateViewId());
        if(color.equalsIgnoreCase("green"))
            stopLight.setImageResource(R.drawable.ic_green_dot);
        else if (color.equalsIgnoreCase("red"))
            stopLight.setImageResource(R.drawable.ic_red_dot);
        else
            stopLight.setImageResource(R.drawable.ic_yellow_dot);
        scrollView.addView(stopLight, params);
        ConstraintSet set = new ConstraintSet();
        set.clone(scrollView);
        set.connect(stopLight.getId(), ConstraintSet.BASELINE,
                statuses.get(stopLights.size()).getId(), ConstraintSet.BASELINE, 16);
        set.connect(stopLight.getId(), ConstraintSet.END,
                statuses.get(stopLights.size()).getId(), ConstraintSet.START, 16);
        set.applyTo(scrollView);
        stopLights.add(stopLight);
    }

    private void createMemberNameTextView(String memberName) {
        TextView name = new TextView(getApplicationContext());
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        name.setId(View.generateViewId());
        name.setText(memberName);
        scrollView.addView(name, params);
        ConstraintSet set = new ConstraintSet();
        set.clone(scrollView);
        set.connect(name.getId(), ConstraintSet.LEFT,
                R.id.scrollViewConstraintLayout, ConstraintSet.LEFT, 16);
        set.connect(name.getId(), ConstraintSet.BASELINE,
                stopLights.get(names.size()).getId(), ConstraintSet.BASELINE, 16);
        name.setHorizontallyScrolling(false);
        name.setMaxLines(3);
        name.setMaxWidth(75);
        name.setSingleLine(false);
        set.applyTo(scrollView);
        names.add(name);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access sms", Toast.LENGTH_SHORT).show();
                readSMS();
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and sms", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(READ_SMS)) {
                        showMessageOKCancel(
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{READ_SMS},
                                                REQUEST_READ_SMS);
                                    }
                                });
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(StatusReportActivity.this)
                .setMessage("You need to allow access to both the permissions")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
