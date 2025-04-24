package org.example.Parsing;

public class ParseContext {
    public int position;
    public String text;
    public final int len;

    public ParseContext(String text) {
        this.position = 0;
        this.text = text;
        this.len = text.length();
    }
    
}
