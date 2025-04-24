package org.example;

import static org.example.Utility.*;

import java.util.ArrayList;
import java.util.List;

public class ParseRule {
    public int position;
    private String text;
    private int len;

    public ParseRule() {
        position = 0;
    }

    public void setText(String subText) {
        this.text = subText;
        this.len = subText.length();
    }

    public void parseSelect() {
        if(text.charAt(position) != '*') throwError();
        advanceAndCheckWhitespace(1, len, text);
        verifyAndAdvance(4, len, text, "from");
        String tableName = checkTable();

        Table table = Storage.hashMap.get(tableName);
        for(List<String> row : table.rows) {
            System.out.println(row);
        }
    }

    public void parseUpdate() {
    }

    public void parseInsert() {

        verifyAndAdvance(4, len, text, "into");

        String tableName = checkTable();

        advanceAndCheckWhitespace(0, len, text);

        verifyAndAdvance(6, len, text, "values");

        checkOpeningBracket(text, len);
        List<String> data = new ArrayList<>();
        extractDataInsideBrackets(len, text, data);
        if(!verifyIfDataInsertedIsCorrect(data, Storage.hashMap.get(tableName).columns.size())) throwError();
        Storage.hashMap.get(tableName).rows.add(data);

    }
    //create table employee (id,age)
    //insert into employee values (1,12)

    public void parseDelete() {

    }

    public void parseCreate() {
        verifyAndAdvance(5, len, text, "table");
        String tableName = readWord();
        advanceAndCheckWhitespace(0, len, text);
        checkOpeningBracket(text,len);
        List<String> columns = new ArrayList<>();
        extractDataInsideBrackets(len, text, columns);
        if(Storage.hashMap.containsKey(tableName)) {
            throwTableExistsError(tableName);
        }
        Storage.hashMap.put(tableName, new Table(tableName, columns, new ArrayList<>()));
        succesfullyCreatedTable(tableName, columns.size());
    }

    private String readWord() {
        StringBuilder sb = new StringBuilder();
        while(position < text.length() && Character.isLetterOrDigit(text.charAt(position))) {
            sb.append(text.charAt(position));
            position++;
        }
        return sb.toString();
    }

    private void advanceAndCheckWhitespace(int spaces, int len, String text) {
        position +=spaces;
        checkWhiteSpace(position, len, text);
        position++;
    }

    private void checkOpeningBracket(String text, int len) {
        if(position >= len || text.charAt(position) != '(') throwError();
        position++;
    }

    private void extractDataInsideBrackets(int len, String text, List<String> list) {
        while(position < len && text.charAt(position) != ')') {
            String dataField = readWord();
            if(dataField.isEmpty()) throwError();
            list.add(dataField);
            if(!checkComma(text.charAt(position))) throwError();
            position++;
        }
    }

    private void verifyAndAdvance(int spaces, int len, String text, String compareWith) {
        if(position + spaces > len || !text.substring(position, position+spaces).equalsIgnoreCase(compareWith)) throwError();
        position +=spaces;
        checkWhiteSpace(position, len, text);
        position++;
    }

    private String checkTable() {
        String tableName = readWord();
        checkIfTableExists(tableName);
        return tableName;
    }

}
