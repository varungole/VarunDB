package org.example;

public class Main {
    public static void main(String[] args) {
        String sqlQuery = "CREATE TABLE EMPLOYEES (EmployeeName,EmployeeAge)";
        Tokenizer tokenizer = new Tokenizer(sqlQuery);
        String tokenized = tokenizer.start(sqlQuery);
        Parser parser = new Parser(sqlQuery);
        parser.parse();
    }
}
