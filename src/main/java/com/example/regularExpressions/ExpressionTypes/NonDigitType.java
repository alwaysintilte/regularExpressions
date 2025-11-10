package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class NonDigitType implements RegularExpressionType {
    @Override
    public MatchResult match(String text, int position) {
        if (position < text.length() && !Character.isDigit(text.charAt(position))) {
            return new MatchResult(true, 1);
        }
        return new MatchResult(false, 0);
    }

    @Override
    public String getType() {
        return "NON_DIGIT";
    }
}