package org.example;

import java.util.HashSet;

import static org.example.Utility.checkWhiteSpace;
import static org.example.Utility.throwError;

public class Parser {

    private int position;
    private final String text;
    private final int totalLength;
    private final HashSet<String> hset;
    private final ParseRule parseRule;

    public Parser(String text) {
        this.text = text;
        position = 0;
        text.charAt(position);
        totalLength = text.length();
        hset = new HashSet<>();
        parseRule = new ParseRule();
    }

    public void ruleSwitchCase(String rule, String subText) {
        parseRule.setText(subText);
        switch (rule) {
            case "select" -> parseRule.parseSelect();
            case "update" -> parseRule.parseUpdate();
            case "insert" -> parseRule.parseInsert();
            case "delete" -> parseRule.parseDelete();
            case "create" -> parseRule.parseCreate();
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
      int spaceIndex = text.indexOf(' ');
      if(spaceIndex == -1) throwError();
      String command = text.substring(0, spaceIndex);
      checkFirst(command);
    }
}
