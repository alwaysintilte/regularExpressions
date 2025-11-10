package com.example.regularExpressions.ExpressionTypes;

import com.example.regularExpressions.Models.MatchResult;
import java.util.Set;

public class CharacterClassType implements RegularExpressionType {
    private final Set<Character> characters;
    private final boolean negated;

    public CharacterClassType(Set<Character> characters, boolean negated) {
        this.characters = characters;
        this.negated = negated;
    }

    @Override
    public MatchResult match(String text, int position) {
        if (position >= text.length()) {
            return new MatchResult(false, 0);
        }

        char c = text.charAt(position);
        boolean contains = characters.contains(c);
        boolean matched = negated ? !contains : contains;

        return new MatchResult(matched, matched ? 1 : 0);
    }

    @Override
    public String getType() {
        return negated ? "NEGATED_CHARACTER_CLASS" : "CHARACTER_CLASS";
    }
}