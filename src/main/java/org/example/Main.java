package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean flag = true;

        while(flag) {
            System.out.println("Please insert your query");
            String sqlQuery = sc.nextLine();
            if(Utility.blankQuery(sqlQuery)) continue;
            try {
                Parser parser = new Parser(sqlQuery);
                parser.parse();
            } catch (Exception e) {
                System.out.println("error " + e.getMessage());
            }
            System.out.println("Do you have more queries?");
            int answer;
            while(!sc.hasNextInt()) {
                System.out.println("Input is not a number");
                sc.next();
            }
            answer = sc.nextInt();
            if(answer != 1) flag = false;
        }
    }
}
