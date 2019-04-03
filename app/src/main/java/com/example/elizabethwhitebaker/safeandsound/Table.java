package com.example.elizabethwhitebaker.safeandsound;

public class Table {
    private String tableName;
    private String[][] cols;

    public Table(String table, String[][] cols) {
        tableName = table;
        this.cols = cols;
    }

    public String getTableName() {
        return tableName;
    }

    public String[][] getCols() { return cols; }

    public String[] getColNames() {
        String[] colNames = new String[cols.length];
        for(int i = 0; i < cols.length; i++) {
            colNames[i] = cols[i][0];
        }
        return colNames;
    }

    public String[] getColTypes() {
        String[] colTypes = new String[cols.length];
        for(int i = 0; i < cols.length; i++) {
            colTypes[i] = cols[i][1];
        }
        return colTypes;
    }
}
