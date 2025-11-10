package com.example.regularExpressions.Models;

public class MatchResult {
    public boolean matched;
    public int length;

    public MatchResult(boolean matched, int length) {
        this.matched = matched;
        this.length = length;
    }
}