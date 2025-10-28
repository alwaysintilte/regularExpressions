package com.example.regularExpressions;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RegexValidatorController {

    private final RegexParser regexParser;

    public RegexValidatorController(RegexParser regexParser) {
        this.regexParser = regexParser;
    }

    @PostMapping("/validate")
    public Result validateRegex(@RequestBody ValidationRequest request) {
        return regexParser.validateAndMatch(request.getPattern(), request.getText());
    }

    // DTO для запроса
    public static class ValidationRequest {
        private String pattern;
        private String text;

        // Getters and Setters
        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}
