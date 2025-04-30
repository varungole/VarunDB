package org.example.Storage;

import java.util.HashMap;

public class Storage {
    public static HashMap<String, HashMap<String, Table>> databases = new HashMap<>();
    public static String currentDatabase = "";

    public static HashMap<String, Table> getCurrentTables() {
        return databases.get(currentDatabase);
    }

    private Storage() {}
}

/*
 Storage.hashMap.put(tableName, new Table(tableName, columns, rows));
 (id,age)
 [1(id), 12(age)]
[2(id), 12(age)]
 */
