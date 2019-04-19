package com.example.elizabethwhitebaker.safeandsound;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.Hashtable;

public class CreateEventActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
//    private static final String TAG = "CreateEventActivity";

    private DBHandler handler;
    private EditText eventNameET, eventDescET;
    private int initID;
    private ArrayList<CheckBox> checkBoxes;
    private Spinner pickGroup;
    private Button btnDeleteChecked, btnDeleteAll, btnCreate;
    private ConstraintLayout scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initID = getIntent().getIntExtra("initID", 0);

        checkBoxes = new ArrayList<>();

        eventNameET = findViewById(R.id.nameEventEditText);
        eventDescET = findViewById(R.id.eventDescriptionEditText);

        pickGroup = findViewById(R.id.pickGroupSpinner);

        btnDeleteChecked = findViewById(R.id.deleteCheckedButton);
        btnDeleteAll = findViewById(R.id.deleteAllButton);
        btnCreate = findViewById(R.id.createButton);
        Button btnGoBack = findViewById(R.id.goBackButton);

        scrollView = findViewById(R.id.scrollViewConstraintLayout);

        pickGroup.setOnItemSelectedListener(this);
        loadSpinnerData();

        btnDeleteChecked.setEnabled(false);
        btnDeleteAll.setEnabled(false);
        btnCreate.setEnabled(false);

        btnDeleteChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintSet set = new ConstraintSet();
                for(CheckBox checkBox : checkBoxes) {
                    if(checkBox.isChecked()) {
                        scrollView.removeView(checkBox);
                        set.clone(scrollView);
                        if(checkBoxes.size() == 1) {
                            set.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                                    R.id.chosenGroupTextView, ConstraintSet.BOTTOM, 16);
                            btnDeleteChecked.setEnabled(false);
                            btnDeleteAll.setEnabled(false);
                            btnCreate.setEnabled(false);
                        }
                        else if(checkBoxes.indexOf(checkBox) != checkBoxes.size() - 1)
                            set.connect(checkBoxes.get(checkBoxes.indexOf(checkBox) + 1).getId(), ConstraintSet.TOP,
                                    checkBoxes.get(checkBoxes.indexOf(checkBox) - 1).getId(), ConstraintSet.BOTTOM, 16);
                        else if(checkBoxes.indexOf(checkBox) == 0)
                            set.connect(checkBoxes.get(1).getId(), ConstraintSet.TOP,
                                    R.id.chosenGroupTextView, ConstraintSet.BOTTOM, 16);
                        else if(checkBoxes.indexOf(checkBox) == checkBoxes.size() - 1)
                            set.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                                    checkBoxes.get(checkBoxes.indexOf(checkBox) - 1).getId(), ConstraintSet.BOTTOM,16);
                        set.applyTo(scrollView);
                        checkBoxes.remove(checkBox);
                    }
                }
            }
        });

        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDeleteChecked.setEnabled(false);
                btnDeleteAll.setEnabled(false);
                btnCreate.setEnabled(false);
                for(CheckBox checkBox : checkBoxes)
                    scrollView.removeView(checkBox);
                checkBoxes.clear();
                ConstraintSet set = new ConstraintSet();
                set.clone(scrollView);
                set.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                        R.id.chosenGroupTextView, ConstraintSet.BOTTOM, 16);
                set.applyTo(scrollView);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Event> es = handler.getAllEvents();
                int count = 0;
                for(Event e : es)
                    if(!eventNameET.getText().toString().equals(e.getEventName()))
                        count++;
                if(!eventNameET.getText().toString().isEmpty() && count == es.size() && !eventDescET.getText().toString().isEmpty()) {
                    handler = new DBHandler(getApplicationContext());
                    Group g = new Group(eventNameET.getText().toString() + " Group");
                    handler.addHandler(g);
                    GroupLeader gL = new GroupLeader(initID, g.getGroupID());
                    handler.addHandler(gL);
                    Event e = new Event(eventNameET.getText().toString(), eventDescET.getText().toString());
                    handler.addHandler(e);
                    for (CheckBox checkBox : checkBoxes) {
                        String groupName = checkBox.getText().toString();
                        Group group = handler.findHandlerGroup(groupName);
                        EventGroup eG = new EventGroup(e.getEventID(), group.getGroupID());
                        handler.addHandler(eG);
                        ArrayList<GroupMember> gMs = handler.findHandlerGroupMembers(group.getGroupID());
                        for (GroupMember gM : gMs) {
                            int memID = gM.getMemberID();
                            Hashtable<Integer, Integer> check = new Hashtable<>();
                            if (!check.get(g.getGroupID()).equals(memID)) {
                                check.put(group.getGroupID(), memID);
                                GroupMember groupM = new GroupMember(g.getGroupID(), memID);
                                handler.addHandler(groupM);
                            }
                        }
                    }
                    handler.close();
                    Intent i = new Intent(CreateEventActivity.this, HomeScreenActivity.class);
                    i.putExtra("initID", initID);
                    startActivity(i);
                }
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateEventActivity.this, HomeScreenActivity.class);
                i.putExtra("initID", initID);
                startActivity(i);
            }
        });
    }

    private void loadSpinnerData() {
        handler = new DBHandler(getApplicationContext());
        ArrayList<Group> gs = handler.getAllGroups();
        ArrayList<String> names = new ArrayList<>();
        names.add("Select group");
        for(Group g : gs)
            names.add(g.getGroupName());
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickGroup.setAdapter(groupAdapter);
        handler.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i != 0) {
            btnDeleteAll.setEnabled(true);
            btnDeleteChecked.setEnabled(true);
            btnCreate.setEnabled(true);
            String groupName = adapterView.getItemAtPosition(i).toString();
            CheckBox checkBox = new CheckBox(this);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                    (ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
            checkBox.setId(View.generateViewId());
            scrollView.addView(checkBox, params);
            checkBox.setText(groupName);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(scrollView);
            constraintSet.connect(checkBox.getId(), ConstraintSet.LEFT,
                    R.id.scrollViewConstraintLayout, ConstraintSet.LEFT, 32);
            constraintSet.clear(R.id.deleteCheckedButton, ConstraintSet.TOP);
            constraintSet.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                    checkBox.getId(), ConstraintSet.BOTTOM, 16);
            if (checkBoxes.size() == 0) {
                constraintSet.connect(checkBox.getId(), ConstraintSet.TOP,
                        R.id.chosenGroupTextView, ConstraintSet.BOTTOM, 16);
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
