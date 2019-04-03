package com.example.elizabethwhitebaker.safeandsound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "safeAndSoundDB.db";
    private static final String[] TABLE_NAMES = {"Initiators", "Groups", "Members", "GroupLeaders", "GroupMembers"};
    private static final String[][] TABLE1_COLUMNS = {{"InitiatorID", "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"}, {"FirstName", "TEXT"}, {"LastName", "TEXT"}, {"Username", "TEXT"}, {"Picture", "BLOB"}, {"PhoneNumber", "TEXT"}};
    private static final String[][] TABLE2_COLUMNS = {{"GroupID", "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"}, {"GroupName", "TEXT"}};
    private static final String[][] TABLE3_COLUMNS = {{"MemberID", "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"}, {"FirstName", "TEXT"}, {"LastName", "TEXT"}, {"PhoneNumber", "TEXT"}, {"Reply", "TEXT"}, {"Comments", "TEXT"}};
    private static final String[][] TABLE4_COLUMNS = {{"GroupLeaderID", "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"}, {"InitiatorID", "INTEGER REFERENCES"}, {"GroupID", "INTEGER REFERENCES"}};
    private static final String[][] TABLE5_COLUMNS = {{"GroupMemberID", "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL"}, {"GroupID", "INTEGER REFERENCES"}, {"MemberID", "INTEGER REFERENCES"}};

    private Table[] tables;

    public DBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        tables = tables();
    }

    private Table[] tables() {
        Table table1 = new Table(TABLE_NAMES[0], TABLE1_COLUMNS);
        Table table2 = new Table(TABLE_NAMES[1], TABLE2_COLUMNS);
        Table table3 = new Table(TABLE_NAMES[2], TABLE3_COLUMNS);
        Table table4 = new Table(TABLE_NAMES[3], TABLE4_COLUMNS);
        Table table5 = new Table(TABLE_NAMES[4], TABLE5_COLUMNS);
        Table[] tables = {table1, table2, table3, table4, table5};
        return tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(Table tab : tables) {
            String[] names = tab.getColNames();
            String[] colTypes = tab.getColTypes();
            String createTable = "CREATE TABLE " + tab.getTableName() + "(";
            for(int i = 0; i < names.length; i++) {
                if (!colTypes[i].contains("REFERENCES")) {
                    createTable += names[i] + " " + colTypes[i];
                } else {
                    createTable += names[i] + " " + colTypes[i] + " ";
                    for (Table t : tables) {
                        for (int j = 0; j < names.length; j++) {
                            if (names[i].equals(t.getColNames()[j])) {
                                createTable += t.getTableName() + "(" + names[i] + ") ON UPDATE CASCADE";
                            }
                        }
                    }
                }
                if (i != names.length - 1) {
                    createTable += ", ";
                } else {
                    createTable += " );";
                }
            }
            db.execSQL(createTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public String loadHandler(Table t) {
        String result = "";
        String query = "SELECT * FROM " + t.getTableName();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        while(c.moveToNext()) {
            int result0 = c.getInt(0);
            String result1 = c.getString(1);
            result += String.valueOf(result0) + " " + result1 +
                    System.getProperty("line.separator");
        }
        c.close();
        db.close();
        return result;
    }

    public void addHandler(Initiator initiator) {
        ContentValues values = new ContentValues();
        values.put(TABLE1_COLUMNS[0][0], initiator.getInitiatorID());
        values.put(TABLE1_COLUMNS[1][0], initiator.getFirstName());
        values.put(TABLE1_COLUMNS[2][0], initiator.getLastName());
        values.put(TABLE1_COLUMNS[3][0], initiator.getUsername());
        values.put(TABLE1_COLUMNS[4][0], initiator.getPhoneNumber());
        values.put(TABLE1_COLUMNS[5][0], initiator.getBytes());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[0], null, values);
        db.close();
    }

    public void addHandler(GroupLeader groupLeader) {
        ContentValues values = new ContentValues();
        values.put(TABLE4_COLUMNS[0][0], groupLeader.getGroupID());
        values.put(TABLE4_COLUMNS[1][0], groupLeader.getInitiatorID());
        values.put(TABLE4_COLUMNS[2][0], groupLeader.getGroupID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[3], null, values);
        db.close();
    }

    public void addHandler(Group group) {
        ContentValues values = new ContentValues();
        values.put(TABLE2_COLUMNS[0][0], group.getGroupID());
        values.put(TABLE2_COLUMNS[1][0], group.getGroupName());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[1], null, values);
        db.close();
    }

    public void addHandler(GroupMember groupMember) {
        ContentValues values = new ContentValues();
        values.put(TABLE5_COLUMNS[0][0], groupMember.getGroupMemberID());
        values.put(TABLE5_COLUMNS[1][0], groupMember.getGroupID());
        values.put(TABLE5_COLUMNS[2][0], groupMember.getMemberID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[4], null, values);
        db.close();
    }

    public void addHandler(Member member) {
        ContentValues values = new ContentValues();
        values.put(TABLE3_COLUMNS[0][0], member.getMemberID());
        values.put(TABLE3_COLUMNS[1][0], member.getFirstName());
        values.put(TABLE3_COLUMNS[2][0], member.getLastName());
        values.put(TABLE3_COLUMNS[3][0], member.getPhoneNumber());
        values.put(TABLE3_COLUMNS[4][0], member.getReply());
        values.put(TABLE3_COLUMNS[5][0], member.getComments());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAMES[2], null, values);
        db.close();
    }

    public Initiator findHandler(String first, String last, String phone) {
        String[] cols = tables[0].getColNames();
        String query = "SELECT * FROM " + tables[0].getTableName() + " WHERE " + cols[1] + " = '" + first + "' " +
                "AND " + cols[2] + " = '" + last + "' " +
                "AND " + cols[5] + " = '" + phone + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        Initiator i = new Initiator();
        if(c.moveToFirst()) {
            c.moveToFirst();
            int id = Integer.parseInt(c.getString(0));
            String firstName = c.getString(1);
            String lastName = c.getString(2);
            String user = c.getString(3);
            byte[] pic = c.getBlob(4);
            String phoneNo = c.getString(5);
            c.close();
        } else {
            i = null;
        }
        db.close();
        return i;
    }

    public GroupLeader findHandler(int groupLeaderID, int initID) {
        String[] cols = tables[3].getColNames();
        String query = "SELECT * FROM " + tables[3].getTableName() + " WHERE " + cols[0] + " = '" + groupLeaderID + "' " +
                "AND " + cols[1] + " = '" + initID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        GroupLeader gL = new GroupLeader();
        if(c.moveToFirst()) {
            c.moveToFirst();
            int leaderID = Integer.parseInt(c.getString(0));
            int initiatorID = Integer.parseInt(c.getString(1));
            int groupID = Integer.parseInt(c.getString(2));
            c.close();
        } else {
            gL = null;
        }
        db.close();
        return gL;
    }

    public Group findHandler(int groupID) {
        String[] cols = tables[1].getColNames();
        String query = "SELECT * FROM " + tables[1].getTableName() + " WHERE " + cols[0] + " = '" + groupID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        Group g = new Group();
        if(c.moveToFirst()) {
            c.moveToFirst();
            int id = Integer.parseInt(c.getString(0));
            String name = c.getString(1);
            c.close();
        } else {
            g = null;
        }
        db.close();
        return g;
    }

    public GroupMember findHandler(int groupMemberID, String memID) {
        String[] cols = tables[4].getColNames();
        int memberID = Integer.parseInt(memID);
        String query = "SELECT * FROM " + tables[4].getTableName() + " WHERE " + cols[0] + " = '" + groupMemberID + "' " +
                "AND " + cols[2] + " = '" + memberID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        GroupMember gM = new GroupMember();
        if(c.moveToFirst()) {
            c.moveToFirst();
            int groupMember = Integer.parseInt(c.getString(0));
            int group = Integer.parseInt(c.getString(1));
            int member = Integer.parseInt(c.getString(2));
            c.close();
        } else {
            gM = null;
        }
        db.close();
        return gM;
    }

    public Member findHandler(String last, String phone) {
        String[] cols = tables[2].getColNames();
        String query = "SELECT * FROM " + tables[2].getTableName() + " WHERE " + cols[2] + " = '" + last + "' " +
                "AND " + cols[3] + " = '" + phone + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        Member m = new Member();
        if(c.moveToFirst()) {
            c.moveToFirst();
            int id = Integer.parseInt(c.getString(0));
            String firstName = c.getString(1);
            String lastName = c.getString(2);
            String phoneNo = c.getString(3);
            String reply = c.getString(4);
            String comments = c.getString(5);
            c.close();
        } else {
            m = null;
        }
        db.close();
        return m;
    }

    public boolean deleteHandler(int id, String table) {
        boolean result = false;
        for(int i = 0; i < TABLE_NAMES.length; i++) {
            if(TABLE_NAMES[i].equals(table)) {
                Table t = tables[i];
                String[] cols = t.getColNames();
                String query = "SELECT * FROM " + table + " WHERE " + cols[0] + " = '" + String.valueOf(id) + "'";
                SQLiteDatabase db = this.getWritableDatabase();
                Cursor c = db.rawQuery(query, null);
                switch(TABLE_NAMES[i]) {
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
                }
                break;
            }
        }
        return result;
    }

    public boolean updateHandler(int id, String firstName, String lastName, String username, byte[] picture, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TABLE1_COLUMNS[0][0], id);
        args.put(TABLE1_COLUMNS[1][0], firstName);
        args.put(TABLE1_COLUMNS[2][0], lastName);
        args.put(TABLE1_COLUMNS[3][0], username);
        args.put(TABLE1_COLUMNS[4][0], picture);
        args.put(TABLE1_COLUMNS[5][0], phoneNumber);
        return db.update(TABLE_NAMES[0], args, TABLE1_COLUMNS[0][0] + "=" + id, null) > 0;
    }

    public boolean updateHandler(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TABLE2_COLUMNS[0][0], id);
        args.put(TABLE2_COLUMNS[1][0], name);
        return db.update(TABLE_NAMES[1], args, TABLE2_COLUMNS[0][0] + "=" + id, null) > 0;
    }

    public boolean updateHandler(int id, String firstName, String lastName, String phoneNumber, boolean reply, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TABLE3_COLUMNS[0][0], id);
        args.put(TABLE3_COLUMNS[1][0], firstName);
        args.put(TABLE3_COLUMNS[2][0], lastName);
        args.put(TABLE3_COLUMNS[3][0], phoneNumber);
        args.put(TABLE3_COLUMNS[4][0], reply);
        args.put(TABLE3_COLUMNS[5][0], comments);
        return db.update(TABLE_NAMES[2], args, TABLE3_COLUMNS[0][0] + "=" + id, null) > 0;
    }

    public boolean updateHandler(int groupLeaderID, int initID, int groupID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TABLE4_COLUMNS[0][0], groupLeaderID);
        args.put(TABLE4_COLUMNS[1][0], initID);
        args.put(TABLE4_COLUMNS[2][0], groupID);
        return db.update(TABLE_NAMES[3], args, TABLE4_COLUMNS[0][0] + "=" + groupLeaderID, null) > 0;
    }

    public boolean updateHandler(int groupMemberID, int groupID, String memberID) {
        int memID = 0;
        try {
            memID = Integer.parseInt(memberID);
        } catch(NumberFormatException nfe) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TABLE5_COLUMNS[0][0], groupMemberID);
        args.put(TABLE5_COLUMNS[1][0], groupID);
        args.put(TABLE5_COLUMNS[2][0], memID);
        return db.update(TABLE_NAMES[4], args, TABLE5_COLUMNS[0][0] + "=" + groupMemberID, null) > 0;
    }
}
