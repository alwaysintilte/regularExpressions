package com.example.regularExpressions.Models;
public class Match {
    private int start;
    private int end;
    private String matchedText;

    public Match() {}

    public Match(int start, int end, String matchedText) {
        this.start = start;
        this.end = end;
        this.matchedText = matchedText;
    }

    // Getters and Setters
    public int getStart() { return start; }
    public void setStart(int start) { this.start = start; }

    public int getEnd() { return end; }
    public void setEnd(int end) { this.end = end; }

    public String getMatchedText() { return matchedText; }
    public void setMatchedText(String matchedText) { this.matchedText = matchedText; }
}
