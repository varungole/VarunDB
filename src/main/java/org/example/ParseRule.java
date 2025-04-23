package org.example;

import static org.example.Utility.*;

import java.util.ArrayList;
import java.util.List;

public class ParseRule {
    public int position;
    Storage storage;

    public ParseRule() {
        position = 0;
        storage = new Storage();
    }

    public void parseSelect(String text) {
        if(text.charAt(position) != '*') {
            throwError();
        }
        position++;
        checkWhiteSpace(position, text.length(), text);
        position++;
        String from = text.substring(position, position+4);
        if(!from.equalsIgnoreCase("from")) {
            throwError();
        }
        position+=4;
        checkWhiteSpace(position, text.length(), text);
        position+=1;
        String tableName = text.substring(position);
        checkIfTableExists(tableName);
        Table table = Storage.hashMap.get(tableName);
        System.out.println(table.columns);
    }

    public void parseUpdate(String text) {
    }

    public void parseInsert(String text) {
    }

    public void parseDelete(String text) {

    }

    public void parseCreate(String text) {
        int len = text.length();
        String table = text.substring(position, position+5);
        if(table.equals("table")) throwError();
        position+=5;
        checkWhiteSpace(position, len, text);
        position++;
        StringBuilder tableName = new StringBuilder();
        while(text.charAt(position) != ' ') {
            tableName.append(text.charAt(position));
            position++;
        }
        checkWhiteSpace(position, len, text);
        position++;
        if(text.charAt(position) != '(') throwError();
        position++;
        List<String> columns = new ArrayList<>();
        while(position != len && text.charAt(position) != ')') {
            StringBuilder columnName = new StringBuilder();
            while(Character.isAlphabetic(text.charAt(position))) {
                columnName.append(text.charAt(position));
                position++;
            }
            columns.add(columnName.toString());
            checkComma(text, position);
            System.out.println(position);
            position++;
        }
        System.out.println(tableName.toString());
        if(storage.hashMap.containsKey(tableName.toString())) {
            throwTableExistsError(table);
        }
        storage.hashMap.put(table, new Table(table, columns));
        succesfullyCreatedTable(tableName.toString(), columns.size());
    }
}
