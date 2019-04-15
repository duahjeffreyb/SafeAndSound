package com.example.elizabethwhitebaker.safeandsound;

public class EventGroup {
    private int eventGroupID;
    private int eventID;
    private int groupID;

    EventGroup() {}
    EventGroup(int eventID, int groupID) {
        this.eventID = eventID;
        this.groupID = groupID;
    }

    int getEventGroupID() {return eventGroupID;}
    void setEventGroupID(int eventGroupID) {this.eventGroupID = eventGroupID;}
    int getEventID() {return eventID;}
    public void setEventID(int eventID) {this.eventID = eventID;}
    int getGroupID() {return groupID;}
    public void setGroupID(int groupID) {this.groupID = groupID;}
}
