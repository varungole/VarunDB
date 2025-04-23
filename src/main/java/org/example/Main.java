package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean flag = true;

        while(flag) {
            System.out.println("Please insert your query");
            String sqlQuery = sc.nextLine();
            if(sqlQuery.isBlank()) {
                System.out.println("Empty query, please try again.");
                continue;
            }
            try {
                Tokenizer tokenizer = new Tokenizer(sqlQuery);
                String tokenized = tokenizer.start(sqlQuery);
                Parser parser = new Parser(sqlQuery);
                parser.parse();
            } catch (Exception e) {
                System.out.println("error " + e.getMessage());
            }
            System.out.println("Do you have more queries?");
            int answer = sc.nextInt();
            sc.nextLine();
            if(answer != 1) flag = false;
        }
    }
}
