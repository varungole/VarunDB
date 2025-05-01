package org.example.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;

import org.example.Parsing.SqlParseException;
import org.example.Storage.Storage;
import org.example.Storage.Table;

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

    public static void databaseDoesntExist(){
        throw new SqlParseException("Database doesnt exist!");
    }

    public static void databaseNotDefined(){
        throw new SqlParseException("Which database to work with not defined");
    }

    public static void setHash(HashSet<String> hset) {
        hset.addAll(SQL_COMMANDS);
    }

    public static final Set<String> SQL_COMMANDS = Set.of(
            "select", "update", "insert", "delete", "create", "alter", "drop", "truncate", "describe", "use"
    );

    public static final Set<String> DATA_TYPES = Set.of(
            "int", "string"
    );

    public static final Set<Character> OPERATOR = Set.of(
            '>','<','='
    );

    private static final Pattern WHERE_MATCHER =
            Pattern.compile("^(>=|<=|>|<|=)\\s*(\\d+(?:\\.\\d+)?)$");

    public static void checkIfTableExists(String tableName) {
        if (Storage.currentDatabase.isEmpty()) {
            throw new SqlParseException("No database selected. Use `USE <database>` first.");
        }

        Map<String, Table> currentTables = Storage.getCurrentTables();
        if (!currentTables.containsKey(tableName)) {
            throw new SqlParseException("Table " + tableName + " does not exist in database " + Storage.currentDatabase + "!");
        }
    }


    public static boolean checkComma(char c) {
        return c == ',' || c == ')';
    }

    public static void throwTableExistsError(String tableName) {
        throw new SqlParseException("Table " + tableName + " already exists!!");
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

   public static void writeMeta(String metaPath, List<String> columns, List<ColumnType> columnTypes) {
       try(PrintWriter writer = new PrintWriter(metaPath)) {
           for(int i=0;i<columns.size();i++) {
               writer.println(columns.get(i) + ":" + columnTypes.get(i));
           }
           writer.flush();
       } catch (FileNotFoundException f) {
           System.out.println("File does not exist");
       }
   }

   public static void writeDB(String dataPath, List<List<String>> rows) {
       try(PrintWriter writer = new PrintWriter(dataPath)) {
           for(List<String> row : rows) {
               writer.write(String.valueOf(row));
               writer.println();
           }
           writer.flush();
       } catch (FileNotFoundException f) {
           System.out.println("File does not exist");
       }
   }

   public static void createDB(String dbName) {
       File dbDir = new File("data/" + dbName);
       if (!dbDir.exists()) {
           boolean created = dbDir.mkdirs();
           if (!created) throw new RuntimeException("Failed to create database folder for " + dbName);
       }
   }

   public static boolean isLetterOrDigitOrBlankSpace(char c) {
        return Character.isLetterOrDigit(c) || Character.isWhitespace(c);
   }

}
