package org.example;

public class Tokenizer {
    private int position = 0;
    private char currentChar;
    private final String text;

    public Tokenizer(String sqlQuery) {
        this.text = sqlQuery;
        currentChar = text.charAt(position);
    }

    private void skipWhiteSpace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    private void advance() {
        position++;
        currentChar = position >= text.length() ? '\0' : text.charAt(position);
    }

    private String findTableName() {
        StringBuilder sb = new StringBuilder();
        while(currentChar != '\0' && !Character.isWhitespace(currentChar)) {
            sb.append(currentChar);
            advance();
        }
        return sb.toString();
    }

    public Token tokenize(String sqlQuery) {

        while(currentChar != '\0') {
            if(Character.isWhitespace(currentChar)) {
                skipWhiteSpace();
                continue;
            }
            if(currentChar == 'S') {
                for(int i=0;i<6;i++) {
                    advance();
                }
                return new Token(TokenType.SELECT, "select");
            }
            if(currentChar == 'C') {
                for(int i=0;i<6;i++) {
                    advance();
                }
                return new Token(TokenType.CREATE, "create");
            }
            if(currentChar == 'U') {advance(); return new Token(TokenType.UPDATE, "update");}
            if(currentChar == 'I') {advance(); return new Token(TokenType.INSERT, "insert");}
            if(currentChar == 'D') {advance(); return new Token(TokenType.DELETE, "delete");}
            if(currentChar == '*') {advance(); return new Token(TokenType.STAR, "all");}
            if(currentChar == 'f') {
                for(int i=0;i<4;i++) {
                    advance();
                }
                return new Token(TokenType.FROM, "from");
            }
            else {
                String tableName = findTableName();
                return new Token(TokenType.TABLE_NAME, tableName);
            }
        }
        return new Token(TokenType.EOF, "");
    }

    public String start(String sqlQuery) {
        Token token;
        StringBuilder tokenizedQuery = new StringBuilder();
        do {
            token = tokenize(sqlQuery);
            tokenizedQuery.append(token.tokenName).append(" ");
        } while(token.tokenType != TokenType.EOF);

        return tokenizedQuery.toString();
    }
}

/*
SELECT * From TABLE_NAME;




 */
