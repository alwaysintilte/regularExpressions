package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class EndOfTextType implements RegularExpressionType {
    @Override
    public MatchResult match(String text, int position) {
        return new MatchResult(position == text.length(), 0);
    }

    @Override
    public String getType() {
        return "END_OF_TEXT";
    }
}
