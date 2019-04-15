package com.example.elizabethwhitebaker.safeandsound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "safeAndSoundDB.db";
    private static final String[] TABLE_NAMES = {"Initiators", "Groups", "Members", "GroupLeaders", "GroupMembers", "Events", "EventMembers"};
    private static final String[] TABLE1_COLUMNS = {"InitiatorID", "FirstName", "LastName", "Username", "Picture", "PhoneNumber", "Password"};
    private static final String[] TABLE2_COLUMNS = {"GroupID", "GroupName"};
    private static final String[] TABLE3_COLUMNS = {"MemberID", "FirstName", "LastName", "PhoneNumber", "Reply", "Comments"};
    private static final String[] TABLE4_COLUMNS = {"GroupLeaderID", "InitiatorID", "GroupID"};
    private static final String[] TABLE5_COLUMNS = {"GroupMemberID", "GroupID", "MemberID"};
    private static final String[] TABLE6_COLUMNS = {"EventID", "EventName", "EventDescription"};
    private static final String[] TABLE7_COLUMNS = {"EventMemberID", "EventID", "MemberID"};

    DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Initiators( " +
                "InitiatorID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "FirstName TEXT, " +
                "LastName TEXT, " +
                "Username TEXT, " +
                "Picture BLOB, " +
                "PhoneNumber TEXT, " +
                "Password TEXT);");
        db.execSQL("CREATE TABLE Groups( " +
                "GroupID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "GroupName TEXT);");
        db.execSQL("CREATE TABLE Members( " +
                "MemberID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "FirstName TEXT, " +
                "LastName TEXT, " +
                "PhoneNumber TEXT, " +
                "Reply TEXT, " +
                "Comments TEXT);");
        db.execSQL("CREATE TABLE Events( " +
                "EventID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "EventName TEXT, " +
                "EventDescription TEXT);");
        db.execSQL("CREATE TABLE GroupLeaders( " +
                "GroupLeaderID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "InitiatorID INTEGER REFERENCES Initiator(InitiatorID) ON UPDATE CASCADE, " +
                "GroupID INTEGER REFERENCES Groups(GroupID) ON UPDATE CASCADE);");
        db.execSQL("CREATE TABLE GroupMembers( " +
                "GroupMemberID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "GroupID INTEGER REFERENCES Groups(GroupID) ON UPDATE CASCADE, " +
                "MemberID INTEGER REFERENCES Members(MemberID) ON UPDATE CASCADE);");
        db.execSQL("CREATE TABLE EventMembers( " +
                "GroupMemberID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "EventID INTEGER REFERENCES Events(EventID) ON UPDATE CASCADE, " +
                "MemberID INTEGER REFERENCES Members(MemberID) ON UPDATE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        if(newV != oldV) {
            db.execSQL("DROP TABLE IF EXISTS Initiators");
            db.execSQL("DROP TABLE IF EXISTS Groups");
            db.execSQL("DROP TABLE IF EXISTS Members");
            db.execSQL("DROP TABLE IF EXISTS GroupLeaders");
            db.execSQL("DROP TABLE IF EXISTS GroupMembers");
            db.execSQL("DROP TABLE IF EXISTS Events");
            db.execSQL("DROP TABLE IF EXISTS EventMembers");
            onCreate(db);
        }
    }

    void addHandler(Initiator initiator) {
        ContentValues values = new ContentValues();
        values.put(TABLE1_COLUMNS[0], initiator.getInitiatorID());
        values.put(TABLE1_COLUMNS[1], initiator.getFirstName());
        values.put(TABLE1_COLUMNS[2], initiator.getLastName());
        values.put(TABLE1_COLUMNS[3], initiator.getUsername());
        values.put(TABLE1_COLUMNS[4], initiator.getBytes());
        values.put(TABLE1_COLUMNS[5], initiator.getPhoneNumber());
        values.put(TABLE1_COLUMNS[6], initiator.getPassword());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[0], null, values);
        db.close();
    }

    public void addHandler(GroupLeader groupLeader) {
        ContentValues values = new ContentValues();
        values.put(TABLE4_COLUMNS[0], groupLeader.getGroupID());
        values.put(TABLE4_COLUMNS[1], groupLeader.getInitiatorID());
        values.put(TABLE4_COLUMNS[2], groupLeader.getGroupID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[3], null, values);
        db.close();
    }

    public void addHandler(Group group) {
        ContentValues values = new ContentValues();
        values.put(TABLE2_COLUMNS[0], group.getGroupID());
        values.put(TABLE2_COLUMNS[1], group.getGroupName());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[1], null, values);
        db.close();
    }

    public void addHandler(GroupMember groupMember) {
        ContentValues values = new ContentValues();
        values.put(TABLE5_COLUMNS[0], groupMember.getGroupMemberID());
        values.put(TABLE5_COLUMNS[1], groupMember.getGroupID());
        values.put(TABLE5_COLUMNS[2], groupMember.getMemberID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[4], null, values);
        db.close();
    }

    public void addHandler(Member member) {
        ContentValues values = new ContentValues();
        values.put(TABLE3_COLUMNS[0], member.getMemberID());
        values.put(TABLE3_COLUMNS[1], member.getFirstName());
        values.put(TABLE3_COLUMNS[2], member.getLastName());
        values.put(TABLE3_COLUMNS[3], member.getPhoneNumber());
        values.put(TABLE3_COLUMNS[4], member.getReply());
        values.put(TABLE3_COLUMNS[5], member.getComments());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[2], null, values);
        db.close();
    }

    public void addHandler(EventMember eventMember) {
        ContentValues values = new ContentValues();
        values.put(TABLE7_COLUMNS[0], eventMember.getEventMemberID());
        values.put(TABLE7_COLUMNS[1], eventMember.getEventID());
        values.put(TABLE7_COLUMNS[2], eventMember.getMemberID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[6], null, values);
        db.close();
    }

    public void addHandler(Event event) {
        ContentValues values = new ContentValues();
        values.put(TABLE6_COLUMNS[0], event.getEventID());
        values.put(TABLE6_COLUMNS[1], event.getEventName());
        values.put(TABLE6_COLUMNS[2], event.getEventDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[5], null, values);
        db.close();
    }

    public Initiator findHandler(String user, String pass) {
        String query = "SELECT * FROM Initiators WHERE Username = " + user +
                " AND Password = " + pass + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        Initiator i = new Initiator();
        if(c.moveToFirst()) {
            c.moveToFirst();
            i.setInitiatorID(Integer.parseInt(c.getString(0)));
            i.setFirstName(c.getString(1));
            i.setLastName(c.getString(2));
            i.setUsername(c.getString(3));
            i.setBytes(c.getBlob(4));
            i.setPhoneNumber(c.getString(5));
            i.setPassword(c.getString(6));
            c.close();
        } else {
            i = null;
        }
        db.close();
        return i;
    }

    public ArrayList<GroupLeader> findHandlerGroupLeader(int initID) {
        String query = "SELECT * FROM GroupLeaders WHERE InitiatorID = " + initID + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        ArrayList<GroupLeader> gLGroups = new ArrayList<>();
        int x = 0;
        while(c.moveToNext()) {
            GroupLeader gL = new GroupLeader();
            if(x == 0)
                c.moveToFirst();
            gL.setGroupLeaderID(Integer.parseInt(c.getString(0)));
            gL.setInitiatorID(Integer.parseInt(c.getString(1)));
            gL.setGroupID(Integer.parseInt(c.getString(2)));
            gLGroups.add(gL);
            x++;
        }
        c.close();
        db.close();
        return gLGroups;
    }

    public Group findHandlerGroup(int groupID) {
        String query = "SELECT * FROM Groups WHERE GroupID = " + groupID + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        Group g = new Group();
        if(c.moveToFirst()) {
            c.moveToFirst();
            g.setGroupID(Integer.parseInt(c.getString(0)));
            g.setGroupName(c.getString(1));
            c.close();
        } else {
            g = null;
        }
        db.close();
        return g;
    }

    public Group findHandlerGroup(String groupName) {
        String query = "SELECT * FROM Groups WHERE GroupName = " + groupName + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        Group g = new Group();
        if(c.moveToFirst()) {
            c.moveToFirst();
            g.setGroupID(Integer.parseInt(c.getString(0)));
            g.setGroupName(c.getString(1));
            c.close();
        } else {
            g = null;
        }
        db.close();
        return g;
    }

    public ArrayList<GroupMember> findHandlerGroupMembers(int groupID) {
        String query = "SELECT * FROM GroupMembers WHERE GroupID = " + groupID + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        ArrayList<GroupMember> gMembers = new ArrayList<>();
        int x = 0;
        while(c.moveToNext()) {
            GroupMember gM = new GroupMember();
            if (x == 0)
                c.moveToFirst();
            gM.setGroupMemberID(Integer.parseInt(c.getString(0)));
            gM.setGroupID(Integer.parseInt(c.getString(1)));
            gM.setMemberID(Integer.parseInt(c.getString(2)));
            gMembers.add(gM);
            x++;
        }
        c.close();
        db.close();
        return gMembers;
    }

    public Member findHandlerMember(int memberID) {
        String query = "SELECT * FROM Members WHERE MemberID = " + memberID + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        Member m = new Member();
        if(c.moveToFirst()) {
            c.moveToFirst();
            m.setMemberID(Integer.parseInt(c.getString(0)));
            m.setFirstName(c.getString(1));
            m.setLastName(c.getString(2));
            m.setPhoneNumber(c.getString(3));
            m.setReply(c.getString(4));
            m.setComments(c.getString(5));
            c.close();
        } else {
            m = null;
        }
        db.close();
        return m;
    }

    public ArrayList<EventMember> findHandlerEventMembers(int EventID) {
        String query = "SELECT * FROM EventMembers WHERE EventID = " + EventID + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        ArrayList<EventMember> eMembers = new ArrayList<>();
        int x = 0;
        while(c.moveToNext()) {
            EventMember eM = new EventMember();
            if(x == 0)
                c.moveToFirst();
            eM.setEventMemberID(Integer.parseInt(c.getString(0)));
            eM.setEventID(Integer.parseInt(c.getString(1)));
            eM.setMemberID(Integer.parseInt(c.getString(2)));
            eMembers.add(eM);
            x++;
        }
        c.close();
        db.close();
        return eMembers;
    }

    public Event findHandlerEvent(int eventID) {
        String query = "SELECT * FROM Events WHERE EventID = " + eventID + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        Event e = new Event();
        if(c.moveToFirst()) {
            c.moveToFirst();
            e.setEventID(Integer.parseInt(c.getString(0)));
            e.setEventName(c.getString(1));
            e.setEventDescription(c.getString(2));
            c.close();
        } else {
            e = null;
        }
        db.close();
        return e;
    }

    public boolean deleteHandler(int id, String table) {
        boolean result = false;
        for(String name : TABLE_NAMES) {
            if(name.equals(table)) {
                String query = "SELECT * FROM " + table + " WHERE " + table + "ID = '" + String.valueOf(id) + "'";
                SQLiteDatabase db = this.getWritableDatabase();
                Cursor c = db.rawQuery(query, null);
                switch(name) {
                    case "Initiators":
                        Initiator initiator = new Initiator();
                        if(c.moveToFirst()) {
                            initiator.setInitiatorID(Integer.parseInt(c.getString(0)));
                            db.delete(TABLE_NAMES[0], id + "=?",
                                    new String[] {
                                            String.valueOf(initiator.getInitiatorID())
                                    });
                            c.close();
                            result = true;
                        }
                        db.close();
                        return result;
                    case "Groups":
                        Group group = new Group();
                        if(c.moveToFirst()) {
                            group.setGroupID(Integer.parseInt(c.getString(0)));
                            db.delete(TABLE_NAMES[1], id + "=?",
                                    new String[] {
                                            String.valueOf(group.getGroupID())
                                    });
                            c.close();
                            result = true;
                        }
                        db.close();
                        return result;
                    case "Members":
                        Member member = new Member();
                        if(c.moveToFirst()) {
                            member.setMemberID(Integer.parseInt(c.getString(0)));
                            db.delete(TABLE_NAMES[2], id + "=?",
                                    new String[] {
                                            String.valueOf(member.getMemberID())
                                    });
                            c.close();
                            result = true;
                        }
                        db.close();
                        return result;
                    case "GroupLeaders":
                        GroupLeader groupLeader = new GroupLeader();
                        if(c.moveToFirst()) {
                            groupLeader.setGroupLeaderID(Integer.parseInt(c.getString(0)));
                            db.delete(TABLE_NAMES[3], id + "=?",
                                    new String[] {
                                            String.valueOf(groupLeader.getGroupLeaderID())
                                    });
                            c.close();
                            result = true;
                        }
                        db.close();
                        return result;
                    case "GroupMembers":
                        GroupMember groupMember = new GroupMember();
                        if(c.moveToFirst()) {
                            groupMember.setGroupMemberID(Integer.parseInt(c.getString(0)));
                            db.delete(TABLE_NAMES[4], id + "=?",
                                    new String[] {
                                            String.valueOf(groupMember.getGroupMemberID())
                                    });
                            c.close();
                            result = true;
                        }
                        db.close();
                        return result;
                    case "Events":
                        Event  event = new Event();
                        if(c.moveToFirst()) {
                            event.setEventID(Integer.parseInt(c.getString(0)));
                            db.delete(TABLE_NAMES[5], id + "=?",
                                    new String[] {
                                            String.valueOf(event.getEventID())
                                    });
                            c.close();
                            result = true;
                        }
                        db.close();
                        return result;
                    case "EventMembers":
                }
                break;
            }
        }
        return false;
    }

    public boolean updateHandler(int id, String firstName, String lastName, String username, byte[] picture, String phoneNumber, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TABLE1_COLUMNS[0], id);
        args.put(TABLE1_COLUMNS[1], firstName);
        args.put(TABLE1_COLUMNS[2], lastName);
        args.put(TABLE1_COLUMNS[3], username);
        args.put(TABLE1_COLUMNS[4], picture);
        args.put(TABLE1_COLUMNS[5], phoneNumber);
        args.put(TABLE1_COLUMNS[6], password);
        return db.update(TABLE_NAMES[0], args, TABLE1_COLUMNS[0] + "=" + String.valueOf(id), null) > 0;
    }

    public boolean updateHandler(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TABLE2_COLUMNS[0], id);
        args.put(TABLE2_COLUMNS[1], name);
        return db.update(TABLE_NAMES[1], args, TABLE2_COLUMNS[0] + "=" + String.valueOf(id), null) > 0;
    }

    public boolean updateHandler(int id, String firstName, String lastName, String phoneNumber, boolean reply, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TABLE3_COLUMNS[0], id);
        args.put(TABLE3_COLUMNS[1], firstName);
        args.put(TABLE3_COLUMNS[2], lastName);
        args.put(TABLE3_COLUMNS[3], phoneNumber);
        args.put(TABLE3_COLUMNS[4], reply);
        args.put(TABLE3_COLUMNS[5], comments);
        return db.update(TABLE_NAMES[2], args, TABLE3_COLUMNS[0] + "=" + String.valueOf(id), null) > 0;
    }

    public boolean updateHandler(int groupLeaderID, int initID, int groupID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TABLE4_COLUMNS[0], groupLeaderID);
        args.put(TABLE4_COLUMNS[1], initID);
        args.put(TABLE4_COLUMNS[2], groupID);
        return db.update(TABLE_NAMES[3], args, TABLE4_COLUMNS[0] + "=" + String.valueOf(groupLeaderID), null) > 0;
    }

    public boolean updateHandler(int groupMemberID, int groupID, String memberID) {
        int memID;
        try {
            memID = Integer.parseInt(memberID);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(TABLE5_COLUMNS[0], groupMemberID);
            args.put(TABLE5_COLUMNS[1], groupID);
            args.put(TABLE5_COLUMNS[2], memID);
            return db.update(TABLE_NAMES[4], args, TABLE5_COLUMNS[0] + "=" + String.valueOf(groupMemberID), null) > 0;
        } catch(NumberFormatException nfe) {
            return false;
        }
    }
}
