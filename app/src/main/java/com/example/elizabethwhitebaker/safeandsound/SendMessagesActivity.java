package com.example.elizabethwhitebaker.safeandsound;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SendMessagesActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
//    private static final String TAG = "SendMessagesActivity";

    private Spinner groupSpinner;
    private ConstraintLayout scrollView;
    private DBHandler handler;
    private ArrayList<CheckBox> checkBoxes;
    private Button btnDeleteAll, btnDeleteChecked, btnSend;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_messages);

        groupSpinner = findViewById(R.id.groupSpinner);

        btnDeleteChecked = findViewById(R.id.deleteCheckedButton);
        btnDeleteAll = findViewById(R.id.deleteAllButton);
        btnSend = findViewById(R.id.sendButton);
        Button btnGoBack = findViewById(R.id.goBackButton);

        message = findViewById(R.id.messageEditText);

        scrollView = findViewById(R.id.scrollViewConstraintLayout);

        groupSpinner.setOnItemSelectedListener(this);
        loadSpinnerData();

        btnDeleteChecked.setEnabled(false);
        btnDeleteAll.setEnabled(false);
        btnSend.setEnabled(false);
        message.setEnabled(false);
        message.setText(R.string.idk);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SendMessagesActivity.this, HomeScreenActivity.class);
                i.putExtra("senderClass", "SendMessagesActivity");
                startActivity(i);
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
                                    R.id.chosenGroupTextView, ConstraintSet.BOTTOM, 16);
                            btnDeleteChecked.setEnabled(false);
                            btnDeleteAll.setEnabled(false);
                            message.setEnabled(false);
                            message.setText(R.string.idk);
                        }
                        else if(checkBoxes.indexOf(checkBox) != checkBoxes.size() - 1)
                            set.connect(checkBoxes.get(checkBoxes.indexOf(checkBox) - 1).getId(), ConstraintSet.TOP,
                                    checkBoxes.get(checkBoxes.indexOf(checkBox) + 1).getId(), ConstraintSet.BOTTOM, 16);
                        else if(checkBoxes.indexOf(checkBox) == 0)
                            set.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                                    checkBoxes.get(checkBoxes.indexOf(checkBox) + 1).getId(), ConstraintSet.BOTTOM, 16);
                        else if(checkBoxes.indexOf(checkBox) == checkBoxes.size() - 1)
                            set.connect(checkBoxes.get(checkBoxes.indexOf(checkBox) - 1).getId(), ConstraintSet.TOP,
                                    R.id.chosenGroupTextView, ConstraintSet.BOTTOM, 16);
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
                message.setEnabled(false);
                message.setText(R.string.idk);
                for(CheckBox checkBox : checkBoxes) {
                    scrollView.removeView(checkBox);
                }
                checkBoxes.clear();
                ConstraintSet set = new ConstraintSet();
                set.clone(scrollView);
                set.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                        R.id.chosenGroupTextView, ConstraintSet.BOTTOM, 16);
                set.applyTo(scrollView);
            }
        });

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(message.getText().length() > 0)
                    btnSend.setEnabled(true);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler = new DBHandler(getApplicationContext());
                ArrayList<String> phoneNumbers = new ArrayList<>();
                for(CheckBox checkBox : checkBoxes) {
                    String groupName = checkBox.getText().toString();
                    Group g = handler.findHandlerGroup(groupName);
                    int groupID = g.getGroupID();
                    ArrayList<GroupMember> gMembers = handler.findHandlerGroupMembers(groupID);
                    for(GroupMember gM : gMembers) {
                        int memID = gM.getMemberID();
                        Member m = handler.findHandlerMember(memID);
                        phoneNumbers.add(m.getPhoneNumber());
                    }
                }
                handler.close();
                for(String phone : phoneNumbers) {
                    sendMessage(phone);
                }
                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                Intent i = new Intent(SendMessagesActivity.this, HomeScreenActivity.class);
                i.putExtra("senderClass", "SendMessagesActivity");
                startActivity(i);
            }
        });
    }

    private void sendMessage(String phone) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, "Your Boss", message.getText().toString(), null, null);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSpinnerData() {
        handler = new DBHandler(getApplicationContext());
        int init = 1;
        int initID = getIntent().getIntExtra("initID", init);
        ArrayList<GroupLeader> groups = handler.findHandlerGroupLeader(initID);
        ArrayList<String> groupNames = new ArrayList<>();
        for(GroupLeader gL : groups) {
            int groupID = gL.getGroupID();
            Group g = handler.findHandlerGroup(groupID);
            groupNames.add(g.getGroupName());
        }

        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, groupNames);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupAdapter);
        handler.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        btnDeleteAll.setEnabled(true);
        btnDeleteChecked.setEnabled(true);
        message.setEnabled(true);
        message.setText("");
        String groupName = adapterView.getItemAtPosition(i).toString();
        CheckBox checkBox = new CheckBox(this);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams
                (ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        scrollView.addView(checkBox, params);
        checkBox.setText(groupName);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(scrollView);
        constraintSet.connect(checkBox.getId(), ConstraintSet.RIGHT,
                R.id.scrollViewConstraintLayout, ConstraintSet.RIGHT, 0);
        constraintSet.connect(checkBox.getId(), ConstraintSet.LEFT,
                R.id.scrollViewConstraintLayout, ConstraintSet.LEFT, 0);
        constraintSet.clear(R.id.deleteCheckedButton, ConstraintSet.TOP);
        constraintSet.connect(checkBox.getId(), ConstraintSet.TOP,
                R.id.chosenGroupTextView, ConstraintSet.BOTTOM, 16);
        if(checkBoxes.size() == 0) {
            constraintSet.connect(R.id.deleteCheckedButton, ConstraintSet.TOP,
                    checkBox.getId(), ConstraintSet.BOTTOM, 16);
        } else {
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