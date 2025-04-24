package org.example.Parsing;

import static org.example.Util.Utility.throwError;

import java.util.ArrayList;
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

    public void parseSelectAll() {
        parseUtil.advanceAndCheckWhitespace(ctx,1);
        parseUtil.verifyAndAdvance(ctx,4, "from");
        String tableName = parseUtil.checkTable(ctx);

        Table table = Storage.hashMap.get(tableName);
        for(List<String> row : table.rows) {
            System.out.println(row);
        }
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
}