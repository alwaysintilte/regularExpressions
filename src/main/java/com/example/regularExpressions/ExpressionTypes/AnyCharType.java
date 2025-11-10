package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class AnyCharType implements RegularExpressionType {
    @Override
    public MatchResult match(String text, int position) {
        if (position >= text.length()) {
            return new MatchResult(false, 0);
        }
        return new MatchResult(true, 1);
    }

    @Override
    public String getType() {
        return "ANY_CHAR";
    }
}
