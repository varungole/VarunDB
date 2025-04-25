package org.example.Storage;

import org.example.Util.ColumnType;

import java.util.List;

public class Table {

    public String tableName;
    public List<String> columns;
    public List<ColumnType> columnType;
    public List<List<String>> rows;

    public Table(String tableName, List<String> columns,List<ColumnType> columnType,List<List<String>> rows) {
        this.tableName = tableName;
        this.columns = columns;
        this.columnType = columnType;
        this.rows = rows;
    }
}

/*
employees --> columns
 Storage.hashMap.put(tableName, new Table(tableName, columns, rows));
 */