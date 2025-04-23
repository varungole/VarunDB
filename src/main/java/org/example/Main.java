package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean flag = true;

        while(flag) {
            String sqlQuery = "CREATE TABLE EMPLOYEES (EmployeeName,EmployeeAge)";
            Tokenizer tokenizer = new Tokenizer(sqlQuery);
            String tokenized = tokenizer.start(sqlQuery);
            Parser parser = new Parser(sqlQuery);
            parser.parse();
            System.out.println("DO you have more queries?");
            int answer = sc.nextInt();
            if(answer != 1) flag = false;
        }
    }
}
