package org.example.Util;

import java.util.*;

import org.example.Parsing.SqlParseException;
import org.example.Storage.Storage;

public class Utility {


    private Utility() {}

    public static void checkWhiteSpace(int position, int totalLength, String text) {
        if (position >= totalLength || !Character.isWhitespace(text.charAt(position))) {
            throwError();
        }
    }

    public static void throwError(){
        throw new SqlParseException("Invalid SQL Syntax");
    }

    public static void setHash(HashSet<String> hset) {
        hset.addAll(SQL_COMMANDS);
    }

    public static final Set<String> SQL_COMMANDS = Set.of(
            "select", "update", "insert", "delete", "create", "alter", "drop", "truncate", "describe"
    );

    public static final Set<String> DATA_TYPES = Set.of(
            "int", "string"
    );

    public static void checkIfTableExists(String tableName) {
        if(!Storage.hashMap.containsKey(tableName)) {
            throw new SqlParseException("Table " + tableName + " does not exist!");
        }
    }

    public static boolean checkComma(char c) {
        return c == ',' || c == ')';
    }

    public static void throwTableExistsError(String tableName) {
        throw new SqlParseException("Table " + tableName + " already exists!!");
    }

    public static void succesfullyCreatedTable(String tableName, int length) {
        System.out.println("Inserted " + length + " columns into table " + tableName);
    }

    public static boolean blankQuery(String sqlQuery) {
        if(sqlQuery.isBlank()) {
            System.out.println("Empty query, please try again.");
            return true;
        }
        return false;
    }

    public static boolean verifyIfDataInsertedIsCorrect(List<String> data, int columnSize,  List<ColumnType> columnTypes) {
        if(data.size() != columnSize) return false;
        for(int i=0;i<columnSize;i++) {
            String d = data.get(i);
            ColumnType c = columnTypes.get(i);
            if(isInteger(d) && !c.getColumnTypeName().equals("int")) incorrectDataType();
            if(!isInteger(d) && c.getColumnTypeName().equals("int")) incorrectDataType();
        }
        return true;
    }

    public static void incorrectDataType() {
        throw new SqlParseException("Incorrect data types assigned");
    }

    public static boolean isInteger(String input) {
        if(input == null) return false;
        int length = input.length();
        if(length == 0) return false;
        int i = 0;
        if(input.charAt(0) == '-') {
            if(length == 1) return false;
            i = 1;
        }
        for(;i<length;i++) {
            char c = input.charAt(i);
            if(c < '0' || c> '9') return false;
        }
        return true;
    }
}
