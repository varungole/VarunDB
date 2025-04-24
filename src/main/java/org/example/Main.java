package org.example;

import java.util.Scanner;

import org.example.Parsing.Parser;
import org.example.Util.Utility;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("Please insert your query");
            if (!sc.hasNextLine()) break;
            String sqlQuery = sc.nextLine();
            if(Utility.blankQuery(sqlQuery)) continue;
            try {
                Parser parser = new Parser(sqlQuery);
                parser.parse();
            } catch (Exception e) {
                System.out.println("error " + e.getMessage());
            }
        }
        sc.close();
    }
}
