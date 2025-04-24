package org.example.Parsing;

import static org.example.Util.Utility.checkComma;
import static org.example.Util.Utility.checkIfTableExists;
import static org.example.Util.Utility.checkWhiteSpace;
import static org.example.Util.Utility.throwError;

import java.util.List;

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
}