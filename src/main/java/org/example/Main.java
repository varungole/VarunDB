package org.example;

public class Main {
    public static void main(String[] args) {
        String sqlQuery = "SELECT * from EMPLOYEES";
        Tokenizer tokenizer = new Tokenizer(sqlQuery);
        String tokenized = tokenizer.start(sqlQuery);
        System.out.println(tokenized);
        Parser parser = new Parser(sqlQuery);
        parser.parse();

    }
}
