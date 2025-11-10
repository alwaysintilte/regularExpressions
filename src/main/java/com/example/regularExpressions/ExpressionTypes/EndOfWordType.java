package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class EndOfWordType implements RegularExpressionType {
    @Override
    public MatchResult match(String text, int position) {
        boolean isWordCharLeft = position > 0 &&
                Character.isLetterOrDigit(text.charAt(position - 1));
        boolean isNonWordCharRight = position == text.length() ||
                !Character.isLetterOrDigit(text.charAt(position));

        return new MatchResult(isWordCharLeft && isNonWordCharRight, 0);
    }

    @Override
    public String getType() {
        return "END_OF_WORD";
    }
}