package org.example.Parsing;

import org.example.Storage.Storage;
import org.example.Storage.Table;

import static org.example.LoggerClass.logger;
import static org.example.Util.Utility.throwError;

public class DeleteParser {

    private ParseContext ctx;
    private ParseUtil parseUtil;

    public DeleteParser(ParseContext ctx, ParseUtil parseUtil) {
        this.ctx = ctx;
        this.parseUtil = parseUtil;
    }

    public void deleteFromTable() {
        parseUtil.verifyAndAdvance(ctx,4, "from");
        String tableName = parseUtil.checkTable(ctx);
        ctx.position++;
        parseUtil.verifyAndAdvance(ctx,5, "where");
        String subText = ctx.text.substring(ctx.position);
        String[] queryMain = new String[2];
        if(subText.contains("=")) {
            queryMain = ctx.text.substring(ctx.position).split("=");
        } else logger.error("Invalid SQL Syntax");
        String mainKey = queryMain[0];
        String mainValue = queryMain[1];
        Table table = Storage.getCurrentTables().get(tableName);
        int index = table.columns.indexOf(mainKey);
        table.rows.removeIf(row -> row.get(index).equals(mainValue));
        System.out.println("Removed the row sucessfully!");
    }

    public void parseDelete() {
        deleteFromTable();
    }
}
