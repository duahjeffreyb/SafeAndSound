package com.example.elizabethwhitebaker.safeandsound;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

class Initiator {
    private int initiatorID;
    private String firstName;
    private String lastName;
    private String username;
    private Bitmap picture;
    private String phoneNumber;
    private String password;

    Initiator() {}
    Initiator(String first, String last, String user, Bitmap pic, String phone, String pass) {
        firstName = first;
        lastName = last;
        username = user;
        picture = pic;
        phoneNumber = phone;
        password = pass;
    }

    int getInitiatorID() { return initiatorID; }
    void setInitiatorID(int initiatorID) { this.initiatorID = initiatorID; }
    String getFirstName() {
        return firstName;
    }
    void setFirstName(String firstName) { this.firstName = firstName; }
    void setLastName(String lastName) {
        this.lastName = lastName;
    }
    void setUsername(String username) {
        this.username = username;
    }
    void setBytes(byte[] bytes) { picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); }
    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    String getPassword() { return password; }
    void setPassword(String password) {
        this.password = password;
    }
    String getLastName() {
        return lastName;
    }
    String getUsername() {
        return username;
    }
    byte[] getBytes() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG, 100, b);
        return b.toByteArray();
    }
    String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Initiator{" +
                "initiatorID=" + initiatorID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", picture=" + picture +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
