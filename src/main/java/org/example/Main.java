package org.example;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.example.Parsing.Parser;

import static org.example.LoggerClass.logger;
import static org.example.Util.Utility.blankQuery;


public class Main {

    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String sqlQuery = sc.nextLine();
            if (blankQuery(sqlQuery)) continue;

            try {
                Parser parser = new Parser(sqlQuery);
                parser.parse();
            } catch (Exception e) {
                logger.error("error" + e.getMessage());
            }
        }
        sc.close();
    }
}
