package com.example.regularExpressions;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Service
public class RegexParser {
    public Result validateAndMatch(String pattern, String text) {
        if (pattern == null || pattern.trim().isEmpty()) {
            return new Result(false, "Ошибка: Регулярное выражение не может быть пустым");
        }

        if (text == null) {
            text = "";
        }

        String validationResult = validateRegex(pattern);
        if (!validationResult.equals("VALID")) {
            return new Result(false, "Ошибка в регулярном выражении: " + validationResult);
        }

        List<Match> matches = findMatches(pattern, text);
        String highlightedText = highlightMatches(text, matches);

        if (matches.isEmpty()) {
            return new Result(true, "Регулярное выражение валидно, но совпадений не найдено", matches, highlightedText);
        } else {
            return new Result(true,
                    "Найдено совпадений: " + matches.size(),
                    matches, highlightedText);
        }
    }

    private String validateRegex(String pattern) {
        try {
            Stack<Character> stack = new Stack<>();
            boolean escape = false;

            for (int i = 0; i < pattern.length(); i++) {
                char c = pattern.charAt(i);

                if (escape) {
                    escape = false;
                    continue;
                }

                if (c == '\\') {
                    escape = true;
                    continue;
                }

                if (c == '(') {
                    stack.push(c);
                }
                else if (c == ')') {
                    if (stack.isEmpty() || stack.pop() != '(') {
                        return "Непарная закрывающая скобка";
                    }
                }
                /*else if (c == '[') {
                    stack.push(c);
                }
                else if (c == ']') {
                    if (stack.isEmpty() || stack.pop() != '[') {
                        return "Непарная закрывающая квадратная скобка";
                    }
                }*/

                if ("*+?".indexOf(c) != -1) {
                    if (i == 0 || "*+?(".indexOf(pattern.charAt(i-1)) != -1) {
                        return "Квантификатор без предшествующего символа: " + c;
                    }
                }

                /*if (c == '{') {
                    int j = i + 1;
                    while (j < pattern.length() && pattern.charAt(j) != '}') {
                        if (!Character.isDigit(pattern.charAt(j)) && pattern.charAt(j) != ',') {
                            return "Некорректный диапазон в {}";
                        }
                        j++;
                    }
                    if (j == pattern.length()) {
                        return "Незакрытая фигурная скобка";
                    }
                }*/
            }

            if (!stack.isEmpty()) {
                return "Непарные скобки в выражении";
            }

            return "VALID";
        } catch (Exception e) {
            return "Ошибка при разборе выражения: " + e.getMessage();
        }
    }

    private List<Match> findMatches(String pattern, String text) {
        List<Match> matches = new ArrayList<>();

        for (int i = 0; i <= text.length(); i++) {
            MatchResult result = matchSequence(pattern, text, i, 0);
            if (result.matched && result.length > 0) {
                matches.add(new Match(i, i + result.length, text.substring(i, i + result.length)));
                i += result.length - 1;
            }
        }

        return matches;
    }

