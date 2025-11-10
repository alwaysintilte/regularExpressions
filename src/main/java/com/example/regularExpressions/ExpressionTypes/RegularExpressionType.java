package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

import java.util.List;

public interface RegularExpressionType {
    MatchResult match(String text, int position);
    String getType();
    default MatchResult match(String text, int position,  List<RegularExpressionType> remainingNodes) {
        return match(text, position);
    }
}
