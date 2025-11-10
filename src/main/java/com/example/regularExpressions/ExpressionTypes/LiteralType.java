package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class LiteralType implements RegularExpressionType {
    private final char character;

    public LiteralType(char character) {
        this.character = character;
    }

    @Override
    public MatchResult match(String text, int position) {
        if (position >= text.length() || text.charAt(position) != character) {
            return new MatchResult(false, 0);
        }
        return new MatchResult(true, 1);
    }

    @Override
    public String getType() {
        return "LITERAL:'" + character + "'";
    }
}
