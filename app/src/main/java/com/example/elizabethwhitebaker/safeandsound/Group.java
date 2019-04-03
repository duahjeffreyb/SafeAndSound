package com.example.elizabethwhitebaker.safeandsound;

public class Group {
    private int groupID;
    private String groupName;

    public Group() {}
    public Group(int id, String name) {
        groupID = id;
        groupName = name;
    }

    public int getGroupID() {
        return groupID;
    }
    public void setGroupID(int groupID) { this.groupID = groupID; }
    public String getGroupName() { return groupName; }
}