package org.example;

public class Token {

    public TokenType tokenType;
    public String tokenName;

    public Token(TokenType tokenType, String tokenName) {
        this.tokenType = tokenType;
        this.tokenName = tokenName;
    }
}
