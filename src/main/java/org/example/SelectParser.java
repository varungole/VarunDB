package org.example;

import static org.example.Utility.checkComma;
import static org.example.Utility.checkIfTableExists;
import static org.example.Utility.checkWhiteSpace;
import static org.example.Utility.throwError;

import java.util.ArrayList;
import java.util.List;

public class SelectParser {
    
    private int position;
    public final String text;
    private final int len;

    public SelectParser(ParseContext ctx) {
        this.position = ctx.position;
        this.text = ctx.text;
        this.len = ctx.len;
    }

    public void parseSelectAll() {
        advanceAndCheckWhitespace(1);
        verifyAndAdvance(4, "from");
        String tableName = checkTable();

        Table table = Storage.hashMap.get(tableName);
        for(List<String> row : table.rows) {
            System.out.println(row);
        }
    }

    public void parseSelectFields() {
        List<String> fields = new ArrayList<>();
        int blankPos = text.indexOf(' ');
        while(position < blankPos) {
            String word = readWord();
            fields.add(word);
            position++;
        }
        advanceAndCheckWhitespace(-1);
        verifyAndAdvance(4, "from");
        String tableName = checkTable();
        Table table = Storage.hashMap.get(tableName);
        //fields
        int[] indexes = fields.stream()
                        .mapToInt(field -> {
                            int index = table.columns.indexOf(field);
                            if(index == -1) {
                                throwError();
                            }
                            return index;
                        }).toArray();
        
        for(List<String> row : table.rows) {
            List<String> answer = new ArrayList<>();
            for(int i : indexes) {
                answer.add(row.get(i));
            }
            System.out.println(answer);
        }
    }

    public void parseSelect() {
        if(text.charAt(position) == '*') parseSelectAll();
        else parseSelectFields();
    }


    private String readWord() {
        StringBuilder sb = new StringBuilder();
        while(position < text.length() && Character.isLetterOrDigit(text.charAt(position))) {
            sb.append(text.charAt(position));
            position++;
        }
        return sb.toString();
    }

    private void advanceAndCheckWhitespace(int spaces) {
        position +=spaces;
        checkWhiteSpace(position, len, text);
        position++;
    }

    private void extractDataInsideBrackets(List<String> list) {
        while(position < len && text.charAt(position) != ')') {
            String dataField = readWord();
            if(dataField.isEmpty()) throwError();
            list.add(dataField);
            if(!checkComma(text.charAt(position))) throwError();
            position++;
        }
    }

    private void verifyAndAdvance(int spaces, String compareWith) {
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
