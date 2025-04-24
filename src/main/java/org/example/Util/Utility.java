package org.example.Util;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.example.Parsing.SqlParseException;
import org.example.Storage.Storage;

public class Utility {


    private Utility() {}

    public static void checkWhiteSpace(int position, int totalLength, String text) {
        if (position >= totalLength || !Character.isWhitespace(text.charAt(position))) {
            System.out.println("Throwing error here");
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

    public static void invalidInput(Scanner sc) {
        while(!sc.hasNextInt()) {
            System.out.println("Please enter 1 to continue or 0 to end");
            sc.next();
        }
    }

    public static boolean verifyIfDataInsertedIsCorrect(List<String> data, int size) {
        return data.size() == size;
    }
}
