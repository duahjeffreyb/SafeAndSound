package com.example.elizabethwhitebaker.safeandsound;

public class Member {
    private int memberID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String reply;
    private String comments;

    public Member() {}
    public Member(int id, String first, String last, String phone, String re, String com) {
        memberID = id;
        firstName = first;
        lastName = last;
        phoneNumber = phone;
        reply = re;
        comments = com;
    }

    public int getMemberID() {
        return memberID;
    }
    public void setMemberID(int memberID) { this.memberID = memberID; }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getReply() {
        return reply;
    }
    public String getComments() {
        return comments;
    }
}