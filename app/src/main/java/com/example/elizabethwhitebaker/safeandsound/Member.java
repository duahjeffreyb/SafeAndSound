package com.example.elizabethwhitebaker.safeandsound;

public class Member {
    private int memberID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String reply;
    private String comments;

    Member() {}
    public Member(int id, String first, String last, String phone, String re, String com) {
        memberID = id;
        firstName = first;
        lastName = last;
        phoneNumber = phone;
        reply = re;
        comments = com;
    }

    int getMemberID() {
        return memberID;
    }
    void setMemberID(int memberID) { this.memberID = memberID; }
    String getFirstName() {
        return firstName;
    }
    String getLastName() {
        return lastName;
    }
    String getPhoneNumber() {
        return phoneNumber;
    }
    String getReply() {
        return reply;
    }
    String getComments() {
        return comments;
    }
    void setFirstName(String firstName) { this.firstName = firstName; }
    void setLastName(String lastName) { this.lastName = lastName; }
    void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    void setReply(String reply) { this.reply = reply; }
    void setComments(String comments) { this.comments = comments; }
}