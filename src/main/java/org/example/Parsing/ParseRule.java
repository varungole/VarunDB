package org.example.Parsing;

import static org.example.Util.Utility.*;

import java.util.ArrayList;
import java.util.List;

import org.example.Storage.Storage;
import org.example.Storage.Table;
import org.example.Util.ColumnType;

public class ParseRule {
    private final ParseUtil parseUtil;
    private ParseContext ctx;

    public ParseRule() {
        this.parseUtil = new ParseUtil();
    }

    public void setText(String subText) {
        this.ctx = new ParseContext(subText);
    }    

    public void parseSelect() {
        SelectParser selectParser = new SelectParser(ctx, parseUtil);
        selectParser.parseSelect();
    }

    public void parseUpdate() {
        String tableName = parseUtil.checkTable(ctx);
        parseUtil.advanceAndCheckWhitespace(ctx,0);
        parseUtil.verifyAndAdvance(ctx, 3, "set");
        List<Pair> columnName = new ArrayList<>();
        parseUtil.extractUpdatePairs(ctx, columnName);
        ctx.position++;
        parseUtil.verifyAndAdvance(ctx, 5, "where");
        String subText = ctx.text.substring(ctx.position);
        String[] queryMain = new String[2];
        if(subText.contains("=")) {
            queryMain = ctx.text.substring(ctx.position).split("=");
        } else throwError();
        String mainKey = queryMain[0];
        String mainValue = queryMain[1];
        Table table = Storage.hashMap.get(tableName);
        int index = table.columns.indexOf(mainKey);
        parseUtil.iterate(table,index,mainValue,columnName);
        System.out.println("Updated successfully!");
    }

    public void parseInsert() {
        parseUtil.verifyAndAdvance(ctx,4, "into");
        String tableName = parseUtil.checkTable(ctx);
        parseUtil.advanceAndCheckWhitespace(ctx,0);
        parseUtil.verifyAndAdvance(ctx,6, "values");
        parseUtil.checkOpeningBracket(ctx);
        List<String> data = new ArrayList<>();
        parseUtil.extractDataInsideBrackets(ctx,data);
        int columnSize = Storage.hashMap.get(tableName).columns.size();
        List<ColumnType> columnTypes = Storage.hashMap.get(tableName).columnType;
        if(!verifyIfDataInsertedIsCorrect(data,columnSize,columnTypes)) throwError();
        Storage.hashMap.get(tableName).rows.add(data);
        System.out.println("Inserted succesfully!");
    }

    public void parseDelete() {
        DeleteParser deleteParser = new DeleteParser(ctx, parseUtil);
        deleteParser.parseDelete();
    }

    public void parseCreate() {
        parseUtil.verifyAndAdvance(ctx,5, "table");
        String tableName = parseUtil.readWord(ctx);
        parseUtil.advanceAndCheckWhitespace(ctx,0);
        parseUtil.checkOpeningBracket(ctx);
        List<String> columns = new ArrayList<>();
        List<ColumnType> columnType = new ArrayList<>();
        parseUtil.extractDataAndDataTypes(ctx,columns,columnType);
        if(columns.isEmpty()) throwError();
        if(Storage.hashMap.containsKey(tableName)) throwTableExistsError(tableName);
        Storage.hashMap.put(tableName, new Table(tableName, columns,columnType,new ArrayList<>()));
        succesfullyCreatedTable(tableName, columns.size());
    }

    public void parseAlter() {
        parseUtil.verifyAndAdvance(ctx,5, "table");
        String tableName = parseUtil.readWord(ctx);
        ctx.position++;
        parseUtil.verifyAndAdvance(ctx,3, "add");
        parseUtil.verifyAndAdvance(ctx,6, "column");
        List<String> extraColumns = new ArrayList<>();
        parseUtil.addExtraColumns(ctx,parseUtil,extraColumns);
        Table table = Storage.hashMap.get(tableName);
        table.columns.addAll(extraColumns);
        System.out.println("Altered table!");
    }

    public void parseDrop() {
        String tableName = parseUtil.readWord(ctx);
        Storage.hashMap.remove(tableName);
        System.out.println("Dropped the table");
    }

    public void parseTruncate() {
        String tableName = parseUtil.readWord(ctx);
        Storage.hashMap.get(tableName).rows = new ArrayList<>();
        System.out.println("Truncate the table");
    }

    public void parseDescribe() {
        System.out.println("here");
        String tableName = parseUtil.readWord(ctx);
        System.out.println(Storage.hashMap.get(tableName).columns);
        System.out.println("Describe the table!");
    }
}
