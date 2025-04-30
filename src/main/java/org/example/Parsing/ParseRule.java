package org.example.Parsing;

import static org.example.Storage.Storage.*;
import static org.example.Util.Utility.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        Table table = Storage.getCurrentTables().get(tableName);
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
        Table table = Storage.getCurrentTables().get(tableName);
        int columnSize = table.columns.size();
        List<ColumnType> columnTypes = table.columnType;
        if(!verifyIfDataInsertedIsCorrect(data,columnSize,columnTypes)) throwError();
        table.rows.add(data);
        System.out.println("Inserted successfully!");
    }

    public void parseDelete() {
        DeleteParser deleteParser = new DeleteParser(ctx, parseUtil);
        deleteParser.parseDelete();
    }

    public void parseCreate() {
        CreateParser createParser = new CreateParser(ctx, parseUtil);
        char checkD = Character.toLowerCase(ctx.text.charAt(ctx.position));
        if(checkD == 'd') createParser.parseCreateDatabase();
        else createParser.parseCreateTable();
    }

    public void parseAlter() {
        parseUtil.verifyAndAdvance(ctx,5, "table");
        String tableName = parseUtil.readWord(ctx);
        ctx.position++;
        parseUtil.verifyAndAdvance(ctx,3, "add");
        parseUtil.verifyAndAdvance(ctx,6, "column");
        List<String> extraColumns = new ArrayList<>();
        parseUtil.addExtraColumns(ctx,parseUtil,extraColumns);
        Table table = Storage.getCurrentTables().get(tableName);
        table.columns.addAll(extraColumns);
        System.out.println("Altered table!");
    }

    public void parseDrop() {
        String tableName = parseUtil.readWord(ctx);
        Storage.getCurrentTables().remove(tableName);
        System.out.println("Dropped the table");
    }

    public void parseTruncate() {
        String tableName = parseUtil.readWord(ctx);
        Storage.getCurrentTables().get(tableName).rows = new ArrayList<>();
        System.out.println("Truncated the table");
    }

    public void parseDescribe() {
        String tableName = parseUtil.readWord(ctx);
        System.out.println(Storage.getCurrentTables().get(tableName).columns);
    }

    public void parseUse() {
        String databaseName = parseUtil.readWord(ctx);
        if (!databases.containsKey(databaseName)) databaseDoesntExist();
        currentDatabase = databaseName;
        System.out.println("Using " + currentDatabase);
    }
}
