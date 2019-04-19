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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ChangeGroupActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
//    private static final String TAG = "ChangeGroupActivity";
    private Spinner chooseGroup, chooseMember;
    private Button btnDeleteChecked, btnDeleteAll, btnAddToGroup, btnRemoveFromGroup, btnDone;
    private int initID;
    private ConstraintLayout scrollView;
    private DBHandler handler;
    private ArrayList<CheckBox> checkBoxes;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_group);

        initID = getIntent().getIntExtra("initID", 0);

        checkBoxes = new ArrayList<>();

        chooseGroup = findViewById(R.id.chooseGroupSpinner);
        chooseMember = findViewById(R.id.chooseMemberSpinner);
        loadSpinnerData();

        scrollView = findViewById(R.id.scrollViewConstraintLayout);

        btnDeleteChecked = findViewById(R.id.deleteCheckedButton);
        btnDeleteAll = findViewById(R.id.deleteAllButton);
        btnRemoveFromGroup = findViewById(R.id.removeFromGroupButton);
        btnAddToGroup = findViewById(R.id.addToGroupButton);
        btnDone = findViewById(R.id.doneButton);

        chooseMember.setEnabled(false);
        btnDeleteChecked.setEnabled(false);
        btnDeleteAll.setEnabled(false);
        btnAddToGroup.setEnabled(false);
        btnRemoveFromGroup.setEnabled(false);
        btnDone.setEnabled(false);

        btnDeleteChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintSet set = new ConstraintSet();
                for(CheckBox checkBox : checkBoxes) {
                    if(checkBox.isChecked()) {
                        scrollView.removeView(checkBox);
                        set.clone(scrollView);
                        if(checkBoxes.size() == 1) {
                            set.connect(R.id.deleteAllButton, ConstraintSet.TOP,
                                    R.id.chosenMemberTextView, ConstraintSet.BOTTOM, 16);
                            btnDeleteChecked.setEnabled(false);
                            btnDeleteAll.setEnabled(false);
                            btnAddToGroup.setEnabled(false);
                            btnRemoveFromGroup.setEnabled(false);
                            btnDone.setEnabled(false);
                        }
                        else if(checkBoxes.indexOf(checkBox) != checkBoxes.size() - 1)
                            set.connect(checkBoxes.get(checkBoxes.indexOf(checkBox) + 1).getId(), ConstraintSet.TOP,
                                    checkBoxes.get(checkBoxes.indexOf(checkBox) - 1).getId(), ConstraintSet.BOTTOM, 16);
                        else if(checkBoxes.indexOf(checkBox) == 0)
                            set.connect(checkBoxes.get(1).getId(), ConstraintSet.TOP,
                                    R.id.chosenMemberTextView, ConstraintSet.BOTTOM, 16);
                        else if(checkBoxes.indexOf(checkBox) == checkBoxes.size() - 1)
                            set.connect(R.id.deleteAllButton, ConstraintSet.TOP,
                                    checkBoxes.get(checkBoxes.indexOf(checkBox) - 1).getId(), ConstraintSet.BOTTOM, 16);
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
                btnAddToGroup.setEnabled(false);
                btnRemoveFromGroup.setEnabled(false);
                btnDone.setEnabled(false);
                for(CheckBox checkBox : checkBoxes)
                    scrollView.removeView(checkBox);
                checkBoxes.clear();
                ConstraintSet set = new ConstraintSet();
                set.clone(scrollView);
                set.connect(R.id.deleteAllButton, ConstraintSet.TOP,
                        R.id.chosenMemberTextView, ConstraintSet.BOTTOM, 16);
                set.applyTo(scrollView);
            }
        });

        btnAddToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler = new DBHandler(getApplicationContext());
                Group g = handler.findHandlerGroup(groupName);
                ArrayList<GroupMember> gms = handler.findHandlerGroupMembers(g.getGroupID());
                ArrayList<Member> mems = new ArrayList<>();
                ArrayList<Integer> memIDs = new ArrayList<>();
                ArrayList<String> memNames = new ArrayList<>();
                for(GroupMember gm : gms)
                    memIDs.add(gm.getMemberID());
                for(int mID : memIDs)
                    mems.add(handler.findHandlerMember(mID));
                for(Member m : mems)
                    memNames.add(m.getFirstName() + " " + m.getLastName());
                for(CheckBox checkBox : checkBoxes) {
                    int count = 0;
                    for(String name : memNames) {
                        if (!checkBox.getText().toString().equals(name))
                            count++;
                    }
                    Member m = handler.findHandlerMember(checkBox.getText().toString().substring(0, checkBox.getText().toString().indexOf(" ")),
                            checkBox.getText().toString().substring(checkBox.getText().toString().indexOf(" ") + 1));
                    if(count == memNames.size())
                        handler.addHandler(new GroupMember(m.getMemberID(), g.getGroupID()));
                    else
                        Toast.makeText(getApplicationContext(), checkBox.getText().toString() + " is already in this group.", Toast.LENGTH_LONG).show();
                }
                handler.close();
            }
        });

        btnRemoveFromGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler = new DBHandler(getApplicationContext());
                Group g = handler.findHandlerGroup(groupName);
                ArrayList<GroupMember> gms = handler.findHandlerGroupMembers(g.getGroupID());
                ArrayList<Member> ms = new ArrayList<>();
                ArrayList<Integer> mIDs = new ArrayList<>();
                ArrayList<String> mNames = new ArrayList<>();
                for(GroupMember gm : gms)
                    mIDs.add(gm.getMemberID());
                for(int mID : mIDs)
                    ms.add(handler.findHandlerMember(mID));
                for(Member m : ms)
                    mNames.add(m.getFirstName() + " " + m.getLastName());
                for(CheckBox checkBox : checkBoxes) {
                    int count = 0;
                    while (count < mNames.size()) {
                        if (checkBox.getText().toString().equals(mNames.get(count))) {
                            handler.deleteHandler(gms.get(count).getGroupMemberID(), "GroupMembers");
                            count = mNames.size();
                        } else {
                            count++;
                            if(count == mNames.size())
                                Toast.makeText(getApplicationContext(), checkBox.getText().toString() + " wasn't in this group.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangeGroupActivity.this, HomeScreenActivity.class);
                i.putExtra("initID", initID);
                startActivity(i);
            }
        });
    }

    private void loadSpinnerData() {
        handler = new DBHandler(this);
        if(!chooseMember.isEnabled()) {
            ArrayList<Group> groups = handler.getAllGroups();
            ArrayList<String> groupNames = new ArrayList<>();
            groupNames.add("Select group");
            for (Group g : groups)
                groupNames.add(g.getGroupName());
            ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, groupNames);
            groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            chooseGroup.setAdapter(groupAdapter);
        } else {
            ArrayList<Member> ms = handler.getAllMembers();
            ArrayList<String> memberNames = new ArrayList<>();
            memberNames.add("Select member");
            for (Member m : ms)
                memberNames.add(m.getFirstName() + " " + m.getLastName());
            ArrayAdapter<String> memberAdapter = new ArrayAdapter<>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, memberNames);
            memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            chooseMember.setAdapter(memberAdapter);
        }
        handler.close();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i != 0) {
            if(view.equals(chooseGroup)) {
                chooseMember.setEnabled(true);
                groupName = adapterView.getItemAtPosition(i).toString();
                loadSpinnerData();
            } else if(view.equals(chooseMember)) {
                btnDeleteAll.setEnabled(true);
                btnDeleteChecked.setEnabled(true);
                btnAddToGroup.setEnabled(true);
                btnRemoveFromGroup.setEnabled(true);
                btnDone.setEnabled(true);
                String memberName = adapterView.getItemAtPosition(i).toString();
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
                constraintSet.clear(R.id.deleteAllButton, ConstraintSet.TOP);
                constraintSet.connect(R.id.deleteAllButton, ConstraintSet.TOP,
                        checkBox.getId(), ConstraintSet.BOTTOM, 16);
                if (checkBoxes.size() == 0) {
                    constraintSet.connect(checkBox.getId(), ConstraintSet.TOP,
                            R.id.chosenMemberTextView, ConstraintSet.BOTTOM, 16);
                } else {
                    constraintSet.connect(checkBox.getId(), ConstraintSet.TOP,
                            checkBoxes.get(checkBoxes.size() - 1).getId(), ConstraintSet.BOTTOM, 16);
                }
                constraintSet.applyTo(scrollView);
                checkBoxes.add(checkBox);
            }
        } else {
            if(view.equals(chooseGroup)) {
                chooseMember.setEnabled(false);
                btnDeleteAll.setEnabled(false);
                btnDeleteChecked.setEnabled(false);
                btnAddToGroup.setEnabled(false);
                btnRemoveFromGroup.setEnabled(false);
                btnDone.setEnabled(false);
                if(!checkBoxes.isEmpty()) {
                    for(CheckBox checkBox : checkBoxes)
                        scrollView.removeView(checkBox);
                    checkBoxes.clear();
                    ConstraintSet set = new ConstraintSet();
                    set.clone(scrollView);
                    set.connect(R.id.deleteAllButton, ConstraintSet.TOP,
                            R.id.chosenMemberTextView, ConstraintSet.BOTTOM, 16);
                    set.applyTo(scrollView);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // TODO Auto-generated method stub
    }
}
