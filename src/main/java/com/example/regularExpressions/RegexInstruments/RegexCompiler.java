package com.example.regularExpressions.RegexInstruments;

import com.example.regularExpressions.ExpressionTypes.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RegexCompiler {
    public List<RegularExpressionType> compile(String pattern) {
        List<RegularExpressionType> types = new ArrayList<>();
        int pos = 0;

        while (pos < pattern.length()) {
            char c = pattern.charAt(pos);

            if (c == '\\') {
                if (pos + 1 >= pattern.length()) {
                    throw new IllegalArgumentException("Incomplete escape sequence");
                }
                char escapedChar = pattern.charAt(pos + 1);
                if (escapedChar == 'A') {
                    types.add(new StartOfTextType());
                } else if (escapedChar == 'Z') {
                    types.add(new EndOfTextType());
                } else if (escapedChar == 'b') {
                    types.add(new WordBoundaryType());
                } else if (escapedChar == 'B') {
                    types.add(new NonWordBoundaryType());
                } else if (escapedChar == '<') {
                    types.add(new StartOfWordType());
                } else if (escapedChar == '>') {
                    types.add(new EndOfWordType());
                } else if (escapedChar == 'd') {
                    types.add(new DigitType());
                } else if (escapedChar == 'D') {
                    types.add(new NonDigitType());
                } else if (escapedChar == 'w') {
                    types.add(new WordCharType());
                } else if (escapedChar == 'W') {
                    types.add(new NonWordCharType());
                } else if (escapedChar == 's') {
                    types.add(new SpaceCharType());
                } else if (escapedChar == 'S') {
                    types.add(new NonSpaceCharType());
                } else {
                    types.add(new LiteralType(escapedChar));
                }
                pos += 2;
            } else if (c == '^') {
                types.add(new StartOfLineType());
                pos++;
            } else if (c == '$') {
                types.add(new EndOfLineType());
                pos++;
            } else if (c == '.') {
                types.add(new AnyCharType());
                pos++;
            } else if (c == '(') {
                int end = findGroupEnd(pattern, pos);
                String groupContent = pattern.substring(pos + 1, end);
                List<RegularExpressionType> groupTypes = compile(groupContent);
                types.add(new GroupType(groupTypes));
                pos = end + 1;
            } else if (c == '[') {
                int end = findCharacterClassEnd(pattern, pos);
                String classContent = pattern.substring(pos + 1, end);
                Set<Character> charSet = parseCharacterClassContent(classContent);
                boolean negated = classContent.startsWith("^");
                types.add(new CharacterClassType(charSet, negated));
                pos = end + 1;
            } else if ((c == '*' || c == '+' || c == '?') && !types.isEmpty()) {
                RegularExpressionType lastType = types.remove(types.size() - 1);
                types.add(new QuantifierType(lastType, c));
                pos++;
            } else {
                types.add(new LiteralType(c));
                pos++;
            }
        }

        return types;
    }

    private int findGroupEnd(String pattern, int start) {
        int count = 1;
        for (int i = start + 1; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '(') count++;
            else if (pattern.charAt(i) == ')') count--;

            if (count == 0) return i;
        }
        throw new IllegalArgumentException("Unclosed group");
    }
    private int findCharacterClassEnd(String pattern, int start) {
        for (int i = start + 1; i < pattern.length(); i++) {
            if (pattern.charAt(i) == ']') {
                return i;
            }
        }
        throw new IllegalArgumentException("Unclosed character class");
    }

    private Set<Character> parseCharacterClassContent(String content) {
        Set<Character> charSet = new HashSet<>();
        boolean negated = content.startsWith("^");
        if (negated) {
            content = content.substring(1);
        }

        for (int i = 0; i < content.length(); i++) {
            if (i + 2 < content.length() && content.charAt(i + 1) == '-') {
                char start = content.charAt(i);
                char end = content.charAt(i + 2);
                for (char c = start; c <= end; c++) {
                    charSet.add(c);
                }
                i += 2;
            } else {
                charSet.add(content.charAt(i));
            }
        }

        return charSet;
    }
}