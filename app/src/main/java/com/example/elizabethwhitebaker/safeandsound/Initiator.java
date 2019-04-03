package com.example.elizabethwhitebaker.safeandsound;

import android.graphics.Bitmap;
import android.media.Image;

import java.io.ByteArrayOutputStream;

public class Initiator {
    private int initiatorID;
    private String firstName;
    private String lastName;
    private String username;
    private Bitmap picture;
    private String phoneNumber;

    public Initiator() {}
    public Initiator(int id, String first, String last, String user, Bitmap pic, String phone) {
        initiatorID = id;
        firstName = first;
        lastName = last;
        username = user;
        picture = pic;
        phoneNumber = phone;
    }

    public int getInitiatorID() { return initiatorID; }
    public void setInitiatorID(int initiatorID) { this.initiatorID = initiatorID; }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUsername() {
        return username;
    }
    public Bitmap getPicture() {
        return picture;
    }
    public byte[] getBytes() {
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 0, s);
        return s.toByteArray();
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
