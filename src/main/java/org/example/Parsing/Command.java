package org.example.Parsing;

import org.example.Parsing.ParseRule;

public enum Command {
    SELECT  { @Override public void apply(ParseRule r, String s) { r.parseSelect();  } },
    INSERT  { @Override public void apply(ParseRule r, String s) { r.parseInsert();  } },
    UPDATE  { @Override public void apply(ParseRule r, String s) { r.parseUpdate();  } },
    DELETE  { @Override public void apply(ParseRule r, String s) { r.parseDelete();  } },
    CREATE  { @Override public void apply(ParseRule r, String s) { r.parseCreate();  } },
    ALTER   { @Override public void apply(ParseRule r, String s) { r.parseAlter();   } },
    DROP    { @Override public void apply(ParseRule r, String s) { r.parseDrop();    } },
    TRUNCATE{ @Override public void apply(ParseRule r, String s) { r.parseTruncate();} },
    DESCRIBE{ @Override public void apply(ParseRule r, String s) { r.parseDescribe();} },
    USE     { @Override public void apply(ParseRule r, String s) { r.parseUse();     } };

    abstract public void apply(ParseRule rule, String sqlClause);

    public static Command fromString(String token) {
        return valueOf(token.trim().toUpperCase());
    }
}
