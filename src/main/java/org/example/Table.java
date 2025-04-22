package org.example;

import java.util.List;
import java.util.Map;

public class Table {

    public String tableName;
    List<String> columns;
    List<Map<String, String>> rows;

    public Table(String tableName, List<String> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }


    public boolean convertToString() {
        for(Map<String, String> row : rows) {
            System.out.println(row.toString());
        }
        return false;
    }
}

/*
Employee
name id salary
ashish 1 10000
varun 2 15000
 */