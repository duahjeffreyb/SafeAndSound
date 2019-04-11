package com.example.elizabethwhitebaker.safeandsound;

class GroupLeader {
    private int groupLeaderID;
    private int initiatorID;
    private int groupID;

    GroupLeader() {}

    int getGroupLeaderID() { return groupLeaderID; }
    void setGroupLeaderID(int groupLeaderID) { this.groupLeaderID = groupLeaderID; }
    int getInitiatorID() { return initiatorID; }
    int getGroupID() {
        return groupID;
    }
    void setInitiatorID(int initiatorID) { this.initiatorID = initiatorID; }
    void setGroupID(int groupID) { this.groupID = groupID; }
}
