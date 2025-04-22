package org.example;

import java.util.HashMap;
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
    }

    public static void checkIfTableExists(String tableName) {
        if(!Storage.hashMap.containsKey(tableName)) {
            System.out.println("Table does not exist");
            System.exit(1);
        }
    }
}