    private MatchResult matchSequence(String pattern, String text, int textPos, int patternPos) {
        int originalTextPos = textPos;
        int originalPatternPos = patternPos;

        while (patternPos < pattern.length()) {
            if (patternPos < pattern.length() - 1) {
                char nextChar = pattern.charAt(patternPos + 1);

                // Обработка квантификаторов
                if (nextChar == '*' || nextChar == '+' || nextChar == '?') {
                    MatchResult result = handleQuantifier(pattern, text, textPos, patternPos, nextChar);
                    if (result.matched) {
                        patternPos = result.newPatternPos;
                        textPos = result.newTextPos;
                        continue;
                    } else {
                        return new MatchResult(false, 0);
                    }
                }
            }

            // Обработка обычных символов
            if (patternPos < pattern.length()) {
                char currentChar = pattern.charAt(patternPos);

                if (currentChar == '\\') {
                    // Escape-символ
                    if (patternPos + 1 >= pattern.length()) {
                        return new MatchResult(false, 0);
                    }
                    char escapedChar = pattern.charAt(patternPos + 1);
                    if (textPos >= text.length() || text.charAt(textPos) != escapedChar) {
                        return new MatchResult(false, 0);
                    }
                    textPos++;
                    patternPos += 2;
                } else if (currentChar == '.') {
                    // Любой символ
                    if (textPos >= text.length()) {
                        return new MatchResult(false, 0);
                    }
                    textPos++;
                    patternPos++;
                } else if (currentChar == '(') {
                    // Группа
                    int groupEnd = findGroupEnd(pattern, patternPos);
                    if (groupEnd == -1) {
                        return new MatchResult(false, 0);
                    }

                    String groupPattern = pattern.substring(patternPos + 1, groupEnd);
                    MatchResult groupResult = matchSequence(groupPattern, text, textPos, 0);

                    if (!groupResult.matched) {
                        return new MatchResult(false, 0);
                    }

                    textPos = groupResult.newTextPos;
                    patternPos = groupEnd + 1;
                } else {
                    // Обычный символ
                    if (textPos >= text.length() || text.charAt(textPos) != currentChar) {
                        return new MatchResult(false, 0);
                    }
                    textPos++;
                    patternPos++;
                }
            }
        }

        return new MatchResult(true, textPos - originalTextPos);
    }

    private MatchResult handleQuantifier(String pattern, String text, int textPos,
                                         int patternPos, char quantifier) {
        char targetChar = pattern.charAt(patternPos);
        int min = 0, max = Integer.MAX_VALUE;

        switch (quantifier) {
            case '*': min = 0; max = Integer.MAX_VALUE; break;
            case '+': min = 1; max = Integer.MAX_VALUE; break;
            case '?': min = 0; max = 1; break;
        }

        int count = 0;
        int currentTextPos = textPos;

        // Подсчитываем количество повторений
        while (count < max && currentTextPos < text.length()) {
            if (targetChar == '.') {
                count++;
                currentTextPos++;
            } else if (text.charAt(currentTextPos) == targetChar) {
                count++;
                currentTextPos++;
            } else {
                break;
            }
        }

        if (count < min) {
            return new MatchResult(false, 0);
        }

        return new MatchResult(true, currentTextPos, patternPos + 2);
    }

    private int findGroupEnd(String pattern, int start) {
        int depth = 1;
        for (int i = start + 1; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '(') depth++;
            else if (pattern.charAt(i) == ')') depth--;

            if (depth == 0) return i;
        }
        return -1;
    }

    private String highlightMatches(String text, List<Match> matches) {
        if (matches.isEmpty()) {
            return text;
        }

        StringBuilder highlighted = new StringBuilder();
        int lastIndex = 0;

        for (Match match : matches) {
            // Добавляем текст до совпадения
            if (match.getStart() > lastIndex) {
                highlighted.append(escapeHtml(text. substring(lastIndex, match.getStart())));
            }

            // Добавляем выделенное совпадение
            highlighted.append("<span class=\"match\">")
                    .append(escapeHtml(match.getMatchedText()))
                    .append("</span>");

            lastIndex = match.getEnd();
        }

        // Добавляем оставшийся текст
        if (lastIndex < text.length()) {
            highlighted.append(escapeHtml(text.substring(lastIndex)));
        }

        return highlighted.toString();
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
    private static class MatchResult {
        boolean matched;
        int newTextPos;
        int newPatternPos;
        int length;

        MatchResult(boolean matched, int length) {
            this.matched = matched;
            this.length = length;
            this.newTextPos = 0;
            this.newPatternPos = 0;
        }

        MatchResult(boolean matched, int newTextPos, int newPatternPos) {
            this.matched = matched;
            this.newTextPos = newTextPos;
            this.newPatternPos = newPatternPos;
            this.length = newTextPos;
        }
    }
}
