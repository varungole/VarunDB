package org.example.Storage;

import java.util.List;

public class Table {

    public String tableName;
    public List<String> columns;
    public List<List<String>> rows;

    public Table(String tableName, List<String> columns, List<List<String>> rows) {
        this.tableName = tableName;
        this.columns = columns;
        this.rows = rows;
    }
}

/*
employees --> columns
 Storage.hashMap.put(tableName, new Table(tableName, columns, rows));
 */