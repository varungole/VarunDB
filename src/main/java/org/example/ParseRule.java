package org.example;

import static org.example.Utility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParseRule {
    public int position;

    public ParseRule() {
        position = 0;
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

        //expect the word table
        if(position + 5 > len || !text.substring(position, position+5).equalsIgnoreCase("table")) throwError();

        position+=5;
        checkWhiteSpace(position, len, text);
        position++;

        //fetch table name
        String tableName = readWord(text);
        checkWhiteSpace(position, len, text);
        position++;

        if(position >= len || text.charAt(position) != '(') throwError();
        position++;

        //start extracting columns
        List<String> columns = new ArrayList<>();
        while(position != len && text.charAt(position) != ')') {
            String columnName = readWord(text);
            columns.add(columnName.toString());
            if(!checkComma(text.charAt(position))) throwError();
            position++;
        }

        //store it in database
        if(Storage.hashMap.containsKey(tableName)) {
            throwTableExistsError(tableName);
        }
        Storage.hashMap.put(tableName, new Table(tableName, columns));
        succesfullyCreatedTable(tableName, columns.size());
        for(Map.Entry<String, Table> front : Storage.hashMap.entrySet()) {
            System.out.println(front.getKey() + " " + front.getValue());
        }
    }


    private String readWord(String text) {
        StringBuilder sb = new StringBuilder();
        while(position < text.length() && Character.isAlphabetic(text.charAt(position))) {
            sb.append(text.charAt(position));
            position++;
        }
        return sb.toString();
    }
}
