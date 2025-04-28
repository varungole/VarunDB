package org.example.Parsing;

import org.example.Storage.Table;
import org.example.Util.ColumnType;

import java.util.List;

import static org.example.Util.Utility.*;

public class ParseUtil {
 
    public String readWord(ParseContext ctx) {
        StringBuilder sb = new StringBuilder();
        while(ctx.position < ctx.text.length() && Character.isLetterOrDigit(ctx.text.charAt(ctx.position))) {
            sb.append(ctx.text.charAt(ctx.position));
            ctx.position++;
        }
        return sb.toString();
    }

    public void advanceAndCheckWhitespace(ParseContext ctx, int spaces) {
        ctx.position +=spaces;
        checkWhiteSpace(ctx.position, ctx.len, ctx.text);
        ctx.position++;
    }

    public void checkOpeningBracket(ParseContext ctx) {
        if(ctx.position >= ctx.len || ctx.text.charAt(ctx.position) != '(') throwError();
        ctx.position++;
    }

    public void extractDataInsideBrackets(ParseContext ctx, List<String> list) {
        while(ctx.position < ctx.len && ctx.text.charAt(ctx.position) != ')') {
            String dataField = readWord(ctx);
            if(dataField.isEmpty()) throwError();
            list.add(dataField);
            if(!checkComma(ctx.text.charAt(ctx.position))) throwError();
            ctx.position++;
        }
    }

    public void extractDataAndDataTypes(ParseContext ctx, List<String> list, List<ColumnType> columnType) {
        while(ctx.position < ctx.len && ctx.text.charAt(ctx.position) != ')') {
            String dataField = readWord(ctx);
            if(dataField.isEmpty()) throwError();
            list.add(dataField);
            checkWhiteSpace(ctx.position, ctx.len, ctx.text);
            ctx.position++;
            String dataType = readWord(ctx);
            ColumnType currColType = ColumnType.fromString(dataType);
            columnType.add(currColType);
            if(!checkComma(ctx.text.charAt(ctx.position))) throwError();
            ctx.position++;
        }
    }

    public void verifyAndAdvance(ParseContext ctx, int spaces, String compareWith) {
        if(ctx.position + spaces > ctx.len || !ctx.text.substring(ctx.position, ctx.position+spaces).equalsIgnoreCase(compareWith)) throwError();
        ctx.position +=spaces;
        checkWhiteSpace(ctx.position, ctx.len, ctx.text);
        ctx.position++;
    }

    public String checkTable(ParseContext ctx) {
        String tableName = readWord(ctx);
        checkIfTableExists(tableName);
        return tableName;
    }

    public void extractUpdatePairs(ParseContext ctx, List<Pair> columnName) {
        while(ctx.position < ctx.len) {
            String key = readWord(ctx);
            if (key.isEmpty()) throwError();
            if(ctx.position >= ctx.len || ctx.text.charAt(ctx.position) != '=') throwError();
            ctx.position++;
            String value = readWord(ctx);
            if(value.isEmpty()) throwError();
            columnName.add(new Pair(key, value,0));
            if(ctx.text.charAt(ctx.position) == ' ') break;
            if(ctx.text.charAt(ctx.position) != ',') throwError();
            ctx.position++;
        }
    }

    public void iterate(Table table, int index, String mainValue, List<Pair> columnName) {
        for(List<String> row : table.rows) {
            if(row.get(index).equals(mainValue)) {
                for(Pair p : columnName) {
                    int setIndex = table.columns.indexOf(p.first);
                    row.set(setIndex, p.second);
                }
            }
        }
    }

    public void addExtraColumns(ParseContext ctx, ParseUtil parseUtil, List<String> extraColumns) {
        while(ctx.position < ctx.len) {
            String word = parseUtil.readWord(ctx);
            extraColumns.add(word);
            if (ctx.position < ctx.len) {
                if (checkComma(ctx.text.charAt(ctx.position))) ctx.position++;
                else throwError();
            }
        }
    }
}