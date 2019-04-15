package com.example.elizabethwhitebaker.safeandsound;

public class Event {
    private int eventID;
    private String eventName;
    private String eventDescription;

    public Event() {};
    public Event(int id, String name, String desc) {
        eventID = id;
        eventName = name;
        eventDescription = desc;
    }

    public int getEventID() {return eventID;}
    public void setEventID(int eventID) {this.eventID = eventID;}
    public String getEventName() {return eventName;}
    public void setEventName(String eventName) {this.eventName = eventName;}
    public String getEventDescription() {return eventDescription;}
    public void setEventDescription(String eventDescription) {this.eventDescription = eventDescription;}
}
