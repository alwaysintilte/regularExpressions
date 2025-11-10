package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class NonWordBoundaryType implements RegularExpressionType {
    @Override
    public MatchResult match(String text, int position) {
        boolean isWordCharLeft = position > 0 &&
                Character.isLetterOrDigit(text.charAt(position - 1));
        boolean isWordCharRight = position < text.length() &&
                Character.isLetterOrDigit(text.charAt(position));

        boolean isNonBoundary = isWordCharLeft == isWordCharRight;
        return new MatchResult(isNonBoundary, 0);
    }

    @Override
    public String getType() {
        return "NON_WORD_BOUNDARY";
    }
}
