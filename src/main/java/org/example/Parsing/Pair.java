package org.example.Parsing;

public class Pair {
    public String first;
    public String second;
    public int third;

    public Pair(String first, String second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static Pair empty() {
        return new Pair("","", 0);
    }

    public boolean isEmpty() {
        return first.isEmpty();
    }

}
