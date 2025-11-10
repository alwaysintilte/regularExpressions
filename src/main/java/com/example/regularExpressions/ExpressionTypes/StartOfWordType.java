package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class StartOfWordType implements RegularExpressionType {
    @Override
    public MatchResult match(String text, int position) {
        boolean isWordCharRight = position < text.length() && Character.isLetterOrDigit(text.charAt(position));
        boolean isNonWordCharLeft = position == 0 || !Character.isLetterOrDigit(text.charAt(position - 1));

        return new MatchResult(isNonWordCharLeft && isWordCharRight, 0);
    }

    @Override
    public String getType() {
        return "START_OF_WORD";
    }
}
