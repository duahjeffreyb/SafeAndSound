// Done
package com.example.elizabethwhitebaker.safeandsound;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
//    private static final String TAG = "SignUpActivity";
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int TAKE_PHOTO = 2;
    private ImageView initImageView;
    private Bitmap pic;
    private String first;
    private String last;
    private String user;
    private String phone;
    private String pass;
    private DBHandler handler;
    private EditText FirstName, LastName, Username, PhoneNumber, Password, ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        handler = new DBHandler(this);

        //EditTexts
        FirstName = findViewById(R.id.firstNameEditText);
        LastName = findViewById(R.id.lastNameEditText);
        Username = findViewById(R.id.usernameEditText);
        PhoneNumber = findViewById(R.id.phoneEditText);
        Password = findViewById(R.id.passwordEditText);
        ConfirmPassword = findViewById(R.id.confirmPasswordEditText);

        //TextView
        TextView or = findViewById(R.id.orTextView);

        //ImageView
        initImageView = findViewById(R.id.initImageView);

        //Buttons
        final Button btnSignUp = findViewById(R.id.signUpButton);
        Button btnCancel = findViewById(R.id.cancelButton);
        Button btnPicture = findViewById(R.id.photoButton);
        Button btnTakePhoto = findViewById(R.id.takePhotoButton);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Photo"), RESULT_LOAD_IMAGE);
            }
        });
        if(hasCamera() && hasDefaultCameraApp()) {
            btnTakePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, TAKE_PHOTO);
                }
            });
        } else {
            btnTakePhoto.setEnabled(false);
            btnTakePhoto.setVisibility(View.GONE);
            or.setVisibility(View.GONE);
            ConstraintSet set = new ConstraintSet();
            ConstraintLayout layout = findViewById(R.id.scrollViewConstraintLayout);
            set.clone(layout);
            set.clear(R.id.photoButton, ConstraintSet.RIGHT);
            set.connect(R.id.photoButton, ConstraintSet.RIGHT, R.id.scrollViewConstraintLayout, ConstraintSet.RIGHT, 0);
            set.connect(R.id.photoButton, ConstraintSet.LEFT, R.id.scrollViewConstraintLayout, ConstraintSet.LEFT, 0);
            set.applyTo(layout);
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first = FirstName.getText().toString();
                last = LastName.getText().toString();
                user = Username.getText().toString();
                phone = PhoneNumber.getText().toString();
                pass = Password.getText().toString();
                String confPass = ConfirmPassword.getText().toString();
                if (!first.isEmpty() && !last.isEmpty() && !user.isEmpty()
                        && (initImageView.getVisibility() == View.VISIBLE) && pic != null && !phone.isEmpty()
                        && !pass.isEmpty() && !confPass.isEmpty() && pass.equals(confPass)) {
                    Initiator initiator = new Initiator(first, last, user, pic, phone, pass);
                    handler.addHandler(initiator);
                    initiator = handler.findHandler(user, pass);
                    handler.close();
                    Intent i = new Intent(SignUpActivity.this, HomeScreenActivity.class);
                    i.putExtra("initID", initiator.getInitiatorID());
                    i.putExtra("first", first);
                    i.putExtra("senderClass", "SignUpActivity");
                    startActivity(i);
                } else {
                    AlertDialog a = new AlertDialog.Builder(btnSignUp.getContext()).create();
                    a.setTitle("No");
                    a.setMessage("No");
                    a.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Bitmap b;
            try {
                b = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                pic = b;
                initImageView.setVisibility(View.VISIBLE);
                initImageView.setImageBitmap(b);
            } catch(IOException e) {
                e.printStackTrace();
            }

        } else if(requestCode == TAKE_PHOTO && resultCode == RESULT_OK && data != null) {
            if(data.getExtras() != null) {
                Bitmap b = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                if(b != null) {
                    b.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File f = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                    FileOutputStream ofile;
                    try {
                        ofile = new FileOutputStream(f);
                        ofile.write(bytes.toByteArray());
                        ofile.close();
                        pic = b;
                    } catch (FileNotFoundException fnfe) {
                        fnfe.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    initImageView.setVisibility(View.VISIBLE);
                    initImageView.setImageBitmap(b);
                }
            }
        }
    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private boolean hasDefaultCameraApp() {
        final PackageManager packageManager = getPackageManager();
        final Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}