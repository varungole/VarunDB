package org.example.Parsing;

import org.example.Storage.Storage;
import org.example.Storage.Table;
import org.example.Util.ColumnType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.LoggerClass.logger;
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
        String dbName = parseUtil.readWord(ctx);
        if (Storage.databases.containsKey(dbName)) {
            logger.error("Database already exists.s");
        } else {
            Storage.databases.put(dbName, new HashMap<>());
            createDB(dbName);
            logger.info("Database " + dbName + " created.");
        }
    }

    public void parseCreateTable() {
        if (currentDatabase.isEmpty()) databaseNotDefined();

        parseUtil.verifyAndAdvance(ctx, 5, "table");
        String tableName = parseUtil.readWord(ctx);
        parseUtil.advanceAndCheckWhitespace(ctx, 0);
        parseUtil.checkOpeningBracket(ctx);

        List<String> columns = new ArrayList<>();
        List<ColumnType> columnTypes = new ArrayList<>();
        parseUtil.extractDataAndDataTypes(ctx, columns, columnTypes);

        if (columns.isEmpty()) {
            logger.error("Columns are empty");
        }

        // Get or create the current database table map
        Map<String, Table> currentTables = databases.get(currentDatabase);
        if (currentTables.containsKey(tableName)) throwTableExistsError(tableName);

        Table newTable = new Table(tableName, columns, columnTypes, new ArrayList<>());
        currentTables.put(tableName, newTable);
        System.out.println("Table " + tableName + " created with " + columns.size() + " columns.");
        String dbPath = "data/" + currentDatabase + "/";
        String metaPath = dbPath + tableName + ".meta";
        writeMeta(metaPath,columns,columnTypes);
    }
}
