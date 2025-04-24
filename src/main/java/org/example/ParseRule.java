package org.example;

import static org.example.Utility.*;

import java.util.ArrayList;
import java.util.List;

public class ParseRule {
    public int position;

    public ParseRule() {
        position = 0;
    }

    public void parseSelect(String text) {
        int len = text.length();
        if(text.charAt(position) != '*') throwError();
        advance(1, len, text);
        String from = text.substring(position, position+4);
        if(!from.equalsIgnoreCase("from")) throwError();
        advance(4, len, text);
        String tableName = text.substring(position);
        checkIfTableExists(tableName);
        Table table = Storage.hashMap.get(tableName);
        for(List<String> row : table.rows) {
            System.out.println(row);
        }
    }

    public void parseUpdate(String text) {
    }

    public void parseInsert(String text) {
        int len = text.length();
        verifyCorrectness(text, 4, "into", len);
        advance(4, len, text);
        String tableName = readWord(text);
        checkIfTableExists(tableName);
        advance(0, len, text);
        verifyCorrectness(text, 6, "values", len);
        advance(6, len, text);
        checkOpeningBracket(text, len);
        List<String> data = new ArrayList<>();
        extractDataInsideBrackets(len, text, data);
        if(!verifyIfDataInsertedIsCorrect(data, Storage.hashMap.get(tableName).columns.size())) throwError();
        Storage.hashMap.get(tableName).rows.add(data);

    }
    //create table employee (id,age)
    //insert into employee values (1,12)

    public void parseDelete(String text) {

    }

    public void parseCreate(String text) {
        int len = text.length();
        verifyCorrectness(text, 5, "table", len);
        advance(5, len, text);
        String tableName = readWord(text);
        advance(0, len, text);
        checkOpeningBracket(text,len);
        List<String> columns = new ArrayList<>();
        extractDataInsideBrackets(len, text, columns);
        if(Storage.hashMap.containsKey(tableName)) {
            throwTableExistsError(tableName);
        }
        Storage.hashMap.put(tableName, new Table(tableName, columns, new ArrayList<>()));
        succesfullyCreatedTable(tableName, columns.size());
    }

    private String readWord(String text) {
        StringBuilder sb = new StringBuilder();
        while(position < text.length() && Character.isLetterOrDigit(text.charAt(position))) {
            sb.append(text.charAt(position));
            position++;
        }
        return sb.toString();
    }

    private void advance(int spaces, int len, String text) {
        position +=spaces;
        checkWhiteSpace(position, len, text);
        position++;
    }

    private void verifyCorrectness(String text, int spaces, String compareWith, int len) {
        if(position + spaces > len || !text.substring(position, position+spaces).equalsIgnoreCase(compareWith)) throwError();
    }

    private void checkOpeningBracket(String text, int len) {
        if(position >= len || text.charAt(position) != '(') throwError();
        position++;
    }

    private void extractDataInsideBrackets(int len, String text, List<String> list) {
        while(position != len && text.charAt(position) != ')') {
            String dataField = readWord(text);
            list.add(dataField);
            if(!checkComma(text.charAt(position))) throwError();
            position++;
        }
    }

}
