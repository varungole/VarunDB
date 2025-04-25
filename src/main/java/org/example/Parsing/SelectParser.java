package org.example.Parsing;

import static org.example.Util.Utility.throwError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.example.Storage.Storage;
import org.example.Storage.Table;

public class SelectParser {

    private final ParseUtil parseUtil;
    private final ParseContext ctx;

    public SelectParser(ParseContext ctx, ParseUtil parseUtil) {
        this.ctx = ctx;
        this.parseUtil = parseUtil;
    }

    public void parseSelect() {
        if(ctx.text.charAt(ctx.position) == '*') parseSelectAll();
        else parseSelectFields();
    }

    private String checkForOrderBy() {
        ctx.position++;
        parseUtil.verifyAndAdvance(ctx,5, "order");
        parseUtil.verifyAndAdvance(ctx,2, "by");
        String key = parseUtil.readWord(ctx);
        return key;
    }

    public void parseSelectAll() {
        parseUtil.advanceAndCheckWhitespace(ctx,1);
        parseUtil.verifyAndAdvance(ctx,4, "from");
        String tableName = parseUtil.checkTable(ctx);
        Table table = Storage.hashMap.get(tableName);
        String key = "";
        if(ctx.position < ctx.len) {
           key = checkForOrderBy();
        }
        print(table, tableName, new int[0], "all", key);
    }

    public void parseSelectFields() {
        List<String> fields = new ArrayList<>();
        int blankPos = ctx.text.indexOf(' ');
        while(ctx.position < blankPos) {
            String word = parseUtil.readWord(ctx);
            if(word.isEmpty()) throwError();
            fields.add(word);
            ctx.position++;
        }
        parseUtil.advanceAndCheckWhitespace(ctx,-1);
        parseUtil.verifyAndAdvance(ctx,4, "from");
        String tableName = parseUtil.checkTable(ctx);
        Table table = Storage.hashMap.get(tableName);
        int[] indexes = new int[fields.size()];
        String key = "";
        if(ctx.position < ctx.len) {
            key = checkForOrderBy();
        }
        for (int i = 0; i < fields.size(); i++) {
            int index = table.columns.indexOf(fields.get(i));
            if (index == -1) throwError();
            indexes[i] = index;
        }
        print(table, tableName, indexes, "specific", key);
    }

    private void print(Table table, String tableName, int[] indexes, String keyword, String key) {
        if(checkRowsEmpty(table, tableName)) return;
        switch (keyword) {
            case "all" -> printAll(table, key);
            case "specific" -> printSpecific(table, indexes, key);
            default -> throwError();
        }
    }

    private void printSpecific(Table table, int[] indexes, String key) {
        if(key.isEmpty()) {
            for (List<String> row : table.rows) {
                List<String> answer = new ArrayList<>();
                for (int i : indexes) {
                    answer.add(row.get(i));
                }
            }
        } else {
            int index = table.columns.indexOf(key);
            if (index == -1) throwError();
            List<List<String>> sortedRows = new ArrayList<>(table.rows);
            sortedRows.sort(Comparator.comparing(row -> row.get(index)));
            for (List<String> row : table.rows) {
                List<String> answer = new ArrayList<>();
                for (int i : indexes) {
                    answer.add(row.get(i));
                }
                System.out.println(answer);
            }
        }
    }

    private void printAll(Table table, String key) {
        if (key.isEmpty()) {
            for (List<String> row : table.rows) {
                System.out.println(row);
            }
        } else {
            int index = table.columns.indexOf(key);
            if (index == -1) throwError();
            System.out.println(index);
            List<List<String>> sortedRows = new ArrayList<>(table.rows);
            sortedRows.sort(Comparator.comparing(row -> row.get(index)));
            for (List<String> row : sortedRows) {
                System.out.println(row);
            }
        }
    }


    private boolean checkRowsEmpty(Table table, String tableName) {
        if(table.rows.isEmpty()) {
            System.out.println("Table " + tableName + " is empty");
            return true;
        }
        return false;
    }


}