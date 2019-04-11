package com.example.elizabethwhitebaker.safeandsound;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class SendMessagesActivity extends AppCompatActivity {
//    private static final String TAG = "SendMessagesActivity";

    private Spinner groupSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_messages);

        groupSpinner = findViewById(R.id.groupSpinner);

        Button btnDeleteChecked = findViewById(R.id.deleteCheckedButton);
        Button btnDeleteAll = findViewById(R.id.deleteAllButton);
        Button btnSend = findViewById(R.id.sendButton);
        Button btnGoBack = findViewById(R.id.goBackButton);

        EditText message = findViewById(R.id.messageEditText);

        ConstraintLayout scrollView = findViewById(R.id.scrollViewConstraintLayout);

        btnDeleteChecked.setEnabled(false);
        btnDeleteChecked.setVisibility(View.GONE);
        btnDeleteAll.setEnabled(false);
        btnDeleteAll.setVisibility(View.GONE);
        btnSend.setEnabled(false);

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendMessagesActivity.this, HomeScreenActivity.class));
            }
        });


    }
}
