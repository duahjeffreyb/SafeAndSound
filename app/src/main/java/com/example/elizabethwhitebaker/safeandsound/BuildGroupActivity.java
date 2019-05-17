package com.example.elizabethwhitebaker.safeandsound;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class BuildGroupActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
//    private static final String TAG = "BuildGroupActivity";

    private DBHandler handler;
    private EditText groupNameET;
    private Button btnDone, btnDeleteChecked, btnDeleteAll;
    private Spinner memberSpinner;
    private ConstraintLayout scrollView;
    private ArrayList<CheckBox> checkBoxes;
    private ArrayList<String> memNames;
    private int initID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_group);

        initID = getIntent().getIntExtra("initID", 0);

        checkBoxes = new ArrayList<>();
        memNames = new ArrayList<>();

        groupNameET = findViewById(R.id.groupNameEditText);

        btnDeleteChecked = findViewById(R.id.deleteCheckedButton);
        btnDeleteAll = findViewById(R.id.deleteAllButton);
        btnDone = findViewById(R.id.doneButton);
        Button btnGoBack = findViewById(R.id.goBackButton);

        memberSpinner = findViewById(R.id.memberSpinner);

        scrollView = findViewById(R.id.scrollViewConstraintLayout);

        memberSpinner.setOnItemSelectedListener(this);
        loadSpinnerData();

        btnDeleteChecked.setEnabled(false);
        btnDeleteAll.setEnabled(false);
        btnDone.setEnabled(false);

        btnDeleteChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintSet set = new ConstraintSet();
                for(CheckBox checkBox : checkBoxes) {
                    if(checkBox.isChecked()) {
                        memNames.add(checkBox.getText().toString());
                        scrollView.removeView(checkBox);
                        set.clone(scrollView);
                        if(checkBoxes.size() == 1) {
                            set.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                                    R.id.chosenMembersTextView, ConstraintSet.BOTTOM, 16);
                            btnDeleteChecked.setEnabled(false);
                            btnDeleteAll.setEnabled(false);
                            btnDone.setEnabled(false);
                        }
                        else if(checkBoxes.indexOf(checkBox) != checkBoxes.size() - 1)
                            set.connect(checkBoxes.get(checkBoxes.indexOf(checkBox) + 1).getId(), ConstraintSet.TOP,
                                    checkBoxes.get(checkBoxes.indexOf(checkBox) - 1).getId(), ConstraintSet.BOTTOM, 16);
                        else if(checkBoxes.indexOf(checkBox) == 0)
                            set.connect(checkBoxes.get(1).getId(), ConstraintSet.TOP,
                                    R.id.chosenMembersTextView, ConstraintSet.BOTTOM, 16);
                        else if(checkBoxes.indexOf(checkBox) == checkBoxes.size() - 1)
                            set.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                                    checkBoxes.get(checkBoxes.indexOf(checkBox) - 1).getId(), ConstraintSet.BOTTOM, 16);
                        set.applyTo(scrollView);
                        checkBoxes.remove(checkBox);
                        reloadSpinnerData();
                    }
                }
            }
        });

        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDeleteChecked.setEnabled(false);
                btnDeleteAll.setEnabled(false);
                btnDone.setEnabled(false);
                for(CheckBox checkBox : checkBoxes)
                    scrollView.removeView(checkBox);
                checkBoxes.clear();
                ConstraintSet set = new ConstraintSet();
                set.clone(scrollView);
                set.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                        R.id.chosenMembersTextView, ConstraintSet.BOTTOM, 16);
                set.applyTo(scrollView);
                loadSpinnerData();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!groupNameET.getText().toString().isEmpty() ) {
                    handler = new DBHandler(getApplicationContext());
                    boolean same = false;
                    ArrayList<Group> gs = handler.getAllGroups();
                    for(Group g : gs)
                        if(groupNameET.getText().toString().equals(g.getGroupName()))
                            same = true;
                    if(!same) {
                        Group g = new Group(groupNameET.getText().toString());
                        handler.addHandler(g);
                        g = handler.findHandlerGroup(groupNameET.getText().toString());
                        for (CheckBox checkBox : checkBoxes) {
                            String name = checkBox.getText().toString();
                            String first = name.substring(0, name.indexOf(" "));
                            String last = name.substring(name.indexOf(" ") + 1);
                            Member m = handler.findHandlerMember(first, last);
                            GroupMember gM = new GroupMember(g.getGroupID(), m.getMemberID());
                            handler.addHandler(gM);
                        }
                        GroupLeader gL = new GroupLeader(initID, g.getGroupID());
                        handler.addHandler(gL);
                        handler.close();
                        Intent i = new Intent(BuildGroupActivity.this, HomeScreenActivity.class);
                        i.putExtra("initID", initID);
                        startActivity(i);
                    }
                }
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BuildGroupActivity.this, HomeScreenActivity.class);
                i.putExtra("initID", initID);
                startActivity(i);
            }
        });
    }

    private void loadSpinnerData() {
        handler = new DBHandler(this);
        ArrayList<Member> ms = handler.getAllMembers();
        memNames.clear();
        memNames.add("Select name");
        for(Member m : ms)
            memNames.add(m.getFirstName() + " " + m.getLastName());
        ArrayAdapter<String> memberAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, memNames);
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberSpinner.setAdapter(memberAdapter);
        handler.close();
    }

    private void reloadSpinnerData() {
        ArrayAdapter<String> memberAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, memNames);
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberSpinner.setAdapter(memberAdapter);
    }

    private void reloadSpinnerData(String name) {
        for(int i = 0; i < memNames.size(); i++) {
            if (name.equals(memNames.get(i))) {
                memNames.remove(memNames.get(i));
            }
        }
        ArrayAdapter<String> memberAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, memNames);
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberSpinner.setAdapter(memberAdapter);
    }

    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if((c != null ? c.getCount() : 0) > 0) {
            while(c != null && c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if(c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pc = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while(pc.moveToNext()) {
                        String phoneNo = pc.getString(pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i != 0) {
            if(checkBoxes.size() == 0) {
                btnDeleteAll.setEnabled(true);
                btnDeleteChecked.setEnabled(true);
                btnDone.setEnabled(true);
            }
            String memberName = adapterView.getItemAtPosition(i).toString();
            reloadSpinnerData(memberName);
            CheckBox checkBox = new CheckBox(this);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                    (ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
            checkBox.setId(View.generateViewId());
            scrollView.addView(checkBox, params);
            checkBox.setText(memberName);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(scrollView);
            constraintSet.connect(checkBox.getId(), ConstraintSet.LEFT,
                    R.id.scrollViewConstraintLayout, ConstraintSet.LEFT, 32);
            constraintSet.clear(R.id.deleteCheckedButton, ConstraintSet.TOP);
            constraintSet.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                    checkBox.getId(), ConstraintSet.BOTTOM, 16);
            if (checkBoxes.size() == 0) {
                constraintSet.connect(checkBox.getId(), ConstraintSet.TOP,
                        R.id.chosenMembersTextView, ConstraintSet.BOTTOM, 16);
            } else {
                constraintSet.connect(checkBox.getId(), ConstraintSet.TOP,
                        checkBoxes.get(checkBoxes.size() - 1).getId(), ConstraintSet.BOTTOM, 16);
            }
            constraintSet.applyTo(scrollView);
            checkBoxes.add(checkBox);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // TODO Auto-generated method stub
    }
}