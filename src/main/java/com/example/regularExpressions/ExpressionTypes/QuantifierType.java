package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;

import java.util.List;

public class QuantifierType implements RegularExpressionType {
    private final RegularExpressionType target;
    private final char quantifier;
    private final int min;
    private final int max;

    public QuantifierType(RegularExpressionType target, char quantifier) {
        this.target = target;
        this.quantifier = quantifier;

        switch (quantifier) {
            case '*': min = 0; max = Integer.MAX_VALUE; break;
            case '+': min = 1; max = Integer.MAX_VALUE; break;
            case '?': min = 0; max = 1; break;
            default: throw new IllegalArgumentException("Unknown quantifier: " + quantifier);
        }
    }

    @Override
    public MatchResult match(String text, int position) {
        int count = 0;
        int currentPos = position;

        while (count < max && currentPos < text.length()) {
            MatchResult result = target.match(text, currentPos);
            if (!result.matched) {
                break;
            }
            count++;
            currentPos += result.length;
        }

        if (count < min) {
            return new MatchResult(false, 0);
        }

        return new MatchResult(true, currentPos - position);
    }
    @Override
    public MatchResult match(String text, int position, List<RegularExpressionType> remainingNodes) {
        int maxPossible = Math.min(max, text.length() - position);

        for (int count = maxPossible; count >= min; count--) {
            int currentPos = position;
            boolean quantifierMatches = true;

            // Проверяем count повторений
            for (int i = 0; i < count; i++) {
                MatchResult result = target.match(text, currentPos);
                if (!result.matched) {
                    quantifierMatches = false;
                    break;
                }
                currentPos += result.length;
            }

            if (quantifierMatches) {
                // РУЧНАЯ проверка оставшихся нод
                boolean remainingMatches = true;
                int remainingPos = currentPos;

                for (RegularExpressionType node : remainingNodes) {
                    MatchResult result = node.match(text, remainingPos);
                    if (!result.matched) {
                        remainingMatches = false;
                        break;
                    }
                    if (result.length > 0) {
                        remainingPos += result.length;
                    }
                }

                if (remainingMatches) {
                    return new MatchResult(true, currentPos - position);
                }
            }
        }
        return new MatchResult(false, 0);
    }
    @Override
    public String getType() {
        return "QUANTIFIER:" + quantifier + "(" + target.getType() + ")";
    }
}
