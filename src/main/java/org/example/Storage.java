package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Storage {

    public static HashMap<String, Table> hashMap;

    public Storage() {
        hashMap = new HashMap<>();
        List<String> columns = new ArrayList<>();
        columns.add("id");
        columns.add("name");
        columns.add("age");
        hashMap.put("EMPLOYEES", new Table("EMPLOYEES", columns));
    }
}

