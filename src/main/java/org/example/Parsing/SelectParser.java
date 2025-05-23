package org.example.Parsing;

import static org.example.Util.Utility.checkWhiteSpace;
import static org.example.Util.Utility.throwError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private String checkForOrderBy(Pair whereClause) {
        if(!whereClause.isEmpty()) ctx.position++;
        parseUtil.verifyAndAdvance(ctx,5, "order");
        parseUtil.verifyAndAdvance(ctx,2, "by");
        return parseUtil.readWord(ctx);
    }

    private Pair checkWhereClause() {
        ctx.position++;
        if(ctx.text.charAt(ctx.position) != 'w') {
            return Pair.empty();
        }
        parseUtil.verifyAndAdvance(ctx, 5, "where");
        String columnName = parseUtil.readWord(ctx);
        checkWhiteSpace(ctx.position, ctx.len, ctx.text);
        ctx.position++;
        String operator = parseUtil.readOperator(ctx);
        checkWhiteSpace(ctx.position, ctx.len, ctx.text);
        ctx.position++;
        int value = Integer.parseInt(parseUtil.readWord(ctx));
        return new Pair(columnName, operator, value);
    }

    public void parseSelectAll() {
        parseUtil.advanceAndCheckWhitespace(ctx,1);
        parseUtil.verifyAndAdvance(ctx,4, "from");

        //table lookup
        String tableName = parseUtil.checkTable(ctx);
        Map<String, Table> currentTables = Storage.getCurrentTables();
        Table table = currentTables.get(tableName);

        //no ORDER, no WHERE
        String orderByKey = "";
        Pair whereClause = Pair.empty();
        if(ctx.position < ctx.len) {
            whereClause = checkWhereClause();
        }
        if(ctx.position < ctx.len) {
           orderByKey = checkForOrderBy(whereClause);
        }

        //unified print
        Printer.print(table, new int[0], orderByKey, whereClause);
    }

    public void parseSelectFields() {
        //collect field names until first space
        List<String> fields = new ArrayList<>();
        int blankPos = ctx.text.indexOf(' ');
        while(ctx.position < blankPos) {
            String column = parseUtil.readWord(ctx);
            if(column.isEmpty()) throwError("Column does not exists");
            fields.add(column);
            ctx.position++;
        }
        //consume from
        parseUtil.advanceAndCheckWhitespace(ctx,-1);
        parseUtil.verifyAndAdvance(ctx,4, "from");

        //table lookup
        String tableName = parseUtil.checkTable(ctx);
        Map<String, Table> currentTables = Storage.getCurrentTables();
        Table table = currentTables.get(tableName);
        //map field names --> indexes
        int[] indexes = new int[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            int index = table.columns.indexOf(fields.get(i));
            indexes[i] = index;
        }

        String orderByKey = "";
        Pair whereClause = Pair.empty();
        if(ctx.position < ctx.len) {
            whereClause = checkWhereClause();
        }
        if(ctx.position < ctx.len) {
            orderByKey = checkForOrderBy(whereClause);
        }
        Printer.print(table, indexes, orderByKey, whereClause);
    }
}