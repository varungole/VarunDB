package org.example.Storage;

import java.util.HashMap;
import java.util.List;

public class Storage {

    public static HashMap<String, Table> tables = new HashMap<>();
    public static HashMap<String, List<Table>> databases = new HashMap<>();
    public static String currentDatabase = "";
    private Storage() {}
}

/*
 Storage.hashMap.put(tableName, new Table(tableName, columns, rows));
 (id,age)
 [1(id), 12(age)]
[2(id), 12(age)]
 */
