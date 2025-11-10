package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

import java.util.List;

public class GroupType implements RegularExpressionType {
    private final List<RegularExpressionType> children;

    public GroupType(List<RegularExpressionType> children) {
        this.children = children;
    }

    @Override
    public MatchResult match(String text, int position) {
        int currentPos = position;

        for (RegularExpressionType child : children) {
            MatchResult result = child.match(text, currentPos);
            if (!result.matched) {
                return new MatchResult(false, 0);
            }
            currentPos += result.length;
        }

        return new MatchResult(true, currentPos - position);
    }

    @Override
    public String getType() {
        return "GROUP(" + children.size() + " children)";
    }
}
