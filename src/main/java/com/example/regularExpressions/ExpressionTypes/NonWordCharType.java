package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class NonWordCharType implements RegularExpressionType {
    @Override
    public MatchResult match(String text, int position) {
        if (position < text.length()) {
            char c = text.charAt(position);
            if (!(Character.isLetterOrDigit(c) || c == '_')) {
                return new MatchResult(true, 1);
            }
        }
        return new MatchResult(false, 0);
    }

    @Override
    public String getType() {
        return "NON_WORD_CHAR";
    }
}