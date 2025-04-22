package org.example;

import static org.example.Utility.*;

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

}
