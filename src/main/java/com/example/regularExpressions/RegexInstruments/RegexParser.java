package com.example.regularExpressions.RegexInstruments;

import com.example.regularExpressions.ExpressionTypes.QuantifierType;
import com.example.regularExpressions.ExpressionTypes.RegularExpressionType;
import com.example.regularExpressions.Models.Match;
import com.example.regularExpressions.Models.MatchResult;
import com.example.regularExpressions.Models.Result;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
public class RegexParser {
    private final RegexCompiler compiler = new RegexCompiler();

    public Result validateAndMatch(String pattern, String text) {
        if (pattern == null || pattern.trim().isEmpty()) {
            return new Result(false, "Ошибка: Регулярное выражение не может быть пустым");
        }
        if (text == null) text = "";
        String validationResult = validateRegex(pattern);
        if (!validationResult.equals("Valid")) {
            return new Result(false, "Ошибка в регулярном выражении: " + validationResult);
        }
        try {
            List<RegularExpressionType> regularExpressionTypes = compiler.compile(pattern);
            List<Match> matches = findMatches(regularExpressionTypes, text);
            String highlightedText = highlightMatches(text, matches);
            if (matches.isEmpty()) {
                return new Result(true, "Регулярное выражение валидно, но совпадений не найдено",
                        matches, highlightedText);
            } else {
                return new Result(true, "Найдено совпадений: " + matches.size(),
                        matches, highlightedText);
            }
        } catch (Exception e) {
            return new Result(false, "Ошибка компиляции: " + e.getMessage());
        }
    }

    private List<Match> findMatches(List<RegularExpressionType> regularExpressionTypes, String text) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i <= text.length(); i++) {
            MatchResult result = matchSequence(regularExpressionTypes, text, i);
            if (result.matched && result.length > 0) {
                matches.add(new Match(i, i + result.length, text.substring(i, i + result.length)));
                i += result.length - 1;
            }
        }

        return matches;
    }

    private MatchResult matchSequence(List<RegularExpressionType> regularExpressionTypes, String text, int position) {
        int currentPos = position;
        for (int i = 0; i < regularExpressionTypes.size(); i++) {
            RegularExpressionType regularExpressionType = regularExpressionTypes.get(i);
            List<RegularExpressionType> remaining = regularExpressionTypes.subList(i + 1, regularExpressionTypes.size());
            MatchResult result = regularExpressionType.match(text, currentPos, remaining);
            if (!result.matched) return new MatchResult(false, 0);
            currentPos += result.length;
        }
        return new MatchResult(true, currentPos - position);
    }

    private String validateRegex(String pattern) {
        try {
            Stack<Character> stack = new Stack<>();
            Stack<Character> cornerStack = new Stack<>();

            if (pattern.endsWith("\\")) {
                return "Незавершенная escape-последовательность";
            }

            for (int i = 0; i < pattern.length(); i++) {
                char c = pattern.charAt(i);

                if (c == '\\') {
                    if ("AZbB<>dDwWsS*+?()[].\\".indexOf(pattern.charAt(i + 1)) == -1) {
                        return "Неизвестная escape-последовательность: \\" + pattern.charAt(i + 1);
                    }
                    i++;
                }

                if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    if (stack.isEmpty() || stack.pop() != '(') {
                        return "Непарная закрывающая скобка";
                    }
                }
                if (c == '[') {
                    cornerStack.push(c);
                } else if (c == ']') {
                    if (cornerStack.isEmpty() || cornerStack.pop() != '[') {
                        return "Непарная закрывающая скобка";
                    }
                }

                if ("*+?".indexOf(c) != -1) {
                    if (i == 0 || "*+?".indexOf(pattern.charAt(i-1)) != -1) {
                        return "Квантификатор без предшествующего символа: " + c;
                    }
                }
            }

            if (!stack.isEmpty()) {
                return "Непарные скобки в выражении";
            }

            return "Valid";
        } catch (Exception e) {
            return "Ошибка при разборе выражения: " + e.getMessage();
        }
    }

    private String highlightMatches(String text, List<Match> matches) {
        if (matches.isEmpty()) {
            return text;
        }
        StringBuilder highlighted = new StringBuilder();
        int lastIndex = 0;
        for (Match match : matches) {
            if (match.getStart() > lastIndex) {
                highlighted.append(text.substring(lastIndex, match.getStart()));
            }
            highlighted.append("<span class=\"match\">")
                    .append(match.getMatchedText())
                    .append("</span>");
            lastIndex = match.getEnd();
        }
        if (lastIndex < text.length()) {
            highlighted.append(text.substring(lastIndex));
        }
        return highlighted.toString();
    }
}