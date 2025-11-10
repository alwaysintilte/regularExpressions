package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

public class WordBoundaryType implements RegularExpressionType {
    @Override
    public MatchResult match(String text, int position) {
        boolean isWordCharLeft = position > 0 &&
                Character.isLetterOrDigit(text.charAt(position - 1));
        boolean isWordCharRight = position < text.length() &&
                Character.isLetterOrDigit(text.charAt(position));

        boolean isBoundary = isWordCharLeft != isWordCharRight;

        return new MatchResult(isBoundary, 0);
    }

    @Override
    public String getType() {
        return "WORD_BOUNDARY";
    }
}
