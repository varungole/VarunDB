package org.example.Parsing;

import static org.example.Util.Utility.*;

public class Parser {

    private final String text;
    private final ParseRule parseRule;

    public Parser(String text) {
        this.text = text;
        parseRule = new ParseRule();
    }

    public void ruleSwitchCase(String rule, String subText) {
        System.out.println(rule + " " + subText);
        parseRule.setText(subText);
        switch (rule) {
            case "select"   ->   parseRule.parseSelect();
            case "update"   ->   parseRule.parseUpdate();
            case "insert"   ->   parseRule.parseInsert();
            case "delete"   ->   parseRule.parseDelete();
            case "create"   ->   parseRule.parseCreate();
            case "alter"    ->   parseRule.parseAlter();
            case "drop"     ->   parseRule.parseDrop();
            case "truncate" ->   parseRule.parseTruncate();
            case "describe" ->   parseRule.parseDescribe();
            case "use" -> parseRule.parseUse();
            default -> throwError("Invalid SQL Syntax");
        }
    }

    //actual parse function
    public void parse() {
        String[] parts = text.split("\\s+",2);
        try {
            Command cmd = Command.valueOf(parts[0].toLowerCase());
            ruleSwitchCase(cmd.toString(), parts[1]);
        } catch (IllegalArgumentException e) {
            throwError("Invalid command " + parts[0]);
        }
    }
}
