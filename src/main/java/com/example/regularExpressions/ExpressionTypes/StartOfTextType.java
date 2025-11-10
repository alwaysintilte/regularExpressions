package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class StartOfTextType implements RegularExpressionType {
    @Override
    public MatchResult match(String text, int position) {
        return new MatchResult(position == 0, 0);
    }

    @Override
    public String getType() {
        return "START_OF_TEXT";
    }
}
