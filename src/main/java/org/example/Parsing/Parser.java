package org.example.Parsing;

import static org.example.Util.Utility.checkMissingClause;
import static org.example.Util.Utility.throwError;

public class Parser {
    private final String rawSql;
    private final ParseRule rule = new ParseRule();

    public Parser(String sql) {
        this.rawSql = sql;
    }

    public void parse() {
        String[] parts = rawSql.trim().split("\\s+", 2);
        Command cmd;
        checkMissingClause(parts);
        try {
            cmd = Command.fromString(parts[0]);
        } catch (IllegalArgumentException e) {
            throwError("Unknown SQL command: “" + parts[0] + "”");
            return;
        }
        String clause = parts[1].trim();
        rule.setText(clause);
        cmd.apply(rule, clause);
    }
}
