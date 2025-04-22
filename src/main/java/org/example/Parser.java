package org.example;

import java.util.HashSet;

import static org.example.Utility.checkWhiteSpace;
import static org.example.Utility.throwError;

public class Parser {

    public int position;
    public String text;
    public char currentChar = '\0';
    int totalLength;
    public final HashSet<String> hset;
    public ParseRule parseRule;

    public Parser(String text) {
        this.text = text;
        position = 0;
        currentChar = text.charAt(position);
        totalLength = text.length();
        hset = new HashSet<>();
        parseRule = new ParseRule();
    }

    public void ruleSwitchCase(String rule, String subText) {
        switch (rule) {
            case "select" -> parseRule.parseSelect(subText);
            case "update" -> parseRule.parseUpdate(subText);
            case "insert" -> parseRule.parseInsert(subText);
            case "delete" -> parseRule.parseDelete(subText);
            default -> throwError();
        }
    }

    public void checkFirst(String first) {
        if(!hset.contains(first.toLowerCase())) {
           throwError();
        }
        position+=first.length();
        checkWhiteSpace(position, totalLength, text);
        position++;
        ruleSwitchCase(first.toLowerCase(), text.substring(position));
    }


    public void parse() {
      Utility.setHash(hset);
      if(position + 6 >=totalLength) throwError();
      checkFirst(text.substring(0, 6));
    }
}
