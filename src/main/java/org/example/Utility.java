package org.example;

import java.util.HashSet;
import java.util.Set;

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
            "select", "update", "insert", "delete", "create"
    );

    public static void checkIfTableExists(String tableName) {
        if(!Storage.hashMap.containsKey(tableName)) {
            System.out.println("Table does not exist");
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
}
