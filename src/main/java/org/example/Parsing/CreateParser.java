package org.example.Parsing;

import org.example.Storage.Table;
import org.example.Util.ColumnType;

import java.util.ArrayList;
import java.util.List;

import static org.example.Storage.Storage.*;
import static org.example.Util.Utility.*;

public class CreateParser {

    private final ParseContext ctx;
    private final ParseUtil parseUtil;

    public CreateParser(ParseContext ctx, ParseUtil parseUtil) {
        this.ctx = ctx;
        this.parseUtil = parseUtil;
    }


    public void parseCreateDatabase() {
        parseUtil.verifyAndAdvance(ctx,8, "database");
        String databaseName = parseUtil.readWord(ctx);
        if(databases.containsKey(databaseName)) {
            System.out.println("Database already exists");
            return;
        }
        databases.put(databaseName, new ArrayList<>());
    }

    public void parseCreateTable() {
        if(currentDatabase.isEmpty()) databaseNotDefined();
        parseUtil.verifyAndAdvance(ctx,5, "table");
        String tableName = parseUtil.readWord(ctx);
        parseUtil.advanceAndCheckWhitespace(ctx,0);
        parseUtil.checkOpeningBracket(ctx);
        List<String> columns = new ArrayList<>();
        List<ColumnType> columnType = new ArrayList<>();
        parseUtil.extractDataAndDataTypes(ctx,columns,columnType);
        if(columns.isEmpty()) throwError();
        if(tables.containsKey(tableName)) throwTableExistsError(tableName);
        Table newTable = new Table(tableName, columns,columnType,new ArrayList<>());
        tables.put(tableName, newTable);
        List<Table> tables = databases.get(currentDatabase);
        tables.add(newTable);
        databases.put(currentDatabase, tables);
        succesfullyCreatedTable(tableName, columns.size());
    }
}
