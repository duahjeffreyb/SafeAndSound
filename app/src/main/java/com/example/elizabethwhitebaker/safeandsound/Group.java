package com.example.elizabethwhitebaker.safeandsound;

public class Group {
    private int groupID;
    private String groupName;

    Group() {}
    public Group(int id, String name) {
        groupID = id;
        groupName = name;
    }

    int getGroupID() {
        return groupID;
    }
    void setGroupID(int groupID) { this.groupID = groupID; }
    String getGroupName() { return groupName; }

    void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}