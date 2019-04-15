package com.example.elizabethwhitebaker.safeandsound;

public class EventMember {
    private int eventMemberID;
    private int eventID;
    private int memberID;

    public EventMember() {};
    public EventMember(int eMID, int eID, int mID) {
        eventMemberID = eMID;
        eventID = eID;
        memberID = mID;
    }

    public int getEventMemberID() {return eventMemberID;}
    public void setEventMemberID(int eventMemberID) {this.eventMemberID = eventMemberID;}
    public int getEventID() {return eventID;}
    public void setEventID(int eventID) {this.eventID = eventID;}
    public int getMemberID() {return memberID;}
    public void setMemberID(int memberID) {this.memberID = memberID;}
}
