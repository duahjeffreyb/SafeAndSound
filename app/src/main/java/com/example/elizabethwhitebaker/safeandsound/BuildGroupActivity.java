package com.example.elizabethwhitebaker.safeandsound;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
    private int initID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_group);

        initID = getIntent().getIntExtra("initID", 0);

        checkBoxes = new ArrayList<>();

        groupNameET = findViewById(R.id.groupNameEditText);

        btnDeleteChecked = findViewById(R.id.deleteCheckedButton);
        btnDeleteAll = findViewById(R.id.deleteAllButton);
        btnDone = findViewById(R.id.doneButton);
        Button btnGoBack = findViewById(R.id.goBackButton);

        memberSpinner = findViewById(R.id.memberSpinner);

        scrollView = findViewById(R.id.scrollViewConstraintLayout);

        memberSpinner.setEnabled(false);
        memberSpinner.setOnItemSelectedListener(this);

        btnDeleteChecked.setEnabled(false);
        btnDeleteAll.setEnabled(false);
        btnDone.setEnabled(false);

        groupNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(groupNameET.getText().length() > 0) {
                    memberSpinner.setEnabled(true);
                    loadSpinnerData();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
                                    R.id.chosenMembersTextView, ConstraintSet.BOTTOM, 16);
                            btnDeleteChecked.setEnabled(false);
                            btnDeleteAll.setEnabled(false);
                            btnDone.setEnabled(false);
                        }
                        else if(checkBoxes.indexOf(checkBox) != checkBoxes.size() - 1)
                            set.connect(checkBoxes.get(checkBoxes.indexOf(checkBox) - 1).getId(), ConstraintSet.TOP,
                                    checkBoxes.get(checkBoxes.indexOf(checkBox) + 1).getId(), ConstraintSet.BOTTOM, 16);
                        else if(checkBoxes.indexOf(checkBox) == 0)
                            set.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                                    checkBoxes.get(checkBoxes.indexOf(checkBox) + 1).getId(), ConstraintSet.BOTTOM, 16);
                        else if(checkBoxes.indexOf(checkBox) == checkBoxes.size() - 1)
                            set.connect(checkBoxes.get(checkBoxes.indexOf(checkBox) - 1).getId(), ConstraintSet.TOP,
                                    R.id.chosenMembersTextView, ConstraintSet.BOTTOM, 16);
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
                btnDone.setEnabled(false);
                for(CheckBox checkBox : checkBoxes)
                    scrollView.removeView(checkBox);
                checkBoxes.clear();
                ConstraintSet set = new ConstraintSet();
                set.clone(scrollView);
                set.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                        R.id.chosenMembersTextView, ConstraintSet.BOTTOM, 16);
                set.applyTo(scrollView);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler = new DBHandler(getApplicationContext());
                Group g = new Group(groupNameET.getText().toString());
                handler.addHandler(g);
                g = handler.findHandlerGroup(groupNameET.getText().toString());
                for(CheckBox checkBox : checkBoxes) {
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
        handler = new DBHandler(getApplicationContext());
        ArrayList<Member> ms = handler.getAllMembers();
        ArrayList<String> names = new ArrayList<>();
        for(Member m : ms)
            names.add(m.getFirstName() + " " + m.getLastName());
        ArrayAdapter<String> memberAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, names);
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberSpinner.setAdapter(memberAdapter);
        handler.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        btnDeleteAll.setEnabled(true);
        btnDeleteChecked.setEnabled(true);
        btnDone.setEnabled(true);
        String memberName = adapterView.getItemAtPosition(i).toString();
        CheckBox checkBox = new CheckBox(this);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        scrollView.addView(checkBox, params);
        checkBox.setText(memberName);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(scrollView);
        constraintSet.connect(checkBox.getId(), ConstraintSet.RIGHT,
                R.id.scrollViewConstraintLayout, ConstraintSet.RIGHT, 0);
        constraintSet.connect(checkBox.getId(), ConstraintSet.LEFT,
                R.id.scrollViewConstraintLayout, ConstraintSet.LEFT, 0);
        constraintSet.connect(checkBox.getId(), ConstraintSet.TOP,
                R.id.chosenMembersTextView, ConstraintSet.BOTTOM, 16);
        if(checkBoxes.size() == 0) {
            constraintSet.clear(R.id.deleteCheckedButton, ConstraintSet.TOP);
            constraintSet.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                    checkBox.getId(), ConstraintSet.BOTTOM, 16);
        } else {
            constraintSet.clear(checkBoxes.get(checkBoxes.size() - 1).getId(), ConstraintSet.TOP);
            constraintSet.connect(checkBoxes.get(checkBoxes.size() - 1).getId(), ConstraintSet.TOP,
                    checkBox.getId(), ConstraintSet.BOTTOM, 16);
        }
        constraintSet.applyTo(scrollView);
        checkBoxes.add(checkBox);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // TODO Auto-generated method stub
    }
}
