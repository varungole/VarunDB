package org.example;

import static org.example.Utility.*;

import java.util.ArrayList;
import java.util.List;

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
        SelectParser selectParser = new SelectParser(ctx);
        selectParser.parseSelect();
    }

    public void parseUpdate() {
    }

    public void parseInsert() {
        parseUtil.verifyAndAdvance(ctx,4, "into");
        String tableName = parseUtil.checkTable(ctx);
        parseUtil.advanceAndCheckWhitespace(ctx,0);
        parseUtil.verifyAndAdvance(ctx,6, "values");
        parseUtil.checkOpeningBracket(ctx);
        List<String> data = new ArrayList<>();
        parseUtil.extractDataInsideBrackets(ctx,data);
        if(!verifyIfDataInsertedIsCorrect(data, Storage.hashMap.get(tableName).columns.size())) throwError();
        Storage.hashMap.get(tableName).rows.add(data);
        System.out.println("Inserted succesfully!");
    }

    public void parseDelete() {
        String tableName = parseUtil.checkTable(ctx);
        Storage.hashMap.remove(tableName);
        System.out.println("Table successfully deleted!");
    }


    public void parseCreate() {
        parseUtil.verifyAndAdvance(ctx,5, "table");
        String tableName = parseUtil.readWord(ctx);
        parseUtil.advanceAndCheckWhitespace(ctx,0);
        parseUtil.checkOpeningBracket(ctx);
        List<String> columns = new ArrayList<>();
        parseUtil.extractDataInsideBrackets(ctx,columns);
        if(Storage.hashMap.containsKey(tableName)) throwTableExistsError(tableName);
        Storage.hashMap.put(tableName, new Table(tableName, columns, new ArrayList<>()));
        succesfullyCreatedTable(tableName, columns.size());
    }
}
