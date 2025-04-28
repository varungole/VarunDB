package org.example.Parsing;

import static org.example.Util.Utility.checkWhiteSpace;
import static org.example.Util.Utility.throwError;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.example.Storage.Storage;
import org.example.Storage.Table;
import org.example.Util.Utility;

public class SelectParser {

    private final ParseUtil parseUtil;
    private final ParseContext ctx;

    private static final Pattern WHERE_MATCHER =
            Pattern.compile("^(>=|<=|>|<|=)\\s*(\\d+(?:\\.\\d+)?)$");

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
        return parseUtil.readWord(ctx);
    }

    private Pair checkWhereClause() {
        ctx.position++;
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
        //consume *
        parseUtil.advanceAndCheckWhitespace(ctx,1);
        parseUtil.verifyAndAdvance(ctx,4, "from");

        //table lookup
        String tableName = parseUtil.checkTable(ctx);
        Table table = Storage.hashMap.get(tableName);

        //no ORDER, no WHERE
        String orderByKey = "";
        Pair whereClause = Pair.empty();
        if(ctx.position < ctx.len) {
            whereClause = checkWhereClause();
        }
        if(ctx.position < ctx.len) {
           orderByKey = checkForOrderBy();
        }

        //unified print
        Printer.print(table, new int[0], orderByKey, whereClause);
    }

    public void parseSelectFields() {
        //collect field names until first space
        List<String> fields = new ArrayList<>();
        int blankPos = ctx.text.indexOf(' ');
        while(ctx.position < blankPos) {
            String word = parseUtil.readWord(ctx);
            if(word.isEmpty()) throwError();
            fields.add(word);
            ctx.position++;
        }

        //consume from
        parseUtil.advanceAndCheckWhitespace(ctx,-1);
        parseUtil.verifyAndAdvance(ctx,4, "from");

        //table lookup
        String tableName = parseUtil.checkTable(ctx);
        Table table = Storage.hashMap.get(tableName);


        //map field names --> indexes
        int[] indexes = new int[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            int index = table.columns.indexOf(fields.get(i));
            if (index == -1) throwError();
            indexes[i] = index;
        }

        String orderByKey = "";
        Pair whereClause = Pair.empty();
        if(ctx.position < ctx.len) {
            orderByKey = checkForOrderBy();
        }
        //seelect those fields
        Printer.print(table, indexes, orderByKey, whereClause);
    }
}