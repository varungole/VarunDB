package org.example.Parsing;

import org.example.Storage.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.example.Storage.Storage.currentDatabase;
import static org.example.Util.Utility.throwError;

public class Printer {

    public static void print(Table table, int[] fieldIdxs, String orderByKey, Pair whereClause) {

        if (table.rows.isEmpty()) {
            System.out.println("Table " + table.tableName + " is empty");
            return;
        }
        String dbPath = "data/" + currentDatabase + "/";
        String tableName = table.tableName;
        String dataPath = dbPath + tableName + ".db";
        List<List<String>> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dataPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("[") && line.endsWith("]")) {
                    line = line.substring(1, line.length() - 1);
                }
                String[] values = line.split(",");
                List<String> row = new ArrayList<>();
                for (String value : values) {
                    row.add(value.trim());  // trim to remove spaces
                }
                rows.add(row);
            }
        } catch (IOException e) {
            System.out.println("File not found or error reading file");
            e.printStackTrace();
        }

        if (!whereClause.isEmpty()) {
            int colByIdx = table.columns.indexOf(whereClause.first);
            if (colByIdx == -1) throwError();

            int threshold = whereClause.third;
            String op = whereClause.second;
            System.out.println(op);
            rows = rows.stream().filter(row -> {
                double cell;
                try {
                    cell = Double.parseDouble(row.get(colByIdx));
                } catch (NumberFormatException e) {
                    return false;
                }
                return switch (op) {
                    case ">" -> cell > threshold;
                    case "<" -> cell < threshold;
                    case "=" -> cell == threshold;
                    case ">=" -> cell >= threshold;
                    case "<=" -> cell <= threshold;
                    default -> false;
                };
            }).toList();
        }

        if (!orderByKey.isEmpty()) {
            int index = table.columns.indexOf(orderByKey);
            if (index == -1) throwError();
            List<List<String>> sortedRows = new ArrayList<>(rows);
            sortedRows.sort(Comparator.comparing(row -> row.get(index)));
            rows = sortedRows;
        }

        for (List<String> row : rows) {
            if (fieldIdxs == null || fieldIdxs.length == 0) {
                System.out.println(String.join(" | ", row));
            } else {
                List<String> out = new ArrayList<>();
                for (int idx : fieldIdxs) {
                    out.add(row.get(idx));
                }
                System.out.println(String.join(" | ", out));
            }
        }
    }
}
