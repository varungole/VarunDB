package org.example;

import java.util.HashSet;

public class Utility {

    public static void checkWhiteSpace(int position, int totalLength, String text) {
        if (position >= totalLength || !Character.isWhitespace(text.charAt(position))) {
            throwError();
        }
    }

    public static void throwError(){
        System.out.println("Invalid SQL Syntax");
        System.exit(1);
    }

    public static void setHash(HashSet<String> hset) {
        hset.add("select");
        hset.add("update");
        hset.add("insert");
        hset.add("delete");
        hset.add("create");
    }

    public static void checkIfTableExists(String tableName) {
        if(!Storage.hashMap.containsKey(tableName)) {
            System.out.println("Table does not exist");
            System.exit(1);
        }
    }

    public static void checkComma(String text, int position) {
        if(text.charAt(position) != ',' && text.charAt(position) != ')') throwError();
    }

    public static void throwTableExistsError(String tableName) {
        System.out.println("Table " + tableName + " already exists!!");
        System.exit(1);
    }

    public static void succesfullyCreatedTable(String tableName, int length) {
        System.out.println("Inserted " + length + " columns into table " + tableName);
    }
}
