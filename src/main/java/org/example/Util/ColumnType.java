package org.example.Util;

public enum ColumnType {
    INTEGER("int"),
    STRING("string");

    private final String columnTypeName;

    ColumnType(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public static ColumnType fromString(String columnTypeName) {
        for(ColumnType columnType: ColumnType.values()) {
            if(columnType.columnTypeName.equalsIgnoreCase(columnTypeName)) {
                return columnType;
            }
        }
        throw new IllegalArgumentException("Data type not found!");
    }
}
