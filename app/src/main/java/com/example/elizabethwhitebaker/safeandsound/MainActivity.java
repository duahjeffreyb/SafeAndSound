package com.example.elizabethwhitebaker.safeandsound;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity {
    //private static final String TAG = "MainActivity";

    private static final int REQUEST_SMS = 0;
    private static final int REQUEST_READ_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSignIn = findViewById(R.id.signin_button);
        Button btnSignUp = findViewById(R.id.signup_button);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasSMSPermission = checkSelfPermission(SEND_SMS);
            if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(SEND_SMS)) {
                    showMessageOKCancel("You need to allow access to Send SMS",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{SEND_SMS},
                                            REQUEST_SMS);
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{SEND_SMS}, REQUEST_SMS);
            }

            int hasReadPermission = checkSelfPermission(READ_SMS);
            if(hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                if(!shouldShowRequestPermissionRationale(READ_SMS)) {
                    showMessageOKCancel("You need to allow access to Read SMS",
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{READ_SMS},
                                    REQUEST_READ_SMS);
                        }
                    });
                    return;
                }
                requestPermissions(new String[]{READ_SMS}, REQUEST_READ_SMS);
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
