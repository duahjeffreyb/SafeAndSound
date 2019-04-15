package com.example.elizabethwhitebaker.safeandsound;

public class Event {
    private int eventID;
    private String eventName;
    private String eventDescription;

    Event() {};
    public Event(String name, String desc) {
        eventName = name;
        eventDescription = desc;
    }

    int getEventID() {return eventID;}
    void setEventID(int eventID) {this.eventID = eventID;}
    String getEventName() {return eventName;}
    void setEventName(String eventName) {this.eventName = eventName;}
    String getEventDescription() {return eventDescription;}
    void setEventDescription(String eventDescription) {this.eventDescription = eventDescription;}
}
