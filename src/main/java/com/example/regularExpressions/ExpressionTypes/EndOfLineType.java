package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class EndOfLineType implements RegularExpressionType {
    @Override
    public MatchResult match(String text, int position) {
        System.out.println("EndOfLineType: position=" + position + ", text.length=" + text.length() + ", equals=" + (position == text.length()));
        return new MatchResult(position == text.length(), 0);
    }

    @Override
    public String getType() {
        return "END_OF_LINE";
    }
}
